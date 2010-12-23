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

import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

public class LuceneSessionImplTest {

    public class QDocument extends EntityPathBase<Document> {

        private static final long serialVersionUID = -4872833626508344081L;

        public QDocument(final String var) {
            super(Document.class, PathMetadataFactory.forVariable(var));
        }

        public final NumberPath<Integer> year = createNumber("year", Integer.class);

        public final StringPath title = createString("title");

        public final NumberPath<Double> gross = createNumber("gross", Double.class);
    }

    private LuceneSession session;

    private Directory directory;

    private StringPath title;

//    private NumberPath<Integer> year;
//
//    private NumberPath<Double> gross;

    @Before
    public void before() throws IOException {
        directory = new RAMDirectory();
        session = new LuceneSessionImpl(directory);
        final QDocument entityPath = new QDocument("doc");
        title = entityPath.title;
//        year = entityPath.year;
//        gross = entityPath.gross;
    }

    @Test
    public void testCreate() throws IOException {

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

//        List<Document> results = session.query(new QueryCallback<List<Document>>() {
//            public List<Document> query(LuceneQuery query) {
//
//                return query.where(title.eq("Jurassic Park")).list();
//
//            }
//        });
        
        List<Document> results = session.createQuery().where(title.eq("Jurassic Park")).list();

        assertEquals(1, results.size());
        assertEquals("Jurassic Park", results.get(0).getField("title").stringValue());

//        Long count = session.query(new QueryCallback<Long>() {
//            public Long query(LuceneQuery query) {
//                //return query.where(title.ne("AA")).count(); 
//                
//                return  query.where(title.startsWith("Nummi")).count();
//
//            }
//        });
        
        Long count = session.createQuery().where(title.startsWith("Nummi")).count();

        assertEquals(1, (long) count);
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
