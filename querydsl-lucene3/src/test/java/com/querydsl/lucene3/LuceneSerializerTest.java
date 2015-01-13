/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.lucene3;

import java.io.StringReader;
import java.util.Arrays;

import com.querydsl.core.*;
import com.querydsl.lucene3.LuceneExpressions;
import com.querydsl.lucene3.LuceneSerializer;
import com.querydsl.lucene3.QueryElement;
import com.querydsl.core.types.*;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.path.CollectionPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.PathBuilder;
import com.querydsl.core.types.path.StringPath;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.NumericUtils;
import org.apache.lucene.util.Version;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
    private StringPath publisher;
    private NumberPath<Integer> year;
    private NumberPath<Double> gross;
    private CollectionPath<String, StringPath> titles;

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

    private IndexWriterConfig config;
    private RAMDirectory idx;
    private IndexWriter writer;
    private IndexSearcher searcher;

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private Document createDocument() {
        Document doc = new Document();

        doc.add(new Field("title", new StringReader("Jurassic Park")));
        doc.add(new Field("author", new StringReader("Michael Crichton")));
        doc.add(new Field("text", new StringReader("It's a UNIX system! I know this!")));
        doc.add(new Field("rating", new StringReader("Good")));
        doc.add(new Field("publisher", "", Store.YES, Index.ANALYZED));
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
        serializer = new LuceneSerializer(true,true);
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        title = entityPath.getString("title");
        author = entityPath.getString("author");
        text = entityPath.getString("text");
        publisher = entityPath.getString("publisher");
        year = entityPath.getNumber("year", Integer.class);
        rating = entityPath.getString("rating");
        gross = entityPath.getNumber("gross", Double.class);
        titles = entityPath.getCollection("title", String.class, StringPath.class);

        longField = entityPath.getNumber("longField", Long.class);
        shortField = entityPath.getNumber("shortField", Short.class);
        byteField = entityPath.getNumber("byteField", Byte.class);
        floatField = entityPath.getNumber("floatField", Float.class);

        idx = new RAMDirectory();
        config = new IndexWriterConfig(Version.LUCENE_31, 
                new StandardAnalyzer(Version.LUCENE_30))
            .setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(idx, config);

        writer.addDocument(createDocument());

        writer.close();

        IndexReader reader = IndexReader.open(idx);
        searcher = new IndexSearcher(reader);
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
    public void QueryElement() throws Exception{
        Query query1 = serializer.toQuery(author.like("Michael"), metadata);
        Query query2 = serializer.toQuery(text.like("Text"), metadata);

        BooleanExpression query = BooleanExpression.anyOf(
            new QueryElement(query1),
            new QueryElement(query2)
        );
        testQuery(query, "author:michael text:text", 1);
    }

    @Test
    public void Like() throws Exception {
        testQuery(author.like("*ichael*"), "author:*ichael*", 1);
    }

    @Test
    public void Like_Custom_Wildcard_Single_Character() throws Exception {
        testQuery(author.like("Mi?hael"), "author:mi?hael", 1);
    }

    @Test
    public void Like_Custom_Wildcard_Multiple_Character() throws Exception {
        testQuery(text.like("*U*X*"), "text:*u*x*", 1);
    }

    @Test
    public void Like_Phrase() throws Exception {
        testQuery(title.like("*rassic Par*"), "+title:**rassic* +title:*par**", 1);
    }

    @Test
    public void Like_or_like() throws Exception {
        testQuery(title.like("House").or(author.like("*ichae*")), "title:house author:*ichae*", 1);
    }

    @Test
    public void Like_and_like() throws Exception {
        testQuery(title.like("*assic*").and(rating.like("G?od")), "+title:*assic* +rating:g?od", 1);
    }

    @Test
    public void Eq() throws Exception {
        testQuery(rating.eq("good"), "rating:good", 1);
    }

    @Test
    public void Eq_with_deep_path() throws Exception{
        StringPath deepPath = entityPath.get("property1", Object.class).getString("property2");
        testQuery(deepPath.eq("good"), "property1.property2:good", 0);
    }

    @Test
    public void FuzzyLike() throws Exception{
        testQuery(LuceneExpressions.fuzzyLike(rating, "Good"), "rating:Good~0.5", 1);
    }

    @Test
    public void FuzzyLike_with_Similarity() throws Exception{
        testQuery(LuceneExpressions.fuzzyLike(rating, "Good", 0.6f), "rating:Good~0.6", 1);
    }

    @Test
    public void FuzzyLike_with_Similarity_and_prefix() throws Exception{
        testQuery(LuceneExpressions.fuzzyLike(rating, "Good", 0.6f, 0), "rating:Good~0.6", 1);
    }

    @Test
    public void Eq_Numeric_Integer() throws Exception {
        testQuery(year.eq(1990), "year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    public void Eq_Numeric_Double() throws Exception {
        testQuery(gross.eq(900.00), "gross:" + GROSS_PREFIX_CODED, 1);
    }

    @Test
    public void Eq_Numeric() throws Exception{
        testQuery(longField.eq(1l), "longField:" + LONG_PREFIX_CODED, 1);
        testQuery(shortField.eq((short)1), "shortField:" + SHORT_PREFIX_CODED, 1);
        testQuery(byteField.eq((byte)1), "byteField:" + BYTE_PREFIX_CODED, 1);
        testQuery(floatField.eq((float)1.0), "floatField:" + FLOAT_PREFIX_CODED, 1);
    }

    @Test
    public void Equals_Ignores_Case() throws Exception {
        testQuery(title.eq("Jurassic"), "title:jurassic", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Title_Equals_Ignore_Case_Or_Year_Equals() throws Exception {
        testQuery(title.equalsIgnoreCase("House").or(year.eq(1990)), "title:house year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    public void Eq_and_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq(1990)), "+title:\"jurassic park\" +year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    public void Eq_and_Eq_and_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq(1990)).and(author.eq("Michael Crichton")), "+(+title:\"jurassic park\" +year:" + YEAR_PREFIX_CODED + ") +author:\"michael crichton\"", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Equals_Ignore_Case_And_Or() throws Exception {
        testQuery(title.equalsIgnoreCase("Jurassic Park").and(rating.equalsIgnoreCase("Bad")).or(author.equalsIgnoreCase("Michael Crichton")), "(+title:\"jurassic park\" +rating:bad) author:\"michael crichton\"", 1);
    }

    @Test
    public void Eq_or_Eq_and_Eq_Does_Not_Find_Results() throws Exception {
        testQuery(title.eq("jeeves").or(rating.eq("superb")).and(author.eq("michael crichton")), "+(title:jeeves rating:superb) +author:\"michael crichton\"", 0);
    }

    @Test
    public void Eq_Phrase() throws Exception {
        testQuery(title.eq("Jurassic Park"), "title:\"jurassic park\"", 1);
    }

    @Test
    @Ignore("Not easily done in Lucene!")
    public void Publisher_Equals_Empty_String() throws Exception {
        testQuery(publisher.eq(""), "publisher:", 1);
    }

    @Test
    public void Eq_Phrase_Should_Not_Find_Results_But_LuceNe_Semantics_Differs_From_Querydsls() throws Exception {
        testQuery(text.eq("UNIX System"), "text:\"unix system\"", 1);
    }

    @Test
    public void Eq_Phrase_Does_Not_Find_Results_Because_Word_In_Middle() throws Exception {
        testQuery(title.eq("Jurassic Amusement Park"), "title:\"jurassic amusement park\"", 0);
    }

    @Test
    public void Like_not_Does_Not_Find_Results() throws Exception {
        testQuery(title.like("*H*e*").not(), "-title:*h*e* +*:*", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Title_Equals_Ignore_Case_Negation_Or_Rating_Equals_Ignore_Case() throws Exception {
        testQuery(title.equalsIgnoreCase("House").not().or(rating.equalsIgnoreCase("Good")), "-title:house rating:good", 1);
    }

    @Test
    public void Eq_not_Does_Not_Find_Results() throws Exception {
        testQuery(title.eq("Jurassic Park").not(), "-title:\"jurassic park\" +*:*", 0);
    }

    @Test
    public void Title_Equals_Not_House() throws Exception {
        testQuery(title.eq("house").not(), "-title:house +*:*", 1);
    }

    @Test
    public void Eq_and_Eq_not_Does_Not_Find_Results_Because_Second_Expression_Finds_Nothing() throws Exception {
        testQuery(rating.eq("superb").and(title.eq("house").not()), "+rating:superb +(-title:house +*:*)", 0);
    }

    @Test
    public void Not_Equals_Finds_One() throws Exception {
        testQuery(title.ne("house"), "-title:house +*:*", 1);
    }

    @Test
    public void Not_Equals_Finds_None() throws Exception {
        testQuery(title.ne("Jurassic Park"), "-title:\"jurassic park\" +*:*", 0);
    }

    @Test
    public void Nothing_Found_With_Not_Equals_Or_Equals() throws Exception {
        testQuery(title.ne("jurassic park").or(rating.eq("lousy")), "(-title:\"jurassic park\" +*:*) rating:lousy", 0);
    }

    @Test
    public void Ne_and_eq() throws Exception {
        testQuery(title.ne("house").and(rating.eq("good")), "+(-title:house +*:*) +rating:good", 1);
    }

    @Test
    public void StartsWith() throws Exception {
        testQuery(title.startsWith("Jurassi"), "title:jurassi*", 1);
    }

    @Test
    public void StartsWith_Phrase() throws Exception {
        testQuery(title.startsWith("jurassic par"), "+title:jurassic* +title:*par*", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Starts_With_Ignore_Case_Phrase_Does_Not_Find_Results() throws Exception {
        testQuery(title.startsWithIgnoreCase("urassic Par"), "+title:urassic* +title:*par*", 0);
    }

    @Test
    public void EndsWith() throws Exception {
        testQuery(title.endsWith("ark"), "title:*ark", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Ends_With_Ignore_Case_Phrase() throws Exception {
        testQuery(title.endsWithIgnoreCase("sic Park"), "+title:*sic* +title:*park", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Ends_With_Ignore_Case_Phrase_Does_Not_Find_Results() throws Exception {
        testQuery(title.endsWithIgnoreCase("sic Par"), "+title:*sic* +title:*par", 0);
    }

    @Test
    public void Contains() throws Exception {
        testQuery(title.contains("rassi"), "title:*rassi*", 1);
    }

    @Test(expected=UnsupportedOperationException.class)
    public void Contains_Ignore_Case_Phrase() throws Exception {
        testQuery(title.containsIgnoreCase("rassi Pa"), "+title:*rassi* +title:*pa*", 1);
    }

    @Test
    public void Contains_User_Inputted_Wildcards_Dont_Work() throws Exception {
        testQuery(title.contains("r*i"), "title:*r\\*i*", 0);
    }

    @Test
    public void Between() throws Exception {
        testQuery(title.between("Indiana", "Kundun"), "title:[indiana TO kundun]", 1);
    }

    @Test
    public void Between_Numeric_Integer() throws Exception {
        testQuery(year.between(1980, 2000), "year:[1980 TO 2000]", 1);
    }

    @Test
    public void Between_Numeric_Double() throws Exception {
        testQuery(gross.between(10.00, 19030.00), "gross:[10.0 TO 19030.0]", 1);
    }

    @Test
    public void Between_Numeric() throws Exception{
        testQuery(longField.between(0l,2l), "longField:[0 TO 2]", 1);
        testQuery(shortField.between((short)0,(short)2), "shortField:[0 TO 2]", 1);
        testQuery(byteField.between((byte)0,(byte)2), "byteField:[0 TO 2]", 1);
        testQuery(floatField.between((float)0.0,(float)2.0), "floatField:[0.0 TO 2.0]", 1);
    }

    @Test
    public void Between_Is_Inclusive_From_Start() throws Exception {
        testQuery(title.between("Jurassic", "Kundun"), "title:[jurassic TO kundun]", 1);
    }

    @Test
    public void Between_Is_Inclusive_To_End() throws Exception {
        testQuery(title.between("Indiana", "Jurassic"), "title:[indiana TO jurassic]", 1);
    }

    @Test
    public void Between_Does_Not_Find_Results() throws Exception {
        testQuery(title.between("Indiana", "Jurassib"), "title:[indiana TO jurassib]", 0);
    }

    @Test
    public void In() throws Exception {
        testQuery(title.in(Arrays.asList("jurassic", "park")), "title:jurassic title:park", 1);
        testQuery(title.in("jurassic","park"), "title:jurassic title:park", 1);
        testQuery(title.eq("jurassic").or(title.eq("park")), "title:jurassic title:park", 1);
    }

    @Test
    public void Lt() throws Exception {
        testQuery(rating.lt("Superb"), "rating:{* TO superb}", 1);
    }

    @Test
    public void Lt_Numeric_Integer() throws Exception {
        testQuery(year.lt(1991), "year:{* TO 1991}", 1);
    }

    @Test
    public void Lt_Numeric_Double() throws Exception {
        testQuery(gross.lt(10000.0), "gross:{* TO 10000.0}", 1);
    }

    @Test
    public void Lt_Not_In_Range_Because_Equal() throws Exception {
        testQuery(rating.lt("Good"), "rating:{* TO good}", 0);
    }

    @Test
    public void Lt_Numeric_Integer_Not_In_Range_Because_Equal() throws Exception {
        testQuery(year.lt(1990), "year:{* TO 1990}", 0);
    }

    @Test
    public void Lt_Numeric_Double_Not_In_Range_Because_Equal() throws Exception {
        testQuery(gross.lt(900.0), "gross:{* TO 900.0}", 0);
    }

    @Test
    public void Loe() throws Exception {
        testQuery(rating.loe("Superb"), "rating:[* TO superb]", 1);
    }

    @Test
    public void Loe_Numeric_Integer() throws Exception {
        testQuery(year.loe(1991), "year:[* TO 1991]", 1);
    }

    @Test
    public void Loe_Numeric_Double() throws Exception {
        testQuery(gross.loe(903.0), "gross:[* TO 903.0]", 1);
    }

    @Test
    public void Loe_Equal() throws Exception {
        testQuery(rating.loe("Good"), "rating:[* TO good]", 1);
    }

    @Test
    public void Loe_Numeric_Integer_Equal() throws Exception {
        testQuery(year.loe(1990), "year:[* TO 1990]", 1);
    }

    @Test
    public void Loe_Numeric_Double_Equal() throws Exception {
        testQuery(gross.loe(900.0), "gross:[* TO 900.0]", 1);
    }

    @Test
    public void Loe_Not_Found() throws Exception {
        testQuery(rating.loe("Bad"), "rating:[* TO bad]", 0);
    }

    @Test
    public void Loe_Numeric_Integer_Not_Found() throws Exception {
        testQuery(year.loe(1989), "year:[* TO 1989]", 0);
    }

    @Test
    public void Loe_Numeric_Double_Not_Found() throws Exception {
        testQuery(gross.loe(899.9), "gross:[* TO 899.9]", 0);
    }

    @Test
    public void Gt() throws Exception {
        testQuery(rating.gt("Bad"), "rating:{bad TO *}", 1);
    }

    @Test
    public void Gt_Numeric_Integer() throws Exception {
        testQuery(year.gt(1989), "year:{1989 TO *}", 1);
    }

    @Test
    public void Gt_Numeric_Double() throws Exception {
        testQuery(gross.gt(100.00), "gross:{100.0 TO *}", 1);
    }

    @Test
    public void Gt_Not_In_Range_Because_Equal() throws Exception {
        testQuery(rating.gt("Good"), "rating:{good TO *}", 0);
    }

    @Test
    public void Gt_Numeric_Integer_Not_In_Range_Because_Equal() throws Exception {
        testQuery(year.gt(1990), "year:{1990 TO *}", 0);
    }

    @Test
    public void Gt_Numeric_Double_Not_In_Range_Because_Equal() throws Exception {
        testQuery(gross.gt(900.00), "gross:{900.0 TO *}", 0);
    }

    @Test
    public void Goe() throws Exception {
        testQuery(rating.goe("Bad"), "rating:[bad TO *]", 1);
    }

    @Test
    public void Goe_Numeric_Integer() throws Exception {
        testQuery(year.goe(1989), "year:[1989 TO *]", 1);
    }

    @Test
    public void Goe_Numeric_Double() throws Exception {
        testQuery(gross.goe(320.50), "gross:[320.5 TO *]", 1);
    }

    @Test
    public void Goe_Equal() throws Exception {
        testQuery(rating.goe("Good"), "rating:[good TO *]", 1);
    }

    @Test
    public void Goe_Numeric_Integer_Equal() throws Exception {
        testQuery(year.goe(1990), "year:[1990 TO *]", 1);
    }

    @Test
    public void Goe_Numeric_Double_Equal() throws Exception {
        testQuery(gross.goe(900.00), "gross:[900.0 TO *]", 1);
    }

    @Test
    public void Goe_Not_Found() throws Exception {
        testQuery(rating.goe("Hood"), "rating:[hood TO *]", 0);
    }

    @Test
    public void Goe_Numeric_Integer_Not_Found() throws Exception {
        testQuery(year.goe(1991), "year:[1991 TO *]", 0);
    }

    @Test
    public void Goe_Numeric_Double_Not_Found() throws Exception {
        testQuery(gross.goe(900.10), "gross:[900.1 TO *]", 0);
    }

    @Test
    public void Equals_Empty_String() throws Exception {
        testQuery(title.eq(""), "title:", 0);
    }

    @Test
    public void Not_Equals_Empty_String() throws Exception {
        testQuery(title.ne(""), "-title: +*:*", 1);
    }

    @Test
    public void Contains_Empty_String() throws Exception {
        testQuery(title.contains(""), "title:**", 1);
    }

    @Test
    public void Like_Empty_String() throws Exception {
        testQuery(title.like(""), "title:", 0);
    }

    @Test
    public void Starts_With_Empty_String() throws Exception {
        testQuery(title.startsWith(""), "title:*", 1);
    }

    @Test
    public void Ends_With_Empty_String() throws Exception {
        testQuery(title.endsWith(""), "title:*", 1);
    }

    @Test
    public void Between_Empty_Strings() throws Exception {
        testQuery(title.between("", ""), "title:[ TO ]", 0);
    }

    @Test
    public void BooleanBuilder() throws Exception{
        testQuery(new BooleanBuilder(gross.goe(900.10)), "gross:[900.1 TO *]", 0);
    }

    @Test
    @Ignore
    public void Fuzzy() throws Exception {
        fail("Not yet implemented!");
    }

    @Test
    @Ignore
    public void Proximity() throws Exception {
        fail("Not yet implemented!");
    }

    @Test
    @Ignore
    public void Boost() throws Exception {
        fail("Not yet implemented!");
    }

    @Test
    public void PathAny() throws Exception {
        testQuery(titles.any().eq("Jurassic"), "title:jurassic", 1);
    }

    private boolean unsupportedOperation(Predicate filter) {
        if (filter instanceof Operation<?>) {
            Operator<?> op = ((Operation<?>) filter).getOperator();
            if (op == Ops.STARTS_WITH_IC || op == Ops.EQ_IGNORE_CASE || op == Ops.STARTS_WITH_IC
                || op == Ops.ENDS_WITH_IC || op == Ops.STRING_CONTAINS_IC) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void various() throws Exception{
        MatchingFiltersFactory filters = new MatchingFiltersFactory(Module.LUCENE, Target.LUCENE);
        for (Predicate filter : filters.string(title, StringConstant.create("jurassic park"))) {
            if (unsupportedOperation(filter)) {
                continue;
            }
            testQuery(filter, 1);
        }

        for (Predicate filter : filters.string(author, StringConstant.create("michael crichton"))) {
            if (unsupportedOperation(filter)) {
                continue;
            }
            testQuery(filter, 1);
        }

        for (Predicate filter : filters.string(title, StringConstant.create("1990"))) {
            if (unsupportedOperation(filter)) {
                continue;
            }
            testQuery(filter, 0);
        }
    }

}
