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

import com.mongodb.DBRef;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.testutil.MongoDB;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.mongodb.domain.*;
import com.querydsl.mongodb.domain.User.Gender;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.mapping.Mapper;

import java.lang.reflect.AnnotatedElement;
import java.net.UnknownHostException;
import java.util.*;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

@Category(MongoDB.class)
public class MongodbQueryTest {

    private final MongoClient mongo;
    private final Morphia morphia;
    private final MongoDatabase database;
    private final Datastore ds;

    private final String dbname = "testdb";
    private final QUser user = QUser.user;
    private final QItem item = QItem.item;
    private final QAddress address = QAddress.address;
    private final QMapEntity mapEntity = QMapEntity.mapEntity;
    private final QDates dates = QDates.dates;
    private final QCountry country = QCountry.country;

    List<User> users = new ArrayList<>();
    List<Document> userDocuments = new ArrayList<>();
    User user1, user2, user3, user4;
    Document u1, u2, u3, u4;
    City tampere, helsinki;

    public MongodbQueryTest() throws UnknownHostException, MongoException {
        mongo = new MongoClient();
        morphia = new Morphia().map(User.class).map(Item.class).map(MapEntity.class).map(Dates.class);
        database = mongo.getDatabase(dbname);
        ds = morphia.createDatastore(mongo, dbname);
    }

    @Before
    public void before() throws UnknownHostException, MongoException {
        ds.delete(ds.createQuery(Item.class));
        ds.delete(ds.createQuery(User.class));
        ds.delete(ds.createQuery(Country.class));
        ds.delete(ds.createQuery(MapEntity.class));

        tampere = new City("Tampere", 61.30, 23.50);
        helsinki = new City("Helsinki", 60.15, 20.03);

        user1 = addUser("Jaakko", "Jantunen", 20, new Address("Aakatu", "00100", helsinki),
                new Address("Aakatu1", "00100", helsinki),
                new Address("Aakatu2", "00100", helsinki));
        user2 = addUser("Jaakki", "Jantunen", 30, new Address("Beekatu", "00200", helsinki));
        user3 = addUser("Jaana", "Aakkonen", 40, new Address("Ceekatu", "00300", tampere));
        user4 = addUser("Jaana", "BeekkoNen", 50, new Address("Deekatu", "00400", tampere));

        u1 = asDocument(user1);
        u2 = asDocument(user2);
        u3 = asDocument(user3);
        u4 = asDocument(user4);

        // order users by lastname, firstname
        userDocuments = Arrays.asList(u3, u4, u2, u1);
    }

    @Test
    public void query1() {
        assertEquals(4L, query(user).fetchCount());
        assertEquals(4L, query(User.class).fetchCount());
    }

    @Test
    public void list_keys() {
        Document u = where(user.firstName.eq("Jaakko")).fetch(user.firstName, user.mainAddress().street).get(0);
        assertEquals("Jaakko", u.get("firstName"));
        assertNull(u.get("lastName"));
        assertEquals("Aakatu", u.get("mainAddress", Document.class).get("street"));
        assertNull(u.get("mainAddress", Document.class).get("postCode"));
    }

    @Test
    public void singleResult_keys() {
        Document u = where(user.firstName.eq("Jaakko")).fetchFirst(user.firstName);
        assertEquals("Jaakko", u.get("firstName"));
        assertNull(u.get("lastName"));
    }

    @Test
    public void uniqueResult_keys() {
        Document u = where(user.firstName.eq("Jaakko")).fetchOne(user.firstName);
        assertEquals("Jaakko", u.get("firstName"));
        assertNull(u.get("lastName"));
    }

    @Test
    public void list_deep_keys() {
        Document u = where(user.firstName.eq("Jaakko")).fetchFirst(user.addresses.any().street);
        List<Document> addresses = u.get("addresses", List.class);
        for (Document a : addresses) {
            assertNotNull(a.get("street"));
            assertNull(a.get("city"));
        }
    }

    @Test
    public void between() {
        assertQuery(user.age.between(20, 30), u2, u1);
        assertQuery(user.age.goe(20).and(user.age.loe(30)), u2, u1);
    }

