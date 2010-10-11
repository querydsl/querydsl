/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import static org.junit.Assert.assertEquals;

import java.sql.Time;
import java.util.Date;
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
        ArrayPath<String> array = entityPath.getArray("array", String[].class);
        assertEquals(String[].class, array.getType());
        assertEquals(String.class, array.getElementType());
    }

    @Test
    public void getList(){
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        entityPath.getList("list", String.class, StringPath.class).get(0).lower();
        entityPath.getList("list", String.class).get(0);
    }

    @Test
    public void getMap(){
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        entityPath.getMap("map", String.class, String.class, StringPath.class).get("").lower();
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
    public void Get(){
        PathBuilder<User> entity = new PathBuilder<User>(User.class, "entity");
        NumberPath<Integer> intPath = new NumberPath<Integer>(Integer.class, "int");
        StringPath strPath = new StringPath("str");
        BooleanPath booleanPath = new BooleanPath("boolean");

        assertEquals("entity.int", entity.get(intPath).toString());
        assertEquals("entity.str", entity.get(strPath).toString());
        assertEquals("entity.boolean", entity.get(booleanPath).toString());

        assertEquals("entity.int", entity.get(entity.get(intPath)).toString());
    }

    @Test
    public void Various(){
        PathBuilder<User> entity = new PathBuilder<User>(User.class, "entity");
        entity.getBoolean("boolean");
        entity.getCollection("col", User.class);
        entity.getComparable("comparable", Comparable.class);
        entity.getDate("date", Date.class);
        entity.getDateTime("dateTime", Date.class);
        entity.getList("list", User.class);
        entity.getMap("map", String.class, User.class);
        entity.getNumber("number", Integer.class);
        entity.getSet("set", User.class);
        entity.getSimple("simple", Object.class);
        entity.getString("string");
        entity.getTime("time", Time.class);
    }

}
