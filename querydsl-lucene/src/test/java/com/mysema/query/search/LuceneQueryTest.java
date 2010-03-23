package com.mysema.query.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.mysema.query.QueryException;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;

/*
 * TODO Refactor SimpleTest and LuceneQuery into same test class, lot of the setUp stuff is similar?
 */
public class LuceneQueryTest {
    private LuceneQuery query;
    private PathBuilder<Object> entityPath;
    private PString title;
    private PString year;

    private RAMDirectory idx;
    private IndexWriter writer;
    private Searcher searcher;

    private Document createDocument(String docTitle, String docAuthor, String docText, String docYear) {
        Document doc = new Document();

        doc.add(new Field("title", docTitle, Store.YES, Index.ANALYZED));
        doc.add(new Field("author", docAuthor, Store.YES, Index.ANALYZED));
        doc.add(new Field("text", docText, Store.YES, Index.ANALYZED));
        doc.add(new Field("year", docYear, Store.YES, Index.ANALYZED));

        return doc;
    }

    @Before
    public void setUp() throws Exception {
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        title = entityPath.getString("title");
        year = entityPath.getString("year");

        idx = new RAMDirectory();
        writer = new IndexWriter(idx, new StandardAnalyzer(Version.LUCENE_CURRENT), true, MaxFieldLength.UNLIMITED);

        writer.addDocument(createDocument("Jurassic Park", "Michael Crichton",
                "It's a UNIX system! I know this!", "1990"));
        writer.addDocument(createDocument("Nummisuutarit", "Aleksis Kivi",
                "ESKO. Ja iloitset ja riemuitset?", "1864"));
        writer.addDocument(createDocument(
                        "The Lord of the Rings",
                        "John R. R. Tolkien",
                        "One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them",
                        "1954"));
        writer.addDocument(createDocument(
                "Introduction to Algorithms",
                "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, and Clifford Stein",
                "Bubble sort",
                "1990"));

        writer.optimize();
        writer.close();

        searcher = new IndexSearcher(idx);
        query = new LuceneQuery(null, new LuceneSerializer(true), searcher);
    }

    @After
    public void tearDown() throws Exception {
        searcher.close();
    }

    @Test
    public void count() {
        query.where(title.eq("Jurassic Park"));
        assertEquals(1, query.count());
    }

    @Test
    public void list() {
        query.where(year.between("1800", "2000"));
        query.orderBy(year.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_Sorted_Ascending_By_Year() {
        query.where(year.between("1800", "2000"));
        query.orderBy(year.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1864", documents.get(0).get("year"));
        assertEquals("1954", documents.get(1).get("year"));
        assertEquals("1990", documents.get(2).get("year"));
        assertEquals("1990", documents.get(3).get("year"));
    }

    @Test
    public void list_Sorted_Descending_By_Year() {
        query.where(year.between("1800", "2000"));
        query.orderBy(year.desc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("1954", documents.get(2).get("year"));
        assertEquals("1864", documents.get(3).get("year"));
    }

    @Test
    public void list_Sorted_Descending_By_Year_And_Ascending_By_Title() {
        query.where(year.between("1800", "2000"));
        query.orderBy(year.desc());
        query.orderBy(title.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("Introduction to Algorithms", documents.get(0).get("title"));
        assertEquals("Jurassic Park", documents.get(1).get("title"));
    }

    @Test
    public void list_Sorted_Descending_By_Year_And_Descending_By_Title() {
        query.where(year.between("1800", "2000"));
        query.orderBy(year.desc());
        query.orderBy(title.desc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("Jurassic Park", documents.get(0).get("title"));
        assertEquals("Introduction to Algorithms", documents.get(1).get("title"));
    }

    @Test
    public void uniqueResult() {
        query.where(title.startsWith("Nummi"));
        Document document = query.uniqueResult();
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test(expected = QueryException.class)
    public void uniqueResult_Finds_More_Than_One_Result() {
        query.where(year.eq("1990"));
        query.uniqueResult();
    }

}
