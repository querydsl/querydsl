/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.querydsl.codegen.utils.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;

import org.junit.Test;

public class ConstructorTest {

    @Test
    public void test() {
        Parameter firstName = new Parameter("firstName", new ClassType(TypeCategory.STRING,
                String.class));
        Parameter lastName = new Parameter("lastName", new ClassType(TypeCategory.STRING,
                String.class));
        Constructor c1 = new Constructor(Arrays.asList(firstName, lastName));
        Constructor c2 = new Constructor(Arrays.asList(firstName, lastName));
        assertEquals(c1, c1);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
    }

}
