/*
 * Copyright 2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.mongodb.document;

import static org.junit.Assert.*;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.DBRef;
import com.querydsl.core.types.Path;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.mongodb.Point;
import com.querydsl.mongodb.domain.QAddress;
import com.querydsl.mongodb.domain.QPerson;
import com.querydsl.mongodb.domain.QUser;

public class MongodbDocumentSerializerTest {

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

    private MongodbDocumentSerializer serializer;

    @Before
    public void before() {
        serializer = new MongodbDocumentSerializer() {
            @Override
            protected DBRef asReference(Object constant) {
                return null;
            }

            @Override
            protected boolean isReference(Path<?> arg) {
                return false;
            }
        };

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
    public void paths() {
        QUser user = QUser.user;
        assertEquals("user", serializer.visit(user, null));
        assertEquals("addresses", serializer.visit(user.addresses, null));
        assertEquals("addresses", serializer.visit(user.addresses.any(), null));
        assertEquals("addresses.street", serializer.visit(user.addresses.any().street, null));
        assertEquals("firstName", serializer.visit(user.firstName, null));
    }

    @Test
    public void indexedAccess() {
        QUser user = QUser.user;
        assertEquals("addresses.0.street", serializer.visit(user.addresses.get(0).street, null));
    }

    @Test
    public void collectionAny() {
        QUser user = QUser.user;
        assertQuery(user.addresses.any().street.eq("Aakatu"), document("addresses.street","Aakatu"));
    }

    @Test
    public void collectionIsEmpty() {
        Document expected = document("$or",
            Arrays.asList(
                document("addresses", Collections.emptyList()),
                document("addresses",
                    document("$exists", false))));
        assertQuery(QUser.user.addresses.isEmpty(), expected);
    }

    @Test
    public void collectionIsNotEmpty() {
        Document expected = document("$nor",
            Arrays.asList(
                document("addresses", Collections.emptyList()),
                document("addresses",
                    document("$exists", false))));
        assertQuery(QUser.user.addresses.isNotEmpty(), expected);
    }

    @Test
    public void equals() {
        assertQuery(title.eq("A"), document("title","A"));
        assertQuery(year.eq(1), document("year",1));
        assertQuery(gross.eq(1.0D), document("gross", 1.0D));
        assertQuery(longField.eq(1L), document("longField", 1L));
        assertQuery(shortField.eq((short) 1), document("shortField", 1));
        assertQuery(byteField.eq((byte) 1), document("byteField", 1));
        assertQuery(floatField.eq(1.0F), document("floatField", 1.0F));

        assertQuery(date.eq(dateVal), document("date", dateVal));
    }

    @Test
    public void eqAndEq() {
        assertQuery(
            title.eq("A").and(year.eq(1)),
            document("title","A").append("year", 1)
        );

        assertQuery(
            title.eq("A").and(year.eq(1).and(gross.eq(1.0D))),
            document("title","A").append("year", 1).append("gross", 1.0D)
        );
    }

    @Test
    public void notEq() {
        assertQuery(title.ne("A"), document("title", document("$ne", "A")));
    }

    @Test
    public void between() {
        assertQuery(year.between(1, 10), document("year", document("$gte", 1).append("$lte", 10)));
    }

    @Test
    public void lessAndGreaterAndBetween() {
        assertQuery(title.lt("A"), document("title", document("$lt", "A")));
        assertQuery(year.gt(1), document("year", document("$gt", 1)));

        assertQuery(title.loe("A"), document("title", document("$lte", "A")));
        assertQuery(year.goe(1), document("year", document("$gte", 1)));

        assertQuery(
                year.gt(1).and(year.lt(10)),
                document("$and", Arrays.asList(
                  document("year", document("$gt", 1)),
                  document("year", document("$lt", 10))))
        );

        assertQuery(
                year.between(1, 10),
                document("year", document("$gte", 1).append("$lte", 10))
        );
    }

    @Test
    public void in() {
        assertQuery(year.in(1,2,3), document("year", document("$in", Arrays.asList(1,2,3))));
    }

    @Test
    public void notIn() {
        assertQuery(year.in(1,2,3).not(), document("year", document("$nin", Arrays.asList(1,2,3))));
        assertQuery(year.notIn(1,2,3), document("year", document("$nin", Arrays.asList(1,2,3))));
    }

    @Test
    public void orderBy() {
        Document orderBy = serializer.toSort(sortList(year.asc()));
        assertEquals(document("year", 1), orderBy);

        orderBy = serializer.toSort(sortList(year.desc()));
        assertEquals(document("year", -1), orderBy);

        orderBy = serializer.toSort(sortList(year.desc(), title.asc()));
        assertEquals(document("year", -1).append("title", 1), orderBy);
    }

    @Test
    public void regexCases() {
        assertQuery(title.startsWith("A"),
                document("title", Pattern.compile("^\\QA\\E")));
        assertQuery(title.startsWithIgnoreCase("A"),
                document("title", Pattern.compile("^\\QA\\E", Pattern.CASE_INSENSITIVE)));

        assertQuery(title.endsWith("A"),
                document("title", Pattern.compile("\\QA\\E$")));
        assertQuery(title.endsWithIgnoreCase("A"),
                document("title", Pattern.compile("\\QA\\E$", Pattern.CASE_INSENSITIVE)));

        assertQuery(title.equalsIgnoreCase("A"),
                document("title", Pattern.compile("^\\QA\\E$", Pattern.CASE_INSENSITIVE)));

        assertQuery(title.contains("A"),
                document("title", Pattern.compile(".*\\QA\\E.*")));
        assertQuery(title.containsIgnoreCase("A"),
                document("title", Pattern.compile(".*\\QA\\E.*", Pattern.CASE_INSENSITIVE)));

        assertQuery(title.matches(".*A^"),
                document("title", Pattern.compile(".*A^")));
    }

    @Test
    public void and() {
        assertQuery(
            title.startsWithIgnoreCase("a").and(title.endsWithIgnoreCase("b")),

            document("$and", Arrays.asList(
                document("title", document("$regex", "^\\Qa\\E").append("$options", "i")),
                document("title", document("$regex", "\\Qb\\E$").append("$options", "i")))));

    }

    @Test
    public void near() {
        assertQuery(MongodbExpressions.near(new Point("point"), 1.0, 2.0),
                document("point", document("$near", Arrays.asList(1.0, 2.0))));
    }

    @Test
    public void near_sphere() {
        assertQuery(MongodbExpressions.nearSphere(new Point("point"), 1.0, 2.0),
                document("point", document("$nearSphere", Arrays.asList(1.0, 2.0))));
    }

    @Test
    public void not() {
        assertQuery(title.eq("A").not(), document("title", document("$ne","A")));

        assertQuery(title.lt("A").not().and(year.ne(1800)),
                document("title", document("$not", document("$lt","A"))).
                append("year", document("$ne", 1800)));
    }

    @Test
    public void objectId() {
        ObjectId id = new ObjectId();
        QPerson person = QPerson.person;
        assertQuery(person.id.eq(id), document("id",id));
        assertQuery(person.addressId.eq(id), document("addressId",id));
    }

    @Test
    public void path() {
        QUser user = QUser.user;
        assertEquals("firstName", serializer.visit(user.firstName, null));
        assertEquals("firstName", serializer.visit(user.as(QUser.class).firstName, null));
        assertEquals("mainAddress.street", serializer.visit(user.mainAddress().street, null));
        assertEquals("mainAddress.street", serializer.visit(user.mainAddress().as(QAddress.class).street, null));
    }


    private List<OrderSpecifier<?>> sortList(OrderSpecifier<?> ... order) {
        return Arrays.asList(order);
    }

    private void assertQuery(Expression<?> e, Document expected) {
        Document result = (Document) serializer.handle(e);
        assertEquals(expected.toJson(), result.toJson());
    }

    public static Document document(String key, Object... value) {
        if (value.length == 1) {
            return new Document(key, value[0]);
        }
        return new Document(key, value);
    }
}
