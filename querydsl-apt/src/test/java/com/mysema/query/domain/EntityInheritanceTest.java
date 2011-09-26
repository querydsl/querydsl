package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class EntityInheritanceTest {
    
    @MappedSuperclass
    public static class TreeEntity<T extends TreeEntity<T>> {
        
        Integer id;
        
        T parent;
        
    }

    @Entity
    public static class TestEntity extends TreeEntity<TestEntity> {
        
        String name;
        
    }
    
    @Test
    public void test() {
        assertEquals(
            QEntityInheritanceTest_TestEntity.class, 
            QEntityInheritanceTest_TestEntity.testEntity.parent.getClass());
    }

}
