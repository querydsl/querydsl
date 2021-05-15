package com.querydsl.codegen.utils.model;

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
    
    @Test
    public void Comparable() {
        // T extends Comparable<? super T>
        Type comparable = new ClassType(Comparable.class);
        Type type = new TypeExtends("T",
                new SimpleType(comparable, new TypeSuper(new TypeExtends("T", comparable))));
        assertEquals("? extends java.lang.Comparable<?>", type.getGenericName(false));
    }

}
