package com.querydsl.mongodb;

import static org.junit.Assert.*;

import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.querydsl.mongodb.domain.Item;
import com.querydsl.mongodb.domain.QUser;
import com.querydsl.mongodb.domain.User;
import com.querydsl.mongodb.morphia.MorphiaQuery;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.testutil.ExternalDB;

@Category(ExternalDB.class)
public class JoinTest {

    private final Mongo mongo;
    private final Morphia morphia;
    private final Datastore ds;

    private final String dbname = "testdb";
    private final QUser user = QUser.user;
    private final QUser friend = new QUser("friend");
    private final QUser friend2 = new QUser("friend2");
    private final QUser enemy = new QUser("enemy");

    public JoinTest() throws UnknownHostException, MongoException {
        mongo = new Mongo();
        morphia = new Morphia().map(User.class).map(Item.class);
        ds = morphia.createDatastore(mongo, dbname);
    }

    @Before
    public void before() throws UnknownHostException, MongoException {
        ds.delete(ds.createQuery(User.class));

        User friend1 = new User("Max", null);
        User friend2 = new User("Jack", null);
        User friend3 = new User("Bob", null);
        ds.save(friend1, friend2, friend3);

        User user1 = new User("Jane", null, friend1);
        User user2 = new User("Mary", null, user1);
        User user3 = new User("Ann", null, friend3);
        ds.save(user1, user2, user3);

        User user4 = new User("Mike", null);
        user4.setFriend(user2);
        user4.setEnemy(user3);
        ds.save(user4);

        User user5 = new User("Bart", null);
        user5.addFriend(user2);
        user5.addFriend(user3);
        ds.save(user5);

    }

    @Test
    public void Count() {
        assertEquals(1, where().join(user.friend(), friend).on(friend.firstName.eq("Max")).count());
        assertEquals(1, where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Max")).count());
        assertEquals(0, where(user.firstName.eq("Mary")).join(user.friend(), friend).on(friend.firstName.eq("Max")).count());
        assertEquals(0, where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Jack")).count());
    }

    @Test
    public void Count_Collection() {
        assertEquals(1, where().join(user.friends, friend).on(friend.firstName.eq("Mary")).count());
        assertEquals(1, where().join(user.friends, friend).on(friend.firstName.eq("Ann")).count());
        assertEquals(1, where().join(user.friends, friend).on(friend.firstName.eq("Ann").or(friend.firstName.eq("Mary"))).count());
        assertEquals(1, where(user.firstName.eq("Bart")).join(user.friends, friend).on(friend.firstName.eq("Mary")).count());
        assertEquals(0, where().join(user.friends, friend).on(friend.firstName.eq("Max")).count());
    }

    @Test
    public void Exists() {
        assertTrue(where().join(user.friend(), friend).on(friend.firstName.eq("Max")).exists());
        assertTrue(where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Max")).exists());
        assertFalse(where(user.firstName.eq("Mary")).join(user.friend(), friend).on(friend.firstName.eq("Max")).exists());
        assertFalse(where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Jack")).exists());
    }

    @Test
    public void Exists_Collection() {
        assertTrue(where().join(user.friends, friend).on(friend.firstName.eq("Mary")).exists());
        assertTrue(where(user.firstName.eq("Bart")).join(user.friends, friend).on(friend.firstName.eq("Mary")).exists());
    }

    @Test
    public void List() {
        assertEquals(1, where().join(user.friend(), friend).on(friend.firstName.eq("Max")).list().size());
        assertEquals(1, where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Max")).list().size());
        assertEquals(0, where(user.firstName.eq("Mary")).join(user.friend(), friend).on(friend.firstName.eq("Max")).list().size());
        assertEquals(0, where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Jack")).list().size());
    }

    public void List_Collection() {
        assertEquals(1, where().join(user.friends, friend).on(friend.firstName.eq("Mary")).list().size());
    }

    @Test
    public void Single() {
        assertEquals("Jane", where().join(user.friend(), friend).on(friend.firstName.eq("Max")).singleResult().getFirstName());
        assertEquals("Jane", where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Max")).singleResult().getFirstName());
        assertNull(where(user.firstName.eq("Mary")).join(user.friend(), friend).on(friend.firstName.eq("Max")).singleResult());
        assertNull(where(user.firstName.eq("Jane")).join(user.friend(), friend).on(friend.firstName.eq("Jack")).singleResult());
    }

    @Test
    public void Single_Collection() {
        assertEquals("Bart", where().join(user.friends, friend).on(friend.firstName.eq("Mary")).singleResult().getFirstName());
    }

    @Test
    public void Double() {
        assertEquals("Mike", where()
                .join(user.friend(), friend).on(friend.firstName.isNotNull())
                .join(user.enemy(), enemy).on(enemy.firstName.isNotNull())
                .singleResult().getFirstName());
    }

    @Test
    public void Double2() {
        assertEquals("Mike", where()
                .join(user.friend(), friend).on(friend.firstName.eq("Mary"))
                .join(user.enemy(), enemy).on(enemy.firstName.eq("Ann"))
                .singleResult().getFirstName());
    }

    @Test
    public void Deep() {
        // Mike -> Mary -> Jane
        assertEquals("Mike", where()
                .join(user.friend(), friend).on(friend.firstName.isNotNull())
                .join(friend.friend(), friend2).on(friend2.firstName.eq("Jane"))
                .singleResult().getFirstName());
    }

    private MongodbQuery<User> query() {
        return new MorphiaQuery<User>(morphia, ds, user);
    }

    private MongodbQuery<User> where(Predicate ... e) {
        return query().where(e);
    }
}
