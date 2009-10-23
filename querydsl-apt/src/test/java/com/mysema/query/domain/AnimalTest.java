package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class AnimalTest {
    
    @QueryEntity
    public static class Animal{
        
    }
    
    @QueryEntity
    public static class Cat extends Animal{
     
        public Cat mate;
        
    }
    
    @Test
    public void test(){
        
    }
    
}
