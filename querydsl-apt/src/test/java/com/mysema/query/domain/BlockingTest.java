package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.annotations.QueryType;

public class BlockingTest extends AbstractTest{

    @QueryEntity
    public class BlockingTestEntity{
        
        BlockingTestEntity field1;
        
        @QueryTransient
        @QueryType(PropertyType.ENTITY)
        BlockingTestEntity field2;
        
        @QueryTransient
        BlockingTestEntity blockedField;
    }
    
    @Test
    public void test(){
        assertTrue(QBlockingTestEntity.blockingTestEntity.field1 != null);
        assertTrue(QBlockingTestEntity.blockingTestEntity.field2 != null);
        
        cl = QBlockingTestEntity.class;
        assertMissing("blockedField");
    }
}
