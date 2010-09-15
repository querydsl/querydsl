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
import com.mysema.query.types.expr.EBoolean;

public class MongodbQueryTest {

    private String dbname = "testdb";
    private Morphia morphia;
    private Datastore ds;
    private QUser user = new QUser("user");

    @Before
    public void before() {
        morphia = new Morphia().map(User.class);
        ds = morphia.createDatastore(dbname);
        ds.delete(ds.createQuery(User.class));
    }

    @Test
    public void testFindUserByName() {
        User u1 = addUser("Jaako", "Jantunen");
        User u2 = addUser("Jaakko", "Janhunen");
        User u3 = addUser("Jaana", "Jantunen");
        User u4 = addUser("Jaana", "Jantula");

        assertQuery(user.firstName.eq("Jaako"), u1);
        assertQuery(user.lastName.eq("Jantula"), u4);
        
        
    }

    private void assertQuery(EBoolean e, User expected) {
        MongodbQuery<User> query = new MongodbQuery<User>(morphia, ds, user);
        List<User> results = query.where(e).list();
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(expected, results.get(0));
    }

    private User addUser(String first, String last) {
        User user = new User(first, last);
        ds.save(user);
        return user;
    }

}
