package com.querydsl.codegen.utils.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeAdapterTest {

    @Test
    public void Delegation() {
        Type inner = Types.OBJECT;
        Type type = new TypeAdapter(inner);
        assertEquals(inner.getCategory(), type.getCategory());
        assertEquals(inner.getComponentType(), type.getComponentType());
        assertEquals(inner.getFullName(), type.getFullName());
        assertEquals(inner.getGenericName(true), type.getGenericName(true));
        assertEquals(inner.getPackageName(), type.getPackageName());
        assertEquals(inner.getParameters(), type.getParameters());
//        assertEquals(inner.getPrimitiveName(), type.getPrimitiveName());
        assertEquals(inner.getSimpleName(), type.getSimpleName());
        assertEquals(inner.isFinal(), type.isFinal());
        assertEquals(inner.isPrimitive(), type.isPrimitive());
    }
}
