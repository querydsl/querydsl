/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.lucene;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.Arrays;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriter.MaxFieldLength;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.BooleanBuilder;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.MatchingFilters;
import com.mysema.query.Module;
import com.mysema.query.QueryMetadata;
import com.mysema.query.StringConstant;
import com.mysema.query.Target;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.PathBuilder;

/**
 * Tests for LuceneSerializer
 *
 * @author vema
 *
 */
public class LuceneSerializerTest {
    private LuceneSerializer serializer;
    private PathBuilder<Object> entityPath;
    private StringPath title;
    private StringPath author;
    private StringPath text;
    private StringPath rating;
    private NumberPath<Integer> year;
    private NumberPath<Double> gross;

    private NumberPath<Long> longField;
    private NumberPath<Short> shortField;
    private NumberPath<Byte> byteField;
    private NumberPath<Float> floatField;

    private static final String YEAR_PREFIX_CODED = NumericUtils.intToPrefixCoded(1990);
    private static final String GROSS_PREFIX_CODED = NumericUtils.doubleToPrefixCoded(900.00);
    private static final String LONG_PREFIX_CODED = NumericUtils.longToPrefixCoded(1);
    private static final String SHORT_PREFIX_CODED = NumericUtils.intToPrefixCoded(1);
    private static final String BYTE_PREFIX_CODED = NumericUtils.intToPrefixCoded(1);
    private static final String FLOAT_PREFIX_CODED = NumericUtils.floatToPrefixCoded((float)1.0);

    private RAMDirectory idx;
    private IndexWriter writer;
    private Searcher searcher;

    private QueryMetadata metadata = new DefaultQueryMetadata();

    private Document createDocument() {
        Document doc = new Document();

        doc.add(new Field("title", new StringReader("Jurassic Park")));
        doc.add(new Field("author", new StringReader("Michael Crichton")));
        doc.add(new Field("text", new StringReader("It's a UNIX system! I know this!")));
        doc.add(new Field("rating", new StringReader("Good")));
        doc.add(new NumericField("year", Store.YES, true).setIntValue(1990));
        doc.add(new NumericField("gross", Store.YES, true).setDoubleValue(900.00));

        doc.add(new NumericField("longField", Store.YES, true).setLongValue(1));
        doc.add(new NumericField("shortField", Store.YES, true).setIntValue(1));
        doc.add(new NumericField("byteField", Store.YES, true).setIntValue(1));
        doc.add(new NumericField("floatField", Store.YES, true).setFloatValue(1));

        return doc;
    }

