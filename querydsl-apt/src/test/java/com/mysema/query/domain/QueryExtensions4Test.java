package com.mysema.query.domain;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.domain.QueryExtensions2Test.Point;
import com.mysema.query.types.path.PNumber;

public class QueryExtensions4Test {

    @SuppressWarnings("serial")
    public static class CustomNumber extends Number implements Comparable<CustomNumber>{       
        @QueryMethod("{0}")
        public int method(){
            return 0;
        }
        public double doubleValue() {
            return 0;
        }
        public float floatValue() {
            return 0;
        }
        public int intValue() {
            return 0;
        }
        public long longValue() {
            return 0;
        }
        public int compareTo(CustomNumber o) {
            return 0;
        }
        
    }
    
    @QueryEntity
    public static class Entity{
        
        Point point;
    }
    
    @Test
    public void test(){
        assertEquals(PNumber.class, QQueryExtensions4Test_CustomNumber.class.getSuperclass());
    }

}
