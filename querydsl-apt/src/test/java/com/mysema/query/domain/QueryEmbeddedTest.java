package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;

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
        
        @QueryEmbedded
        Map<String,Child> children2;
        
    }
    
    public class Child {
        
        String childProperty;
        
    }
    
    @Test
    public void Parent_Child_ChildProperty(){
        assertNotNull(QQueryEmbeddedTest_Parent.parent.child.childProperty);
    }
    
    @Test
    public void Parent_Children_Any_ChildrenProperty(){
        assertNotNull(QQueryEmbeddedTest_Parent2.parent2.children.any().childProperty);
    }
    
    @Test
    public void Parent_Children2_MapAccess(){
        assertNotNull(QQueryEmbeddedTest_Parent2.parent2.children2.containsKey("XXX"));
        assertNotNull(QQueryEmbeddedTest_Parent2.parent2.children2.get("XXX").childProperty);
    }
}
