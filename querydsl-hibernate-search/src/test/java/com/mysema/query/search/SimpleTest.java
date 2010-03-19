/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.hibernate.Session;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PathBuilder;


public class SimpleTest {
    private LuceneSerializer serializer;
    private PathBuilder<Object> entityPath;
    private PString title;
    private PString author;
    private PString text;
    private PString year;

    private RAMDirectory idx;
    private IndexWriter writer;
    private Searcher searcher;

    private Document createDocument() {
        Document doc = new Document();

        doc.add(new Field("title", new StringReader("Jurassic Park")));
        doc.add(new Field("author", new StringReader("Michael Crichton")));
        doc.add(new Field("text", new StringReader("It's a UNIX system! I know this!")));
        doc.add(new Field("year", new StringReader("1990")));

        return doc;
    }

    @Before
    public void setUp() throws Exception {
        // TODO Tests for non lower case
        serializer = new LuceneSerializer(true);
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        title = entityPath.getString("title");
        author = entityPath.getString("author");
        text = entityPath.getString("text");
        year = entityPath.getString("year");

        idx = new RAMDirectory();
        writer = new IndexWriter(idx, new StandardAnalyzer(Version.LUCENE_CURRENT), true, MaxFieldLength.UNLIMITED);

        writer.addDocument(createDocument());

        writer.optimize();
        writer.close();

        searcher = new IndexSearcher(idx);
    }

    @After
    public void tearDown() throws Exception {
        searcher.close();
    }

