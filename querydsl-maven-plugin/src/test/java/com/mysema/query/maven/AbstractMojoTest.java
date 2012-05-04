package com.mysema.query.maven;

import java.lang.reflect.Field;

/**
 * @author tiwe
 *
 */
public abstract class AbstractMojoTest {
    
    private final Class<?> cl;
    
    public AbstractMojoTest(Class<?> cl) {
        this.cl = cl;
    }
    
    protected void set(Object obj, String fieldName, Object value) throws Exception{
        Field field = cl.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
    }

}
