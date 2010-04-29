/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.apache.commons.collections15.BeanMap;
import org.junit.Test;

import com.mysema.query.BooleanBuilder;

public class PathBuilderTest {
    
    public static class User {
        
        private String firstName, lastName, username;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }
        
        
    }
    
    @Test
    public void getByExample(){
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        assertEquals("entity.lastName = lastName && entity.firstName = firstName", getByExample(user).toString());
    }
    
    @Test
    public void getArray(){
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        PArray<String> array = entityPath.getArray("array", String[].class);
        assertEquals(String[].class, array.getType());
        assertEquals(String.class, array.getElementType());
    }
    
    @Test
    public void getList(){
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        entityPath.getList("list", String.class, PString.class).get(0).lower();
        entityPath.getList("list", String.class).get(0);
    }
    
    @Test
    public void getMap(){
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        entityPath.getMap("map", String.class, String.class, PString.class).get("").lower();
        entityPath.getMap("map", String.class, String.class).get("");
    }

    @SuppressWarnings("unchecked")
    private <T> BooleanBuilder getByExample(T entity){
        PathBuilder<T> entityPath = new PathBuilder<T>((Class<T>)entity.getClass(), "entity");
        BooleanBuilder conditions = new BooleanBuilder();
        for (Map.Entry<String,Object> entry : new BeanMap(entity).entrySet()){
            if (!entry.getKey().equals("class")){
                if (entry.getValue() != null){
                    conditions.and(entityPath.get(entry.getKey()).eq(entry.getValue()));
                }    
            }                                    
        }
        return conditions;
    }
    
    @Test
    public void get(){
        PathBuilder<User> entity = new PathBuilder<User>(User.class, "entity");
        PNumber<Integer> intPath = new PNumber<Integer>(Integer.class, "int");
        PString strPath = new PString("str");
        PBoolean booleanPath = new PBoolean("boolean");
        
        assertEquals("entity.int", entity.get(intPath).toString());
        assertEquals("entity.str", entity.get(strPath).toString());
        assertEquals("entity.boolean", entity.get(booleanPath).toString());
        
        assertEquals("entity.int", entity.get(entity.get(intPath)).toString());
    }
    
}
