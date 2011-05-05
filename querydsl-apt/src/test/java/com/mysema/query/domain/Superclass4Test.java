package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class Superclass4Test {
    
    public static class SuperClass {
        
        String superClassProperty;
        
    }
    
    @QueryEntity
    public static class Entity extends SuperClass {
        
        String entityProperty;
        
    }

    @Test
    public void SuperClass_Properties() {
        assertNotNull(QSuperclass4Test_SuperClass.superClass.superClassProperty);
    }
    
    @Test
    public void Entity_Properties() {
        assertNotNull(QSuperclass4Test_Entity.entity.entityProperty);
        assertNotNull(QSuperclass4Test_Entity.entity.superClassProperty);
    }
    
}
