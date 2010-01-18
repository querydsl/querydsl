package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerydslConfig;

public class QuerydslConfigTest {
    
    @QuerydslConfig(entityAccessors=true)
    @QueryEntity
    public static class Superclass{
        
        Entity prop3;
    }
    
    @QuerydslConfig(entityAccessors=true, listAccessors = true, mapAccessors= true)
    @QueryEntity
    public static class Entity extends Superclass{
        
        Entity prop1;
        
        Entity prop2;
        
        List<Entity> entityList;
        
        Map<String,Entity> entityMap;
    }
    
    @Test
    public void test_long_path(){
        assertEquals("entity.prop1.prop2.prop1", QQuerydslConfigTest_Entity.entity.prop1().prop2().prop1().toString());
    }

}
