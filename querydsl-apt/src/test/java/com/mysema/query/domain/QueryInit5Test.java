package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryInit;

public class QueryInit5Test {
    
    @QueryEntity 
    static class OtherClass {
        
    }
    
    @QueryEntity 
    static class OtherClassTwo {
        
    }
    
    @QueryEntity
    static class Parent {
        private int x;
        @QueryInit("*")
        private OtherClass z;
    }
    
    @QueryEntity
    static class Child extends Parent {
        @QueryInit("*")
        private OtherClassTwo y;
    }
    
    @Test
    public void test() {
        //QChild c = QParent.parent.as(QChild.class)
        QQueryInit5Test_Parent.parent.as(QQueryInit5Test_Child.class);
    }

}
