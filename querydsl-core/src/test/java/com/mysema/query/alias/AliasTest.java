/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import static com.mysema.query.alias.Alias.$;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Test;

public class AliasTest {
    
    public interface Person{
        
        String getFirstName();
        
        String getLastName();
        
        int getAge();
        
        List<Person> getList();
        
        Map<String,Person> getMap();
    }
    
    @Test
    public void basicUsage(){
        Person person = Alias.alias(Person.class);
        assertEquals("lower(person.firstName)", $(person.getFirstName()).lower().toString());
        assertEquals("person.age", $(person.getAge()).toString());
        assertEquals("person.map.get(a)", $(person.getMap().get("a")).toString());
        assertEquals("person.list.get(0)", $(person.getList().get(0)).toString());
    }

}
