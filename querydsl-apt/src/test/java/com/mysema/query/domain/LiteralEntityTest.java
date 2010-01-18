package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class LiteralEntityTest {
    
    @QueryEntity
    enum EnumEntity{
        
    }
    
    @Test
    public void test(){
        assertNotNull(QLiteralEntityTest_EnumEntity.enumEntity);
    }

}
