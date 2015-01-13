package com.querydsl.apt.domain;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

import com.querydsl.apt.domain.QGeneric3Test_MyOrder;
import com.querydsl.core.types.path.StringPath;

import org.junit.Test;

public class Generic3Test extends AbstractTest {
    
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
    public void test() throws NoSuchFieldException {
        start(QGeneric3Test_MyOrder.class, QGeneric3Test_MyOrder.myOrder);
        match(StringPath.class, "property1");
        match(StringPath.class, "property2");
    }
    
}
