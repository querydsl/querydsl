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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoubleField;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.LongField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.RAMDirectory;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.MatchingFiltersFactory;
import com.querydsl.core.QuerydslModule;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.StringConstant;
import com.querydsl.core.Target;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CollectionPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;

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

    private static final String YEAR_PREFIX_CODED = "";
    private static final String GROSS_PREFIX_CODED = "";
    private static final String LONG_PREFIX_CODED = "";
    private static final String SHORT_PREFIX_CODED = "";
    private static final String BYTE_PREFIX_CODED = "";
    private static final String FLOAT_PREFIX_CODED = "";

    private IndexWriterConfig config;
    private RAMDirectory idx;
    private IndexWriter writer;
    private IndexSearcher searcher;

    private static final Set<? extends Operator> UNSUPPORTED_OPERATORS = Collections.unmodifiableSet(EnumSet.of(Ops.STARTS_WITH_IC,
            Ops.EQ_IGNORE_CASE, Ops.ENDS_WITH_IC, Ops.STRING_CONTAINS_IC));

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private Document createDocument() {
        Document doc = new Document();

        doc.add(new Field("title", new StringReader("Jurassic Park")));
        doc.add(new Field("author", new StringReader("Michael Crichton")));
        doc.add(new Field("text", new StringReader(
                "It's a UNIX system! I know this!")));
        doc.add(new Field("rating", new StringReader("Good")));
        doc.add(new Field("publisher", "", Store.YES, Index.ANALYZED));
        doc.add(new IntField("year", 1990, Store.YES));
        doc.add(new DoubleField("gross", 900.0, Store.YES));

        doc.add(new LongField("longField", 1, Store.YES));
        doc.add(new IntField("shortField", 1, Store.YES));
        doc.add(new IntField("byteField", 1, Store.YES));
        doc.add(new FloatField("floatField", 1, Store.YES));

        return doc;
    }

    @Before
    public void setUp() throws Exception {
        serializer = new LuceneSerializer(true, true);
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        title = entityPath.getString("title");
        author = entityPath.getString("author");
        text = entityPath.getString("text");
        publisher = entityPath.getString("publisher");
        year = entityPath.getNumber("year", Integer.class);
        rating = entityPath.getString("rating");
        gross = entityPath.getNumber("gross", Double.class);
        titles = entityPath.getCollection("title", String.class,
                StringPath.class);

        longField = entityPath.getNumber("longField", Long.class);
        shortField = entityPath.getNumber("shortField", Short.class);
        byteField = entityPath.getNumber("byteField", Byte.class);
        floatField = entityPath.getNumber("floatField", Float.class);

        idx = new RAMDirectory();
        config = new IndexWriterConfig(new StandardAnalyzer())
                .setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        writer = new IndexWriter(idx, config);

        writer.addDocument(createDocument());

        writer.close();

        IndexReader reader = DirectoryReader.open(idx);
        searcher = new IndexSearcher(reader);
    }

    @After
    public void tearDown() throws Exception {
        searcher.getIndexReader().close();
    }

    private void testQuery(Expression<?> expr, int expectedHits)
            throws Exception {
        Query query = serializer.toQuery(expr, metadata);
        TopDocs docs = searcher.search(query, 100);
        assertEquals(expectedHits, docs.totalHits);
    }

    private void testQuery(Expression<?> expr, String expectedQuery,
            int expectedHits) throws Exception {
        Query query = serializer.toQuery(expr, metadata);
        TopDocs docs = searcher.search(query, 100);
        assertEquals(expectedHits, docs.totalHits);
        assertEquals(expectedQuery, query.toString());
    }

    @Test
    public void queryElement() throws Exception {
        Query query1 = serializer.toQuery(author.like("Michael"), metadata);
        Query query2 = serializer.toQuery(text.like("Text"), metadata);

        BooleanExpression query = Expressions.anyOf(new QueryElement(query1),
                new QueryElement(query2));
        testQuery(query, "author:michael text:text", 1);
    }

    @Test
    public void like() throws Exception {
        testQuery(author.like("*ichael*"), "author:*ichael*", 1);
    }

    @Test
    public void like_custom_wildcard_single_character() throws Exception {
        testQuery(author.like("Mi?hael"), "author:mi?hael", 1);
    }

    @Test
    public void like_custom_wildcard_multiple_character() throws Exception {
        testQuery(text.like("*U*X*"), "text:*u*x*", 1);
    }

    @Test
    public void like_phrase() throws Exception {
        testQuery(title.like("*rassic Par*"), "+title:**rassic* +title:*par**",
                1);
    }

    @Test
    public void like_or_like() throws Exception {
        testQuery(title.like("House").or(author.like("*ichae*")),
                "title:house author:*ichae*", 1);
    }

    @Test
    public void like_and_like() throws Exception {
        testQuery(title.like("*assic*").and(rating.like("G?od")),
                "+title:*assic* +rating:g?od", 1);
    }

    @Test
    public void eq() throws Exception {
        testQuery(rating.eq("good"), "rating:good", 1);
    }

    @Test
    public void eq_with_deep_path() throws Exception {
        StringPath deepPath = entityPath.get("property1", Object.class)
                .getString("property2");
        testQuery(deepPath.eq("good"), "property1.property2:good", 0);
    }

    @Test
    public void fuzzyLike() throws Exception {
        testQuery(LuceneExpressions.fuzzyLike(rating, "Good"), "rating:Good~2",
                1);
    }

    @Test
    public void fuzzyLike_with_similarity() throws Exception {
        testQuery(LuceneExpressions.fuzzyLike(rating, "Good", 2),
                "rating:Good~2", 1);
    }

    @Test
    public void fuzzyLike_with_similarity_and_prefix() throws Exception {
        testQuery(LuceneExpressions.fuzzyLike(rating, "Good", 2, 0),
                "rating:Good~2", 1);
    }

    @Test
    @Ignore
    public void eq_numeric_integer() throws Exception {
        testQuery(year.eq(1990), "year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    @Ignore
    public void eq_numeric_double() throws Exception {
        testQuery(gross.eq(900.00), "gross:" + GROSS_PREFIX_CODED, 1);
    }

    @Test
    @Ignore
    public void eq_numeric() throws Exception {
        testQuery(longField.eq(1L), "longField:" + LONG_PREFIX_CODED, 1);
        testQuery(shortField.eq((short) 1), "shortField:" + SHORT_PREFIX_CODED,
                1);
        testQuery(byteField.eq((byte) 1), "byteField:" + BYTE_PREFIX_CODED, 1);
        testQuery(floatField.eq((float) 1.0), "floatField:"
                + FLOAT_PREFIX_CODED, 1);
    }

    @Test
    public void equals_ignores_case() throws Exception {
        testQuery(title.eq("Jurassic"), "title:jurassic", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void title_equals_ignore_case_or_year_equals() throws Exception {
        testQuery(title.equalsIgnoreCase("House").or(year.eq(1990)),
                "title:house year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    @Ignore
    public void eq_and_eq() throws Exception {
        testQuery(title.eq("Jurassic Park").and(year.eq(1990)),
                "+title:\"jurassic park\" +year:" + YEAR_PREFIX_CODED, 1);
    }

    @Test
    @Ignore
    public void eq_and_eq_and_eq() throws Exception {
        testQuery(
                title.eq("Jurassic Park").and(year.eq(1990))
                        .and(author.eq("Michael Crichton")),
                "+(+title:\"jurassic park\" +year:" + YEAR_PREFIX_CODED
                        + ") +author:\"michael crichton\"", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void equals_ignore_case_and_or() throws Exception {
        testQuery(
                title.equalsIgnoreCase("Jurassic Park")
                        .and(rating.equalsIgnoreCase("Bad"))
                        .or(author.equalsIgnoreCase("Michael Crichton")),
                "(+title:\"jurassic park\" +rating:bad) author:\"michael crichton\"",
                1);
    }

    @Test
    public void eq_or_eq_and_eq_does_not_find_results() throws Exception {
        testQuery(
                title.eq("jeeves").or(rating.eq("superb"))
                        .and(author.eq("michael crichton")),
                "+(title:jeeves rating:superb) +author:\"michael crichton\"", 0);
    }

    @Test
    public void eq_phrase() throws Exception {
        testQuery(title.eq("Jurassic Park"), "title:\"jurassic park\"", 1);
    }

    @Test
    @Ignore("Not easily done in Lucene!")
    public void publisher_equals_empty_string() throws Exception {
        testQuery(publisher.eq(""), "publisher:", 1);
    }

    @Test
    public void eq_phrase_should_not_find_results_but_luceNe_semantics_differs_from_querydsls()
            throws Exception {
        testQuery(text.eq("UNIX System"), "text:\"unix system\"", 1);
    }

    @Test
    public void eq_phrase_does_not_find_results_because_word_in_middle()
            throws Exception {
        testQuery(title.eq("Jurassic Amusement Park"),
                "title:\"jurassic amusement park\"", 0);
    }

    @Test
    public void like_not_does_not_find_results() throws Exception {
        testQuery(title.like("*H*e*").not(), "-title:*h*e* +*:*", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void title_equals_ignore_case_negation_or_rating_equals_ignore_case()
            throws Exception {
        testQuery(
                title.equalsIgnoreCase("House").not()
                        .or(rating.equalsIgnoreCase("Good")),
                "-title:house rating:good", 1);
    }

    @Test
    public void eq_not_does_not_find_results() throws Exception {
        testQuery(title.eq("Jurassic Park").not(),
                "-title:\"jurassic park\" +*:*", 0);
    }

    @Test
    public void title_equals_not_house() throws Exception {
        testQuery(title.eq("house").not(), "-title:house +*:*", 1);
    }

    @Test
    public void eq_and_eq_not_does_not_find_results_because_second_expression_finds_nothing()
            throws Exception {
        testQuery(rating.eq("superb").and(title.eq("house").not()),
                "+rating:superb +(-title:house +*:*)", 0);
    }

    @Test
    public void not_equals_finds_one() throws Exception {
        testQuery(title.ne("house"), "-title:house +*:*", 1);
    }

    @Test
    public void not_equals_finds_none() throws Exception {
        testQuery(title.ne("Jurassic Park"), "-title:\"jurassic park\" +*:*", 0);
    }

    @Test
    public void nothing_found_with_not_equals_or_equals() throws Exception {
        testQuery(title.ne("jurassic park").or(rating.eq("lousy")),
                "(-title:\"jurassic park\" +*:*) rating:lousy", 0);
    }

    @Test
    public void ne_and_eq() throws Exception {
        testQuery(title.ne("house").and(rating.eq("good")),
                "+(-title:house +*:*) +rating:good", 1);
    }

    @Test
    public void startsWith() throws Exception {
        testQuery(title.startsWith("Jurassi"), "title:jurassi*", 1);
    }

    @Test
    public void startsWith_phrase() throws Exception {
        testQuery(title.startsWith("jurassic par"),
                "+title:jurassic* +title:*par*", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void starts_with_ignore_case_phrase_does_not_find_results()
            throws Exception {
        testQuery(title.startsWithIgnoreCase("urassic Par"),
                "+title:urassic* +title:*par*", 0);
    }

    @Test
    public void endsWith() throws Exception {
        testQuery(title.endsWith("ark"), "title:*ark", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ends_with_ignore_case_phrase() throws Exception {
        testQuery(title.endsWithIgnoreCase("sic Park"),
                "+title:*sic* +title:*park", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void ends_with_ignore_case_phrase_does_not_find_results()
            throws Exception {
        testQuery(title.endsWithIgnoreCase("sic Par"),
                "+title:*sic* +title:*par", 0);
    }

    @Test
    public void contains() throws Exception {
        testQuery(title.contains("rassi"), "title:*rassi*", 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void contains_ignore_case_phrase() throws Exception {
        testQuery(title.containsIgnoreCase("rassi Pa"),
                "+title:*rassi* +title:*pa*", 1);
    }

    @Test
    public void contains_user_inputted_wildcards_dont_work() throws Exception {
        testQuery(title.contains("r*i"), "title:*r\\*i*", 0);
    }

    @Test
    public void between() throws Exception {
        testQuery(title.between("Indiana", "Kundun"),
                "title:[indiana TO kundun]", 1);
    }

    @Test
    public void between_numeric_integer() throws Exception {
        testQuery(year.between(1980, 2000), "year:[1980 TO 2000]", 1);
    }

    @Test
    public void between_numeric_double() throws Exception {
        testQuery(gross.between(10.00, 19030.00), "gross:[10.0 TO 19030.0]", 1);
    }

    @Test
    public void between_numeric() throws Exception {
        testQuery(longField.between(0L, 2L), "longField:[0 TO 2]", 1);
        testQuery(shortField.between((short) 0, (short) 2),
                "shortField:[0 TO 2]", 1);
        testQuery(byteField.between((byte) 0, (byte) 2), "byteField:[0 TO 2]",
                1);
        testQuery(floatField.between((float) 0.0, (float) 2.0),
                "floatField:[0.0 TO 2.0]", 1);
    }

    @Test
    public void between_is_inclusive_from_start() throws Exception {
        testQuery(title.between("Jurassic", "Kundun"),
                "title:[jurassic TO kundun]", 1);
    }

    @Test
    public void between_is_inclusive_to_end() throws Exception {
        testQuery(title.between("Indiana", "Jurassic"),
                "title:[indiana TO jurassic]", 1);
    }

    @Test
    public void between_does_not_find_results() throws Exception {
        testQuery(title.between("Indiana", "Jurassib"),
                "title:[indiana TO jurassib]", 0);
    }

    @Test
    public void in() throws Exception {
        testQuery(title.in(Arrays.asList("jurassic", "park")),
                "title:jurassic title:park", 1);
        testQuery(title.in("jurassic", "park"), "title:jurassic title:park", 1);
        testQuery(title.eq("jurassic").or(title.eq("park")),
                "title:jurassic title:park", 1);
    }

    @Test
    public void lt() throws Exception {
        testQuery(rating.lt("Superb"), "rating:{* TO superb}", 1);
    }

    @Test
    public void lt_numeric_integer() throws Exception {
        testQuery(year.lt(1991), "year:{* TO 1991}", 1);
    }

    @Test
    public void lt_numeric_double() throws Exception {
        testQuery(gross.lt(10000.0), "gross:{* TO 10000.0}", 1);
    }

    @Test
    public void lt_not_in_range_because_equal() throws Exception {
        testQuery(rating.lt("Good"), "rating:{* TO good}", 0);
    }

    @Test
    public void lt_numeric_integer_not_in_range_because_equal()
            throws Exception {
        testQuery(year.lt(1990), "year:{* TO 1990}", 0);
    }

    @Test
    public void lt_numeric_double_not_in_range_because_equal() throws Exception {
        testQuery(gross.lt(900.0), "gross:{* TO 900.0}", 0);
    }

    @Test
    public void loe() throws Exception {
        testQuery(rating.loe("Superb"), "rating:[* TO superb]", 1);
    }

    @Test
    public void loe_numeric_integer() throws Exception {
        testQuery(year.loe(1991), "year:[* TO 1991]", 1);
    }

    @Test
    public void loe_numeric_double() throws Exception {
        testQuery(gross.loe(903.0), "gross:[* TO 903.0]", 1);
    }

    @Test
    public void loe_equal() throws Exception {
        testQuery(rating.loe("Good"), "rating:[* TO good]", 1);
    }

    @Test
    public void loe_numeric_integer_equal() throws Exception {
        testQuery(year.loe(1990), "year:[* TO 1990]", 1);
    }

    @Test
    public void loe_numeric_double_equal() throws Exception {
        testQuery(gross.loe(900.0), "gross:[* TO 900.0]", 1);
    }

    @Test
    public void loe_not_found() throws Exception {
        testQuery(rating.loe("Bad"), "rating:[* TO bad]", 0);
    }

    @Test
    public void loe_numeric_integer_not_found() throws Exception {
        testQuery(year.loe(1989), "year:[* TO 1989]", 0);
    }

    @Test
    public void loe_numeric_double_not_found() throws Exception {
        testQuery(gross.loe(899.9), "gross:[* TO 899.9]", 0);
    }

    @Test
    public void gt() throws Exception {
        testQuery(rating.gt("Bad"), "rating:{bad TO *}", 1);
    }

    @Test
    public void gt_numeric_integer() throws Exception {
        testQuery(year.gt(1989), "year:{1989 TO *}", 1);
    }

    @Test
    public void gt_numeric_double() throws Exception {
        testQuery(gross.gt(100.00), "gross:{100.0 TO *}", 1);
    }

    @Test
    public void gt_not_in_range_because_equal() throws Exception {
        testQuery(rating.gt("Good"), "rating:{good TO *}", 0);
    }

    @Test
    public void gt_numeric_integer_not_in_range_because_equal()
            throws Exception {
        testQuery(year.gt(1990), "year:{1990 TO *}", 0);
    }

    @Test
    public void gt_numeric_double_not_in_range_because_equal() throws Exception {
        testQuery(gross.gt(900.00), "gross:{900.0 TO *}", 0);
    }

    @Test
    public void goe() throws Exception {
        testQuery(rating.goe("Bad"), "rating:[bad TO *]", 1);
    }

    @Test
    public void goe_numeric_integer() throws Exception {
        testQuery(year.goe(1989), "year:[1989 TO *]", 1);
    }

    @Test
    public void goe_numeric_double() throws Exception {
        testQuery(gross.goe(320.50), "gross:[320.5 TO *]", 1);
    }

    @Test
    public void goe_equal() throws Exception {
        testQuery(rating.goe("Good"), "rating:[good TO *]", 1);
    }

    @Test
    public void goe_numeric_integer_equal() throws Exception {
        testQuery(year.goe(1990), "year:[1990 TO *]", 1);
    }

    @Test
    public void goe_numeric_double_equal() throws Exception {
        testQuery(gross.goe(900.00), "gross:[900.0 TO *]", 1);
    }

    @Test
    public void goe_not_found() throws Exception {
        testQuery(rating.goe("Hood"), "rating:[hood TO *]", 0);
    }

    @Test
    public void goe_numeric_integer_not_found() throws Exception {
        testQuery(year.goe(1991), "year:[1991 TO *]", 0);
    }

    @Test
    public void goe_numeric_double_not_found() throws Exception {
        testQuery(gross.goe(900.10), "gross:[900.1 TO *]", 0);
    }

    @Test
    public void equals_empty_string() throws Exception {
        testQuery(title.eq(""), "title:", 0);
    }

    @Test
    public void not_equals_empty_string() throws Exception {
        testQuery(title.ne(""), "-title: +*:*", 1);
    }

    @Test
    public void contains_empty_string() throws Exception {
        testQuery(title.contains(""), "title:**", 1);
    }

    @Test
    public void like_empty_string() throws Exception {
        testQuery(title.like(""), "title:", 0);
    }

    @Test
    public void starts_with_empty_string() throws Exception {
        testQuery(title.startsWith(""), "title:*", 1);
    }

    @Test
    public void ends_with_empty_string() throws Exception {
        testQuery(title.endsWith(""), "title:*", 1);
    }

    @Test
    public void between_empty_strings() throws Exception {
        testQuery(title.between("", ""), "title:[ TO ]", 0);
    }

    @Test
    public void booleanBuilder() throws Exception {
        testQuery(new BooleanBuilder(gross.goe(900.10)), "gross:[900.1 TO *]",
                0);
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
    public void pathAny() throws Exception {
        testQuery(titles.any().eq("Jurassic"), "title:jurassic", 1);
    }

    private boolean unsupportedOperation(Predicate filter) {
        return UNSUPPORTED_OPERATORS.contains(((Operation<?>) filter).getOperator());
    }

    @Test
    public void various() throws Exception {
        MatchingFiltersFactory filters = new MatchingFiltersFactory(
                QuerydslModule.LUCENE, Target.LUCENE);
        for (Predicate filter : filters.string(title,
                StringConstant.create("jurassic park"))) {
            if (unsupportedOperation(filter)) {
                continue;
            }
            testQuery(filter, 1);
        }

        for (Predicate filter : filters.string(author,
                StringConstant.create("michael crichton"))) {
            if (unsupportedOperation(filter)) {
                continue;
            }
            testQuery(filter, 1);
        }

        for (Predicate filter : filters.string(title,
                StringConstant.create("1990"))) {
            if (unsupportedOperation(filter)) {
                continue;
            }
            testQuery(filter, 0);
        }
    }

}
