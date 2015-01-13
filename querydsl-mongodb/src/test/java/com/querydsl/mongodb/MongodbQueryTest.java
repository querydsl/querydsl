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
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.ReadPreference;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.SearchResults;
import com.querydsl.mongodb.domain.*;
import com.querydsl.mongodb.domain.User.Gender;
import com.querydsl.mongodb.morphia.MorphiaQuery;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.core.testutil.ExternalDB;

@Category(ExternalDB.class)
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
        helsinki= new City("Helsinki", 60.15, 20.03);

        u1 = addUser("Jaakko", "Jantunen", 20, new Address("Aakatu", "00100", helsinki),
                new Address("Aakatu1", "00100", helsinki),
                new Address("Aakatu2", "00100", helsinki));
        u2 = addUser("Jaakki", "Jantunen", 30, new Address("Beekatu", "00200", helsinki));
        u3 = addUser("Jaana", "Aakkonen", 40, new Address("Ceekatu","00300", tampere));
        u4 = addUser("Jaana", "BeekkoNen", 50, new Address("Deekatu","00400",tampere));
    }

    @Test
    public void List_Keys() {
        User u = where(user.firstName.eq("Jaakko")).list(user.firstName, user.mainAddress().street).get(0);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
        assertEquals("Aakatu", u.getMainAddress().street);
        assertNull(u.getMainAddress().postCode);
    }

    @Test
    public void SingleResult_Keys() {
        User u = where(user.firstName.eq("Jaakko")).singleResult(user.firstName);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
    }

    @Test
    public void UniqueResult_Keys() {
        User u = where(user.firstName.eq("Jaakko")).uniqueResult(user.firstName);
        assertEquals("Jaakko", u.getFirstName());
        assertNull(u.getLastName());
    }

    @Test
    public void List_Deep_Keys() {
        User u = where(user.firstName.eq("Jaakko")).singleResult(user.addresses.any().street);
        for (Address a : u.getAddresses()) {
            assertNotNull(a.street);
            assertNull(a.city);
        }
    }

    @Test
    public void Contains() {
        assertQuery(user.friends.contains(u1), u3, u4, u2);
    }

    @Test
    public void Contains2() {
        assertQuery(user.friends.contains(u4));
    }

    @Test
    public void NotContains() {
        assertQuery(user.friends.contains(u1).not(), u1);
    }

    @Test
    public void Contains_Key() {
        MapEntity entity = new MapEntity();
        entity.getProperties().put("key", "value");
        ds.save(entity);

        assertTrue(query(mapEntity).where(mapEntity.properties.get("key").isNotNull()).exists());
        assertFalse(query(mapEntity).where(mapEntity.properties.get("key2").isNotNull()).exists());

        assertTrue(query(mapEntity).where(mapEntity.properties.containsKey("key")).exists());
        assertFalse(query(mapEntity).where(mapEntity.properties.containsKey("key2")).exists());
    }

    @Test
    public void Equals_Ignore_Case() {
        assertTrue(where(user.firstName.equalsIgnoreCase("jAaKko")).exists());
        assertFalse(where(user.firstName.equalsIgnoreCase("AaKk")).exists());
    }

    @Test
    public void Equals_and_Between() {
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.between(20, 30)), u2, u1);
        assertQuery(user.firstName.startsWith("Jaa").and(user.age.goe(20).and(user.age.loe(30))), u2, u1);
    }

    @Test
    public void Exists() {
        assertTrue(where(user.firstName.eq("Jaakko")).exists());
        assertFalse(where(user.firstName.eq("JaakkoX")).exists());
        assertTrue(where(user.id.eq(u1.getId())).exists());
    }

    @Test
    public void Find_By_Id() {
        assertNotNull(where(user.id.eq(u1.getId())).singleResult() != null);
    }

    @Test
    public void NotExists() {
        assertFalse(where(user.firstName.eq("Jaakko")).notExists());
        assertTrue(where(user.firstName.eq("JaakkoX")).notExists());
    }

    @Test
    public void UniqueResult() {
        assertEquals("Jantunen", where(user.firstName.eq("Jaakko")).uniqueResult().getLastName());
    }

    @Test(expected=NonUniqueResultException.class)
    public void UniqueResultContract() {
        where(user.firstName.isNotNull()).uniqueResult();
    }

    @Test
    public void SingleResult() {
        where(user.firstName.isNotNull()).singleResult();
    }

    @Test
    public void LongPath() {
        assertEquals(2, query().where(user.mainAddress().city().name.eq("Helsinki")).count());
        assertEquals(2, query().where(user.mainAddress().city().name.eq("Tampere")).count());
    }

    @Test
    public void CollectionPath() {
        assertEquals(1, query().where(user.addresses.any().street.eq("Aakatu1")).count());
        assertEquals(0, query().where(user.addresses.any().street.eq("akatu")).count());
    }

    @Test
    public void Dates() {
        long current = System.currentTimeMillis();
        int dayInMillis = 24 * 60 * 60 * 1000;
        Date start = new Date(current);
        ds.delete(ds.createQuery(Dates.class));
        Dates d = new Dates();
        d.setDate(new Date(current + dayInMillis));
        ds.save(d);
        Date end = new Date(current + 2 * dayInMillis);

        assertEquals(d, query(dates).where(dates.date.between(start, end)).singleResult());
        assertEquals(0, query(dates).where(dates.date.between(new Date(0), start)).count());
    }

    @Test
    public void ElemMatch() {
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1"}}}
        assertEquals(1, query().anyEmbedded(user.addresses, address).on(address.street.eq("Aakatu1")).count());
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1" , "postCode" : "00100"}}}
        assertEquals(1, query().anyEmbedded(user.addresses, address).on(address.street.eq("Aakatu1"), address.postCode.eq("00100")).count());
//      { "addresses" : { "$elemMatch" : { "street" : "akatu"}}}
        assertEquals(0, query().anyEmbedded(user.addresses, address).on(address.street.eq("akatu")).count());
//      { "addresses" : { "$elemMatch" : { "street" : "Aakatu1" , "postCode" : "00200"}}}
        assertEquals(0, query().anyEmbedded(user.addresses, address).on(address.street.eq("Aakatu1"), address.postCode.eq("00200")).count());
    }

    @Test
    public void IndexedAccess() {
        assertEquals(1, query().where(user.addresses.get(0).street.eq("Aakatu1")).count());
        assertEquals(0, query().where(user.addresses.get(1).street.eq("Aakatu1")).count());
    }

    @Test
    public void Count() {
        assertEquals(4, query().count());
    }

    @Test
    public void Order() {
        List<User> users = query().orderBy(user.age.asc()).list();
        assertEquals(asList(u1, u2, u3, u4), users);

        users = query().orderBy(user.age.desc()).list();
        assertEquals(asList(u4, u3, u2, u1), users);
    }

    @Test
    public void Restrict() {
        assertEquals(asList(u1, u2), query().limit(2).orderBy(user.age.asc()).list());
        assertEquals(asList(u2, u3), query().limit(2).offset(1).orderBy(user.age.asc()).list());
    }

    @Test
    public void ListResults() {
        SearchResults<User> results = query().limit(2).orderBy(user.age.asc()).listResults();
        assertEquals(4l, results.getTotal());
        assertEquals(2, results.getResults().size());

        results = query().offset(2).orderBy(user.age.asc()).listResults();
        assertEquals(4l, results.getTotal());
        assertEquals(2, results.getResults().size());
    }

    @Test
    public void EmptyResults() {
        SearchResults<User> results = query().where(user.firstName.eq("XXX")).listResults();
        assertEquals(0l, results.getTotal());
        assertEquals(Collections.emptyList(), results.getResults());
    }

    @Test
    public void EqInAndOrderByQueries() {
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
        //This shoud produce 'and' also
        assertQuery(where(user.firstName.eq("Jaana"), user.lastName.eq("Aakkonen")), u3);

        assertQuery(user.firstName.ne("Jaana"), u2, u1);

    }

    @Test
    public void RegexQueries() {
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
    public void Like() {
        assertQuery(user.firstName.like("Jaan"));
        assertQuery(user.firstName.like("Jaan%"), u3, u4);
        assertQuery(user.firstName.like("jaan%"));

        assertQuery(user.lastName.like("%unen"), u2, u1);
    }

    @Test
    public void IsNotNull() {
        assertQuery(user.firstName.isNotNull(), u3, u4, u2, u1);
    }

    @Test
    public void IsNull() {
        assertQuery(user.firstName.isNull());
    }

    @Test
    public void IsEmpty() {
        assertQuery(user.firstName.isEmpty());
    }

    @Test
    public void isEmpty2() {
        assertQuery(user.friends.isEmpty(), u1);
    }

    @Test
    public void Not() {
        assertQuery(user.firstName.eq("Jaakko").not(), u3, u4, u2);
        assertQuery(user.firstName.ne("Jaakko").not(), u1);
        assertQuery(user.firstName.matches("Jaakko").not(), u3, u4, u2);
    }

    @Test
    public void Or() {
        assertQuery(user.lastName.eq("Aakkonen").or(user.lastName.eq("BeekkoNen")), u3, u4);
    }

    //This is not supported yet
//    @Test
//    public void UniqueResult() {
//
//        addUser("Dille", "Duplikaatti");
//        addUser("Dille", "Duplikaatti");
//
//        assertEquals(2, where(user.firstName.eq("Dille")).count());
//        assertEquals(1, where(user.firstName.eq("Dille")).countDistinct());
//
//    }

    @Test
    public void Iterate() {
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
    public void UniqueResultAndLimitAndOffset() {
        MongodbQuery<User> q = query().where(user.firstName.startsWith("Ja")).orderBy(user.age.asc());
        assertEquals(4, q.list().size());
        assertEquals(u1, q.list().get(0));
    }

    @Test
    public void References() {
        for (User u : users) {
            if (u.getFriend() != null) {
                assertQuery(user.friend().eq(u.getFriend()), u);
                where(user.friend().ne(u.getFriend())).list();
            }
        }
    }

    @Test
    public void Various() {
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
//        predicates.add(str.like("a"));
        predicates.add(str.loe("a"));
        predicates.add(str.lt("a"));
        predicates.add(str.matches("a"));
        predicates.add(str.ne("a"));
        predicates.add(str.notBetween("a", "b"));
        predicates.add(str.notIn("a","b","c"));
        predicates.add(str.startsWith("a"));
        predicates.add(str.startsWithIgnoreCase("a"));

        for (Predicate predicate : predicates) {
            where(predicate).count();
            where(predicate.not()).count();
        }
    }

    @Test
    public void Enum_Eq() {
        assertQuery(user.gender.eq(Gender.MALE), u3, u4, u2, u1);
    }

    @Test
    public void Enum_Ne() {
        assertQuery(user.gender.ne(Gender.MALE));
    }

    @Test
    public void In_ObjectIds() {
        Item i = new Item();
        i.setCtds(Arrays.asList(ObjectId.get(), ObjectId.get(), ObjectId.get()));
        ds.save(i);

        assertTrue(where(item, item.ctds.contains(i.getCtds().get(0))).count() > 0);
        assertTrue(where(item, item.ctds.contains(ObjectId.get())).count() == 0);
    }

    @Test
    public void In_ObjectIds2() {
        Item i = new Item();
        i.setCtds(Arrays.asList(ObjectId.get(), ObjectId.get(), ObjectId.get()));
        ds.save(i);

        assertTrue(where(item, item.ctds.any().in(i.getCtds())).count() > 0);
        assertTrue(where(item, item.ctds.any().in(Arrays.asList(ObjectId.get(), ObjectId.get()))).count() == 0);
    }

    @Test
    public void Size() {
        assertQuery(user.addresses.size().eq(2), u1);
    }

    @Test
    public void ReadPreference() {
        MorphiaQuery<User> query = query();
        query.setReadPreference(ReadPreference.primary());
        assertEquals(4, query.count());

    }

    //TODO
    // - test dates
    // - test with empty values and nulls
    // - test more complex ands

    private void assertQuery(Predicate e, User ... expected) {
        assertQuery(where(e).orderBy(user.lastName.asc(), user.firstName.asc()), expected );
    }

    private void assertQuery(Predicate e, OrderSpecifier<?> orderBy, User ... expected ) {
        assertQuery(where(e).orderBy(orderBy), expected);
    }

    private <T> MongodbQuery<T> where(EntityPath<T> entity, Predicate... e) {
        return new MorphiaQuery<T>(morphia, ds, entity).where(e);
    }

    private MongodbQuery<User> where(Predicate ... e) {
        return query().where(e);
    }

    private MorphiaQuery<User> query() {
        return new MorphiaQuery<User>(morphia, ds, user);
    }

    private <T> MorphiaQuery<T> query(EntityPath<T> path) {
        return new MorphiaQuery<T>(morphia, ds, path);
    }

    private void assertQuery(MongodbQuery<User> query, User ... expected ) {
        //System.out.println(querydsl.toString());
        List<User> results = query.list();

        assertNotNull(results);
        if (expected == null ) {
            assertEquals("Should get empty result", 0, results.size());
            return;
        }
        assertEquals(expected.length, results.size());
        int i = 0;
        for (User u : expected) {
            assertEquals(u, results.get(i++));
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
        }
        ds.save(user);
        users.add(user);
        return user;
    }

}
