package com.mysema.query.mongodb;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.code.morphia.Datastore;
import com.google.code.morphia.Morphia;
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
        
        u1 = addUser("Jaakko", "Jantunen");
        u2 = addUser("Jaakki", "Jantunen");
        u3 = addUser("Jaana", "Aakkonen");
        u4 = addUser("Jaana", "BeekkoNen");
    }

    @Test
    public void testEqInAndOrderByQueries() {
       
        assertQuery(user.firstName.eq("Jaakko"), u1);
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
        
    }
    
    @Test
    public void testStartsWithEndsWith() {
        
        assertQuery(user.firstName.startsWith("Jaan"), u3, u4);
        assertQuery(user.firstName.startsWith("jaan"));
        assertQuery(user.firstName.startsWithIgnoreCase("jaan"), u3, u4);

        assertQuery(user.lastName.endsWith("unen"), u2, u1);
        
        assertQuery(user.lastName.endsWithIgnoreCase("onen"), u3, u4);
        
    }
    
    

    private void assertQuery(Predicate e, User ... expected) {
        assertQuery(where(e).orderBy(user.lastName.asc(), user.firstName.asc()), expected );
    }
    
    private void assertQuery(Predicate e, OrderSpecifier<?> orderBy, User ... expected ) {
        assertQuery(where(e).orderBy(orderBy), expected);
    }

    
    private MongodbQuery<User> where(Predicate ... e) {
        return new MongodbQuery<User>(morphia, ds, user).where(e);
    }
    
    private void assertQuery(MongodbQuery<User> query, User ... expected ) {
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

}
