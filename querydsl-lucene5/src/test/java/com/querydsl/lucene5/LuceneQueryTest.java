/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.lucene5;

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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleDocValuesField;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.NumericDocValuesField;
import org.apache.lucene.document.SortedDocValuesField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.sandbox.queries.DuplicateFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Sort;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.BytesRef;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.google.common.collect.Sets;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryException;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.Param;
import com.querydsl.core.types.dsl.StringPath;

/**
 * Tests for LuceneQuery
 *
 * @author vema
 *
 */
public class LuceneQueryTest {

    private LuceneQuery query;
    private StringPath title;
    private NumberPath<Integer> year;
    private NumberPath<Double> gross;

    private final StringPath sort = Expressions.stringPath("sort");

    private RAMDirectory idx;
    private IndexWriter writer;
    private IndexSearcher searcher;
    private Field titleField = null;
    private SortedDocValuesField titleSortedField;
    private TextField authorField = null;
    private SortedDocValuesField authorSortedField;
    private TextField textField = null;
    private IntField yearField = null;
    private DoubleField grossField = null;
    private SortedDocValuesField textSortedField;
    private NumericDocValuesField yearSortedField;
    private DoubleDocValuesField grossSortedField;

    private Document createDocument(final String docTitle,
            final String docAuthor, final String docText, final int docYear,
            final double docGross) {
        Document doc = new Document();
        // Reusing field for performance
        if (titleField == null) {
            titleField = new TextField("title", docTitle, Store.YES);
            doc.add(titleField);
            titleSortedField = new SortedDocValuesField("title", new BytesRef(
                    docTitle));
            doc.add(titleSortedField);
        } else {
            titleField.setStringValue(docTitle);
            titleSortedField.setBytesValue(new BytesRef(docTitle));
            doc.add(titleField);
            doc.add(titleSortedField);
        }
        if (authorField == null) {
            authorField = new TextField("author", docAuthor, Store.YES);
            doc.add(authorField);
            authorSortedField = new SortedDocValuesField("author",
                    new BytesRef(docAuthor));
            doc.add(authorSortedField);

        } else {
            authorField.setStringValue(docAuthor);
            authorSortedField.setBytesValue(new BytesRef(docAuthor));
            doc.add(authorField);
            doc.add(authorSortedField);
        }
        if (textField == null) {
            textField = new TextField("text", docText, Store.YES);
            doc.add(textField);
            textSortedField = new SortedDocValuesField("text", new BytesRef(
                    docText));
            doc.add(textSortedField);
        } else {
            textField.setStringValue(docText);
            textSortedField.setBytesValue(new BytesRef(docText));
            doc.add(textField);
            doc.add(textSortedField);
        }
        if (yearField == null) {
            yearField = new IntField("year", docYear, Store.YES);
            doc.add(yearField);
            yearSortedField = new NumericDocValuesField("year", docYear);
            doc.add(yearSortedField);
        } else {
            yearField.setIntValue(docYear);
            yearSortedField.setLongValue(docYear);
            doc.add(yearField);
            doc.add(yearSortedField);
        }

        if (grossField == null) {
            grossField = new DoubleField("gross", docGross, Store.YES);
            doc.add(grossField);
            grossSortedField = new DoubleDocValuesField("gross", docGross);
            doc.add(grossSortedField);
        } else {
            grossField.setDoubleValue(docGross);
            grossSortedField.setDoubleValue(docGross);
            doc.add(grossField);
            doc.add(grossSortedField);
        }

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
        writer.addDocument(createDocument(
                "The Lord of the Rings",
                "John R. R. Tolkien",
                "One Ring to rule them all, One Ring to find them, One Ring to bring them all and in the darkness bind them",
                1954, 89.00));
        writer.addDocument(createDocument(
                "Introduction to Algorithms",
                "Thomas H. Cormen, Charles E. Leiserson, Ronald L. Rivest, and Clifford Stein",
                "Bubble sort", 1990, 30.50));

        writer.close();

        IndexReader reader = DirectoryReader.open(idx);
        searcher = new IndexSearcher(reader);
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
    }

