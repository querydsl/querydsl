package com.mysema.query.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbedded;
import com.mysema.query.annotations.QueryEntity;

public class QueryEmbedded5Test {

    @QueryEntity
    public class User {
        
        @QueryEmbedded
        List<Complex<?>> rawList;
    
        @QueryEmbedded
        List<Complex<String>> list;
        
        @QueryEmbedded
        Collection<Complex<String>> collection;
        
        @QueryEmbedded
        Map<String, Complex<String>> map;
        
        @QueryEmbedded
        Map<String, Complex<String>> rawMap1;
        
        @QueryEmbedded
        Map<String, Complex<?>> rawMap2;
        
//        @QueryEmbedded
//        Map<?, Complex<String>> rawMap3;
                
    }
    
    
    public class Complex<T extends Comparable<T>> implements Comparable<Complex<T>> {

        T a;
        
        @Override
        public int compareTo(Complex<T> arg0) {
            return 0;
        }
        
    }
    
    @Test
    public void test(){
        /// TODO
    }
    
}