    @Test
    public void between_not() {
        assertQuery(user.age.between(20, 30).not(), u3, u4);
        assertQuery(user.age.goe(20).and(user.age.loe(30)).not(), u3, u4);
    }

    @Test
    public void contains_key() {
        MapEntity entity = new MapEntity();
        entity.getProperties().put("key", "value");
        ds.save(entity);

        assertTrue(query(mapEntity).where(mapEntity.properties.get("key").isNotNull()).fetchCount() > 0);
        assertFalse(query(mapEntity).where(mapEntity.properties.get("key2").isNotNull()).fetchCount() > 0);

        assertTrue(query(mapEntity).where(mapEntity.properties.containsKey("key")).fetchCount() > 0);
        assertFalse(query(mapEntity).where(mapEntity.properties.containsKey("key2")).fetchCount() > 0);
    }

    @Test
    public void contains_key_not() {
        MapEntity entity = new MapEntity();
        entity.getProperties().put("key", "value");
        ds.save(entity);

        assertFalse(query(mapEntity).where(mapEntity.properties.get("key").isNotNull().not()).fetchCount() > 0);
        assertTrue(query(mapEntity).where(mapEntity.properties.get("key2").isNotNull().not()).fetchCount() > 0);

        assertFalse(query(mapEntity).where(mapEntity.properties.containsKey("key").not()).fetchCount() > 0);
        assertTrue(query(mapEntity).where(mapEntity.properties.containsKey("key2").not()).fetchCount() > 0);
    }

    @Test
    public void equals_ignore_case() {
        assertTrue(where(user.firstName.equalsIgnoreCase("jAaKko")).fetchCount() > 0);
        assertFalse(where(user.firstName.equalsIgnoreCase("AaKk")).fetchCount() > 0);
    }

    @Test
    public void equals_ignore_case_not() {
        assertTrue(where(user.firstName.equalsIgnoreCase("jAaKko").not()).fetchCount() > 0);
        assertTrue(where(user.firstName.equalsIgnoreCase("AaKk").not()).fetchCount() > 0);
    }

