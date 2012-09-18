package com.mysema.query.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import org.junit.Test;

public class Generic3Test {
    
    @MappedSuperclass
    public static abstract class BaseEntity<E extends BaseEntity<E>> {
        
    }
    
    @MappedSuperclass
    public static abstract class Order<O extends Order<O>> extends BaseEntity<O> implements Cloneable {
        
        String property1;        
    }
    
    @Entity
    public static class MyOrder<O extends MyOrder<O>> extends Order<O> {
        
        String property2;
    }
    
    @Test
    public void test() {
        
    }
    
}
