/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.mysema.codegen.model.ClassType;
import com.mysema.codegen.model.Parameter;
import com.mysema.codegen.model.TypeCategory;

public class ParameterTest {

    @Test
    public void test() {
        Parameter param1 = new Parameter("test", new ClassType(TypeCategory.STRING, String.class));
        Parameter param2 = new Parameter("test2", new ClassType(TypeCategory.STRING, String.class));
        Parameter param3 = new Parameter("test2",
                new ClassType(TypeCategory.NUMERIC, Integer.class));

        assertTrue(param1.equals(param2));
        assertFalse(param1.equals(param3));
        assertFalse(param2.equals(param3));
    }
}
