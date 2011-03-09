package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeExtendsTest {

    @Test
    public void GetGenericName() {
        assertEquals("? extends java.util.Collection<java.lang.Object>", new TypeExtends(Types.COLLECTION).getGenericName(false));
    }

    @Test
    public void GetGenericName_As_ArgType(){
        assertEquals("java.util.Collection<java.lang.Object>", new TypeExtends(Types.COLLECTION).getGenericName(true));
    }

}
