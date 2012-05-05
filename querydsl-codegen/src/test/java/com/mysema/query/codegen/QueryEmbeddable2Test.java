package com.mysema.query.codegen;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;

@Ignore
public class QueryEmbeddable2Test {

    @QueryEntity
    public static class User {
    
        Complex<String> complex;
        
    }
    
    @QueryEmbeddable
    public static class Complex<T extends Comparable<T>> implements Comparable<Complex<T>> {

        T a;
        
        @Override
        public int compareTo(Complex<T> arg0) {
            return 0;
        }
        
        public boolean equals(Object o) {
            return o == this;
        }
        
    }
        
}