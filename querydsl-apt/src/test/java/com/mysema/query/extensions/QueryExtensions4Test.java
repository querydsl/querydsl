/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.extensions;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryMethod;
import com.mysema.query.types.path.NumberPath;

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

        @Override
        public int compareTo(CustomNumber o) {
            return 0;
        }

        @Override
        public int hashCode(){
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o){
            return o instanceof CustomNumber;
        }

    }

    @QueryEntity
    public static class Entity{

        CustomNumber number;
    }

    @Test
    public void CustomNumber_Has_Right_Type(){
        assertEquals(NumberPath.class, QQueryExtensions4Test_CustomNumber.class.getSuperclass());
        assertEquals(NumberPath.class, QQueryExtensions4Test_Entity.entity.number.getClass().getSuperclass());
    }

}
