package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerydslConfig;

public class QuerydslConfig2Test {

    @QuerydslConfig(entityAccessors=true)
    @QueryEntity
    public static class Entity extends Superclass{
        
        Entity prop1;
        
    }
    
    @QueryEntity
    public static class Entity2 extends Superclass2{
        
        Entity prop1;
        
    }
    
    @QueryEntity
    public static class Superclass{
        
        Entity prop2;
    }
        
    @QuerydslConfig(entityAccessors=true)
    @QueryEntity
    public static class Superclass2{
        
        Entity prop2;
    }
    
    @Test
    public void test(){
        assertNotNull(QQuerydslConfig2Test_Entity.entity);
        assertNotNull(QQuerydslConfig2Test_Entity2.entity2);
    }
}
