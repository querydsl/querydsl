/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.types.operation.Ops;


// TODO: Auto-generated Javadoc
/**
 * OpsTest provides.
 * 
 * @author tiwe
 * @version $Id$
 */
public class OpsTest {
    
    /**
     * Test.
     */
    @Test
    public void test(){
        Map<String,Field> fields = new HashMap<String,Field>();
        for (Class<?> cl : Arrays.<Class<?>>asList(Ops.class, 
                Ops.DateTimeOps.class, 
                Ops.MathOps.class, 
                Ops.StringOps.class)){
            for (Field field : cl.getDeclaredFields()){
                Field old = fields.put(field.getName(), field);
                if (old != null){
                    fail("Duplicate field name " + field.getName() + 
                            " in " + field.getDeclaringClass().getSimpleName() + 
                            " and " + 
                            old.getDeclaringClass().getSimpleName());
                }
            }
        }
    }

}
