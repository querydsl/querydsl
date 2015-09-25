/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.support;

import static org.junit.Assert.assertEquals;

import java.util.*;

import org.junit.Test;

import com.google.common.base.Optional;
import com.google.common.collect.Ordering;

public class ClassUtilsTest {

    @Test
    public void GetName() {
        assertEquals("int", ClassUtils.getName(int.class));
        assertEquals("int", ClassUtils.getName(int.class, Collections.<String> emptySet(),
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
    public void GetName_Packge() {
        assertEquals("Locale", ClassUtils.getName(Locale.class, 
                Collections.singleton("java.util"), Collections.<String>emptySet()));
        assertEquals("java.util.Locale", ClassUtils.getName(Locale.class, 
                Collections.singleton("java.util.gen"), Collections.<String>emptySet()));
    }

    @Test
    public void Normalize() {
        assertEquals(List.class, ClassUtils.normalize(ArrayList.class));
        assertEquals(Set.class, ClassUtils.normalize(HashSet.class));
        assertEquals(Map.class, ClassUtils.normalize(HashMap.class));
//        assertEquals(Collection.class, ClassUtils.normalize(Bag.class));
    }

    @Test
    public void Normalize_Accessibility() {
        //com.google.common.base.Absent is not public, cannot be accessed from outside package
        assertEquals(Optional.class, ClassUtils.normalize(Optional.absent().getClass()));
        //com.google.common.collect.AllEqualOrdering is not public, cannot be accessed from outside package
        assertEquals(Ordering.class, ClassUtils.normalize(Ordering.allEqual().getClass()));

        //TODO interface normalization support? How to know which one?
        //com.google.common.base.Functions.ToStringFunction is not public, cannot be accessed from outside package
        //assertEquals(Function.class, ClassUtils.normalize(Functions.toStringFunction().getClass()));
    }
}
