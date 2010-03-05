/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class ArrayTest {
    
    @QueryEntity
    public class ArrayTestEntity{
        ArrayTestEntity[] entityArray;
        int[] primitiveArray;
        String[] stringArray;
    }
    
    @Test
    public void test(){
        QArrayTest_ArrayTestEntity entity = QArrayTest_ArrayTestEntity.arrayTestEntity;
        assertEquals(ArrayTestEntity[].class, entity.entityArray.getType());
        assertEquals(ArrayTestEntity.class, entity.entityArray.get(0).getType());
    }

}
