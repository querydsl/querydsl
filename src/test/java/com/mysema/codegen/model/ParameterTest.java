/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class ParameterTest {

    @Test
    public void test() {
        Parameter param1 = new Parameter("test", new ClassType(TypeCategory.STRING, String.class));
        Parameter param2 = new Parameter("test2", new ClassType(TypeCategory.STRING, String.class));
        Parameter param3 = new Parameter("test2",
                new ClassType(TypeCategory.NUMERIC, Integer.class));

        assertFalse(param1.equals(param2));
        assertFalse(param1.equals(param3));
        assertFalse(param2.equals(param3));
    }

}
