/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.fail;

import java.lang.reflect.Field;

import org.junit.Test;

public class SQLTypeMappingTest {

    @Test
    public void Get() throws IllegalArgumentException, IllegalAccessException {
        JDBCTypeMapping mapping = new JDBCTypeMapping();
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
