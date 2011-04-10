/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.annotations.QueryType;

public class BlockingTest extends AbstractTest{

    @QueryEntity
    public static class Entity{

        Entity field1;

        @QueryTransient
        @QueryType(PropertyType.ENTITY)
        Entity field2;

        @QueryTransient
        Entity blockedField;
    }

    @QueryEntity
    public static abstract class Entity2{

        @QueryTransient
        @QueryType(PropertyType.ENTITY)
        public abstract Entity getField2();

        @QueryTransient
        public abstract Entity getBlockedField();
    }

    @Test
    public void Entity_Fields_are_available(){
        assertTrue(QBlockingTest_Entity.entity.field1 != null);

        cl = QBlockingTest_Entity.class;
        assertMissing("blockedField");
    }
    
}
