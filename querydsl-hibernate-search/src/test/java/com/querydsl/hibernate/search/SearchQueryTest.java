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
package com.querydsl.hibernate.search;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Session;
import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.SearchResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.expr.BooleanExpression;

public class SearchQueryTest extends AbstractQueryTest {

    private final QUser user = new QUser("user");

    @Override
    public void setUp() {
        super.setUp();
        createUser("Bob", "Stewart", "Smith", "bob@example.com");

        createUser("Barbara", "X", "Lock", "barbara@a.com");
        createUser("Anton", "X", "Bruckner", "anton@b.com");
        createUser("Robert", "X", "Downing", "bob@c.com");
        createUser("John", "X", "Stewart", "john@d.com");

        Session session = getSession();
        session.flush();
        session.getTransaction().commit();
        session.beginTransaction();
    }

    @Test
    public void Exists() {
        assertTrue(query().where(user.emailAddress.eq("bob@example.com")).exists());
        assertFalse(query().where(user.emailAddress.eq("bobby@example.com")).exists());
    }

    @Test
    public void NotExists() {
        assertFalse(query().where(user.emailAddress.eq("bob@example.com")).notExists());
        assertTrue(query().where(user.emailAddress.eq("bobby@example.com")).notExists());
    }

    @Test
    public void Count() {
        BooleanExpression filter = user.emailAddress.eq("bob@example.com");
        assertEquals(1, query().where(filter).count());
    }

    @Test
    public void UniqueResult() {
        BooleanExpression filter = user.emailAddress.eq("bob@example.com");
        User u = query().where(filter).uniqueResult();
        assertNotNull(u);
        assertEquals("bob@example.com", u.getEmailAddress());
    }

    @Test
    public void List() {
        BooleanExpression filter = user.emailAddress.eq("bob@example.com");
        List<User> list = query().where(filter).list();
        assertEquals(1, list.size());
        User u = query().where(filter).uniqueResult();
        assertEquals(u, list.get(0));
    }

    @Test(expected = NonUniqueResultException.class)
    public void Unique_Result_Throws_Exception_On_Multiple_Results() {
        query().where(user.middleName.eq("X")).uniqueResult();
    }

    @Test
    public void SingleResult() {
        assertNotNull(query().where(user.middleName.eq("X")).singleResult());
    }

    @Test
    public void Ordering() {
        BooleanExpression filter = user.middleName.eq("X");
        // asc
        List<String> asc = getFirstNames(query().where(filter).orderBy(
                user.firstName.asc()).list());
        assertEquals(Arrays.asList("Anton", "Barbara", "John", "Robert"), asc);

        // desc
        List<String> desc = getFirstNames(query().where(filter).orderBy(
                user.firstName.desc()).list());
        assertEquals(Arrays.asList("Robert", "John", "Barbara", "Anton"), desc);
    }

    @Test
    public void Paging() {
        BooleanExpression filter = user.middleName.eq("X");
        OrderSpecifier<?> order = user.firstName.asc();

        // limit
        List<String> limit = getFirstNames(query().where(filter).orderBy(order)
                .limit(2).list());
        assertEquals(Arrays.asList("Anton", "Barbara"), limit);

        // offset
        List<String> offset = getFirstNames(query().where(filter)
                .orderBy(order).offset(1).list());
        assertEquals(Arrays.asList("Barbara", "John", "Robert"), offset);

        // limit + offset
        List<String> limitAndOffset = getFirstNames(query().where(filter)
                .orderBy(order).limit(2).offset(1).list());
        assertEquals(Arrays.asList("Barbara", "John"), limitAndOffset);
    }

    @Test
    public void ListResults() {
        BooleanExpression filter = user.middleName.eq("X");
        SearchResults<User> users = query().where(filter).orderBy(
                user.firstName.asc()).limit(2).listResults();
        List<String> asc = getFirstNames(users.getResults());
        assertEquals(Arrays.asList("Anton", "Barbara"), asc);
        assertEquals(4, users.getTotal());
    }
    
    @Test
    public void No_Where() {
        assertEquals(5, query().list().size());
    }
    
    @Test @Ignore // OufOfMemoryError
    public void Limit_Max_Value() {
        assertEquals(5, query().limit(Long.MAX_VALUE).list().size());
    }

    private List<String> getFirstNames(List<User> users) {
        List<String> rv = new ArrayList<String>(users.size());
        for (User user : users) {
            rv.add(user.getFirstName());
        }
        return rv;
    }

    private User createUser(String firstName, String middleName,
            String lastName, String email) {
        User user = new User();
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmailAddress(email);
        getSession().save(user);
        return user;
    }

    private SearchQuery<User> query() {
        return new SearchQuery<User>(getSession(), user);
    }

}
