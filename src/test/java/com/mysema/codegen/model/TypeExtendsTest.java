package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeExtendsTest {

    @Test
    public void GetGenericName() {
        assertEquals("? extends java.util.Collection<java.lang.Object>", new TypeExtends(Types.COLLECTION).getGenericName(false));
    }

}
