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
package com.querydsl.mongodb;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.*;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.common.collect.Lists;
import com.mongodb.BasicDBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.QueryResults;
import com.querydsl.core.testutil.MongoDB;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ListPath;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.mongodb.domain.*;
import com.querydsl.mongodb.domain.User.Gender;
import com.querydsl.mongodb.morphia.MorphiaQuery;

@Category(MongoDB.class)
public class MongodbQueryTest {

    private final Mongo mongo;
    private final Morphia morphia;
    private final Datastore ds;

    private final String dbname = "testdb";
    private final QUser user = QUser.user;
    private final QItem item = QItem.item;
    private final QAddress address = QAddress.address;
    private final QMapEntity mapEntity = QMapEntity.mapEntity;
    private final QDates dates = QDates.dates;

    List<User> users = Lists.newArrayList();
    User u1, u2, u3, u4;
    City tampere, helsinki;

    public MongodbQueryTest() throws UnknownHostException, MongoException {
        mongo = new Mongo();
        morphia = new Morphia().map(User.class).map(Item.class).map(MapEntity.class).map(Dates.class);
        ds = morphia.createDatastore(mongo, dbname);
    }

    @Before
    public void before() throws UnknownHostException, MongoException {
        ds.delete(ds.createQuery(Item.class));
        ds.delete(ds.createQuery(User.class));
        ds.delete(ds.createQuery(MapEntity.class));

        tampere = new City("Tampere", 61.30, 23.50);
        helsinki = new City("Helsinki", 60.15, 20.03);

        u1 = addUser("Jaakko", "Jantunen", 20, new Address("Aakatu", "00100", helsinki),
                new Address("Aakatu1", "00100", helsinki),
                new Address("Aakatu2", "00100", helsinki));
        u2 = addUser("Jaakki", "Jantunen", 30, new Address("Beekatu", "00200", helsinki));
        u3 = addUser("Jaana", "Aakkonen", 40, new Address("Ceekatu","00300", tampere));
        u4 = addUser("Jaana", "BeekkoNen", 50, new Address("Deekatu","00400",tampere));

        // order users by lastname, firstname
        users = Lists.newArrayList(u3, u4, u2, u1);
    }

    @Test
    public void query1() {
        assertEquals(4L, query(user).fetchCount());
        assertEquals(4L, query(User.class).fetchCount());
    }

    @Test
    public void list_keys() {
        User u = where(user.firstName.eq("Jaakko")).fetch(user.firstName, user.mainAddress().street).get(0);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
        assertEquals("Aakatu", u.getMainAddress().street);
        assertNull(u.getMainAddress().postCode);
    }

    @Test
    public void singleResult_keys() {
        User u = where(user.firstName.eq("Jaakko")).fetchFirst(user.firstName);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
    }

    @Test
    public void uniqueResult_keys() {
        User u = where(user.firstName.eq("Jaakko")).fetchOne(user.firstName);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
    }

