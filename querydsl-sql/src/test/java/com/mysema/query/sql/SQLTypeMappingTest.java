/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.Test;

public class SQLTypeMappingTest {

    @Test
    public void testGet() throws IllegalArgumentException, IllegalAccessException {
        SQLTypeMapping mapping = new SQLTypeMapping();
        for (Field field : java.sql.Types.class.getFields()){
            if (field.getType().equals(int.class)){
                int val = field.getInt(null);
                if (mapping.get(val) == null){
                    fail("Got no value for " + field.getName());
                }
            }
        }
    }

}
