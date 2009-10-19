package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class GenericTest {
    
    @QueryEntity
    public class GenericType<T extends ItemType> {
        T itemType;
    }
    
    @QueryEntity
    public class ItemType {

    }

    @Test
    public void test(){
        // TODO
    }
}
