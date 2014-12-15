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
package com.mysema.query.elasticsearch;

import com.google.common.collect.Lists;
import com.mysema.query.elasticsearch.domain.QUser;
import com.mysema.query.types.Expression;
import com.mysema.query.types.path.*;
import org.apache.lucene.queryparser.flexible.core.util.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;

import static org.elasticsearch.index.query.QueryBuilders.*;
import static org.junit.Assert.assertEquals;

public class ElasticsearchSerializerTest {

    private PathBuilder<Object> entityPath;
    private StringPath id;
    private StringPath title;
    private NumberPath<Integer> year;
    private NumberPath<Double> gross;

    private NumberPath<Long> longField;
    private NumberPath<Short> shortField;
    private NumberPath<Byte> byteField;
    private NumberPath<Float> floatField;

    private DatePath<Date> date;
    private final Date dateVal = new Date();
    private DateTimePath<Timestamp> dateTime;
    private final Timestamp dateTimeVal = new Timestamp(System.currentTimeMillis());

    private ElasticsearchSerializer serializer;

    @Before
    public void before() {
        serializer = new ElasticsearchSerializer();
        entityPath = new PathBuilder<Object>(Object.class, "obj");
        id = entityPath.getString("id");
        title = entityPath.getString("title");
        year = entityPath.getNumber("year", Integer.class);
        gross = entityPath.getNumber("gross", Double.class);
        longField = entityPath.getNumber("longField", Long.class);
        shortField = entityPath.getNumber("shortField", Short.class);
        byteField = entityPath.getNumber("byteField", Byte.class);
        floatField = entityPath.getNumber("floatField", Float.class);
        date = entityPath.getDate("date", Date.class);
        dateTime = entityPath.getDateTime("dateTime", Timestamp.class);
    }

    @Test
    public void Paths() {
        QUser user = QUser.user;
        assertEquals("user", serializer.visit(user, null));
        //assertEquals("addresses", serializer.visit(user.addresses, null));
        //assertEquals("addresses", serializer.visit(user.addresses.any(), null));
        //assertEquals("addresses.street", serializer.visit(user.addresses.any().street, null));
        assertEquals("firstName", serializer.visit(user.firstName, null));
    }

    @Test
    public void PropertyAnnotation() {
        //QDummyEntity entity = QDummyEntity.dummyEntity;
        //assertEquals("prop", serializer.visit(entity.property, null));
    }

    @Test
    public void IndexedAccess() {
        QUser user = QUser.user;
        //assertEquals("addresses.0.street", serializer.visit(user.addresses.get(0).street, null));
    }

    @Test
    public void CollectionAny() {
        QUser user = QUser.user;
        //assertQuery(eq("addresses.street", "Aakatu"), user.addresses.any().street.eq("Aakatu"));
    }

    @Test
    public void Equals() {
        assertQuery(and(eq("title", "A")), title.eq("A"));
        assertQuery(and(eq("year", 1)), year.eq(1));
        assertQuery(and(eq("gross", 1.0D)), gross.eq(1.0D));
        assertQuery(and(eq("longField", 1L)), longField.eq(1L));
        assertQuery(and(eq("shortField", 1)), shortField.eq((short)1));
        assertQuery(and(eq("byteField", 1L)), byteField.eq((byte)1));
        assertQuery(and(eq("floatField", 1.0F)), floatField.eq(1.0F));

        assertQuery(and(eq("date", dateVal)), date.eq(dateVal));
        assertQuery(and(eq("dateTime", dateTimeVal)), dateTime.eq(dateTimeVal));

        assertQuery(and(idsQuery().ids("id1")), id.eq("id1"));
    }

    @Test
    public void EqAndEq() {
        assertQuery(
            and(eq("title", "A"), eq("year", 1)),
            title.eq("A").and(year.eq(1))
        );

        assertQuery(
            and(eq("year", 1), eq("gross", 1.0D), eq("title", "A")),
            title.eq("A").and(year.eq(1).and(gross.eq(1.0D)))
        );

        assertQuery(
                and(eq("title", "A"), eq("year", 1), eq("gross", 1.0D)),
                title.eq("A").and(year.eq(1)).and(gross.eq(1.0D))
        );
    }

    @Test
    public void EqOrEq() {
        assertQuery(
                or(eq("title", "A"), eq("year", 1)),
                title.eq("A").or(year.eq(1))
        );

        assertQuery(
                or(eq("year", 1), eq("gross", 1.0D), eq("title", "A")),
                title.eq("A").or(year.eq(1).or(gross.eq(1.0D)))
        );

        assertQuery(
                or(eq("title", "A"), eq("year", 1), eq("gross", 1.0D)),
                title.eq("A").or(year.eq(1)).or(gross.eq(1.0D))
        );
    }

