/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.mongodb;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
import com.mysema.query.SearchResults;
import com.mysema.query.mongodb.domain.QUser;
import com.mysema.query.mongodb.domain.User;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.Predicate;

public class MongodbQueryTest {

    private String dbname = "testdb";
    private Morphia morphia = new Morphia().map(User.class);
    private Datastore ds = morphia.createDatastore(dbname);
    private QUser user = new QUser("user");

    User u1, u2, u3, u4; 

    @Before
    public void before() {
        ds.delete(ds.createQuery(User.class));

        u1 = addUser("Jaakko", "Jantunen", 20);
        u2 = addUser("Jaakki", "Jantunen", 30);
        u3 = addUser("Jaana", "Aakkonen", 40);
        u4 = addUser("Jaana", "BeekkoNen", 50);
    }
    
    @Test
    public void testUniqueResult(){
        assertEquals("Jantunen", where(user.firstName.eq("Jaakko")).uniqueResult().getLastName());
    }
    
    @Test
    public void testCount(){
        assertEquals(4, query().count());
    }

    @Test
    public void testOrder(){
        List<User> users = query().orderBy(user.age.asc()).list();
        assertEquals(Arrays.asList(u1, u2, u3, u4), users);
        
        users = query().orderBy(user.age.desc()).list();
        assertEquals(Arrays.asList(u4, u3, u2, u1), users);
    }
    
    @Test
    public void testRestrict(){
        assertEquals(Arrays.asList(u1, u2), query().limit(2).orderBy(user.age.asc()).list());
        assertEquals(Arrays.asList(u2, u3), query().limit(2).offset(1).orderBy(user.age.asc()).list());
    }
    
    @Test
    public void testListResults(){
        SearchResults<User> results = query().limit(2).orderBy(user.age.asc()).listResults();
        assertEquals(4l, results.getTotal());
        assertEquals(2, results.getResults().size());
        
        results = query().offset(2).orderBy(user.age.asc()).listResults();
        assertEquals(4l, results.getTotal());
        assertEquals(2, results.getResults().size());
    }
    
    @Test
    public void testEmptyResults(){
        SearchResults<User> results = query().where(user.firstName.eq("XXX")).listResults();
        assertEquals(0l, results.getTotal());
        assertEquals(Collections.emptyList(), results.getResults());
    }
    
    @Test
    public void testEqInAndOrderByQueries() {
       
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
    public void testRegexQueries() {
        
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
    public void testNot() {
        assertQuery(user.firstName.eq("Jaakko").not(), u3, u4, u2);
        assertQuery(user.firstName.ne("Jaakko").not(), u1);
        assertQuery(user.firstName.matches("Jaakko").not(), u3, u4, u2);
    }
    
    
    //This is not supported yet
//    @Test
//    public void testUniqueResult() {
//        
//        addUser("Dille", "Duplikaatti");
//        addUser("Dille", "Duplikaatti");
//        
//        assertEquals(2, where(user.firstName.eq("Dille")).count());
//        assertEquals(1, where(user.firstName.eq("Dille")).countDistinct());
//        
//    }
    
    @Test
    public void testIterate() {

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
    public void testUniqueResultAndLimitAndOffset() {        
        MongodbQuery<User> q = query().where(user.firstName.startsWith("Ja")).orderBy(user.age.asc());        
        assertEquals(4, q.list().size());        
        assertEquals(u1, q.uniqueResult());             
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
    
    private MongodbQuery<User> where(Predicate ... e) {
        return query().where(e);
    }
    
    private MongodbQuery<User> query() {
        return new MongodbQuery<User>(morphia, ds, user);
    }
    
    private void assertQuery(MongodbQuery<User> query, User ... expected ) {
        //System.out.println(query.toString());
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
    
    private User addUser(String first, String last, int age) {
        User user = new User(first, last, age, new Date());
        ds.save(user);
        return user;
    }

}
