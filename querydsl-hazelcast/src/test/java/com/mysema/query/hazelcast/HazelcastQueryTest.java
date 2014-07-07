package com.mysema.query.hazelcast;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;

import java.net.UnknownHostException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.mysema.query.hazelcast.domain.QUser;
import com.mysema.query.hazelcast.domain.User;
import com.mysema.query.hazelcast.domain.User.Gender;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Predicate;

public class HazelcastQueryTest {

    private final QUser user = QUser.user;

    static User u1, u2, u3, u4;

    private static IMap<String, User> map;

    @BeforeClass
    public static void setUp() throws Exception {
        HazelcastInstance hz = Hazelcast.newHazelcastInstance();
        map = hz.getMap("users");

        fillTable();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Hazelcast.shutdownAll();
    }

    public static void fillTable() throws UnknownHostException {
        map.clear();

        u1 = addUser("Jaakko", "Jantunen", 20, Gender.MALE, null);
        u2 = addUser("Jaakki", "Jantunen", 30, Gender.FEMALE, "One detail");
        u3 = addUser("Jaana", "Aakkonen", 40, Gender.MALE, "No details");
        u4 = addUser("Jaana", "BeekkoNen", 50, Gender.FEMALE, null);
    }

    private static User addUser(String first, String last, int age, Gender gender, String details) {
        User user = new User(first, last, age, new Date());
        user.setId(UUID.randomUUID().toString());
        user.setGender(gender);
        user.setDetails(details);
        map.put(user.getId(), user);
        System.out.println(user.getId());
        return user;
    }

    @Test
    public void eq() {
        User u = where(user.firstName.eq("Jaakko")).uniqueResult();
        assertThat(u, equalTo(u1));
    }

    @Test
    public void eqNot() {
        List<User> result = where(user.firstName.eq("Jaakko").not()).list();
        assertThat(result, containsInAnyOrder(u2, u3, u4));
    }

    @Test
    public void like() {
        List<User> result = where(user.firstName.like("%aakk%")).list();
        assertThat(result, containsInAnyOrder(u1, u2));
    }

    @Test
    public void notEq() {
        List<User> result = where(user.lastName.ne("Jantunen")).list();
        assertThat(result, containsInAnyOrder(u3, u4));
    }

    @Test
    public void between() {
        List<User> result = where(user.age.between(29, 41)).list();
        assertThat(result, containsInAnyOrder(u3, u2));
    }

    @Test
    public void greaterOrEquals() {
        List<User> result = where(user.age.goe(40)).list();
        assertThat(result, containsInAnyOrder(u3, u4));
    }

    @Test
    public void greaterThen() {
        List<User> result = where(user.age.gt(20)).list();
        assertThat(result, containsInAnyOrder(u2, u3, u4));
    }

    @Test
    public void lowerOrEquals() {
        List<User> result = where(user.age.loe(20)).list();
        assertThat(result, containsInAnyOrder(u1));
    }

    @Test
    public void lowerThen() {
        List<User> result = where(user.age.lt(40)).list();
        assertThat(result, containsInAnyOrder(u1, u2));
    }

    @Test
    public void in() {
        List<User> result = where(user.age.in(40, 15)).list();
        assertThat(result, containsInAnyOrder(u3));
    }

    @Test
    public void notIn() {
        List<User> result = where(user.gender.notIn(Gender.MALE, Gender.FEMALE)).list();
        assertThat(result, hasSize(0));
    }

    @Test
    public void isNull() {
        List<User> result = where(user.details.isNull()).list();
        assertThat(result, containsInAnyOrder(u1, u4));
    }

    @Test
    public void isNotNull() {
        List<User> result = where(user.details.isNotNull()).list();
        assertThat(result, containsInAnyOrder(u2, u3));
    }

    @Test
    public void and() {
        User result = where(user.firstName.eq("Jaana"), user.age.eq(50)).uniqueResult();
        assertThat(result, equalTo(u4));
    }

    @Test
    public void or() {
        List<User> result = where(
                ExpressionUtils.or(user.firstName.eq("Jaakko"), user.lastName.eq("BeekkoNen")))
                .list();
        assertThat(result, containsInAnyOrder(u1, u4));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void coalesce() {
        where(user.details.coalesce("").asString().isNotNull()).list();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void caseWhenThen() {
        where(user.firstName.when("Jaakko").then(1).otherwise(2).eq(5)).list();
    }

    private AbstractIMapQuery<User> query() {
        return new IMapValueQuery<User>(map);
    }

    private AbstractIMapQuery<User> where(Predicate... e) {
        return query().where(e);
    }

}
