/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.lucene;

import static org.easymock.EasyMock.createMockBuilder;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.DuplicateFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.NonUniqueResultException;
import com.mysema.query.QueryException;
import com.mysema.query.QueryModifiers;
import com.mysema.query.SearchResults;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.expr.Param;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;

/**
 * Tests for LuceneQuery
 *
 * @author vema
 *
 */
public class LuceneQueryTest {

    public class QDocument extends EntityPathBase<Document> {

        private static final long serialVersionUID = -4872833626508344081L;

        public QDocument(final String var) {
            super(Document.class, PathMetadataFactory.forVariable(var));
        }

        public final NumberPath<Integer> year = createNumber("year",
                Integer.class);
        public final StringPath title = createString("title");
        public final NumberPath<Double> gross = createNumber("gross",
                Double.class);
    }

    private LuceneQuery query;
    private StringPath title;
    private NumberPath<Integer> year;
    private NumberPath<Double> gross;

    private final StringPath sort = new StringPath("sort");

    private RAMDirectory idx;
    private IndexWriter writer;
    private Searcher searcher;

    private Document createDocument(final String docTitle,
            final String docAuthor, final String docText, final int docYear,
            final double docGross) {
        final Document doc = new Document();

        doc.add(new Field("title", docTitle, Store.YES, Index.ANALYZED));
        doc.add(new Field("author", docAuthor, Store.YES, Index.ANALYZED));
        doc.add(new Field("text", docText, Store.YES, Index.ANALYZED));
        doc.add(new NumericField("year", Store.YES, true).setIntValue(docYear));
        doc.add(new NumericField("gross", Store.YES, true)
                .setDoubleValue(docGross));

        return doc;
    }

    @Before
    public void setUp() throws Exception {
        final QDocument entityPath = new QDocument("doc");
        title = entityPath.title;
        year = entityPath.year;
        gross = entityPath.gross;

        idx = new RAMDirectory();
        writer = createWriter(idx);

        writer.addDocument(createDocument("Jurassic Park", "Michael Crichton",
                "It's a UNIX system! I know this!", 1990, 90.00));
        writer.addDocument(createDocument("Nummisuutarit", "Aleksis Kivi",
                "ESKO. Ja iloitset ja riemuitset?", 1864, 10.00));
        writer
                .addDocument(createDocument(
                        "The Lord of the Rings",
                        "John R. R. Tolkien",
                        "One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them",
                        1954, 89.00));
        writer
                .addDocument(createDocument(
                        "Introduction to Algorithms",
                        "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, and Clifford Stein",
                        "Bubble sort", 1990, 30.50));

        writer.optimize();
        writer.close();

        searcher = new IndexSearcher(idx);
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
    }

    private IndexWriter createWriter(RAMDirectory idx2) throws Exception {
        return new IndexWriter(idx, new StandardAnalyzer(Version.LUCENE_30), true,
                               MaxFieldLength.UNLIMITED);
    }

    @After
    public void tearDown() throws Exception {
        searcher.close();
    }

    public void count_Empty_Where_Clause() {
        assertEquals(4, query.count());
    }

    @Test
    public void count() {
        query.where(title.eq("Jurassic Park"));
        assertEquals(1, query.count());
    }

