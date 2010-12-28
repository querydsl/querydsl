package com.mysema.query.lucene.session;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.ReadOnlyBufferException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.QueryException;
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

        addData();
                  
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
        assertEquals(3, query.where(year.gt(1800)).count());
        
        //Adding new document
        session.beginAppend().addDocument(createDocument("title","author", "", 2010, 1));
        
        //New query will not see the addition
        query = session.createQuery();
        assertEquals(3, query.where(year.gt(1800)).count());
        
        session.flush();
        
        //This will see the addition
        LuceneQuery query1 = session.createQuery();
        assertEquals(4, query1.where(year.gt(1800)).count());
        
        //The old query still sees the same 3
        assertEquals(3, query.count());
             
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
    
    private void addData() {
        LuceneSession session = sessionFactory.openSession(false);
        createDocuments(session);
        session.close(); 
    }

    
    
    private void createDocuments(LuceneSession session) {
        
        session.beginAppend()
           .addDocument(createDocument(
                "Jurassic Park",
                "Michael Crichton",
                "It's a UNIX system! I know this!",
                1990,
                90.00))
           .addDocument(createDocument(
                "Nummisuutarit",
                "Aleksis Kivi",
                "ESKO. Ja iloitset ja riemuitset?",
                1864,
                10.00))
            .addDocument(createDocument(
                "Hobitti",
                "J.R.R Tolkien",
                "Miten voin palvella teitä, hyvät kääpiöt?",
                1937,
                20.00));
       
    }

    private Document createDocument(
            final String docTitle,
            final String docAuthor,
            final String docText,
            final int docYear,
            final double docGross) {
        final Document doc = new Document();

        doc.add(new Field("title", docTitle, Store.YES, Index.ANALYZED));
        doc.add(new Field("author", docAuthor, Store.YES, Index.ANALYZED));
        doc.add(new Field("text", docText, Store.YES, Index.ANALYZED));
        doc.add(new NumericField("year", Store.YES, true).setIntValue(docYear));
        doc.add(new NumericField("gross", Store.YES, true).setDoubleValue(docGross));

        return doc;
    }

}
