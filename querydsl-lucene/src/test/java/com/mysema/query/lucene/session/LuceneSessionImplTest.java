package com.mysema.query.lucene.session;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.junit.Before;
import org.junit.Test;

public class LuceneSessionImplTest {

    private LuceneSession session;

    private Directory directory;

    private final QDocument document = new QDocument("doc");
    
    @Before
    public void before() throws IOException {
        directory = new RAMDirectory();
        session = new LuceneSessionImpl(directory);
    }

    @Test
    public void Create() throws IOException {

        session.updateNew(new WriteCallback() {
            public void write(IndexWriter writer){
                try {
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
                } catch (CorruptIndexException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                

            }
        });

        List<Document> results = session.createQuery().where(document.title.eq("Jurassic Park")).list();
        assertEquals(1, results.size());
        assertEquals("Jurassic Park", results.get(0).getField("title").stringValue());

        Long count = session.createQuery().where(document.title.startsWith("Nummi")).count();
        assertEquals(1, (long) count);
    }

    private Document createDocument(
            String docTitle,
            String docAuthor,
            String docText,
            int docYear,
            double docGross) {
        Document doc = new Document();

        doc.add(new Field("title", docTitle, Store.YES, Index.ANALYZED));
        doc.add(new Field("author", docAuthor, Store.YES, Index.ANALYZED));
        doc.add(new Field("text", docText, Store.YES, Index.ANALYZED));
        doc.add(new NumericField("year", Store.YES, true).setIntValue(docYear));
        doc.add(new NumericField("gross", Store.YES, true).setDoubleValue(docGross));

        return doc;
    }

}
