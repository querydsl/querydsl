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
package com.querydsl.mongodb;

import static org.junit.Assert.assertEquals;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.querydsl.mongodb.domain.QDummyEntity;
import com.querydsl.mongodb.domain.QUser;
import com.querydsl.mongodb.morphia.MorphiaSerializer;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.path.*;

public class MongodbSerializerTest {

    private PathBuilder<Object> entityPath;
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

    private MongodbSerializer serializer;

    @Before
    public void before() {
        serializer = new MorphiaSerializer(null);
        entityPath = new PathBuilder<Object>(Object.class, "obj");
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
        assertEquals("addresses", serializer.visit(user.addresses, null));
        assertEquals("addresses", serializer.visit(user.addresses.any(), null));
        assertEquals("addresses.street", serializer.visit(user.addresses.any().street, null));
        assertEquals("firstName", serializer.visit(user.firstName, null));
    }

    @Test
    public void PropertyAnnotation() {
        QDummyEntity entity = QDummyEntity.dummyEntity;
        assertEquals("prop", serializer.visit(entity.property, null));
    }

    @Test
    public void IndexedAccess() {
        QUser user = QUser.user;
        assertEquals("addresses.0.street", serializer.visit(user.addresses.get(0).street, null));
    }

    @Test
    public void CollectionAny() {
        QUser user = QUser.user;
        assertQuery(user.addresses.any().street.eq("Aakatu"), dbo("addresses.street","Aakatu"));
    }

    @Test
    public void Equals() {
        assertQuery(title.eq("A"), dbo("title","A"));
        assertQuery(year.eq(1), dbo("year",1));
        assertQuery(gross.eq(1.0D), dbo("gross", 1.0D));
        assertQuery(longField.eq(1L), dbo("longField", 1L));
        assertQuery(shortField.eq((short)1), dbo("shortField", 1));
        assertQuery(byteField.eq((byte)1), dbo("byteField", 1L));
        assertQuery(floatField.eq(1.0F), dbo("floatField", 1.0F));

        assertQuery(date.eq(dateVal), dbo("date", dateVal));
        assertQuery(dateTime.eq(dateTimeVal), dbo("dateTime", dateTimeVal));
    }

    @Test
    public void EqAndEq() {
        assertQuery(
            title.eq("A").and(year.eq(1)),
            dbo("title","A").append("year", 1)
        );

        assertQuery(
            title.eq("A").and(year.eq(1).and(gross.eq(1.0D))),
            dbo("title","A").append("year", 1).append("gross", 1.0D)
        );
    }

    @Test
    public void NotEq() {
        assertQuery(title.ne("A"), dbo("title", dbo("$ne", "A")));
    }

    @Test
    public void Between() {
        System.err.println(dbo("year", dbo("$gte", 1).append("$lte", 10)));
        assertQuery(year.between(1, 10), dbo("year", dbo("$gte", 1).append("$lte", 10)));
    }

    @Test
    public void LessAndGreaterAndBetween() {
        assertQuery(title.lt("A"), dbo("title", dbo("$lt", "A")));
        assertQuery(year.gt(1), dbo("year", dbo("$gt", 1)));

        assertQuery(title.loe("A"), dbo("title", dbo("$lte", "A")));
        assertQuery(year.goe(1), dbo("year", dbo("$gte", 1)));

        assertQuery(
                year.gt(1).and(year.lt(10)),
                dbo("$and", dblist(
                  dbo("year", dbo("$gt", 1)),
                  dbo("year", dbo("$lt", 10))))
        );

        assertQuery(
                year.between(1, 10),
                dbo("year", dbo("$gte", 1).append("$lte", 10))
        );
    }

    @Test
    public void In() {
        assertQuery(year.in(1,2,3), dbo("year", dbo("$in", 1,2,3)));
    }

    @Test
    public void OrderBy() {
        DBObject orderBy = serializer.toSort(sortList(year.asc()));
        assertEquals(dbo("year", 1), orderBy);

        orderBy = serializer.toSort(sortList(year.desc()));
        assertEquals(dbo("year", -1), orderBy);

        orderBy = serializer.toSort(sortList(year.desc(), title.asc()));
        assertEquals(dbo("year", -1).append("title", 1), orderBy);
    }

    @Test
    public void Regexcases() {
        assertQuery(title.startsWith("A"),
                dbo("title", dbo("$regex", "^\\QA\\E")));
        assertQuery(title.startsWithIgnoreCase("A"),
                dbo("title", dbo("$regex", "^\\QA\\E").append("$options", "i")));

        assertQuery(title.endsWith("A"),
                dbo("title", dbo("$regex", "\\QA\\E$")));
        assertQuery(title.endsWithIgnoreCase("A"),
                dbo("title", dbo("$regex", "\\QA\\E$").append("$options", "i")));

        assertQuery(title.equalsIgnoreCase("A"),
                dbo("title", dbo("$regex", "^\\QA\\E$").append("$options", "i")));

        assertQuery(title.contains("A"),
                dbo("title", dbo("$regex", ".*\\QA\\E.*")));
        assertQuery(title.containsIgnoreCase("A"),
                dbo("title", dbo("$regex", ".*\\QA\\E.*").append("$options", "i")));

        assertQuery(title.matches(".*A^"),
                dbo("title", dbo("$regex", ".*A^")));

    }

    @Test
    public void And() {
        assertQuery(
            title.startsWithIgnoreCase("a").and(title.endsWithIgnoreCase("b")),

            dbo("$and", dblist(
                dbo("title", dbo("$regex", "^\\Qa\\E").append("$options", "i")),
                dbo("title", dbo("$regex", "\\Qb\\E$").append("$options", "i")))));

    }

    @Test
    public void Not() {
        assertQuery(title.eq("A").not(), dbo("title", dbo("$ne","A")));

        assertQuery(title.lt("A").not().and(year.ne(1800)),
                dbo("title", dbo("$not", dbo("$lt","A"))).
                append("year", dbo("$ne", 1800)));
    }


    private List<OrderSpecifier<?>> sortList(OrderSpecifier<?> ... order) {
        return Arrays.asList(order);
    }

    private void assertQuery(Expression<?> e, BasicDBObject expected) {
        BasicDBObject result = (BasicDBObject) serializer.handle(e);
        assertEquals(expected.toString(), result.toString());
    }

    public static BasicDBObject dbo(String key, Object... value) {
        if (value.length == 1) {
            return new BasicDBObject(key, value[0]);
        }
        return new BasicDBObject(key, value);
    }

    public static BasicDBList dblist(Object... contents) {
        BasicDBList list = new BasicDBList();
        list.addAll(Arrays.asList(contents));
        return list;
    }


}