    @Test
    public void equals_and_between() {
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.between(20, 30)), u2, u1);
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.goe(20).and(user.age.loe(30))), u2, u1);
    }

    @Test
    public void equals_and_between_not() {
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.between(20, 30)).not(), u3, u4);
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.goe(20).and(user.age.loe(30))).not(), u3, u4);
    }

    @Test
    public void exists() {
        assertTrue(where(user.firstName.eq("Jaakko")).fetchCount() > 0);
        assertFalse(where(user.firstName.eq("JaakkoX")).fetchCount() > 0);
    }

    @Test
    public void find_by_id() {
        assertNotNull(where(user.id.eq(user1.getId())).fetchFirst() != null);
    }

    @Test
    public void notExists() {
        assertFalse(where(user.firstName.eq("Jaakko")).fetchCount() == 0);
        assertTrue(where(user.firstName.eq("JaakkoX")).fetchCount() == 0);
    }

    @Test
    public void uniqueResult() {
        assertEquals("Jantunen", where(user.firstName.eq("Jaakko")).fetchOne().get("lastName"));
    }

    @Test(expected = NonUniqueResultException.class)
    public void uniqueResultContract() {
        where(user.firstName.isNotNull()).fetchOne();
    }

    @Test
    public void singleResult() {
        where(user.firstName.isNotNull()).fetchFirst();
    }

    @Test
    public void longPath() {
        assertEquals(2, query().where(user.mainAddress().city().name.eq("Helsinki")).fetchCount());
        assertEquals(2, query().where(user.mainAddress().city().name.eq("Tampere")).fetchCount());
    }

    @Test
    public void collectionPath() {
        assertEquals(1, query().where(user.addresses.any().street.eq("Aakatu1")).fetchCount());
        assertEquals(0, query().where(user.addresses.any().street.eq("akatu")).fetchCount());
    }

    @Test
    public void dates() {
        long current = System.currentTimeMillis();
        int dayInMillis = 24 * 60 * 60 * 1000;
        Date start = new Date(current);
        ds.delete(ds.createQuery(Dates.class));
        Dates d = new Dates();
        d.setDate(new Date(current + dayInMillis));
        ds.save(d);
        Date end = new Date(current + 2 * dayInMillis);

        Document datesDocument = asDocument(d);

        assertEquals(datesDocument, query(dates).where(dates.date.between(start, end)).fetchFirst());
        assertEquals(0, query(dates).where(dates.date.between(new Date(0), start)).fetchCount());
    }

    @Test
    public void elemMatch() {
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1"}}}
        assertEquals(1, query().anyEmbedded(user.addresses, address).on(address.street.eq("Aakatu1")).fetchCount());
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1" , "postCode" : "00100"}}}
        assertEquals(1, query().anyEmbedded(user.addresses, address)
                .on(address.street.eq("Aakatu1"), address.postCode.eq("00100")).fetchCount());
//      { "addresses" : { "$elemMatch" : { "street" : "akatu"}}}
        assertEquals(0, query().anyEmbedded(user.addresses, address).on(address.street.eq("akatu")).fetchCount());
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1" , "postCode" : "00200"}}}
        assertEquals(0, query().anyEmbedded(user.addresses, address)
                .on(address.street.eq("Aakatu1"), address.postCode.eq("00200")).fetchCount());
    }

    @Test
    public void indexedAccess() {
        assertEquals(1, query().where(user.addresses.get(0).street.eq("Aakatu1")).fetchCount());
        assertEquals(0, query().where(user.addresses.get(1).street.eq("Aakatu1")).fetchCount());
    }

    @Test
    public void count() {
        assertEquals(4, query().fetchCount());
    }

    @Test
    public void order() {
        List<Document> users = query().orderBy(user.age.asc()).fetch();
        assertEquals(asList(u1, u2, u3, u4), users);

        users = query().orderBy(user.age.desc()).fetch();
        assertEquals(asList(u4, u3, u2, u1), users);
    }

    @Test
    public void restrict() {
        assertEquals(asList(u1, u2), query().limit(2).orderBy(user.age.asc()).fetch());
        assertEquals(asList(u2, u3), query().limit(2).offset(1).orderBy(user.age.asc()).fetch());
    }

    @Test
    public void listResults() {
        QueryResults<Document> results = query().limit(2).orderBy(user.age.asc()).fetchResults();
        assertEquals(4L, results.getTotal());
        assertEquals(2, results.getResults().size());

        results = query().offset(2).orderBy(user.age.asc()).fetchResults();
        assertEquals(4L, results.getTotal());
        assertEquals(2, results.getResults().size());
    }

    @Test
    public void emptyResults() {
        QueryResults<Document> results = query().where(user.firstName.eq("XXX")).fetchResults();
        assertEquals(0L, results.getTotal());
        assertEquals(Collections.emptyList(), results.getResults());
    }

    @Test
    public void eqInAndOrderByQueries() {
        assertQuery(user.firstName.eq("Jaakko"), u1);
        assertQuery(user.firstName.equalsIgnoreCase("jaakko"), u1);
        assertQuery(user.lastName.eq("Aakkonen"), u3);

        assertQuery(user.firstName.in("Jaakko", "Teppo"), u1);
        assertQuery(user.lastName.in("Aakkonen", "BeekkoNen"), u3, u4);

        assertQuery(user.firstName.eq("Jouko"));

        assertQuery(user.firstName.eq("Jaana"), user.lastName.asc(), u3, u4);
        assertQuery(user.firstName.eq("Jaana"), user.lastName.desc(), u4, u3);
        assertQuery(user.lastName.eq("Jantunen"), user.firstName.asc(), u2, u1);
        assertQuery(user.lastName.eq("Jantunen"), user.firstName.desc(), u1, u2);

        assertQuery(user.firstName.eq("Jaana").and(user.lastName.eq("Aakkonen")), u3);
        //This should produce 'and' also
        assertQuery(where(user.firstName.eq("Jaana"), user.lastName.eq("Aakkonen")), u3);

        assertQuery(user.firstName.ne("Jaana"), u2, u1);
    }

    @Test
    public void regexQueries() {
        assertQuery(user.firstName.startsWith("Jaan"), u3, u4);
        assertQuery(user.firstName.startsWith("jaan"));
        assertQuery(user.firstName.startsWithIgnoreCase("jaan"), u3, u4);

        assertQuery(user.lastName.endsWith("unen"), u2, u1);

        assertQuery(user.lastName.endsWithIgnoreCase("onen"), u3, u4);

        assertQuery(user.lastName.contains("oN"), u4);
        assertQuery(user.lastName.containsIgnoreCase("on"), u3, u4);

        assertQuery(user.firstName.matches(".*aa.*[^i]$"), u3, u4, u1);
    }

    @Test
    public void regexQueries_not() {
        assertQuery(user.firstName.startsWith("Jaan").not(), u2, u1);
        assertQuery(user.firstName.startsWith("jaan").not(), u3, u4, u2, u1);
        assertQuery(user.firstName.startsWithIgnoreCase("jaan").not(), u2, u1);

        assertQuery(user.lastName.endsWith("unen").not(), u3, u4);

        assertQuery(user.lastName.endsWithIgnoreCase("onen").not(), u2, u1);

        assertQuery(user.lastName.contains("oN").not(), u3, u2, u1);
        assertQuery(user.lastName.containsIgnoreCase("on").not(), u2, u1);

        assertQuery(user.firstName.matches(".*aa.*[^i]$").not(), u2);
    }

    @Test
    public void like() {
        assertQuery(user.firstName.like("Jaan"));
        assertQuery(user.firstName.like("Jaan%"), u3, u4);
        assertQuery(user.firstName.like("jaan%"));

        assertQuery(user.lastName.like("%unen"), u2, u1);
    }

    @Test
    public void like_not() {
        assertQuery(user.firstName.like("Jaan").not(), u3, u4, u2, u1);
        assertQuery(user.firstName.like("Jaan%").not(), u2, u1);
        assertQuery(user.firstName.like("jaan%").not(), u3, u4, u2, u1);

        assertQuery(user.lastName.like("%unen").not(), u3, u4);
    }

    @Test
    public void isNotNull() {
        assertQuery(user.firstName.isNotNull(), u3, u4, u2, u1);
    }

    @Test
    public void isNotNull_not() {
        assertQuery(user.firstName.isNotNull().not());
    }

    @Test
    public void isNull() {
        assertQuery(user.firstName.isNull());
    }

    @Test
    public void isNull_not() {
        assertQuery(user.firstName.isNull().not(), u3, u4, u2, u1);
    }

    @Test
    public void isEmpty() {
        assertQuery(user.firstName.isEmpty());
        assertQuery(user.friends.isEmpty(), u1);
    }

    @Test
    public void isEmpty_not() {
        assertQuery(user.firstName.isEmpty().not(), u3, u4, u2, u1);
        assertQuery(user.friends.isEmpty().not(), u3, u4, u2);
    }

    @Test
    public void not() {
        assertQuery(user.firstName.eq("Jaakko").not(), u3, u4, u2);
        assertQuery(user.firstName.ne("Jaakko").not(), u1);
        assertQuery(user.firstName.matches("Jaakko").not(), u3, u4, u2);
        assertQuery(user.friends.isNotEmpty(), u3, u4, u2);
    }

    @Test
    public void or() {
        assertQuery(user.lastName.eq("Aakkonen").or(user.lastName.eq("BeekkoNen")), u3, u4);
    }

    @Test
    public void or_not() {
        assertQuery(user.lastName.eq("Aakkonen").or(user.lastName.eq("BeekkoNen")).not(), u2, u1);
    }

    @Test
    public void iterate() {
        User a = addUser("A", "A");
        User b = addUser("A1", "B");
        User c = addUser("A2", "C");

        Iterator<Document> i = where(user.firstName.startsWith("A"))
                .orderBy(user.firstName.asc())
                .iterate();

        assertEquals(a.getId(), i.next().get("_id"));
        assertEquals(b.getId(), i.next().get("_id"));
        assertEquals(c.getId(), i.next().get("_id"));
        assertEquals(false, i.hasNext());
    }

    @Test
    public void uniqueResultAndLimitAndOffset() {
        SimpleMongodbQuery q = query().where(user.firstName.startsWith("Ja")).orderBy(user.age.asc());
        assertEquals(4, q.fetch().size());
        assertEquals(u1, q.fetch().get(0));
    }

    @Test
    public void references() {
        for (User u : users) {
            if (u.getFriend() != null) {
                assertQuery(user.friend().eq(u.getFriend()), asDocument(u));
            }
        }
    }

    @Test
    public void references2() {
        for (User u : users) {
            if (u.getFriend() != null) {
                assertQuery(user.enemy().eq(u.getEnemy()), asDocument(u));
            }
        }
    }

    @Test
    public void various() {
        ListPath<Address, QAddress> list = user.addresses;
        StringPath str = user.lastName;
        List<Predicate> predicates = new ArrayList<Predicate>();
        predicates.add(str.between("a", "b"));
        predicates.add(str.contains("a"));
        predicates.add(str.containsIgnoreCase("a"));
        predicates.add(str.endsWith("a"));
        predicates.add(str.endsWithIgnoreCase("a"));
        predicates.add(str.eq("a"));
        predicates.add(str.equalsIgnoreCase("a"));
        predicates.add(str.goe("a"));
        predicates.add(str.gt("a"));
        predicates.add(str.in("a", "b", "c"));
        predicates.add(str.isEmpty());
        predicates.add(str.isNotNull());
        predicates.add(str.isNull());
        predicates.add(str.like("a"));
        predicates.add(str.loe("a"));
        predicates.add(str.lt("a"));
        predicates.add(str.matches("a"));
        predicates.add(str.ne("a"));
        predicates.add(str.notBetween("a", "b"));
        predicates.add(str.notIn("a", "b", "c"));
        predicates.add(str.startsWith("a"));
        predicates.add(str.startsWithIgnoreCase("a"));
        predicates.add(list.isEmpty());
        predicates.add(list.isNotEmpty());

        for (Predicate predicate : predicates) {
            long count1 = where(predicate).fetchCount();
            long count2 = where(predicate.not()).fetchCount();
            assertEquals(predicate.toString(), 4, count1 + count2);
        }
    }

    @Test
    public void enum_eq() {
        assertQuery(user.gender.eq(Gender.MALE), u3, u4, u2, u1);
    }

    @Test
    public void enum_ne() {
        assertQuery(user.gender.ne(Gender.MALE));
    }

    @Test
    public void in_objectIds() {
        Item i = new Item();
        i.setCtds(Arrays.asList(ObjectId.get(), ObjectId.get(), ObjectId.get()));
        ds.save(i);

        assertTrue(where(item, item.ctds.contains(i.getCtds().get(0))).fetchCount() > 0);
        assertTrue(where(item, item.ctds.contains(ObjectId.get())).fetchCount() == 0);
    }

    @Test
    public void in_objectIds2() {
        Item i = new Item();
        i.setCtds(Arrays.asList(ObjectId.get(), ObjectId.get(), ObjectId.get()));
        ds.save(i);

        assertTrue(where(item, item.ctds.any().in(i.getCtds())).fetchCount() > 0);
        assertTrue(where(item, item.ctds.any().in(Arrays.asList(ObjectId.get(), ObjectId.get()))).fetchCount() == 0);
    }

    @Test
    public void size() {
        assertQuery(user.addresses.size().eq(2), u1);
    }

    @Test
    public void size_not() {
        assertQuery(user.addresses.size().eq(2).not(), u3, u4, u2);
    }

    @Test
    public void readPreference() {
        SimpleMongodbQuery query = query();
        query.setReadPreference(ReadPreference.primary());
        assertEquals(4, query.fetchCount());
    }

    @Test
    public void asDBObject() {
        SimpleMongodbQuery query = query();
        query.where(user.firstName.eq("Bob"), user.lastName.eq("Wilson"));
        assertEquals(
                new Document().append("firstName", "Bob").append("lastName", "Wilson"),
                query.asDocument());
    }

    private Document asDocument(AbstractEntity entity) {
        return database.getCollection(ds.getCollection(entity.getClass()).getName())
                .find(new Document("_id", entity.getId())).first();
    }

    private void assertQuery(Predicate e, Document... expected) {
        assertQuery(where(e).orderBy(user.lastName.asc(), user.firstName.asc()), expected);
    }

    private void assertQuery(Predicate e, OrderSpecifier<?> orderBy, Document... expected) {
        assertQuery(where(e).orderBy(orderBy), expected);
    }

    private <T> SimpleMongodbQuery where(EntityPath<T> entity, Predicate... e) {
        return new SimpleMongodbQuery(morphia, ds, entity.getType(), database).where(e);
    }

    private SimpleMongodbQuery where(Predicate... e) {
        return query().where(e);
    }

    private SimpleMongodbQuery query() {
        return new SimpleMongodbQuery(morphia, ds, user.getType(), database);
    }

    private <T> SimpleMongodbQuery query(EntityPath<T> path) {
        return new SimpleMongodbQuery(morphia, ds, path.getType(), database);
    }

    private <T> SimpleMongodbQuery query(Class<? extends T> clazz) {
        return new SimpleMongodbQuery(morphia, ds, clazz, database);
    }

    private void assertQuery(SimpleMongodbQuery query, Document... expected) {
        String toString = query.toString();
        List<Document> results = query.fetch();

        assertNotNull(toString, results);
        if (expected == null) {
            assertEquals("Should get empty result", 0, results.size());
            return;
        }
        assertEquals(toString, expected.length, results.size());
        int i = 0;
        for (Document u : expected) {
            assertEquals(toString, u, results.get(i++));
        }
    }

    private User addUser(String first, String last) {
        User user = new User(first, last);
        ds.save(user);
        return user;
    }

    private User addUser(String first, String last, int age, Address mainAddress, Address... addresses) {
        User user = new User(first, last, age, new Date());
        user.setGender(Gender.MALE);
        user.setMainAddress(mainAddress);
        for (Address address : addresses) {
            user.addAddress(address);
        }
        for (User u : users) {
            user.addFriend(u);
        }
        if (!users.isEmpty()) {
            user.setFriend(users.get(users.size() - 1));
            user.setEnemy(users.get(users.size() - 1));
        }
        ds.save(user);
        users.add(user);

        return user;
    }

    private static class SimpleMongodbQuery extends AbstractFetchableMongodbQuery<Document, SimpleMongodbQuery> {

        private final Datastore datastore;
        private final MongoDatabase database;

        SimpleMongodbQuery(final Morphia morphia, final Datastore datastore, final Class<?> entityType,
                           final MongoDatabase database) {
            super(database.getCollection(datastore.getCollection(entityType).getName()), Function.identity(),
                    new SampleSerializer(morphia));
            this.datastore = datastore;
            this.database = database;
        }

        @Override
        protected MongoCollection<Document> getCollection(Class<?> type) {
            return database.getCollection(datastore.getCollection(type).getName());
        }
    }

    static class SampleSerializer extends MongodbDocumentSerializer {
        private final Morphia morphia;

        SampleSerializer(Morphia morphia) {
            this.morphia = morphia;
        }

        @Override
        protected boolean isReference(Path<?> arg) {
            return arg.getAnnotatedElement().isAnnotationPresent(Reference.class);
        }

        @Override
        protected DBRef asReference(Object constant) {
            Key<?> key = morphia.getMapper().getKey(constant);
            return morphia.getMapper().keyToDBRef(key);
        }

        @Override
        protected DBRef asReferenceKey(Class<?> entity, Object id) {
            String collection = morphia.getMapper().getCollectionName(entity);
            Key<?> key = new Key<Object>(entity, collection, id);
            return morphia.getMapper().keyToDBRef(key);
        }

        @Override
        protected String getKeyForPath(Path<?> expr, PathMetadata metadata) {
            AnnotatedElement annotations = expr.getAnnotatedElement();
            if (annotations.isAnnotationPresent(Id.class)) {
                Path<?> parent = expr.getMetadata().getParent();
                if (parent.getAnnotatedElement().isAnnotationPresent(Reference.class)) {
                    return null; // go to parent
                } else {
                    return "_id";
                }
            } else if (annotations.isAnnotationPresent(Property.class)) {
                Property property = annotations.getAnnotation(Property.class);
                if (!property.value().equals(Mapper.IGNORED_FIELDNAME)) {
                    return property.value();
                }
            } else if (annotations.isAnnotationPresent(Reference.class)) {
                Reference reference = annotations.getAnnotation(Reference.class);
                if (!reference.value().equals(Mapper.IGNORED_FIELDNAME)) {
                    return reference.value();
                }
            }
            return super.getKeyForPath(expr, metadata);
        }
    }
}
