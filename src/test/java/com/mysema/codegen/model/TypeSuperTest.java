package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeSuperTest {

    @Test
    public void GetGenericName() {
        assertEquals("? super java.lang.String", new TypeSuper(Types.STRING).getGenericName(false));
    }

}