    @Test
    public void test_like() throws Exception {
        Query q = serializer.toQuery(author.like("*ichael*"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("author:*ichael*", q.toString());
    }

    @Test
    public void test_like_Custom_Wildcard_Single_Character() throws Exception {
        Query q = serializer.toQuery(author.like("Mi?hael"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("author:mi?hael", q.toString());
    }

    @Test
    public void test_like_Custom_Wildcard_Multiple_Character() throws Exception {
        Query q = serializer.toQuery(text.like("*U*X*"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("text:*u*x*", q.toString());
    }

    @Test
    @Ignore
    public void test_like_Phrase() throws Exception {
        Query q = serializer.toQuery(title.like("rassic Par"));
        // TODO Wildcard phrases not supported by Lucene by default.
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("like:*\"rassic Par\"*", q.toString());
    }

    @Test
    public void test_like_or_like() throws Exception {
        Query q = serializer.toQuery(title.like("House").or(year.like("*99*")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("title:house year:*99*", q.toString());
    }

    @Test
    public void test_like_and_like() throws Exception {
        Query q = serializer.toQuery(title.like("*assic*").and(year.like("199?")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("+title:*assic* +year:199?", q.toString());
    }

    @Test
    public void test_eq_or_eq() throws Exception {
        Query q = serializer.toQuery(title.eq("House").or(year.eq("1990")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("title:house year:1990", q.toString());
    }

    @Test
    public void test_eq_and_eq() throws Exception {
        Query q = serializer.toQuery(title.eq("Jurassic Park").and(year.eq("1990")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("+title:\"jurassic park\" +year:1990", q.toString());
    }

    @Test
    public void test_eq_and_eq_and_eq() throws Exception {
        Query q = serializer.toQuery(title.eq("Jurassic Park").and(year.eq("1990")).and(author.eq("Michael Crichton")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("+(+title:\"jurassic park\" +year:1990) +author:\"michael crichton\"", q.toString());
    }

    @Test
    public void test_eq_and_eq_or_eq() throws Exception {
        Query q = serializer.toQuery(title.eq("Jurassic Park").and(year.eq("190")).or(author.eq("Michael Crichton")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("(+title:\"jurassic park\" +year:190) author:\"michael crichton\"", q.toString());
    }

    @Test
    public void test_eq_or_eq_and_eq_Does_Not_Find_Results() throws Exception {
        Query q = serializer.toQuery(title.eq("Jeeves").or(year.eq("1915")).and(author.eq("Michael Crichton")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(0, docs.totalHits);
        assertEquals("+(title:jeeves year:1915) +author:\"michael crichton\"", q.toString());
    }

    @Test
    public void test_eq() throws Exception {
        Query q = serializer.toQuery(year.eq("1990"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("year:1990", q.toString());
    }

    @Test
    public void test_eq_Phrase() throws Exception {
        Query q = serializer.toQuery(title.eq("Jurassic Park"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("title:\"jurassic park\"", q.toString());
    }

    @Test
    public void test_eq_Phrase_Does_Not_Find_Results() throws Exception {
        Query q = serializer.toQuery(title.eq("Jurassic Amusement Park"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(0, docs.totalHits);
        assertEquals("title:\"jurassic amusement park\"", q.toString());
    }

    @Test
    public void test_like_not_Does_Not_Find_Results() throws Exception {
        Query q = serializer.toQuery(title.like("*H*e*").not());
        TopDocs docs = searcher.search(q, 100);
        assertEquals(0, docs.totalHits);
        assertEquals("-title:*h*e*", q.toString());
    }

    @Test
    public void test_eq_not_Does_Not_Find_Results() throws Exception {
        Query q = serializer.toQuery(title.eq("Jurassic Park").not());
        TopDocs docs = searcher.search(q, 100);
        assertEquals(0, docs.totalHits);
        assertEquals("-title:\"jurassic park\"", q.toString());
    }

    @Test
    @Ignore
    public void test_eq_and_eq_not() throws Exception {
        Query q = serializer.toQuery(year.eq("1990").and(title.eq("House")));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("+year:1990 +(-title:house)", q.toString());
    }

    @Test
    public void test_startsWith() throws Exception {
        Query q = serializer.toQuery(title.startsWith("Jurassi"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("title:jurassi*", q.toString());
    }

    @Test
    @Ignore
    public void test_startsWith_Phrase() throws Exception {
        Query q = serializer.toQuery(title.startsWith("Jurassic Par"));
        TopDocs docs = searcher.search(q, 100);
        assertEquals(1, docs.totalHits);
        assertEquals("title:jurassic par*", q.toString());
    }

    @Test
    public void specs(){
        Session session = null;
        PathBuilder<Object> entityPath = new PathBuilder<Object>(Object.class, "obj");
        PString stringPath = entityPath.getString("prop");

//        LuceneQuery<Object> query = new LuceneQuery<Object>(session, entityPath);

//        LuceneSerializer serializer = new LuceneSerializer();

//        Terms & Phrases
//        "test" or "hello dolly"
//        c.Match("test") or c.Match("hello dolly")
//        assertEquals("obj.prop:test obj.prop:hello dolly", serializer.toQuery(stringPath.like("test").or(stringPath.like("hello dolly"))).toString());

//        Fields
//        title:"The Right way" and text:go
//        c.Title == "The Right way" and c.Text == "go"
//        assertEquals("obj.prop:The Right Way AND obj.prop:go", serializer.toQuery(stringPath.eq("The Right Way").and(stringPath.eq("go"))).toString());

//        WildCard
//        amb?r
//        c.ContactName.Match("amb?r")
        stringPath.like("amb?r");

//        Prefix
//        amber*
//        c.ContactName.StartsWith("amber")
        stringPath.startsWith("amber");

//        Fuzzy
//        roam~ or roam~0.8
//        c.ContactName.Like("roam") or c.ContactName.Like("roam", 0.8)
        // TODO

//        Proximity
//        "jakarta apache"~10
//        c.ContactName.Like("jakarta apache", 10)
        // TODO

//        Inclusive Range
//        mod_date:[20020101 TO 20030101]
//        c.ModifiedDate.Includes("20020101", "20030101")
//        stringPath.between("20020101", "20030101");
        // TODO

//        Exclusive Range
//        title:{Aida TO Carmen}
//        c.Title.Between("Aida", "Carmen")
        stringPath.between("Aida", "Carmen");

//        Boosting
//        jakarta^4 apache
//        c.Title.Match("jakarta".Boost(4), apache)
        // TODO

//        Boolean Or
//        "jakarta apache" OR jakarta
//        where c.Match("jakarta apache") || c.Match("jakarta")
        stringPath.like("jakarta apache").or(stringPath.like("jakarta"));

//        Boolean And
//        "jakarta apache" AND "Apache Lucene"
//        where c.Match("jakarta apache") && c.Match("Apache Lucene")
        stringPath.like("jakarta apache").and(stringPath.like("Apache Lucene"));

//        Boolean Not
//        "jakarta apache" NOT "Apache Lucene"
//        where c.Match("jakarta apache") && !c.Match("Apache Lucene")
        stringPath.like("jakarta apache").and(stringPath.like("Apache Lucene").not());

//        Required
//        +jakarta lucene
//        c.Title.Match("jakarta".Require(), "lucene")
//         TODO

//        Grouping
//        (jakarta OR apache) AND website
//        where (c.Title == "jakarta" || c.Title == "apache") && (c.Title == "website")
        stringPath.eq("jakarta").or(stringPath.eq("apache")).and(stringPath.eq("website"));

//        Native syntax
//        ie. title:{+return +"pink panther")
//        c.Search("title:(return +\"pink panther\"")
//        TODO
    }

}
