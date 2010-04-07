package com.mysema.codegen;

import static org.junit.Assert.assertEquals;

import java.util.Locale;

import org.junit.Test;

public class ClassUtilsTest {

    @Test
    public void testGetName() {
        assertEquals("Object", ClassUtils.getName(Object.class));
        assertEquals("Object[]", ClassUtils.getName(Object[].class));
        assertEquals("int", ClassUtils.getName(int.class));
        assertEquals("int[]", ClassUtils.getName(int[].class));
        assertEquals("void", ClassUtils.getName(void.class));
        assertEquals("java.util.Locale", ClassUtils.getName(Locale.class));
        assertEquals("java.util.Locale[]", ClassUtils.getName(Locale[].class));
    }

}
