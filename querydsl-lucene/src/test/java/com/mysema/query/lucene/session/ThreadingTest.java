package com.mysema.query.lucene.session;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import com.mysema.query.lucene.session.impl.LuceneSessionFactoryImpl;
import com.mysema.query.lucene.session.impl.LuceneTransactionHandler;

public class ThreadingTest {

    private LuceneSessionFactoryImpl sessionFactory;

    private TestDao dao;

    private static class Task implements Callable<Throwable> {

        TestDao dao;

        String action;

        Task(TestDao dao, String action) {
            this.dao = dao;
            this.action = action;
        }

        @Override
        public Throwable call() throws Exception {
            try {
                if (action.equals("read")) {
                    dao.read();
                }
                if (action.equals("write")) {
                    dao.write();
                }
                if (action.equals("reset")) {
                    dao.reset();
                }
                return null;
            } catch (Throwable t) {
                return t;
            }
        }

    }

    private static interface TestDao {
        void write();

        void reset();

        void read();
    }

    private static class TestDaoImpl implements TestDao {

        private LuceneSessionFactory sessionFactory;

        private QDocument doc = new QDocument("d");

        int counter = 1;

        int readCount = 0;

        private final Object writeLock = new Object();

        private final Object readLock = new Object();

        private final Object resetLock = new Object();

        TestDaoImpl(LuceneSessionFactory sessionFactory) {
            this.sessionFactory = sessionFactory;
        }

        @Override
        @LuceneTransactional
        public void write() {
            LuceneSession session = sessionFactory.getCurrentSession();

            LuceneWriter writer = session.beginAppend();
            int numOfDocs = 1000;
            for (int i = 0; i < numOfDocs; i++) {
                writer.addDocument(QueryTestHelper.createDocument(
                        "title " + counter,
                        "",
                        "",
                        counter,
                        0));

            }
            synchronized (writeLock) {
                counter += numOfDocs;
            }
            System.out.println("Added " + numOfDocs + " documents ");
        }

        @Override
        @LuceneTransactional
        public void reset() {
            LuceneSession session = sessionFactory.getCurrentSession();

            synchronized (resetLock) {
                counter = 0;
            }
            session.beginReset().addDocument(
                    QueryTestHelper.createDocument("title " + counter, "", "", counter, 0));
            System.out.println("Resetted index ");
        }

        @Override
        @LuceneTransactional(readOnly = true)
        public void read() {
            LuceneSession session = sessionFactory.getCurrentSession();
            int count = (int) session.createQuery().where(doc.title.startsWith("title")).count();

            synchronized (readLock) {
                readCount++;
                if (readCount % 1000 == 0) {
                    System.out.println("Document count is " + count + " after " + readCount
                                       + " reads");
                }
            }
        }

    }

    @Before
    public void before() throws IOException {
        String path = "target/index";
        FileUtils.deleteDirectory(new File(path));

        sessionFactory = new LuceneSessionFactoryImpl("target/index");
        // Initialize index
        QueryTestHelper.addData(sessionFactory);

        AspectJProxyFactory factory = new AspectJProxyFactory(new TestDaoImpl(sessionFactory));
        factory.addAspect(new LuceneTransactionHandler());
        dao = factory.getProxy();
    }

    private static class TaskRunner implements Callable<List<Throwable>> {

        private String action;

        private int requestsPerMinute;

        private int starDelayInSecs;

        public boolean stop = false;

        private int clients;

        ScheduledExecutorService threads;

        TestDao dao;

        TaskRunner(String action, int clients, int requestsPerMinute, int startDelayInSecs,
                   TestDao dao) {
            this.action = action;
            this.clients = clients;
            this.requestsPerMinute = requestsPerMinute;
            this.starDelayInSecs = startDelayInSecs;
            this.dao = dao;
        }

