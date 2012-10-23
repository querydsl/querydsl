package com.mysema.query.domain;

import java.util.List;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class Generic8Test {
    
    @QuerySupertype
    public static class Superclass<T> {

        Long id;
        
        List<T> values;
        
        List<? extends T> values2;

    }
    
    @QueryEntity
    public static class Entity extends Superclass<String> {

    }

    
    @QueryEntity
    public static class Entity2 extends Superclass<Integer> {

    }

    @Test
    public void test() {
        
    }

}
