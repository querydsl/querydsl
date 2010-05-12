/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.lucene;

import static org.junit.Assert.*;
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
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathMetadataFactory;

/**
 * Tests for LuceneQuery
 *
 * @author vema
 *
 */
public class LuceneQueryTest {

    public class QDocument extends PEntity<Document>{

        private static final long serialVersionUID = -4872833626508344081L;

        public QDocument(String var) {
            super(Document.class, PathMetadataFactory.forVariable(var));
        }

        public final PString year = createString("year");

        public final PString title = createString("title");

    }

    private LuceneQuery query;
//    private PathBuilder<Object> entityPath;
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
//        entityPath = new PathBuilder<Object>(Object.class, "obj");
//        title = entityPath.getString("title");
//        year = entityPath.getString("year");

        QDocument entityPath = new QDocument("doc");
        title = entityPath.title;
        year = entityPath.year;

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
        query = new LuceneQuery(true, searcher);
    }

    @After
    public void tearDown() throws Exception {
        searcher.close();
    }

    @Test(expected = QueryException.class)
    public void count_Empty_Where_Clause() {
        query.count();
    }

    @Test
    public void count() {
        query.where(title.eq("Jurassic Park"));
        assertEquals(1, query.count());
    }

    @Test
    public void countDistinct() {
        query.where(year.like("19*").or(title.like("The Lord*")));
        assertEquals(3, query.countDistinct());
    }

    @Test
    public void list_Sorted_By_Year_Ascending() {
        query.where(year.between("1800", "2000"));
        query.orderBy(year.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_Not_Sorted() {
        query.where(year.between("1800", "2000"));
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_Not_Sorted_Limit_2() {
        query.where(year.between("1800", "2000"));
        query.limit(2);
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
    }

    @Test
    public void list_Sorted_By_Year_Limit_1() {
        query.where(year.between("1800", "2000"));
        query.limit(1);
        query.orderBy(year.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(1, documents.size());
    }

    @Test
    public void list_Not_Sorted_Offset_2() {
        query.where(year.between("1800", "2000"));
        query.offset(2);
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
    }

    @Test
    public void list_Sorted_Ascending_By_Year_Offset_2() {
        query.where(year.between("1800", "2000"));
        query.offset(2);
        query.orderBy(year.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
    }

    @Test
    public void list_Sorted_Ascending_By_Year_Restrict_Limit_2_Offset_1() {
        query.where(year.between("1800", "2000"));
        query.restrict(new QueryModifiers(2l, 1l));
        query.orderBy(year.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
        assertEquals("1954", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
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
    
    @Test
    public void uniqueResult_Finds_No_Results() {
        query.where(year.eq("2200"));
        assertNull(query.uniqueResult());
    }

    @Test
    public void listDistinct() {
        query.where(year.between("1900", "2000").or(title.startsWith("Jura")));
        query.orderBy(year.asc());
        List<Document> documents = query.listDistinct();
        assertFalse(documents.isEmpty());
        assertEquals(3, documents.size());
    }

    @Test
    public void listResults() {
        query.where(year.between("1800", "2000"));
        query.restrict(new QueryModifiers(2l, 1l));
        query.orderBy(year.asc());
        SearchResults<Document> results = query.listResults();
        assertFalse(results.isEmpty());
        assertEquals("1954", results.getResults().get(0).get("year"));
        assertEquals("1990", results.getResults().get(1).get("year"));
        assertEquals(2, results.getLimit());
        assertEquals(1, results.getOffset());
        assertEquals(4, results.getTotal());
    }

    @Test
    public void listDistinctResults() {
        query.where(year.between("1800", "2000").or(title.eq("The Lord of the Rings")));
        query.restrict(new QueryModifiers(1l, 1l));
        query.orderBy(year.asc());
        SearchResults<Document> results = query.listDistinctResults();
        assertFalse(results.isEmpty());
        assertEquals("1954", results.getResults().get(0).get("year"));
        assertEquals(1, results.getLimit());
        assertEquals(1, results.getOffset());
        assertEquals(4, results.getTotal());
    }

    @Test
    public void list_All() {
        List<Document> results = query.where(title.like("*")).orderBy(title.asc(), year.desc()).list();
        assertEquals(4, results.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Sorted_Ascending_Limit_Negative() {
        query.where(year.between("1800", "2000"));
        query.limit(-1);
        query.orderBy(year.asc());
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Not_Sorted_Limit_Negative() {
        query.where(year.between("1800", "2000"));
        query.limit(-1);
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Sorted_Ascending_Limit_0() {
        query.where(year.between("1800", "2000"));
        query.limit(0);
        query.orderBy(year.asc());
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Not_Sorted_Limit_0() {
        query.where(year.between("1800", "2000"));
        query.limit(0);
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Sorted_Ascending_Offset_Negative() {
        query.where(year.between("1800", "2000"));
        query.offset(-1);
        query.orderBy(year.asc());
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Not_Sorted_Offset_Negative() {
        query.where(year.between("1800", "2000"));
        query.offset(-1);
        query.list();
    }

    @Test
    public void list_Sorted_Ascending_Offset_0() {
        query.where(year.between("1800", "2000"));
        query.offset(0);
        query.orderBy(year.asc());
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_Not_Sorted_Offset_0() {
        query.where(year.between("1800", "2000"));
        query.offset(0);
        List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }
}
