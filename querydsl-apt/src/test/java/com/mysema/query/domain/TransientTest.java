package com.mysema.query.domain;

import static org.junit.Assert.*;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.junit.Test;

import com.mysema.query.annotations.PropertyType;
import com.mysema.query.annotations.QueryType;

public class TransientTest {
    
    @Entity
    class ExampleEntity {
        
        @QueryType(PropertyType.SIMPLE)
        @Transient
        String property1;
        
        @Transient
        String property2;
        
        @QueryType(PropertyType.SIMPLE)
        transient String property3;
        
        transient String property4;
    }
    
    @Test
    public void test(){
        assertNotNull(QTransientTest_ExampleEntity.exampleEntity.property1);
        assertNotNull(QTransientTest_ExampleEntity.exampleEntity.property3);
    }

}
