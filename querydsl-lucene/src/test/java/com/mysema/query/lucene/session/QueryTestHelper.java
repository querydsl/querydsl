package com.mysema.query.lucene.session;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;

public final class QueryTestHelper {

    public static void addData(LuceneSessionFactory sessionFactory) {
        LuceneSession session = sessionFactory.openSession(false);
        createDocuments(session);
        session.close(); 
    }

    public static Document getDocument() {
        return createDocument("Sankarin seisaus",
                "Veronica Pimenoff",
                "Päivät kuluvat, ilmastointi saa nenän vuotamaan verta",
                2010,
                80.00);
    }
    
    public static void createDocuments(LuceneSession session) {
        
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
                20.00))
            .addDocument(getDocument());
       
    }

    public static Document createDocument(
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
