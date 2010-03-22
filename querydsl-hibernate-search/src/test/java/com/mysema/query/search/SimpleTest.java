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

import com.mysema.query.types.Expr;
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

    private void testQuery(Expr<?> expr, String expectedQuery, int expectedHits) throws Exception {
        Query query = serializer.toQuery(expr);
        TopDocs docs = searcher.search(query, 100);
        assertEquals(expectedHits, docs.totalHits);
        assertEquals(expectedQuery, query.toString());
    }

    @Test
    public void like() throws Exception {
        testQuery(author.like("*ichael*"), "author:*ichael*", 1);
    }

    @Test
    public void like_Custom_Wildcard_Single_Character() throws Exception {
        testQuery(author.like("Mi?hael"), "author:mi?hael", 1);
    }

    @Test
    public void like_Custom_Wildcard_Multiple_Character() throws Exception {
        testQuery(text.like("*U*X*"), "text:*u*x*", 1);
    }

    @Test
    public void like_Phrase() throws Exception {
        testQuery(title.like("*rassic Par*"), "+title:**rassic* +title:*par**", 1);
    }

    @Test
    public void like_or_like() throws Exception {
        testQuery(title.like("House").or(year.like("*99*")), "title:house year:*99*", 1);
    }

    @Test
    public void like_and_like() throws Exception {
        testQuery(title.like("*assic*").and(year.like("199?")), "+title:*assic* +year:199?", 1);
    }

    @Test
    public void eq() throws Exception {
        testQuery(year.eq("1990"), "year:1990", 1);
    }

    @Test
    public void eq_Should_Not_Find_Results_But_Lucene_Semantics_Differs_From_Querydsls() throws Exception {
        testQuery(title.eq("Jurassic"), "title:jurassic", 1);
    }

    @Test
    public void eq_or_eq() throws Exception {
        testQuery(title.eq("House").or(year.eq("1990")), "title:house year:1990", 1);
    }

    @Test
    public void eq_and_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq("1990")), "+title:\"jurassic park\" +year:1990", 1);
    }

    @Test
    public void eq_and_eq_and_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq("1990")).and(author.eq("Michael Crichton")), "+(+title:\"jurassic park\" +year:1990) +author:\"michael crichton\"", 1);
    }

    @Test
    public void eq_and_eq_or_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq("190")).or(author.eq("Michael Crichton")), "(+title:\"jurassic park\" +year:190) author:\"michael crichton\"", 1);
    }

    @Test
    public void eq_or_eq_and_eq_Does_Not_Find_Results() throws Exception {
        testQuery(title.eq("Jeeves").or(year.eq("1915")).and(author.eq("Michael Crichton")), "+(title:jeeves year:1915) +author:\"michael crichton\"", 0);
    }

    @Test
    public void eq_Phrase() throws Exception {
        testQuery(title.eq("Jurassic Park"), "title:\"jurassic park\"", 1);
    }

    @Test
    public void eq_Phrase_Should_Not_Find_Results_But_Lucene_Semantics_Differs_From_Querydsls() throws Exception {
        testQuery(text.eq("UNIX System"), "text:\"unix system\"", 1);
    }

    @Test
    public void eq_Phrase_Does_Not_Find_Results_Because_Word_In_Middle() throws Exception {
        testQuery(title.eq("Jurassic Amusement Park"), "title:\"jurassic amusement park\"", 0);
    }

    @Test
    public void like_not_Does_Not_Find_Results() throws Exception {
        testQuery(title.like("*H*e*").not(), "-title:*h*e*", 0);
    }

    @Test
    public void eq_not_or_eq() throws Exception {
        testQuery(title.eq("House").not().or(year.eq("1990")), "(-title:house) year:1990", 1);
    }

    @Test
    public void eq_not_Does_Not_Find_Results() throws Exception {
        testQuery(title.eq("Jurassic Park").not(), "-title:\"jurassic park\"", 0);
    }

    @Test
    public void eq_and_eq_not_Does_Not_Find_Results_Because_Second_Expression_Finds_Nothing() throws Exception {
        testQuery(year.eq("1990").and(title.eq("House").not()), "+year:1990 +(-title:house)", 0);
    }

    @Test
    public void startsWith() throws Exception {
        testQuery(title.startsWith("Jurassi"), "title:jurassi*", 1);
    }

    @Test
    public void startsWith_Phrase() throws Exception {
        testQuery(title.startsWith("Jurassic Par"), "+title:jurassic* +title:*par*", 1);
    }

    @Test
    public void startsWith_Phrase_Does_Not_Find_Results() throws Exception {
        testQuery(title.startsWith("urassic Par"), "+title:urassic* +title:*par*", 0);
    }

    @Test
    public void endsWith() throws Exception {
        testQuery(title.endsWith("ark"), "title:*ark", 1);
    }

    @Test
    public void endsWith_Phrase() throws Exception {
        testQuery(title.endsWith("sic Park"), "+title:*sic* +title:*park", 1);
    }

    @Test
    public void endsWith_Phrase_Does_Not_Find_Results() throws Exception {
        testQuery(title.endsWith("sic Par"), "+title:*sic* +title:*par", 0);
    }

    @Test
    public void contains() throws Exception {
        testQuery(title.contains("rassi"), "title:*rassi*", 1);
    }

    @Test
    public void contains_Phrase() throws Exception {
        testQuery(title.contains("rassi Pa"), "+title:*rassi* +title:*pa*", 1);
    }

    @Test
    public void contains_User_Inputted_Wildcards_Dont_Work() throws Exception {
        testQuery(title.contains("r*i"), "title:*r\\*i*", 0);
    }

    @Test
    public void between() throws Exception {
        testQuery(title.between("Indiana", "Kundun"), "title:[indiana TO kundun]", 1);
    }

    @Test
    public void between_Phrase() throws Exception {
        testQuery(title.between("Jurassic Park", "Kundun"), "title:[jurassic TO kundun]", 1);
    }

    @Test
    @Ignore
    public void between_Phrase_Not_Split() throws Exception {
        testQuery(title.between("Jurassic Park", "Kundun"), "title:[\"jurassic park\" TO kundun]", 1);
    }

    @Test
    public void between_Is_Inclusive_From_Start() throws Exception {
        testQuery(title.between("Jurassic", "Kundun"), "title:[jurassic TO kundun]", 1);
    }

    @Test
    public void between_Is_Inclusive_To_End() throws Exception {
        testQuery(title.between("Indiana", "Jurassic"), "title:[indiana TO jurassic]", 1);
    }

    @Test
    public void between_Does_Not_Find_Results() throws Exception {
        testQuery(title.between("Indiana", "Jurassib"), "title:[indiana TO jurassib]", 0);
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