    private IndexWriter createWriter(RAMDirectory idx) throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(new StandardAnalyzer())
                .setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        return new IndexWriter(idx, config);
    }

    @After
    public void tearDown() throws Exception {
        searcher.getIndexReader().close();
    }

    @Test
    public void between() {
        assertEquals(3, query.where(year.between(1950, 1990)).fetchCount());
    }

    @Test
    public void count_empty_where_clause() {
        assertEquals(4, query.fetchCount());
    }

    @Test
    public void exists() {
        assertTrue(query.where(title.eq("Jurassic Park")).fetchCount() > 0);
        assertFalse(query.where(title.eq("Jurassic Park X")).fetchCount() > 0);
    }

    @Test
    public void notExists() {
        assertFalse(query.where(title.eq("Jurassic Park")).fetchCount() == 0);
        assertTrue(query.where(title.eq("Jurassic Park X")).fetchCount() == 0);
    }

    @Test
    public void count() {
        query.where(title.eq("Jurassic Park"));
        assertEquals(1, query.fetchCount());
    }

    @Test(expected = QueryException.class)
    @Ignore
    public void count_index_problem() throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.getIndexReader().maxDoc()).andThrow(
                new IllegalArgumentException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.fetchCount();
        verify(searcher);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void countDistinct() {
        query.where(year.between(1900, 3000));
        assertEquals(3, query.distinct().fetchCount());
    }

    @Test
    public void in() {
        assertEquals(2, query.where(title.in("Jurassic Park", "Nummisuutarit")).fetchCount());
    }

    @Test
    public void in2() {
        assertEquals(3, query.where(year.in(1990, 1864)).fetchCount());
    }

    @Test
    public void in_toString() {
        assertEquals("year:`____F year:`____H", query.where(year.in(1990, 1864)).toString());
    }

    @Test
    public void list_sorted_by_year_ascending() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.asc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_not_sorted() {
        query.where(year.between(1800, 2000));
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    @Ignore
    // FIXME
    public void sorted_by_different_locales() throws Exception {
        Document d1 = new Document();
        Document d2 = new Document();
        Document d3 = new Document();
        d1.add(new TextField("sort", "a\u00c4", Store.YES));
        d2.add(new TextField("sort", "ab", Store.YES));
        d3.add(new TextField("sort", "aa", Store.YES));
        writer = createWriter(idx);
        writer.addDocument(d1);
        writer.addDocument(d2);
        writer.addDocument(d3);
        writer.close();

        IndexReader reader = DirectoryReader.open(writer, true);
        searcher = new IndexSearcher(reader);
        query = new LuceneQuery(
                new LuceneSerializer(true, true, Locale.ENGLISH), searcher);
        assertEquals(3, query.fetch().size());
        List<Document> results = query.where(sort.startsWith("a"))
                .orderBy(sort.asc()).fetch();
        assertEquals(3, results.size());
        assertEquals("aa", results.get(0).getField("sort").stringValue());
        assertEquals("a\u00c4", results.get(1).getField("sort").stringValue());
        assertEquals("ab", results.get(2).getField("sort").stringValue());

        query = new LuceneQuery(new LuceneSerializer(true, true, new Locale(
                "fi", "FI")), searcher);
        results = query.where(sort.startsWith("a")).orderBy(sort.asc()).fetch();
        assertEquals("aa", results.get(0).getField("sort").stringValue());
        assertEquals("ab", results.get(1).getField("sort").stringValue());
        assertEquals("a\u00c4", results.get(2).getField("sort").stringValue());
    }

    @Test
    public void list_not_sorted_limit_2() {
        query.where(year.between(1800, 2000));
        query.limit(2);
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
    }

    @Test
    public void list_sorted_by_year_limit_1() {
        query.where(year.between(1800, 2000));
        query.limit(1);
        query.orderBy(year.asc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(1, documents.size());
    }

    @Test
    public void list_not_sorted_offset_2() {
        query.where(year.between(1800, 2000));
        query.offset(2);
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
    }

    @Test
    public void list_sorted_ascending_by_year_offset_2() {
        query.where(year.between(1800, 2000));
        query.offset(2);
        query.orderBy(year.asc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
    }

    @Test
    public void list_sorted_ascending_by_year_restrict_limit_2_offset_1() {
        query.where(year.between(1800, 2000));
        query.restrict(new QueryModifiers(2L, 1L));
        query.orderBy(year.asc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(2, documents.size());
        assertEquals("1954", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
    }

    @Test
    public void list_sorted_ascending_by_year() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.asc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1864", documents.get(0).get("year"));
        assertEquals("1954", documents.get(1).get("year"));
        assertEquals("1990", documents.get(2).get("year"));
        assertEquals("1990", documents.get(3).get("year"));
    }

    @Test
    public void list_sort() {
        Sort sort = LuceneSerializer.DEFAULT.toSort(Collections
                .singletonList(year.asc()));

        query.where(year.between(1800, 2000));
        // query.orderBy(year.asc());
        query.sort(sort);
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1864", documents.get(0).get("year"));
        assertEquals("1954", documents.get(1).get("year"));
        assertEquals("1990", documents.get(2).get("year"));
        assertEquals("1990", documents.get(3).get("year"));
    }

    @Test
    public void list_distinct_property() {
        assertEquals(4, query.fetch().size());
        assertEquals(3, query.distinct(year).fetch().size());
    }

    @Test
    public void list_with_filter() {
        Filter filter = new DuplicateFilter("year");
        assertEquals(4, query.fetch().size());
        assertEquals(3, query.filter(filter).fetch().size());
    }

    @Test
    public void count_distinct_property() {
        assertEquals(4L, query.fetchCount());
        assertEquals(3L, query.distinct(year).fetchCount());
    }

    @Test
    public void list_sorted_descending_by_year() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.desc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("1954", documents.get(2).get("year"));
        assertEquals("1864", documents.get(3).get("year"));
    }

    @Test
    public void list_sorted_descending_by_gross() {
        query.where(gross.between(0.0, 1000.00));
        query.orderBy(gross.desc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("90.0", documents.get(0).get("gross"));
        assertEquals("89.0", documents.get(1).get("gross"));
        assertEquals("30.5", documents.get(2).get("gross"));
        assertEquals("10.0", documents.get(3).get("gross"));
    }

    @Test
    public void list_sorted_descending_by_year_and_ascending_by_title() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.desc());
        query.orderBy(title.asc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
        assertEquals("1990", documents.get(0).get("year"));
        assertEquals("1990", documents.get(1).get("year"));
        assertEquals("Introduction to Algorithms", documents.get(0)
                .get("title"));
        assertEquals("Jurassic Park", documents.get(1).get("title"));
    }

    @Test
    public void list_sorted_descending_by_year_and_descending_by_title() {
        query.where(year.between(1800, 2000));
        query.orderBy(year.desc());
        query.orderBy(title.desc());
        final List<Document> documents = query.fetch();
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
    public void list_index_problem_in_max_doc() throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.getIndexReader().maxDoc()).andThrow(new IOException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.fetch();
        verify(searcher);
    }

    @Ignore
    @Test(expected = QueryException.class)
    public void list_sorted_index_problem_in_max_doc() throws IOException {
        searcher = createMockBuilder(IndexSearcher.class).addMockedMethod(
                "maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.getIndexReader().maxDoc()).andThrow(new IOException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.orderBy(title.asc());
        query.fetch();
        verify(searcher);
    }

    @Test
    public void offset() {
        assertTrue(query.where(title.eq("Jurassic Park")).offset(30).fetch()
                .isEmpty());
    }

    @Test
    public void load_list() {
        Document document = query.where(title.ne("")).load(title).fetch()
                .get(0);
        assertNotNull(document.get("title"));
        assertNull(document.get("year"));
    }

    @Test
    public void load_list_fieldSelector() {
        Document document = query.where(title.ne(""))
                .load(Sets.newHashSet("title")).fetch().get(0);
        assertNotNull(document.get("title"));
        assertNull(document.get("year"));
    }

    @Test
    public void load_singleResult() {
        Document document = query.where(title.ne("")).load(title).fetchFirst().orElse(null);
        assertNotNull(document.get("title"));
        assertNull(document.get("year"));
    }

    @Test
    public void load_singleResult_fieldSelector() {
        Document document = query.where(title.ne(""))
                .load(Sets.newHashSet("title")).fetchFirst().orElse(null);
        assertNotNull(document.get("title"));
        assertNull(document.get("year"));
    }

    @Test
    public void singleResult() {
        assertNotNull(query.where(title.ne("")).fetchFirst().orElse(null));
    }

    @Test
    public void single_result_takes_limit() {
        assertEquals("Jurassic Park", query.where(title.ne("")).limit(1)
                .fetchFirst().orElse(null).get("title"));
    }

    @Test
    public void single_result_considers_limit_and_actual_result_size() {
        query.where(title.startsWith("Nummi"));
        final Document document = query.limit(3).fetchFirst().orElse(null);
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test
    public void single_result_returns_null_if_nothing_is_in_range() {
        query.where(title.startsWith("Nummi"));
        assertNull(query.offset(10).fetchFirst().orElse(null));
    }

    @Test
    public void single_result_considers_offset() {
        assertEquals("Introduction to Algorithms", query.where(title.ne(""))
                .offset(3).fetchFirst().orElse(null).get("title"));
    }

    @Test
    public void single_result_considers_limit_and_offset() {
        assertEquals("The Lord of the Rings", query.where(title.ne(""))
                .limit(1).offset(2).fetchFirst().orElse(null).get("title"));
    }

    @Test(expected = NonUniqueResultException.class)
    public void uniqueResult_contract() {
        query.where(title.ne("")).fetchOne().orElse(null);
    }

    @Test
    public void unique_result_takes_limit() {
        assertEquals("Jurassic Park", query.where(title.ne("")).limit(1)
                .fetchOne().orElse(null).get("title"));
    }

    @Test
    public void unique_result_considers_limit_and_actual_result_size() {
        query.where(title.startsWith("Nummi"));
        final Document document = query.limit(3).fetchOne().orElse(null);
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test
    public void unique_result_returns_null_if_nothing_is_in_range() {
        query.where(title.startsWith("Nummi"));
        assertNull(query.offset(10).fetchOne().orElse(null));
    }

    @Test
    public void unique_result_considers_offset() {
        assertEquals("Introduction to Algorithms", query.where(title.ne(""))
                .offset(3).fetchOne().orElse(null).get("title"));
    }

    @Test
    public void unique_result_considers_limit_and_offset() {
        assertEquals("The Lord of the Rings", query.where(title.ne(""))
                .limit(1).offset(2).fetchOne().orElse(null).get("title"));
    }

    @Test
    public void uniqueResult() {
        query.where(title.startsWith("Nummi"));
        final Document document = query.fetchOne().orElse(null);
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test
    public void uniqueResult_with_param() {
        final Param<String> param = new Param<String>(String.class, "title");
        query.set(param, "Nummi");
        query.where(title.startsWith(param));
        final Document document = query.fetchOne().orElse(null);
        assertEquals("Nummisuutarit", document.get("title"));
    }

    @Test(expected = ParamNotSetException.class)
    public void uniqueResult_param_not_set() {
        final Param<String> param = new Param<String>(String.class, "title");
        query.where(title.startsWith(param));
        query.fetchOne().orElse(null);
    }

    @Test(expected = QueryException.class)
    public void uniqueResult_finds_more_than_one_result() {
        query.where(year.eq(1990));
        query.fetchOne().orElse(null);
    }

    @Test
    public void uniqueResult_finds_no_results() {
        query.where(year.eq(2200));
        assertNull(query.fetchOne().orElse(null));
    }

    @Test
    @Ignore
    public void uniqueResult_finds_no_results_because_no_documents_in_index()
            throws IOException {
        searcher = createMockBuilder(IndexSearcher.class)
                .addMockedMethod("maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.getIndexReader().maxDoc()).andReturn(0);
        replay(searcher);
        assertNull(query.where(year.eq(3000)).fetchOne().orElse(null));
        verify(searcher);
    }

    @Test(expected = QueryException.class)
    @Ignore
    public void uniqueResult_sorted_index_problem_in_max_doc()
            throws IOException {
        searcher = createMockBuilder(IndexSearcher.class)
                .addMockedMethod("maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.getIndexReader().maxDoc()).andThrow(
                new IllegalArgumentException());
        replay(searcher);
        query.where(title.eq("Jurassic Park"));
        query.fetchOne().orElse(null);
        verify(searcher);
    }

    @Test
    @Ignore
    public void count_returns_0_because_no_documents_in_index()
            throws IOException {
        searcher = createMockBuilder(IndexSearcher.class)
                .addMockedMethod("maxDoc").createMock();
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        expect(searcher.getIndexReader().maxDoc()).andReturn(0);
        replay(searcher);
        assertEquals(0, query.where(year.eq(3000)).fetchCount());
        verify(searcher);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listDistinct() {
        query.where(year.between(1900, 2000).or(title.startsWith("Jura")));
        query.orderBy(year.asc());
        final List<Document> documents = query.distinct().fetch();
        assertFalse(documents.isEmpty());
        assertEquals(3, documents.size());
    }

    @Test
    public void listResults() {
        query.where(year.between(1800, 2000));
        query.restrict(new QueryModifiers(2L, 1L));
        query.orderBy(year.asc());
        final QueryResults<Document> results = query.fetchResults();
        assertFalse(results.isEmpty());
        assertEquals("1954", results.getResults().get(0).get("year"));
        assertEquals("1990", results.getResults().get(1).get("year"));
        assertEquals(2, results.getLimit());
        assertEquals(1, results.getOffset());
        assertEquals(4, results.getTotal());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void listDistinctResults() {
        query.where(year.between(1800, 2000).or(
                title.eq("The Lord of the Rings")));
        query.restrict(new QueryModifiers(1L, 1L));
        query.orderBy(year.asc());
        final QueryResults<Document> results = query.distinct().fetchResults();
        assertFalse(results.isEmpty());
        assertEquals("1954", results.getResults().get(0).get("year"));
        assertEquals(1, results.getLimit());
        assertEquals(1, results.getOffset());
        assertEquals(4, results.getTotal());
    }

    @Test
    public void list_all() {
        final List<Document> results = query.where(title.like("*"))
                .orderBy(title.asc(), year.desc()).fetch();
        assertEquals(4, results.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_sorted_ascending_limit_negative() {
        query.where(year.between(1800, 2000));
        query.limit(-1);
        query.orderBy(year.asc());
        query.fetch();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_not_sorted_limit_negative() {
        query.where(year.between(1800, 2000));
        query.limit(-1);
        query.fetch();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_sorted_ascending_limit_0() {
        query.where(year.between(1800, 2000));
        query.limit(0);
        query.orderBy(year.asc());
        query.fetch();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_not_sorted_limit_0() {
        query.where(year.between(1800, 2000));
        query.limit(0);
        query.fetch();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_sorted_ascending_offset_negative() {
        query.where(year.between(1800, 2000));
        query.offset(-1);
        query.orderBy(year.asc());
        query.fetch();
    }

    @Test(expected = IllegalArgumentException.class)
    public void list_not_sorted_offset_negative() {
        query.where(year.between(1800, 2000));
        query.offset(-1);
        query.fetch();
    }

    @Test
    public void list_sorted_ascending_offset_0() {
        query.where(year.between(1800, 2000));
        query.offset(0);
        query.orderBy(year.asc());
        final List<Document> documents = query.fetch();
        assertFalse(documents.isEmpty());
        assertEquals(4, documents.size());
    }

    @Test
    public void list_not_sorted_offset_0() {
        query.where(year.between(1800, 2000));
        query.offset(0);
        final List<Document> documents = query.fetch();
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
    public void all_by_excluding_where() {
        assertEquals(4, query.fetch().size());
    }

    @Test
    public void empty_index_should_return_empty_list() throws Exception {
        idx = new RAMDirectory();

        writer = createWriter(idx);
        writer.close();
        IndexReader reader = DirectoryReader.open(idx);
        searcher = new IndexSearcher(reader);
        query = new LuceneQuery(new LuceneSerializer(true, true), searcher);
        assertTrue(query.fetch().isEmpty());
    }

    @Test(expected = QueryException.class)
    public void list_results_throws_an_illegal_argument_exception_when_sum_of_limit_and_offset_is_negative() {
        query.limit(1).offset(Integer.MAX_VALUE).fetchResults();
    }

    @Test
    public void limit_max_value() {
        assertEquals(4, query.limit(Long.MAX_VALUE).fetch().size());
    }
}