    @Before
    public void setUp() throws Exception {
        // TODO Tests for non lower case
        serializer = new LuceneSerializer(true,true);
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        title = entityPath.getString("title");
        author = entityPath.getString("author");
        text = entityPath.getString("text");
        year = entityPath.getNumber("year", Integer.class);
        rating = entityPath.getString("rating");
        gross = entityPath.getNumber("gross", Double.class);

        longField = entityPath.getNumber("longField", Long.class);
        shortField = entityPath.getNumber("shortField", Short.class);
        byteField = entityPath.getNumber("byteField", Byte.class);
        floatField = entityPath.getNumber("floatField", Float.class);

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

    private void testQuery(Expression<?> expr, int expectedHits) throws Exception {
        Query query = serializer.toQuery(expr, metadata);
        TopDocs docs = searcher.search(query, 100);
        assertEquals(expectedHits, docs.totalHits);
    }

    private void testQuery(Expression<?> expr, String expectedQuery, int expectedHits) throws Exception {
        Query query = serializer.toQuery(expr, metadata);
        TopDocs docs = searcher.search(query, 100);
        assertEquals(expectedHits, docs.totalHits);
        assertEquals(expectedQuery, query.toString());
    }

    @Test
    public void queryElement() throws Exception{
        Query query1 = serializer.toQuery(author.like("Michael"), metadata);
        Query query2 = serializer.toQuery(text.like("Text"), metadata);

        BooleanExpression query = BooleanExpression.anyOf(
            new QueryElement(query1),
            new QueryElement(query2)
        );
        testQuery(query, "author:michael text:text", 1);
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
        testQuery(title.like("House").or(author.like("*ichae*")), "title:house author:*ichae*", 1);
    }

    @Test
    public void like_and_like() throws Exception {
        testQuery(title.like("*assic*").and(rating.like("G?od")), "+title:*assic* +rating:g?od", 1);
    }

    @Test
    public void eq() throws Exception {
        testQuery(rating.eq("Good"), "rating:good", 1);
    }

    @Test
    public void eq_Numeric_Integer() throws Exception {
        testQuery(year.eq(1990), "year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    public void eq_Numeric_Double() throws Exception {
        testQuery(gross.eq(900.00), "gross:" + GROSS_PREFIX_CODED, 1);
    }

    @Test
    public void eq_Numeric() throws Exception{
    testQuery(longField.eq(1l), "longField:" + LONG_PREFIX_CODED, 1);
    testQuery(shortField.eq((short)1), "shortField:" + SHORT_PREFIX_CODED, 1);
    testQuery(byteField.eq((byte)1), "byteField:" + BYTE_PREFIX_CODED, 1);
    testQuery(floatField.eq((float)1.0), "floatField:" + FLOAT_PREFIX_CODED, 1);
    }

    @Test
    public void eq_Should_Not_Find_Results_But_Lucene_Semantics_Differs_From_Querydsls() throws Exception {
        testQuery(title.eq("Jurassic"), "title:jurassic", 1);
    }

    @Test
    public void eq_or_eq() throws Exception {
        testQuery(title.eq("House").or(year.eq(1990)), "title:house year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    public void eq_and_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq(1990)), "+title:\"jurassic park\" +year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    public void eq_and_eq_and_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq(1990)).and(author.eq("Michael Crichton")), "+(+title:\"jurassic park\" +year:" + YEAR_PREFIX_CODED + ") +author:\"michael crichton\"", 1);
    }

    @Test
    public void eq_and_eq_or_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(rating.eq("Bad")).or(author.eq("Michael Crichton")), "(+title:\"jurassic park\" +rating:bad) author:\"michael crichton\"", 1);
    }

    @Test
    public void eq_or_eq_and_eq_Does_Not_Find_Results() throws Exception {
        testQuery(title.eq("Jeeves").or(rating.eq("Superb")).and(author.eq("Michael Crichton")), "+(title:jeeves rating:superb) +author:\"michael crichton\"", 0);
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
        testQuery(title.eq("House").not().or(rating.eq("Good")), "-title:house rating:good", 1);
    }

    @Test
    public void eq_not_Does_Not_Find_Results() throws Exception {
        testQuery(title.eq("Jurassic Park").not(), "-title:\"jurassic park\"", 0);
    }

    @Test
    public void eq_and_eq_not_Does_Not_Find_Results_Because_Second_Expression_Finds_Nothing() throws Exception {
        testQuery(rating.eq("Superb").and(title.eq("House").not()), "+rating:superb -title:house", 0);
    }

    @Test
    public void ne_Does_Not_Find_Results() throws Exception {
        testQuery(title.ne("House"), "-title:house", 0);
    }

    @Test
    public void ne() throws Exception {
        testQuery(title.ne("Jurassic Park"), "-title:\"jurassic park\"", 0);
    }

    @Test
    public void ne_or_eq() throws Exception {
        testQuery(title.ne("Jurassic Park").or(rating.eq("Lousy")), "-title:\"jurassic park\" rating:lousy", 0);
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
    public void between_Numeric_Integer() throws Exception {
        testQuery(year.between(1980, 2000), "year:[1980 TO 2000]", 1);
    }

    @Test
    public void between_Numeric_Double() throws Exception {
        testQuery(gross.between(10.00, 19030.00), "gross:[10.0 TO 19030.0]", 1);
    }

    @Test
    public void between_Numeric() throws Exception{
    testQuery(longField.between(0l,2l), "longField:[0 TO 2]", 1);
    testQuery(shortField.between((short)0,(short)2), "shortField:[0 TO 2]", 1);
    testQuery(byteField.between((byte)0,(byte)2), "byteField:[0 TO 2]", 1);
    testQuery(floatField.between((float)0.0,(float)2.0), "floatField:[0.0 TO 2.0]", 1);
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
    public void in() throws Exception {
        testQuery(title.in(Arrays.asList("Jurassic","Park")), "title:jurassic title:park", 1);
        testQuery(title.in("Jurassic","Park"), "title:jurassic title:park", 1);
        testQuery(title.eq("Jurassic").or(title.eq("Park")), "title:jurassic title:park", 1);
    }

    @Test
    public void lt() throws Exception {
        testQuery(rating.lt("Superb"), "rating:{* TO superb}", 1);
    }

    @Test
    public void lt_Numeric_Integer() throws Exception {
        testQuery(year.lt(1991), "year:{* TO 1991}", 1);
    }

    @Test
    public void lt_Numeric_Double() throws Exception {
        testQuery(gross.lt(10000.0), "gross:{* TO 10000.0}", 1);
    }

    @Test
    public void lt_Not_In_Range_Because_Equal() throws Exception {
        testQuery(rating.lt("Good"), "rating:{* TO good}", 0);
    }

    @Test
    public void lt_Numeric_Integer_Not_In_Range_Because_Equal() throws Exception {
        testQuery(year.lt(1990), "year:{* TO 1990}", 0);
    }

    @Test
    public void lt_Numeric_Double_Not_In_Range_Because_Equal() throws Exception {
        testQuery(gross.lt(900.0), "gross:{* TO 900.0}", 0);
    }

    @Test
    public void loe() throws Exception {
        testQuery(rating.loe("Superb"), "rating:[* TO superb]", 1);
    }

    @Test
    public void loe_Numeric_Integer() throws Exception {
        testQuery(year.loe(1991), "year:[* TO 1991]", 1);
    }

    @Test
    public void loe_Numeric_Double() throws Exception {
        testQuery(gross.loe(903.0), "gross:[* TO 903.0]", 1);
    }

    @Test
    public void loe_Equal() throws Exception {
        testQuery(rating.loe("Good"), "rating:[* TO good]", 1);
    }

    @Test
    public void loe_Numeric_Integer_Equal() throws Exception {
        testQuery(year.loe(1990), "year:[* TO 1990]", 1);
    }

    @Test
    public void loe_Numeric_Double_Equal() throws Exception {
        testQuery(gross.loe(900.0), "gross:[* TO 900.0]", 1);
    }

    @Test
    public void loe_Not_Found() throws Exception {
        testQuery(rating.loe("Bad"), "rating:[* TO bad]", 0);
    }

    @Test
    public void loe_Numeric_Integer_Not_Found() throws Exception {
        testQuery(year.loe(1989), "year:[* TO 1989]", 0);
    }

    @Test
    public void loe_Numeric_Double_Not_Found() throws Exception {
        testQuery(gross.loe(899.9), "gross:[* TO 899.9]", 0);
    }

    @Test
    public void gt() throws Exception {
        testQuery(rating.gt("Bad"), "rating:{bad TO *}", 1);
    }

    @Test
    public void gt_Numeric_Integer() throws Exception {
        testQuery(year.gt(1989), "year:{1989 TO *}", 1);
    }

    @Test
    public void gt_Numeric_Double() throws Exception {
        testQuery(gross.gt(100.00), "gross:{100.0 TO *}", 1);
    }

    @Test
    public void gt_Not_In_Range_Because_Equal() throws Exception {
        testQuery(rating.gt("Good"), "rating:{good TO *}", 0);
    }

    @Test
    public void gt_Numeric_Integer_Not_In_Range_Because_Equal() throws Exception {
        testQuery(year.gt(1990), "year:{1990 TO *}", 0);
    }

    @Test
    public void gt_Numeric_Double_Not_In_Range_Because_Equal() throws Exception {
        testQuery(gross.gt(900.00), "gross:{900.0 TO *}", 0);
    }

    @Test
    public void ge() throws Exception {
        testQuery(rating.goe("Bad"), "rating:[bad TO *]", 1);
    }

    @Test
    public void goe_Numeric_Integer() throws Exception {
        testQuery(year.goe(1989), "year:[1989 TO *]", 1);
    }

    @Test
    public void goe_Numeric_Double() throws Exception {
        testQuery(gross.goe(320.50), "gross:[320.5 TO *]", 1);
    }

    @Test
    public void goe_Equal() throws Exception {
        testQuery(rating.goe("Good"), "rating:[good TO *]", 1);
    }

    @Test
    public void goe_Numeric_Integer_Equal() throws Exception {
        testQuery(year.goe(1990), "year:[1990 TO *]", 1);
    }

    @Test
    public void goe_Numeric_Double_Equal() throws Exception {
        testQuery(gross.goe(900.00), "gross:[900.0 TO *]", 1);
    }

    @Test
    public void goe_Not_Found() throws Exception {
        testQuery(rating.goe("Hood"), "rating:[hood TO *]", 0);
    }

    @Test
    public void goe_Numeric_Integer_Not_Found() throws Exception {
        testQuery(year.goe(1991), "year:[1991 TO *]", 0);
    }

    @Test
    public void goe_Numeric_Double_Not_Found() throws Exception {
        testQuery(gross.goe(900.10), "gross:[900.1 TO *]", 0);
    }

    @Test
    public void booleanBuilder() throws Exception{
    testQuery(new BooleanBuilder(gross.goe(900.10)), "gross:[900.1 TO *]", 0);
    }

    @Test
    @Ignore
    public void fuzzy() throws Exception {
        fail("Not yet implemented!");
    }

    @Test
    @Ignore
    public void proximity() throws Exception {
        fail("Not yet implemented!");
    }

    @Test
    @Ignore
    public void boost() throws Exception {
        fail("Not yet implemented!");
    }

    @Test
    public void various() throws Exception{
        MatchingFilters filters = new MatchingFilters(Module.LUCENE, Target.LUCENE);
        for (BooleanExpression filter : filters.string(title, StringConstant.create("Jurassic"))){
            System.out.println(filter);
            testQuery(filter, 1);
        }

        for (BooleanExpression filter : filters.string(author, StringConstant.create("Michael Crichton"))){
            System.out.println(filter);
            testQuery(filter, 1);
        }

        for (BooleanExpression filter : filters.string(title, StringConstant.create("1990"))){
            System.out.println(filter);
            testQuery(filter, 0);
        }
    }

}
