package com.querydsl.apt.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic5Test {
    
    @MappedSuperclass
    public static class Base<B extends Base<B>> {
        
    }
    
    @Entity
    public static class Entity1<T extends Entity1<T>> {
        
    }
    
    @Entity
    public static class Entity2 extends Entity1<Entity2> {
        
    }
    
    @Entity
    public static class Entity3<T extends Entity3<T>> extends Base<T> {
        
    }
    
    @Entity
    public static class Entity4 extends Entity3<Entity4> {
        
    }
        
    @Test
    public void test() {
        
    }

}