        @Override
        public List<Throwable> call() throws Exception {

            threads = Executors.newScheduledThreadPool(clients);

            List<Throwable> errors = new ArrayList<Throwable>();

            List<Future<Throwable>> tasks = new ArrayList<Future<Throwable>>();

            // Fire tasks
            for (int i = 0; i < clients; i++) {
                tasks.add(threads.schedule(
                        new Task(dao, action),
                        i * starDelayInSecs,
                        TimeUnit.SECONDS));
            }

            long startTime = System.currentTimeMillis();
            int numOfRequests = 0;
            // Take results and fire again
            while (tasks.size() > 0) {
                int taskReadyIndex = -1;
                for (int i = 0; i < tasks.size(); i++) {
                    try {
                        tasks.get(i).get(1, TimeUnit.MILLISECONDS);
                        taskReadyIndex = i;
                        break;
                    } catch (TimeoutException e) {
                        // Ignore
                    }
                }
                if (taskReadyIndex == -1) {
                    continue;
                }

                Throwable error = tasks.get(taskReadyIndex).get();
                tasks.remove(taskReadyIndex);
                numOfRequests++;
                if (error != null) {
                    errors.add(error);
                }

                if (!stop) {
                    tasks.add(threads.schedule(
                            new Task(dao, action),
                            60 / requestsPerMinute,
                            TimeUnit.SECONDS));
                }
            }
            float elapsedMins = ((float) (System.currentTimeMillis() - startTime) / 1000) / 60;
            System.out.println(numOfRequests + " " + action + " requests, " + numOfRequests
                               / elapsedMins + " requests/min");
            threads.shutdown();
            return errors;
        }
    }

    @Test
    @Ignore
    public void Simultaneous() throws InterruptedException, ExecutionException {

        sessionFactory.setDefaultLockTimeout(5000);

        ExecutorService threads = Executors.newFixedThreadPool(3);

        int simulationtimeInSecs = 60;

        // Readers
        TaskRunner readRunner = new TaskRunner("read", 100, 120, simulationtimeInSecs / 1000, dao);
        Future<List<Throwable>> reads = threads.submit(readRunner);

        // Writers
        TaskRunner writeRunner = new TaskRunner("write", 1, 6, 0, dao);
        Future<List<Throwable>> writes = threads.submit(writeRunner);

        // Resetters
        TaskRunner resetRunner = new TaskRunner("reset", 1, 2, 15, dao);
        Future<List<Throwable>> resets = threads.submit(resetRunner);

        Thread.sleep(simulationtimeInSecs * 1000);

        readRunner.stop = true;
        writeRunner.stop = true;
        resetRunner.stop = true;

        List<Throwable> readErrors = reads.get();
        List<Throwable> writeErrors = writes.get();
        List<Throwable> resetErrors = resets.get();
        threads.shutdown();

        System.out.println("Read errors: " + readErrors.size());
        System.out.println("Write errors: " + writeErrors.size());
        System.out.println("Reset errors: " + resetErrors.size());

        printErrors(readErrors);
        printErrors(writeErrors);
        printErrors(resetErrors);

        assertEquals("Read errors", 0, readErrors.size());
        assertEquals("Write errors", 0, writeErrors.size());
        assertEquals("Reset errors", 0, resetErrors.size());

        //
        // Map<Class<?>, Integer> errorTypes = new HashMap<Class<?>, Integer>();
        // List<Throwable> errors = new ArrayList<Throwable>();
        // for (int i = 0; i < runs; i++) {
        // Throwable t = pool.take().get();
        // if (t != null) {
        // if (!errorTypes.containsKey(t.getClass())) {
        // errorTypes.put(t.getClass(), 0);
        // }
        // errorTypes.put(t.getClass(), errorTypes.get(t.getClass()) + 1);
        // errors.add(t);
        // }
        // }
    }

    private void printErrors(List<Throwable> errors) {
        int count = 0;
        for (Throwable t : errors) {
            System.out.println("------------------");
            System.out.println("");
            t.printStackTrace(System.out);
            if (count++ > 3) {
                break;
            }
        }

    }
}
