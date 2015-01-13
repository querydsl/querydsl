package com.querydsl.apt.domain;

import javax.persistence.Entity;

import org.junit.Test;

public class Generic6Test {

    @Entity
    public static class Cycle2<T extends Cycle1<?,?>> {
        
    }
    
    @Entity
    public static class Cycle1<U extends Comparable<U>, T extends Cycle2<?>> implements Comparable<Cycle1<U, T>> {

        @Override
        public int compareTo(Cycle1<U, T> o) {
            return 0;
        }
        
    }
    
    @Test
    public void test() {
    
    }
    
}
