package com.mysema.query.domain;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEntity;

@Ignore
public class Superclass4Test {
    
    public static class SuperClass {
        
    }
    
    @QueryEntity
    public static class Entity extends SuperClass {
        
    }
    
}
