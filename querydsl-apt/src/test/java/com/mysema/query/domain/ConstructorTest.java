package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class ConstructorTest {
    
    @QuerySupertype
    public static class CategorySuperclass{
        
    }
    
    @QueryEntity
    public static class Category<T extends Category<T>> extends CategorySuperclass{
        
        public Category(int i){}
        
    }
    
    @QueryEntity
    public static class ClassWithConstructor{
        
        public ClassWithConstructor(){}
        
    }
    
    @Test
    public void test(){
        // TODO
    }

}
