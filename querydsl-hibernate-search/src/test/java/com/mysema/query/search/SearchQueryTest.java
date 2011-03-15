/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.junit.Test;

import com.mysema.query.NonUniqueResultException;
import com.mysema.query.SearchResults;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.BooleanExpression;

public class SearchQueryTest extends AbstractQueryTest{

    private final QUser user = new QUser("user");

    @Override
    public void setUp(){
        super.setUp();
        createUser("Bob", "Stewart","Smith", "bob@example.com");

        createUser("Barbara","X","Lock","barbara@a.com");
        createUser("Anton","X","Bruckner","anton@b.com");
        createUser("Robert","X","Downing","bob@c.com");
        createUser("John","X","Stewart","john@d.com");

        Session session = getSession();
        session.flush();
        session.getTransaction().commit();
        session.beginTransaction();
    }

    @Test
    public void BasicTests(){
        BooleanExpression filter = user.emailAddress.eq("bob@example.com");

        // count
        assertEquals(1, query().where(filter).count());

        // uniqueResult
        User u = query().where(filter).uniqueResult();
        assertNotNull(u);
        assertEquals("bob@example.com", u.getEmailAddress());

        // list
        List<User> list = query().where(filter).list();
        assertEquals(1, list.size());
        assertEquals(u, list.get(0));
    }
    
    @Test(expected=NonUniqueResultException.class)
    public void Unique_Result_Throws_Exception_On_Multiple_Results(){
        query().where(user.middleName.eq("X")).uniqueResult();
    }

    @Test
    public void SingleResult(){
        assertNotNull(query().where(user.middleName.eq("X")).singleResult());
    }

    @Test
    public void Ordering(){
        BooleanExpression filter = user.middleName.eq("X");
        // asc
        List<String> asc = getFirstNames(query().where(filter).orderBy(user.firstName.asc()).list());
        assertEquals(Arrays.asList("Anton","Barbara","John","Robert"), asc);

        // desc
        List<String> desc = getFirstNames(query().where(filter).orderBy(user.firstName.desc()).list());
        assertEquals(Arrays.asList("Robert","John","Barbara","Anton"), desc);
    }

    @Test
    public void Paging(){
        BooleanExpression filter = user.middleName.eq("X");
        OrderSpecifier<?> order = user.firstName.asc();

        // limit
        List<String> limit = getFirstNames(query().where(filter).orderBy(order).limit(2).list());
        assertEquals(Arrays.asList("Anton","Barbara"), limit);

        // offset
        List<String> offset = getFirstNames(query().where(filter).orderBy(order).offset(1).list());
        assertEquals(Arrays.asList("Barbara","John","Robert"), offset);

        // limit + offset
        List<String> limitAndOffset = getFirstNames(query().where(filter).orderBy(order).limit(2).offset(1).list());
        assertEquals(Arrays.asList("Barbara","John"), limitAndOffset);
    }

    @Test
    public void ListResults(){
        BooleanExpression filter = user.middleName.eq("X");
        SearchResults<User> users = query().where(filter).orderBy(user.firstName.asc()).limit(2).listResults();
        List<String> asc = getFirstNames(users.getResults());
        assertEquals(Arrays.asList("Anton","Barbara"), asc);
        assertEquals(4, users.getTotal());
    }

    private List<String> getFirstNames(List<User> users) {
        List<String> rv = new ArrayList<String>(users.size());
        for (User user : users){
            rv.add(user.getFirstName());
        }
        return rv;
    }

    private User createUser(String firstName, String middleName, String lastName, String email){
        User user = new User();
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmailAddress(email);
        getSession().save(user);
        return user;
    }

    private SearchQuery<User> query(){
        return new SearchQuery<User>(getSession(), user);
    }

}
