/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.apt.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.querydsl.core.annotations.Config;
import com.querydsl.core.annotations.QueryEntity;

public class QuerydslConfig2Test {

    @Config(entityAccessors = true)
    @QueryEntity
    public static class Entity extends Superclass {

        Entity prop1;

    }

    @Config(createDefaultVariable = false)
    @QueryEntity
    public static class Entity2 extends Superclass2 {

        Entity prop1;

    }

    @Config(entityAccessors = true, createDefaultVariableStaticProperties = true)
    @QueryEntity
    public static class EntityWithStaticProperties extends Superclass {

        Entity prop1;

        String stringProp;

        List<String> stringProps;

    }

    @QueryEntity
    public static class Superclass {

        Entity prop2;
    }

    @Config(entityAccessors = true)
    @QueryEntity
    public static class Superclass2 {

        Entity prop2;
    }

    @Test
    public void test() {
        assertNotNull(QQuerydslConfig2Test_Entity.entity);
    }

    @Test
    public void testStaticProperties() {
        assertEquals(QQuerydslConfig2Test_EntityWithStaticProperties.prop1_,
                QQuerydslConfig2Test_EntityWithStaticProperties.entityWithStaticProperties.prop1);

        assertEquals(QQuerydslConfig2Test_EntityWithStaticProperties.prop2_,
                QQuerydslConfig2Test_EntityWithStaticProperties.entityWithStaticProperties.prop2);

        assertEquals(QQuerydslConfig2Test_EntityWithStaticProperties.stringProp_,
                QQuerydslConfig2Test_EntityWithStaticProperties.entityWithStaticProperties.stringProp);

        assertEquals(QQuerydslConfig2Test_EntityWithStaticProperties.stringProps_,
                QQuerydslConfig2Test_EntityWithStaticProperties.entityWithStaticProperties.stringProps);
    }

    @Test(expected = NoSuchFieldException.class)
    public void create_default_variable() throws SecurityException, NoSuchFieldException {
        QQuerydslConfig2Test_Entity2.class.getField("entity2");
    }

    @Test(expected = NoSuchFieldException.class)
    public void create_entity_static_properties() throws SecurityException, NoSuchFieldException {
        QQuerydslConfig2Test_Entity.class.getField("prop1_");
    }

}

