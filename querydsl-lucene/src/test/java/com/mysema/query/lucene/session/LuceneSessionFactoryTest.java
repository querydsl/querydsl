package com.mysema.query.lucene.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.lucene.LuceneQuery;
import com.mysema.query.types.path.StringPath;

public class LuceneSessionFactoryTest {

    private LuceneSessionFactory sessionFactory;

    private Directory directory;

    private StringPath title;

    @Before
    public void before() throws IOException {
        directory = new RAMDirectory();
        sessionFactory = new LuceneSessionFactoryImpl(directory);

        final QDocument entityPath = new QDocument("doc");
        title = entityPath.title;
    }

    @Test
    public void testCreate() throws IOException {

        LuceneSession session = sessionFactory.openSession(false);

        IndexWriter writer = session.createOverwriteWriter();

        writer.addDocument(createDocument(
                "Jurassic Park",
                "Michael Crichton",
                "It's a UNIX system! I know this!",
                1990,
                90.00));
        writer.addDocument(createDocument(
                "Nummisuutarit",
                "Aleksis Kivi",
                "ESKO. Ja iloitset ja riemuitset?",
                1864,
                10.00));
        
        session.flush();
        
        //Testing the write
        IndexSearcher searcher = new IndexSearcher(directory);
        Document doc1 = searcher.doc(0);
        Document doc2 = searcher.doc(1);
        
        assertNotNull(doc1);
        assertNotNull(doc2);
        
        assertEquals("Jurassic Park", doc1.getField("title").stringValue());
        assertEquals("Nummisuutarit", doc2.getField("title").stringValue());
        
        LuceneQuery query = session.createQuery();
        List<Document> results = query.where(title.eq("Jurassic Park")).list();

        assertEquals(1, results.size());
        assertEquals("Jurassic Park", results.get(0).getField("title").stringValue());

        //TODO Kysely ei toimi, jos ei ota uutta LuceneQuery objektia
        query = session.createQuery();
        long count = query.where(title.startsWith("Nummi")).count();
        assertEquals(1, count);
        
        // TODO Tästä tulee 0 eikä 2!!
        // query.where(title.ne("AA")).count();

        session.close();
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
