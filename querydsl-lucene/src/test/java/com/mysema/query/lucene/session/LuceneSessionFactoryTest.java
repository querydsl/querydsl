package com.mysema.query.lucene.session;

import static com.mysema.query.lucene.session.QueryTestHelper.addData;
import static com.mysema.query.lucene.session.QueryTestHelper.createDocument;
import static com.mysema.query.lucene.session.QueryTestHelper.createDocuments;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.lucene.session.impl.LuceneSessionFactoryImpl;
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
        //Testing the queries work through session
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
        
        //Now we will see the three documents
        LuceneQuery query = session.createQuery();
        assertEquals(4, query.where(year.gt(1800)).count());
        
        //Adding new document
        session.beginAppend().addDocument(createDocument("title","author", "", 2010, 1));
        
        //New query will not see the addition
        query = session.createQuery();
        assertEquals(4, query.where(year.gt(1800)).count());
        
        session.flush();
        
        //This will see the addition
        LuceneQuery query1 = session.createQuery();
        assertEquals(5, query1.where(year.gt(1800)).count());
        
        //The old query still sees the same 3
        assertEquals(4, query.count());
        
        session.close();
    }
    
    @Test(expected=NoSessionBoundException.class)
    public void testCurrentSession() {
        sessionFactory.getCurrentSession();
    }
    
   
    @Test(expected=SessionReadOnlyException.class)
    public void testReadonly() {
        LuceneSession session = sessionFactory.openSession(true);
        session.beginOverwrite();
    }

    @Test(expected=SessionClosedException.class)
    public void testSessionClosedCreate() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.createQuery();
    }
    
    @Test(expected=SessionClosedException.class)
    public void testSessionClosedAppend() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.beginAppend();
    }
    
    @Test(expected=SessionClosedException.class)
    public void testSessionClosedFlush() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.flush();
    }
    
    @Test(expected=SessionClosedException.class)
    public void testSessionClosedClosed() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.close();
    }
    
    @Test(expected=SessionClosedException.class)
    public void testSessionClosedOverwrite() {
        LuceneSession session = sessionFactory.openSession(false);
        session.close();
        session.beginOverwrite();
    }
    
    @Test
    public void testResourcesAreReleased() {
        
        
        
        
        
    }
    


}
