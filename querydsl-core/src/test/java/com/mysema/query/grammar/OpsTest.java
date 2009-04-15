/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;


/**
 * OpsTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class OpsTest {
    
    @Test
    public void test(){
        Map<String,Field> fields = new HashMap<String,Field>();
        for (Class<?> cl : Arrays.<Class<?>>asList(Ops.class, 
                Ops.OpDateTime.class, 
                Ops.OpMath.class, 
                Ops.OpNumberAgg.class, 
                Ops.OpString.class)){
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
