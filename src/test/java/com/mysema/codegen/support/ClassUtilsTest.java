/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.support;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Bag;
import org.junit.Test;

public class ClassUtilsTest {

    @Test
    public void GetName() {
        assertEquals("int", ClassUtils.getName(int.class));
        assertEquals(
                "int",
                ClassUtils.getName(int.class, Collections.<String> emptySet(),
                        Collections.<String> emptySet()));
        assertEquals("Object", ClassUtils.getName(Object.class));
        assertEquals("Object[]", ClassUtils.getName(Object[].class));
        assertEquals("int", ClassUtils.getName(int.class));
        assertEquals("int[]", ClassUtils.getName(int[].class));
        assertEquals("void", ClassUtils.getName(void.class));
        assertEquals("java.util.Locale", ClassUtils.getName(Locale.class));
        assertEquals("java.util.Locale[]", ClassUtils.getName(Locale[].class));
    }

    @Test
    public void Normalize() {
        assertEquals(List.class, ClassUtils.normalize(ArrayList.class));
        assertEquals(Set.class, ClassUtils.normalize(HashSet.class));
        assertEquals(Map.class, ClassUtils.normalize(HashMap.class));
        assertEquals(Collection.class, ClassUtils.normalize(Bag.class));
    }

}
