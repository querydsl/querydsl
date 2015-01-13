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
package com.querydsl.core.types.path;

import java.sql.Time;
import java.util.Date;
import java.util.Map;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.util.BeanMap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PathBuilderTest {

    @Test
    public void getEnum() {
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        EnumPath<Gender> enumPath = entityPath.getEnum("gender", Gender.class);
        assertNotNull(enumPath.ordinal());
        assertEquals(enumPath, entityPath.get(enumPath));
    }

    @Test
    public void getByExample() {
        User user = new User();
        user.setFirstName("firstName");
        user.setLastName("lastName");
        String byExample = getByExample(user).toString();
        assertTrue(byExample.contains("entity.lastName = lastName"));
        assertTrue(byExample.contains("entity.firstName = firstName"));
    }

    @Test
    public void getArray() {
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        ArrayPath<String[], String> array = entityPath.getArray("array", String[].class);
        assertEquals(String[].class, array.getType());
        assertEquals(String.class, array.getElementType());
    }

    @Test
    public void getList() {
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        entityPath.getList("list", String.class, StringPath.class).get(0).lower();
        entityPath.getList("list", String.class).get(0);
    }

    @Test
    public void getMap() {
        PathBuilder<User> entityPath = new PathBuilder<User>(User.class, "entity");
        entityPath.getMap("map", String.class, String.class, StringPath.class).get("").lower();
        entityPath.getMap("map", String.class, String.class).get("");
    }

    @SuppressWarnings("unchecked")
    private <T> BooleanBuilder getByExample(T entity) {
        PathBuilder<T> entityPath = new PathBuilder<T>((Class<T>)entity.getClass(), "entity");
        BooleanBuilder conditions = new BooleanBuilder();
        Map<String, Object> beanMap = new BeanMap(entity);
        for (Map.Entry<String,Object> entry : beanMap.entrySet()) {
            if (!entry.getKey().equals("class")) {
                if (entry.getValue() != null) {
                    conditions.and(entityPath.get(entry.getKey()).eq(entry.getValue()));
                }
            }
        }
        return conditions;
    }

    @Test
    public void Get() {
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
    public void Various() {
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
