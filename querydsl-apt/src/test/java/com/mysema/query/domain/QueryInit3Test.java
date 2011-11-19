package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class QueryInit3Test {

    @QueryEntity
    public class Entity{

        @QueryInit("*.*")
        Entity prop1;

        @QueryInit("*")
        Entity prop2;

        List<Entity> entityList;

        Map<String,Entity> entityMap;
    }
    
    @Test
    public void test(){
        assertEquals("entity.prop1.prop2.prop1", QQueryInit3Test_Entity.entity.prop1.prop2.prop1.toString());
    }
    
}
