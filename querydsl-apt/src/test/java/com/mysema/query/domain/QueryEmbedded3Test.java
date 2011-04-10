package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

public class QueryEmbedded3Test {

    @QueryEntity
    public static class Parent {
    
        String parentProperty;

        @QueryEmbedded
        List<Child> children;
        
        @QueryEmbedded
        Child child;
        
    }
        
    public static class Child {
     
        String childProperty;
        
    }
    
    @Test
    public void test(){
        assertNotNull(QQueryEmbedded3Test_Parent.parent.child.childProperty);
        assertNotNull(QQueryEmbedded3Test_Parent.parent.children.any().childProperty);
    }
    
}
