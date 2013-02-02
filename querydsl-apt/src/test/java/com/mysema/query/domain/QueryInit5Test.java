package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class QueryInit5Test {
    
    @QueryEntity 
    static class OtherClass {        
        OtherClass entity;
    }
    
    @QueryEntity 
    static class OtherClassTwo {        
        OtherClassTwo entity;        
    }
    
    @QueryEntity
    static class Parent {
        int x;
        
        @QueryInit("*")
        OtherClass z;
    }
    
    @QueryEntity
    static class Child extends Parent {
        @QueryInit("*")
        OtherClassTwo y;
    }
    
    @Test
    public void test() {
        //QChild c = QParent.parent.as(QChild.class)
        assertNotNull(QQueryInit5Test_Parent.parent.z.entity);
        
        QQueryInit5Test_Child child = QQueryInit5Test_Parent.parent.as(QQueryInit5Test_Child.class);
        assertNotNull(child.z.entity);
        assertNotNull(child.y.entity);
        
    }

}
