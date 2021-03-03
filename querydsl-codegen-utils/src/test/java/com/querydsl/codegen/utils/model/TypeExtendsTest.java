package com.querydsl.codegen.utils.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeExtendsTest {

    @Test
    public void GetVarName() {
        assertEquals("var", new TypeExtends("var", Types.COLLECTION).getVarName());
    }

    @Test
    public void GetGenericName() {
        assertEquals("? extends java.util.Collection<java.lang.Object>", new TypeExtends(
                Types.COLLECTION).getGenericName(false));
    }

    @Test
    public void GetGenericName_As_ArgType() {
        assertEquals("java.util.Collection<java.lang.Object>",
                new TypeExtends(Types.COLLECTION).getGenericName(true));
    }

    @Test
    public void GetGenericName_With_Object() {
        assertEquals("?", new TypeExtends(Types.OBJECT).getGenericName(false));
    }

}
