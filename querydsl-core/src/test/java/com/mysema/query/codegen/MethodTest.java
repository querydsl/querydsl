/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.codegen;

import static org.junit.Assert.*;

import org.junit.Test;

public class MethodTest {

    private TypeFactory typeFactory = new TypeFactory();

    @Test
    public void test(){
    Method m1 = new Method(typeFactory.create(String.class), "method", "abc", typeFactory.create(String.class));
    Method m2 = new Method(typeFactory.create(String.class), "method", "abc", typeFactory.create(String.class));
    assertEquals(m1, m1);
    assertEquals(m1, m2);
    assertEquals(m1.hashCode(), m2.hashCode());
    }

    @Test
    public void createCopy(){
    Method method = new Method(typeFactory.create(String.class), "method", "abc", typeFactory.create(String.class));
    Type typeModel = new SimpleType(TypeCategory.ENTITY, "com.mysema.query.DomainClass", "com.mysema.query", "DomainClass", false);
        EntityType type = new EntityType("Q", typeModel);
        assertNotNull(method.createCopy(type));
    }

}