    @Test
    public void list_deep_keys() {
        User u = where(user.firstName.eq("Jaakko")).fetchFirst(user.addresses.any().street);
        for (Address a : u.getAddresses()) {
            assertNotNull(a.street);
            assertNull(a.city);
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
    public void contains() {
        assertQuery(user.friends.contains(u1), u3, u4, u2);
    }

    @Test
    public void contains2() {
        assertQuery(user.friends.contains(u4));
    }

    @Test
    public void notContains() {
        assertQuery(user.friends.contains(u1).not(), u1);
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
        assertTrue(where(user.id.eq(u1.getId())).fetchCount() > 0);
    }

    @Test
    public void find_by_id() {
        assertNotNull(where(user.id.eq(u1.getId())).fetchFirst() != null);
    }

    @Test
    public void notExists() {
        assertFalse(where(user.firstName.eq("Jaakko")).fetchCount() == 0);
        assertTrue(where(user.firstName.eq("JaakkoX")).fetchCount() == 0);
    }

    @Test
    public void uniqueResult() {
        assertEquals("Jantunen", where(user.firstName.eq("Jaakko")).fetchOne().getLastName());
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

        assertEquals(d, query(dates).where(dates.date.between(start, end)).fetchFirst());
        assertEquals(0, query(dates).where(dates.date.between(new Date(0), start)).fetchCount());
    }

    @Test
    public void elemMatch() {
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1"}}}
        assertEquals(1, query().anyEmbedded(user.addresses, address).on(address.street.eq("Aakatu1")).fetchCount());
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1" , "postCode" : "00100"}}}
        assertEquals(1, query().anyEmbedded(user.addresses, address).on(address.street.eq("Aakatu1"), address.postCode.eq("00100")).fetchCount());
//      { "addresses" : { "$elemMatch" : { "street" : "akatu"}}}
        assertEquals(0, query().anyEmbedded(user.addresses, address).on(address.street.eq("akatu")).fetchCount());
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1" , "postCode" : "00200"}}}
        assertEquals(0, query().anyEmbedded(user.addresses, address).on(address.street.eq("Aakatu1"), address.postCode.eq("00200")).fetchCount());
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
        List<User> users = query().orderBy(user.age.asc()).fetch();
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
        QueryResults<User> results = query().limit(2).orderBy(user.age.asc()).fetchResults();
        assertEquals(4L, results.getTotal());
        assertEquals(2, results.getResults().size());

        results = query().offset(2).orderBy(user.age.asc()).fetchResults();
        assertEquals(4L, results.getTotal());
        assertEquals(2, results.getResults().size());
    }

    @Test
    public void emptyResults() {
        QueryResults<User> results = query().where(user.firstName.eq("XXX")).fetchResults();
        assertEquals(0L, results.getTotal());
        assertEquals(Collections.emptyList(), results.getResults());
    }

    @Test
    public void eqInAndOrderByQueries() {
        assertQuery(user.firstName.eq("Jaakko"), u1);
        assertQuery(user.firstName.equalsIgnoreCase("jaakko"), u1);
        assertQuery(user.lastName.eq("Aakkonen"), u3);

        assertQuery(user.firstName.in("Jaakko","Teppo"), u1);
        assertQuery(user.lastName.in("Aakkonen","BeekkoNen"), u3, u4);

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

        Iterator<User> i = where(user.firstName.startsWith("A"))
                            .orderBy(user.firstName.asc())
                            .iterate();

        assertEquals(a, i.next());
        assertEquals(b, i.next());
        assertEquals(c, i.next());
        assertEquals(false, i.hasNext());
    }

    @Test
    public void uniqueResultAndLimitAndOffset() {
        MorphiaQuery<User> q = query().where(user.firstName.startsWith("Ja")).orderBy(user.age.asc());
        assertEquals(4, q.fetch().size());
        assertEquals(u1, q.fetch().get(0));
    }

    @Test
    public void references() {
        for (User u : users) {
            if (u.getFriend() != null) {
                assertQuery(user.friend().eq(u.getFriend()), u);
                assertQuery(user.friend().id.eq(u.getFriend().getId()), u);
                assertQuery(user.friend().ne(u.getFriend()), otherUsers(u));
                assertQuery(user.friend().id.ne(u.getFriend().getId()), otherUsers(u));
            }
        }
    }

    @Test
    public void references2() {
        for (User u : users) {
            if (u.getFriend() != null) {
                assertQuery(user.enemy().eq(u.getEnemy()), u);
                assertQuery(user.enemy().id.eq(u.getEnemy().getId()), u);
                assertQuery(user.enemy().ne(u.getEnemy()), otherUsers(u));
                assertQuery(user.enemy().id.ne(u.getEnemy().getId()), otherUsers(u));
            }
        }
    }

    private User[] otherUsers(User user) {
        List<User> list = Lists.newArrayList();
        for (User u : users) {
            if (!u.equals(user)) {
                list.add(u);
            }
        }
        return list.toArray(new User[list.size()]);
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
        predicates.add(str.in("a","b","c"));
        predicates.add(str.isEmpty());
        predicates.add(str.isNotNull());
        predicates.add(str.isNull());
        predicates.add(str.like("a"));
        predicates.add(str.loe("a"));
        predicates.add(str.lt("a"));
        predicates.add(str.matches("a"));
        predicates.add(str.ne("a"));
        predicates.add(str.notBetween("a", "b"));
        predicates.add(str.notIn("a","b","c"));
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
        MorphiaQuery<User> query = query();
        query.setReadPreference(ReadPreference.primary());
        assertEquals(4, query.fetchCount());
    }

    @Test
    public void asDBObject() {
        MorphiaQuery<User> query = query();
        query.where(user.firstName.eq("Bob"), user.lastName.eq("Wilson"));
        assertEquals(
                new BasicDBObject().append("firstName", "Bob").append("lastName", "Wilson"),
                query.asDBObject());
    }

    //TODO
    // - test dates
    // - test with empty values and nulls
    // - test more complex ands

    private void assertQuery(Predicate e, User ... expected) {
        assertQuery(where(e).orderBy(user.lastName.asc(), user.firstName.asc()), expected);
    }

    private void assertQuery(Predicate e, OrderSpecifier<?> orderBy, User ... expected) {
        assertQuery(where(e).orderBy(orderBy), expected);
    }

    private <T> MorphiaQuery<T> where(EntityPath<T> entity, Predicate... e) {
        return new MorphiaQuery<T>(morphia, ds, entity).where(e);
    }

    private MorphiaQuery<User> where(Predicate ... e) {
        return query().where(e);
    }

    private MorphiaQuery<User> query() {
        return new MorphiaQuery<User>(morphia, ds, user);
    }

    private <T> MorphiaQuery<T> query(EntityPath<T> path) {
        return new MorphiaQuery<T>(morphia, ds, path);
    }


    private <T> MorphiaQuery<T> query(Class<? extends T> clazz) {
        return new MorphiaQuery<T>(morphia, ds, clazz);
    }

    private void assertQuery(MorphiaQuery<User> query, User ... expected) {
        String toString = query.toString();
        List<User> results = query.fetch();

        assertNotNull(toString, results);
        if (expected == null) {
            assertEquals("Should get empty result", 0, results.size());
            return;
        }
        assertEquals(toString, expected.length, results.size());
        int i = 0;
        for (User u : expected) {
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

}