    @Test
    public void In() {
        assertQuery(
            and(in("title", Lists.newArrayList("A", "B", "C"))),
            title.in(Lists.newArrayList("A", "B", "C"))
        );
    }

    @Test
    public void Between() {
        Date start = new Date(31L * 24L * 60L * 60L * 1000L);
        Date end = new Date();
        assertQuery(
                and(between("date", start, end)),
                date.between(start, end)
        );

        assertQuery(
                and(between("year", 1, 2)),
                year.between(1, 2)
        );
    }

    @Test
    public void InAndBetween() {
        Date start = new Date(31L * 24L * 60L * 60L * 1000L);
        Date end = new Date();
        assertQuery(
                and(in("title", Lists.newArrayList("A", "B", "C")), between("date", start, end)),
                title.in(Lists.newArrayList("A", "B", "C")).and(date.between(start, end))
        );
    }

    @Test
    public void EqOrEqAndEq() {

        assertQuery(
                or(eq("title", "A"), and(eq("year", 1), eq("gross", 1.0D))),
                title.eq("A").or(year.eq(1).and(gross.eq(1.0D)))
        );

        assertQuery(
                and(
                        or(eq("title", "A"), eq("year", 1)),
                        eq("gross", 1.0D)),
                title.eq("A").or(year.eq(1)).and(gross.eq(1.0D))
        );
    }

    @Test
    public void EqAndEqOrEq() {

        assertQuery(
                and(
                        eq("title", "A"),
                        or(eq("year", 1), eq("gross", 1.0D))),
                title.eq("A").and(year.eq(1).or(gross.eq(1.0D)))
        );

        assertQuery(
                or(
                        and(eq("title", "A"), eq("year", 1)),
                        eq("gross", 1.0D)),
                title.eq("A").and(year.eq(1)).or(gross.eq(1.0D))
        );
    }

    @Test
    public void EqAndEqOrEqAndEq() {

        assertQuery(
                and(
                        or(
                                and(eq("title", "A"), eq("year", 1)),
                                eq("title", "B")),
                        eq("year", 2)
                ),
                title.eq("A").and(year.eq(1)).or(title.eq("B")).and(year.eq(2))
        );

        assertQuery(
                or(
                        and(eq("title", "A"), eq("year", 1)),
                        and(eq("title", "B"), eq("year", 2))
                ),
                title.eq("A").and(year.eq(1)).or(title.eq("B").and(year.eq(2)))
        );

        assertQuery(
                and(
                        or(
                                eq("year", 1),
                                eq("title", "B")),
                        eq("year", 2),
                        eq("title", "A")),
                title.eq("A").and(year.eq(1).or(title.eq("B")).and(year.eq(2)))
        );

        assertQuery(
                and(
                        eq("title", "A"),
                        or(
                                eq("year", 1),
                                and(eq("title", "B"), eq("year", 2)))),
                title.eq("A").and(year.eq(1).or(title.eq("B").and(year.eq(2))))
        );

    }

    public static QueryBuilder eq(String key, Object value) {
        return QueryBuilders.queryString(StringUtils.toString(value)).field(key);
    }

    public static QueryBuilder in(String key, Iterable<?> values) {
        BoolQueryBuilder query = boolQuery();
        for (Object value : values) {
            query.should(eq(key, value));
        }
        return query;
    }

    public static QueryBuilder and(QueryBuilder... builders) {
        BoolQueryBuilder query = boolQuery();
        must(query, builders);
        return query;
    }

    public static QueryBuilder or(QueryBuilder... builders) {
        BoolQueryBuilder query = boolQuery();
        should(query, builders);
        return query;
    }

    public static BoolQueryBuilder must(BoolQueryBuilder query, QueryBuilder... builders) {
        for (QueryBuilder builder : builders) {
            query.must(builder);
        }
        return query;
    }

    public static BoolQueryBuilder should(BoolQueryBuilder query, QueryBuilder... builders) {
        for (QueryBuilder builder : builders) {
            query.should(builder);
        }
        return query;
    }

    public static QueryBuilder between(String key, int start, int end) {
        return rangeQuery(key).from(start).to(end);
    }

    public static QueryBuilder between(String key, double start, double end) {
        return rangeQuery(key).from(start).to(end);
    }

    public static QueryBuilder between(String key, Date start, Date end) {
        return rangeQuery(key).from(start).to(end);
    }

    private void assertQuery(QueryBuilder expected, Expression<?> e) {
        QueryBuilder result = (QueryBuilder) serializer.handle(e);
        assertEquals(expected.toString(), result.toString());
    }


}
