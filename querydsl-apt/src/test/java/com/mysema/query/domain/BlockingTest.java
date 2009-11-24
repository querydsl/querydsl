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
    
    @QueryEntity
    public abstract class BlockingTestEntity2{
        
        @QueryTransient
        @QueryType(PropertyType.ENTITY)
        public abstract BlockingTestEntity getField2();
        
        @QueryTransient
        public abstract BlockingTestEntity getBlockedField();
    }
    
    @Test
    public void test(){
        assertTrue(QBlockingTestEntity.blockingTestEntity.field1 != null);
        assertTrue(QBlockingTestEntity.blockingTestEntity.field2 != null);
        
        cl = QBlockingTestEntity.class;
        assertMissing("blockedField");
    }
    
    @Test
    public void test2(){
        assertTrue(QBlockingTestEntity2.blockingTestEntity2.field2 != null);
        
        cl = QBlockingTestEntity2.class;
        assertMissing("blockedField");
    }
}
