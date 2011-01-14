package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.annotations.QueryEmbeddable;
import com.mysema.query.annotations.QueryEntity;

public class QueryEmbeddable3Test {

    @QueryEntity
    public class User {
        
        List<Complex<?>> rawList;
    
        List<Complex<String>> list;
        
        Set<Complex<String>> set;
        
        Collection<Complex<String>> collection;
        
        Map<String, Complex<String>> map;
        
        Map<String, Complex<String>> rawMap1;
        
        Map<String, Complex<?>> rawMap2;
        
//        @QueryEmbedded
//        Map<?, Complex<String>> rawMap3;
                
    }
    
    @QueryEmbeddable
    public class Complex<T extends Comparable<T>> implements Comparable<Complex<T>> {

        T a;
        
        @Override
        public int compareTo(Complex<T> arg0) {
            return 0;
        }
        
    }
    
    @Test
    public void User_rawList(){
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.rawList.any().getClass());
    }
    
    @Test
    public void User_list(){
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.list.any().getClass());
    }
    
    @Test
    public void User_set(){
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.set.any().getClass());
    }
    
    @Test
    public void User_collection(){
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.collection.any().getClass());
    }
    
    @Test
    public void User_map(){
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.map.get("XXX").getClass());
    }
    
    @Test
    public void User_rawMap1(){
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.rawMap1.get("XXX").getClass());
    }
    
    @Test
    public void User_rawMap2(){
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.rawMap1.get("XXX").getClass());
    }
    
}
