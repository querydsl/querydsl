package com.mysema.query.lucene.session.impl;

import static com.mysema.query.lucene.session.QueryTestHelper.addData;
import static com.mysema.query.lucene.session.QueryTestHelper.createDocument;
import static com.mysema.query.lucene.session.QueryTestHelper.createDocuments;
import static com.mysema.query.lucene.session.QueryTestHelper.getDocument;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.QueryException;
import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.session.LuceneSession;
import com.mysema.query.lucene.session.LuceneSessionFactory;
import com.mysema.query.lucene.session.QDocument;
import com.mysema.query.lucene.session.SessionClosedException;
import com.mysema.query.lucene.session.SessionNotBoundException;
import com.mysema.query.lucene.session.SessionReadOnlyException;
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
    public void BasicQuery() {

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
    public void Flush() {
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

    @Test(expected = SessionNotBoundException.class)
    public void CurrentSession() {
        sessionFactory.getCurrentSession();
    }

    @Test(expected = SessionReadOnlyException.class)
    public void Readonly() {
        LuceneSession session = sessionFactory.openSession(true);
        session.beginReset();
    }

    @Test(expected = SessionClosedException.class)
    public void SessionClosedCreate() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.createQuery();
    }

    @Test(expected = SessionClosedException.class)
    public void SessionClosedAppend() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.beginAppend();
    }

    @Test(expected = SessionClosedException.class)
    public void SessionClosedFlush() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.flush();
    }

    @Test(expected = SessionClosedException.class)
    public void SessionClosedClosed() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.close();
    }

    @Test(expected = SessionClosedException.class)
    public void SessionClosedOverwrite() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.beginReset();
    }

    @Test
    public void Reset() {
        addData(sessionFactory);

        LuceneSession session = sessionFactory.openSession(true);
        assertEquals(4, session.createQuery().count());
        session.close();

        session = sessionFactory.openSession(false);

        assertEquals(4, session.createQuery().count());
        session.beginReset().addDocument(getDocument());
        session.flush();
        assertEquals(1, session.createQuery().count());
        session.close();

        session = sessionFactory.openSession(true);
        assertEquals(1, session.createQuery().count());
        session.close();
    }

    private class CountingSessionFactory extends LuceneSessionFactoryImpl {
        
        List<Leasable> leasables = new ArrayList<Leasable>();

        Map<Leasable, Integer> leases = new HashMap<Leasable, Integer>();

        Map<Leasable, Integer> releases = new HashMap<Leasable, Integer>();

        public CountingSessionFactory(Directory directory) {
            super(directory);
        }

        @Override
        public void lease(Leasable leasable) {
            if (!leasables.contains(leasable)) {
                leasables.add(leasable);
            }
            if (!leases.containsKey(leasable)) {
                leases.put(leasable, 0);
            }
            leases.put(leasable, leases.get(leasable) + 1);
            super.lease(leasable);
        }

        @Override
        public void release(Leasable leasable) {
            if (!leasables.contains(leasable)) {
                leasables.add(leasable);
            }
            if (!releases.containsKey(leasable)) {
                releases.put(leasable, 0);
            }
            releases.put(leasable, releases.get(leasable) + 1);
            super.release(leasable);
        }
        
    }
    
    @Test
    public void ResourcesAreReleased() throws IOException {

        CountingSessionFactory sessionFactory = new CountingSessionFactory(directory);

        LuceneSession session = sessionFactory.openSession(false);

        //Lease one writer
        session.beginAppend().addDocument(getDocument());
        session.flush();

        //Lease searcher 1
        LuceneQuery query = session.createQuery();
        assertEquals(1, query.where(year.gt(1800)).count());

        session.beginAppend().addDocument(getDocument());
        //Release searcher 1
        session.flush();

        //Lease searcher 2
        query = session.createQuery();
        assertEquals(2, query.where(year.gt(1800)).count());

        //Release searcher 2 and writer
        session.close();

        // Second session
        session = sessionFactory.openSession(true);
        //Lease searcher 3
        query = session.createQuery();
        assertEquals(2, query.where(year.gt(1800)).count());
        //Release searcher 3
        session.close();

        assertEquals(4, sessionFactory.leasables.size());
        assertEquals(4, sessionFactory.leases.size());
        assertEquals(4, sessionFactory.releases.size());

        //First one should be writer
        assertTrue(sessionFactory.leasables.get(0) instanceof FileLockingWriter);
        assertTrue(sessionFactory.leasables.get(1) instanceof LuceneSearcher);
        assertTrue(sessionFactory.leasables.get(2) instanceof LuceneSearcher);
        assertTrue(sessionFactory.leasables.get(3) instanceof LuceneSearcher);
        
        //The writer should be closed
        assertEquals(1, (int) sessionFactory.leases.get(sessionFactory.leasables.get(0)));
        assertEquals(1, (int) sessionFactory.releases.get(sessionFactory.leasables.get(0)));

        // First and second searchers should be released totally
        assertEquals(2, (int) sessionFactory.leases.get(sessionFactory.leasables.get(1)));
        assertEquals(2, (int) sessionFactory.releases.get(sessionFactory.leasables.get(1)));

        assertEquals(2, (int) sessionFactory.leases.get(sessionFactory.leasables.get(2)));
        assertEquals(2, (int) sessionFactory.releases.get(sessionFactory.leasables.get(2)));

        // Third searcher leaves it as current
        assertEquals(2, (int) sessionFactory.leases.get(sessionFactory.leasables.get(3)));
        assertEquals(1, (int) sessionFactory.releases.get(sessionFactory.leasables.get(3)));


    }
    
    @Test
    public void StringPathCreationWorks() throws IOException {
        sessionFactory = new LuceneSessionFactoryImpl("target/stringpathtest");
        LuceneSession session = sessionFactory.openSession(false);
        session.beginReset().addDocument(getDocument());
        session.flush();
        assertEquals(1, session.createQuery().where(year.gt(1800)).count());
        session.close();
    }
    
    @Test(expected=QueryException.class)
    public void GetsQueryException() throws IOException {
        String path = "target/exceptiontest";
        sessionFactory = new LuceneSessionFactoryImpl(path);
        LuceneSession session = sessionFactory.openSession(false);
        session.beginAppend().addDocument(getDocument());
        FileUtils.deleteDirectory(new File(path));
        session.close();
    }
    
}
