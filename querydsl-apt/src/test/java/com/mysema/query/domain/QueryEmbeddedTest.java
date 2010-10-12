package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

public class QueryEmbeddedTest {

    @QueryEntity
    public class Parent {
    
        String parentProperty;
        
        @QueryEmbedded
        Child child;
        
    }

    @QueryEntity
    public class Parent2 {
    
        String parentProperty;
        
        @QueryEmbedded
        List<Child> children;
        
    }
        
    public class Child {
     
        String childProperty;
        
    }
    
    @Test
    public void test(){
        assertNotNull(QQueryEmbeddedTest_Parent.parent.child.childProperty);
        assertNotNull(QQueryEmbeddedTest_Parent2.parent2.children.any().childProperty);
    }
    
}
