package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class AnimalTest {
    
    @QueryEntity
    public static class Animal{
        
        public String name;
        
    }
    
    @QueryEntity
    public static class Cat extends Animal{
     
        public Cat mate;
        
    }
    
    @Test
    public void test(){
        assertTrue("direct copy of PString field failed", QCat.cat.name == QCat.cat._super.name);
        
    }
    
}
