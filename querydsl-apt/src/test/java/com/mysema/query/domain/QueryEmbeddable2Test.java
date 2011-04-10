package com.mysema.query.domain;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;

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
        
    }
    
    @Test
    public void User_Complex_a() {
        assertNotNull(QQueryEmbeddable2Test_User.user.complex.a);
    }
    
}
