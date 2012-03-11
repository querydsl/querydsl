package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeSuperTest {

    @Test
    public void GetVarName() {
        assertEquals("var", new TypeSuper("var", Types.STRING).getVarName());
    }

    @Test
    public void GetGenericName() {
        assertEquals("? super java.lang.String", new TypeSuper(Types.STRING).getGenericName(false));
    }

    @Test
    public void GetGenericName_As_ArgType() {
        assertEquals("java.lang.Object", new TypeSuper(Types.STRING).getGenericName(true));
    }

}
