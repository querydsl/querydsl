/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.Config;

public class QuerydslConfig2Test {

    @Config(entityAccessors=true)
    @QueryEntity
    public static class Entity extends Superclass{

        Entity prop1;

    }

    @Config(createDefaultVariable=false)
    @QueryEntity
    public static class Entity2 extends Superclass2{

        Entity prop1;

    }

    @QueryEntity
    public static class Superclass{

        Entity prop2;
    }

    @Config(entityAccessors=true)
    @QueryEntity
    public static class Superclass2{

        Entity prop2;
    }

    @Test
    public void test(){
        assertNotNull(QQuerydslConfig2Test_Entity.entity);
    }

    @Test(expected=NoSuchFieldException.class)
    public void Create_Default_Variable() throws SecurityException, NoSuchFieldException{
        QQuerydslConfig2Test_Entity2.class.getField("entity2");
    }
}

