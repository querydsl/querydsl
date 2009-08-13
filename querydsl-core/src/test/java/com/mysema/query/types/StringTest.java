/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import static com.mysema.query.alias.Alias.$;
import static com.mysema.query.alias.Alias.alias;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.serialization.OperationPatterns;
import com.mysema.query.types.expr.EArrayConstructor;
import com.mysema.query.types.expr.EConstructor;
import com.mysema.query.types.operation.Operator;
import com.mysema.query.types.operation.OperatorImpl;
import com.mysema.query.types.operation.Ops;

// TODO: Auto-generated Javadoc
/**
 * StringTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class StringTest {
    
    /**
     * Test pattern availability.
     * 
     * @throws IllegalArgumentException the illegal argument exception
     * @throws IllegalAccessException the illegal access exception
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testPatternAvailability() throws IllegalArgumentException, IllegalAccessException{
        OperationPatterns ops = new OperationPatterns(){{
            // TODO
        }};
        Set<Field> missing = new HashSet<Field>();
        for (Field field : Ops.class.getFields()){
            if (field.getType().equals(OperatorImpl.class)){
                Operator op = (Operator)field.get(null);
                if (ops.getPattern(op) == null) missing.add(field);    
            }            
        }
        for (Class<?> cl : Ops.class.getClasses()){
            for (Field field : cl.getFields()){
                if (field.getType().equals(OperatorImpl.class)){
                    Operator op = (Operator)field.get(null);
                    if (ops.getPattern(op) == null) missing.add(field);    
                }                
            }   
        }
        
        if (!missing.isEmpty()){
            for (Field field : missing){
                System.err.println(field.getName());
            }
            fail();
        }
    }
    
    /**
     * Test to string.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testToString(){
        SomeType alias = alias(SomeType.class, "alias");
        
        // Path toString
        assertEquals("alias.name", $(alias.getName()).toString());
        assertEquals("alias.ref.name", $(alias.getRef().getName()).toString());
        assertEquals("alias.refs.get(0)", $(alias.getRefs().get(0)).toString());
        
        // Operation toString
        assertEquals("lower(alias.name)", $(alias.getName()).lower().toString());
        
        // EConstructor
        EConstructor<SomeType> someType = new EConstructor<SomeType>(SomeType.class,$(alias));
        assertEquals("new SomeType(alias)", someType.toString());
        
        // EArrayConstructor
        EArrayConstructor<SomeType> someTypeArray = new EArrayConstructor<SomeType>(SomeType.class,$(alias));
        assertEquals("[alias]", someTypeArray.toString());
    }
    
    /**
     * The Interface SomeType.
     */
    public interface SomeType{        
        
        /**
         * Gets the name.
         * 
         * @return the name
         */
        String getName();
        
        /**
         * Gets the ref.
         * 
         * @return the ref
         */
        SomeType getRef();
        
        /**
         * Gets the refs.
         * 
         * @return the refs
         */
        List<SomeType> getRefs();
        
        /**
         * Gets the amount.
         * 
         * @return the amount
         */
        int getAmount();
    }

}
