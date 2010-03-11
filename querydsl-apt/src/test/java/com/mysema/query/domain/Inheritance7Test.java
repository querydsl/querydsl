package com.mysema.query.domain;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class Inheritance7Test {
    
    @QueryEntity
    public static class User{
        
    }
    
    @QueryEntity
    public static class Category<T extends Category<T>> implements Comparable<T>{
     
        private User owner;

        public User getOwner() {
            return owner;
        }

        @Override
        public int compareTo(T o) {
            return 0;
        }
        
    }

    @QueryEntity
    public static class SubCategory extends Category<SubCategory> {
        
        private SubCategory parent;

        public SubCategory getParent() {
            return parent;
        }
        
    }
    
    @QueryEntity
    public static class SubCategory2 extends Category<SubCategory2> {
        
        private SubCategory2 parent;

        public SubCategory2 getParent() {
            return parent;
        }
        
    }
    
    @Test
    public void test(){
        // TODO
    }
    
}
