/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.search;

import static org.junit.Assert.assertEquals;

import org.hibernate.Session;
import org.junit.Test;


public class SearchQueryTest extends AbstractQueryTest{
    
    private QUser user = new QUser("user");
    
    public void setUp(){
        super.setUp();
        Session session = getSession();
        
        User user = new User();
        user.setEmailAddress("bob@example.com");
        user.setFirstName("Bob");
        user.setLastName("Smith");
        user.setMiddleName("Stewart");
        session.save(user);
        session.flush();
        session.getTransaction().commit();
        
        session.beginTransaction();
    }
    
    @Test
    public void test(){
        assertEquals(1, query().where(user.emailAddress.eq("bob@example.com")).count());
    }
    
    private SearchQuery<User> query(){        
        return new SearchQuery<User>(getSession(), user, true);
    }

}
