package com.mysema.query.lucene.session;

import static com.mysema.query.lucene.session.QueryTestHelper.addData;
import static com.mysema.query.lucene.session.QueryTestHelper.createDocument;
import static com.mysema.query.lucene.session.QueryTestHelper.createDocuments;
import static com.mysema.query.lucene.session.QueryTestHelper.getDocument;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.session.impl.LuceneSearcher;
import com.mysema.query.lucene.session.impl.LuceneSessionFactoryImpl;
import com.mysema.query.lucene.session.impl.LuceneWriterImpl;
import com.mysema.query.lucene.session.impl.ReleaseListener;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class LuceneSessionFactoryTest {

    private LuceneSessionFactory sessionFactory;

    private Directory directory;

    private StringPath title;

    private NumberPath<Integer> year;

    @Before
    public void before() throws IOException {
        directory = new RAMDirectory();
        sessionFactory = new LuceneSessionFactoryImpl(directory);

        final QDocument entityPath = new QDocument("doc");
        title = entityPath.title;
        year = entityPath.year;
    }

    @Test
    public void testBasicQuery() {

        addData(sessionFactory);

        LuceneSession session = sessionFactory.openSession(true);
        // Testing the queries work through session
        LuceneQuery query = session.createQuery();
        List<Document> results = query.where(title.eq("Jurassic Park")).list();

        assertEquals(1, results.size());
        assertEquals("Jurassic Park", results.get(0).getField("title").stringValue());

        query = session.createQuery();
        long count = query.where(title.startsWith("Nummi")).count();
        assertEquals(1, count);

        // TODO Tästä tulee 0 eikä 2!!
        // query.where(title.ne("AA")).count();

        session.close();
    }

    @Test
    public void testFlush() {
        LuceneSession session = sessionFactory.openSession(false);
        createDocuments(session);
        session.flush();

        // Now we will see the three documents
        LuceneQuery query = session.createQuery();
        assertEquals(4, query.where(year.gt(1800)).count());

        // Adding new document
        session.beginAppend().addDocument(createDocument("title", "author", "", 2010, 1));

        // New query will not see the addition
        query = session.createQuery();
        assertEquals(4, query.where(year.gt(1800)).count());

        session.flush();

        // This will see the addition
        LuceneQuery query1 = session.createQuery();
        assertEquals(5, query1.where(year.gt(1800)).count());

        // The old query still sees the same 3
        assertEquals(4, query.count());

        session.close();
    }

    @Test(expected=SessionNotBoundException.class)
    public void testCurrentSession() {
        sessionFactory.getCurrentSession();
    }

    @Test(expected = SessionReadOnlyException.class)
    public void testReadonly() {
        LuceneSession session = sessionFactory.openSession(true);
        session.beginOverwrite();
    }

    @Test(expected = SessionClosedException.class)
    public void testSessionClosedCreate() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.createQuery();
    }

    @Test(expected = SessionClosedException.class)
    public void testSessionClosedAppend() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.beginAppend();
    }

    @Test(expected = SessionClosedException.class)
    public void testSessionClosedFlush() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.flush();
    }

    @Test(expected = SessionClosedException.class)
    public void testSessionClosedClosed() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.close();
    }

    @Test(expected = SessionClosedException.class)
    public void testSessionClosedOverwrite() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.beginOverwrite();
    }

    private class ReleaseCounter implements ReleaseListener {
        List<LuceneSearcher> searchers = new ArrayList<LuceneSearcher>();
        List<LuceneWriterImpl> writers = new ArrayList<LuceneWriterImpl>();
        Map<LuceneSearcher, Integer> leases = new HashMap<LuceneSearcher, Integer>();
        Map<LuceneSearcher, Integer> releases = new HashMap<LuceneSearcher, Integer>();
        Map<LuceneWriterImpl, Integer> closes = new HashMap<LuceneWriterImpl, Integer>();

        public void lease(LuceneSearcher searcher) {
            if (!searchers.contains(searcher)) {
                searchers.add(searcher);
            }
            if (!leases.containsKey(searcher)) {
                leases.put(searcher, 0);
            }
            leases.put(searcher, leases.get(searcher) + 1);
        }

        public void release(LuceneSearcher searcher) {
            if (!releases.containsKey(searcher)) {
                releases.put(searcher, 0);
            }
            releases.put(searcher, releases.get(searcher) + 1);
        }

        public void close(LuceneWriterImpl writer) {
            if (!writers.contains(writer)) {
                writers.add(writer);
            }
            if (!closes.containsKey(writer)) {
                closes.put(writer, 0);
            }
            closes.put(writer, closes.get(writer) + 1);
        }
    }

    @Test
    public void testResourcesAreReleased() throws IOException {

        ReleaseCounter counter = new ReleaseCounter();
        
        sessionFactory = new LuceneSessionFactoryImpl(directory, counter);
        
        LuceneSession session = sessionFactory.openSession(false);

        session.beginAppend().addDocument(getDocument());
        session.flush();
        
        LuceneQuery query = session.createQuery();
        assertEquals(1, query.where(year.gt(1800)).count());

        session.beginAppend().addDocument(getDocument());
        session.flush();
        
        query = session.createQuery();
        assertEquals(2, query.where(year.gt(1800)).count());
        
        session.close();
        
        //Second session
        session = sessionFactory.openSession(true);
        query = session.createQuery();
        assertEquals(2, query.where(year.gt(1800)).count());
        session.close();
        
        assertEquals(3, counter.leases.size());
        assertEquals(3, counter.releases.size());
        assertEquals(3, counter.searchers.size());
        assertEquals(1, counter.closes.size());
        assertEquals(1, counter.writers.size());
        
        //First and second searchers should be released totally
        assertEquals(2, (int)counter.leases.get(counter.searchers.get(0)));
        assertEquals(2, (int)counter.releases.get(counter.searchers.get(0)));

        assertEquals(2, (int)counter.leases.get(counter.searchers.get(1)));
        assertEquals(2, (int)counter.releases.get(counter.searchers.get(1)));

        //Third searcher leaves it as current
        assertEquals(2, (int)counter.leases.get(counter.searchers.get(2)));
        assertEquals(1, (int)counter.releases.get(counter.searchers.get(2)));
        
        //The writer should be closed
        assertEquals(1, (int)counter.closes.get(counter.writers.get(0)));
    }

}
