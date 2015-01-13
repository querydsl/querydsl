package com.querydsl.apt.domain;

import java.util.Set;

import javax.persistence.Entity;

import com.google.common.collect.ForwardingSet;

public class CustomCollection {
    
    @Entity
    public static class MyCustomCollection<T> extends ForwardingSet<T> {

        @Override
        protected Set<T> delegate() {
            return null;
        }

    }
    
    @Entity
    public static class MyCustomCollection2 extends ForwardingSet<String> {

        @Override
        protected Set<String> delegate() {
            return null;
        }

    }
    
    @Entity
    public static class MyEntity {
        
        MyCustomCollection<String> strings;
        
        MyCustomCollection2 strings2;
    }

}