    @Test(expected = QueryException.class)
    public void count_Index_Problem() throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.maxDoc()).andThrow(new IOException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.count();
        verify(searcher);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void countDistinct() {
        query.where(year.between(1900, 3000));
        assertEquals(3, query.countDistinct());
    }

    @Test
    public void list_Sorted_By_Year_Ascending() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.asc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_Not_Sorted() {
        query.where(year.between(1800, 2000));
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void sorted_By_Different_Locales() throws Exception {
        Document d1 = new Document();
        Document d2 = new Document();
        Document d3 = new Document();
        d1.add(new Field("sort", "a\u00c4",Store.YES, Index.NOT_ANALYZED));
        d2.add(new Field("sort", "ab",Store.YES, Index.NOT_ANALYZED));
        d3.add(new Field("sort", "aa",Store.YES, Index.NOT_ANALYZED));
        writer = createWriter(idx);
        writer.addDocument(d1);
        writer.addDocument(d2);
        writer.addDocument(d3);
        writer.close();

        searcher = new IndexSearcher(idx);
        query = new LuceneQuery(new LuceneSerializer(true, true, Locale.ENGLISH), searcher);
        assertEquals(3, query.list().size());
        List<Document> results = query.where(sort.startsWith("a")).orderBy(sort.asc()).list();
        assertEquals(3, results.size());
        assertEquals("aa", results.get(0).getFieldable("sort").stringValue());
        assertEquals("a\u00c4", results.get(1).getFieldable("sort").stringValue());
        assertEquals("ab", results.get(2).getFieldable("sort").stringValue());

        query = new LuceneQuery(new LuceneSerializer(true, true, new Locale("fi", "FI")), searcher);
        results = query.where(sort.startsWith("a")).orderBy(sort.asc()).list();
        assertEquals("aa", results.get(0).getFieldable("sort").stringValue());
        assertEquals("ab", results.get(1).getFieldable("sort").stringValue());
        assertEquals("a\u00c4", results.get(2).getFieldable("sort").stringValue());


    }

    @Test
    public void list_Not_Sorted_Limit_2() {
        query.where(year.between(1800, 2000));
        query.limit(2);
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
    }

    @Test
    public void list_Sorted_By_Year_Limit_1() {
        query.where(year.between(1800, 2000));
        query.limit(1);
        query.orderBy(year.asc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(1, documents.size());
    }

    @Test
    public void list_Not_Sorted_Offset_2() {
        query.where(year.between(1800, 2000));
        query.offset(2);
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
    }

    @Test
    public void list_Sorted_Ascending_By_Year_Offset_2() {
        query.where(year.between(1800, 2000));
        query.offset(2);
        query.orderBy(year.asc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
    }

    @Test
    public void list_Sorted_Ascending_By_Year_Restrict_Limit_2_Offset_1() {
        query.where(year.between(1800, 2000));
        query.restrict(new QueryModifiers(2l, 1l));
        query.orderBy(year.asc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
        assertEquals("1954", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
    }

    @Test
    public void list_Sorted_Ascending_By_Year() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.asc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1864", documents.get(0).get("year"));
        assertEquals("1954", documents.get(1).get("year"));
        assertEquals("1990", documents.get(2).get("year"));
        assertEquals("1990", documents.get(3).get("year"));
    }

    @Test
    public void list_Distinct_Property(){
        assertEquals(4, query.list().size());
        assertEquals(3, query.distinct(year).list().size());
    }

    @Test
    public void list_With_Filter(){
        Filter filter = new DuplicateFilter("year");
        assertEquals(4, query.list().size());
        assertEquals(3, query.filter(filter).list().size());
    }

    @Test
    public void count_Distinct_Property(){
        assertEquals(4l, query.count());
        assertEquals(3l, query.distinct(year).count());
    }

    @Test
    public void list_Sorted_Descending_By_Year() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.desc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("1954", documents.get(2).get("year"));
        assertEquals("1864", documents.get(3).get("year"));
    }

    @Test
    public void list_Sorted_Descending_By_Gross() {
        query.where(gross.between(0.0, 1000.00));
        query.orderBy(gross.desc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("90.0", documents.get(0).get("gross"));
        assertEquals("89.0", documents.get(1).get("gross"));
        assertEquals("30.5", documents.get(2).get("gross"));
        assertEquals("10.0", documents.get(3).get("gross"));
    }

    @Test
    public void list_Sorted_Descending_By_Year_And_Ascending_By_Title() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.desc());
        query.orderBy(title.asc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("Introduction to Algorithms", documents.get(0)
                .get("title"));
        assertEquals("Jurassic Park", documents.get(1).get("title"));
    }

    @Test
    public void list_Sorted_Descending_By_Year_And_Descending_By_Title() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.desc());
        query.orderBy(title.desc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("Jurassic Park", documents.get(0).get("title"));
        assertEquals("Introduction to Algorithms", documents.get(1)
                .get("title"));
    }

    @Ignore
    @Test(expected = QueryException.class)
    public void list_Index_Problem_In_Max_Doc() throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.maxDoc()).andThrow(new IOException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.list();
        verify(searcher);
    }

    @Ignore
    @Test(expected = QueryException.class)
    public void list_Sorted_Index_Problem_In_Max_Doc() throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.maxDoc()).andThrow(new IOException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.orderBy(title.asc());
        query.list();
        verify(searcher);
    }

    @Test
    public void offset() {
        assertTrue(query.where(title.eq("Jurassic Park")).offset(30).list()
                .isEmpty());
    }

    @Test
    public void singleResult() {
        assertNotNull(query.where(title.ne("")).singleResult());
    }

    @Test(expected=NonUniqueResultException.class)
    public void uniqueResult_Contract() {
        query.where(title.ne("")).uniqueResult();
    }

    @Test
    public void Unique_Result_Takes_Limit() {
        assertEquals("Jurassic Park", query
                                        .where(title.ne(""))
                                        .limit(1)
                                        .uniqueResult().get("title"));
    }

    @Test
    public void Unique_Result_Considers_Limit_And_Actual_Result_Size() {
        query.where(title.startsWith("Nummi"));
        final Document document = query.limit(3).uniqueResult();
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test
    public void Unique_Result_Returns_Null_If_Nothing_Is_In_Range() {
        query.where(title.startsWith("Nummi"));
        assertNull(query.offset(10).uniqueResult());
    }

    @Test
    public void Unique_Result_Considers_Offset() {
        assertEquals("Introduction to Algorithms", query.where(title.ne("")).offset(3).uniqueResult().get("title"));
    }

    @Test
    public void Unique_Result_Considers_Limit_And_Offset() {
        assertEquals("The Lord of the Rings", query.where(title.ne("")).limit(1).offset(2).uniqueResult().get("title"));
    }

    @Test
    public void uniqueResult() {
        query.where(title.startsWith("Nummi"));
        final Document document = query.uniqueResult();
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test
    public void uniqueResult_With_Param() {
        final Param<String> param = new Param<String>(String.class, "title");
        query.set(param, "Nummi");
        query.where(title.startsWith(param));
        final Document document = query.uniqueResult();
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test(expected = ParamNotSetException.class)
    public void uniqueResult_Param_Not_Set() {
        final Param<String> param = new Param<String>(String.class, "title");
        query.where(title.startsWith(param));
        query.uniqueResult();
    }

    @Test(expected = QueryException.class)
    public void uniqueResult_Finds_More_Than_One_Result() {
        query.where(year.eq(1990));
        query.uniqueResult();
    }

    @Test
    public void uniqueResult_Finds_No_Results() {
        query.where(year.eq(2200));
        assertNull(query.uniqueResult());
    }

    @Test
    public void uniqueResult_Finds_No_Results_Because_No_Documents_In_Index()
            throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.maxDoc()).andReturn(0);
        replay(searcher);
        assertNull(query.where(year.eq(3000)).uniqueResult());
        verify(searcher);
    }

    @Test(expected = QueryException.class)
    public void uniqueResult_Sorted_Index_Problem_In_Max_Doc()
            throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.maxDoc()).andThrow(new IOException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.uniqueResult();
        verify(searcher);
    }

    @Test
    public void count_Returns_0_Because_No_Documents_In_Index()
            throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.maxDoc()).andReturn(0);
        replay(searcher);
        assertEquals(0, query.where(year.eq(3000)).count());
        verify(searcher);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void listDistinct() {
        query.where(year.between(1900, 2000).or(title.startsWith("Jura")));
        query.orderBy(year.asc());
        final List<Document> documents = query.listDistinct();
        assertFalse(documents.isEmpty());
        assertEquals(3, documents.size());
    }

    @Test
    public void listResults() {
        query.where(year.between(1800, 2000));
        query.restrict(new QueryModifiers(2l, 1l));
        query.orderBy(year.asc());
        final SearchResults<Document> results = query.listResults();
        assertFalse(results.isEmpty());
        assertEquals("1954", results.getResults().get(0).get("year"));
        assertEquals("1990", results.getResults().get(1).get("year"));
        assertEquals(2, results.getLimit());
        assertEquals(1, results.getOffset());
        assertEquals(4, results.getTotal());
    }

    @Test(expected=UnsupportedOperationException.class)
    public void listDistinctResults() {
        query.where(year.between(1800, 2000).or(
                title.eq("The Lord of the Rings")));
        query.restrict(new QueryModifiers(1l, 1l));
        query.orderBy(year.asc());
        final SearchResults<Document> results = query.listDistinctResults();
        assertFalse(results.isEmpty());
        assertEquals("1954", results.getResults().get(0).get("year"));
        assertEquals(1, results.getLimit());
        assertEquals(1, results.getOffset());
        assertEquals(4, results.getTotal());
    }

    @Test
    public void list_All() {
        final List<Document> results = query.where(title.like("*")).orderBy(
                title.asc(), year.desc()).list();
        assertEquals(4, results.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Sorted_Ascending_Limit_Negative() {
        query.where(year.between(1800, 2000));
        query.limit(-1);
        query.orderBy(year.asc());
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Not_Sorted_Limit_Negative() {
        query.where(year.between(1800, 2000));
        query.limit(-1);
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Sorted_Ascending_Limit_0() {
        query.where(year.between(1800, 2000));
        query.limit(0);
        query.orderBy(year.asc());
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Not_Sorted_Limit_0() {
        query.where(year.between(1800, 2000));
        query.limit(0);
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Sorted_Ascending_Offset_Negative() {
        query.where(year.between(1800, 2000));
        query.offset(-1);
        query.orderBy(year.asc());
        query.list();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_Not_Sorted_Offset_Negative() {
        query.where(year.between(1800, 2000));
        query.offset(-1);
        query.list();
    }

    @Test
    public void list_Sorted_Ascending_Offset_0() {
        query.where(year.between(1800, 2000));
        query.offset(0);
        query.orderBy(year.asc());
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_Not_Sorted_Offset_0() {
        query.where(year.between(1800, 2000));
        query.offset(0);
        final List<Document> documents = query.list();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void iterate() {
        query.where(year.between(1800, 2000));
        final Iterator<Document> iterator = query.iterate();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            ++count;
        }
        assertEquals(4, count);
    }

    @Test
    public void All_By_Excluding_Where() {
        assertEquals(4, query.list().size());
    }

    @Test
    public void Empty_Index_Should_Return_Empty_List() throws Exception {
        idx = new RAMDirectory();
        writer = new IndexWriter(idx, new StandardAnalyzer(
                Version.LUCENE_30), true, MaxFieldLength.UNLIMITED);
        writer.optimize();
        writer.close();
        searcher = new IndexSearcher(idx);
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        assertTrue(query.list().isEmpty());
    }

    @Test(expected = QueryException.class)
    public void List_Results_Throws_An_Illegal_Argument_Exception_When_Sum_Of_Limit_And_Offset_Is_Negative() {
        query.limit(1).offset(Integer.MAX_VALUE).listResults();
    }
}
