package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

public abstract class AbstractTest {
    
    public Class<?> cl;
    
    protected void match(Class<?> expectedType, String name) throws SecurityException, NoSuchFieldException{
        assertEquals(cl.getSimpleName()+"."+name + " failed", expectedType, cl.getField(name).getType());
    }

}
