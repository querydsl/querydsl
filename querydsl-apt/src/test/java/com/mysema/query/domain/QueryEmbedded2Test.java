package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

public class QueryEmbedded2Test {

    @QueryEntity
    public static class Parent {
    
        String parentProperty;
        
        @QueryEmbedded
        Child child;
        
    }
    
    @QueryEmbeddable
    public static class Child {
     
        String childProperty;
        
    }
    
    @Test
    public void test(){
        assertNotNull(QQueryEmbedded2Test_Parent.parent.child.childProperty);
    }
    
}
