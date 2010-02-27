/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Constructor;

import org.junit.Test;

public class EConstructorTest {
    
    public static class Projection{
        
        public Projection(){
            
        }
        
        public Projection(Long id){
            
        }
        
        public Projection(long id, String text){
            
        }
        
        public Projection(CharSequence text){
            
        }
    }

    @Test
    public void test_Constructor(){
        ENumber<Long> longVal = ENumberConst.create(1l);
        EString stringVal = EStringConst.create("");
        new EConstructor<Projection>(Projection.class, new Class[]{long.class, String.class}, longVal, stringVal)
            .getJavaConstructor();
    }
        
    @Test
    public void test_create(){
        ENumber<Long> longVal = ENumberConst.create(1l);
        EString stringVal = EStringConst.create("");
        Constructor<?> c = EConstructor.create(Projection.class, longVal, stringVal).getJavaConstructor();
        assertEquals(long.class, c.getParameterTypes()[0]);
        assertEquals(String.class, c.getParameterTypes()[1]);
    }
    
    @Test
    public void test_create2(){
        ENumber<Long> longVal = ENumberConst.create(1l);
        Constructor<?> c = EConstructor.create(Projection.class, longVal).getJavaConstructor();
        assertEquals(Long.class, c.getParameterTypes()[0]);
    }
    
    @Test
    public void test_create3(){
        Constructor<?> c = EConstructor.create(Projection.class).getJavaConstructor();
        assertEquals(0, c.getParameterTypes().length);
    }
    
    @Test
    public void test_create4(){
        EString stringVal = EStringConst.create("");
        Constructor<?> c = EConstructor.create(Projection.class, stringVal).getJavaConstructor();
        assertEquals(CharSequence.class, c.getParameterTypes()[0]);
    }
    
}
