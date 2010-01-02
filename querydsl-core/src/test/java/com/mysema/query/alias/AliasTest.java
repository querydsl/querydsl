package com.mysema.query.alias;

import static com.mysema.query.alias.Alias.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class AliasTest {
    
    public interface Person{
        
        String getFirstName();
        
        String getLastName();
        
        int getAge();
    }
    
    @Test
    public void basicUsage(){
        Person person = Alias.alias(Person.class);
        assertEquals("lower(person.firstName)", $(person.getFirstName()).lower().toString());
        assertEquals("person.age", $(person.getAge()).toString());
    }

}
