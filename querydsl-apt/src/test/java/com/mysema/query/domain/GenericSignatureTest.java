package com.mysema.query.domain;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class GenericSignatureTest {
    
    @QueryEntity
    public static class Entity<T extends Entity<T>> {
        
        // collection
        Collection<Entity> rawCollection;       
        
        Collection<Entity<T>> genericCollection;        
        
        Collection<T> genericCollection2;
        
        // list
        List<Entity> rawList;        
        
        List<Entity<T>> genericList;        
        
        List<T> genericList2;
        
        // set
        Set<Entity> rawSet;        
        
        Set<Entity<T>> genericSet;        
        
        Set<T> genericSet2;
        
        // map
        Map<String,Entity> rawMap;        
        
        Map<String,Entity<T>> genericMap;        
        
        Map<String,T> genericMap2;
    }
    
    @Test
    public void test(){
        QGenericSignatureTest_Entity entity = QGenericSignatureTest_Entity.entity;
        // collection
        assertEquals(Entity.class, entity.rawCollection.getParameter(0));
        assertEquals(Entity.class, entity.genericCollection.getParameter(0));
        assertEquals(Entity.class, entity.genericCollection2.getParameter(0));
        
        // list
        assertEquals(Entity.class, entity.rawList.getParameter(0));
        assertEquals(Entity.class, entity.genericList.getParameter(0));
        assertEquals(Entity.class, entity.genericList2.getParameter(0));
        
        // set
        assertEquals(Entity.class, entity.rawSet.getParameter(0));
        assertEquals(Entity.class, entity.genericSet.getParameter(0));
        assertEquals(Entity.class, entity.genericSet2.getParameter(0));
        
        // map
        assertEquals(Entity.class, entity.rawMap.getParameter(1));
        assertEquals(Entity.class, entity.genericMap.getParameter(1));
        assertEquals(Entity.class, entity.genericMap2.getParameter(1));
    }

}
