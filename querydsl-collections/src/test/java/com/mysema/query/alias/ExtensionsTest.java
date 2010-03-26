/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Extensions.gt;
import static com.mysema.query.alias.Extensions.having;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.collections.MiniApi;

public class ExtensionsTest {

    private List<Person> meAndMyFriends;

    public static class Person {
        private String firstName, lastName;
        private int age;
        
        public Person(){}

        public Person(String firstName, String lastName, int age) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.age = age;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public int getAge() {
            return age;
        }

    }

    @Before
    public void setup() {
        Person me = new Person("Mario", "Fusco", 35);
        Person luca = new Person("Luca", "Marrocco", 29);
        Person biagio = new Person("Biagio", "Beatrice", 39);
        Person celestino = new Person("Celestino", "Bellone", 29);
        meAndMyFriends = Arrays.asList(me, luca, biagio, celestino);
    }
    
    @Test
    public void oldFriends(){
//        List<Person> oldFriends = filter(having(on(Person.class).getAge(), greaterThan(30)), meAndMyFriends);
        
        // querydsl style
        Person alias = Alias.alias(Person.class);
        MiniApi.from(alias, meAndMyFriends).where($(alias.getAge()).gt(30)).list($(alias));
        
        // lambdaj style
        MiniApi.from(alias, meAndMyFriends).where(having(alias.getAge(), gt(30))).list($(alias));
        
        assertEquals("person.age > 30", having(alias.getAge(), gt(30)).toString());
        
    }
    
    @Test
    public void test(){
        Person alias = Alias.alias(Person.class);        
        assertEquals("person.firstName > Bob", having(alias.getFirstName(), gt("Bob")).toString());
    }

    
}
