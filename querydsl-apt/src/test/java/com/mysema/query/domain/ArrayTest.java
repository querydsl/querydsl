package com.mysema.query.domain;

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
        // TODO
    }

}
