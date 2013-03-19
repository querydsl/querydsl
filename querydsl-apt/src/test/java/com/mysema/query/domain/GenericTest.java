/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mysema.query.domain;

import java.util.Date;

import org.junit.Ignore;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QueryTransient;
import com.mysema.query.domain.rel.SimpleType;
import com.mysema.query.domain.rel.SimpleType2;

@Ignore
public class GenericTest {

    @QueryEntity
    public static class GenericType<T extends ItemType> {
        T itemType;
    }

    @QueryEntity
    @SuppressWarnings("unchecked")
    public static class GenericType2<T extends ItemType> {
        T itemType;

        // simple        
        GenericSimpleType prop1;
        GenericSimpleType<?> prop2;
        GenericSimpleType<? extends GenericSimpleType<?>> prop3;

        // comparable
        GenericComparableType comp1;
        GenericComparableType<Number> comp2;
        GenericComparableType<Date> comp3;

        // number

        @QueryTransient
        GenericNumberType num1; // NOTE : doesn't work!

        GenericNumberType<Number> num2;
        GenericNumberType<Date> num3;
    }

    public static class GenericSimpleType<T extends GenericSimpleType<T>>{

    }

    @SuppressWarnings("unchecked")
    public static class GenericComparableType<T> implements Comparable<GenericComparableType<T>>{
        @Override
        public int compareTo(GenericComparableType<T> o) {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof GenericComparableType;
        }
    }

    @SuppressWarnings({ "unchecked", "serial" })
    public static class GenericNumberType<T> extends Number implements Comparable<GenericNumberType<T>>{
        @Override
        public double doubleValue() {
            return 0;
        }
        @Override
        public float floatValue() {
            return 0;
        }
        @Override
        public int intValue() {
            return 0;
        }
        @Override
        public long longValue() {
            return 0;
        }
        @Override
        public int compareTo(GenericNumberType<T> o) {
            return 0;
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof GenericNumberType;
        }
    }

    @QueryEntity
    @SuppressWarnings("unchecked")
    public static class ItemType {
        Amount<SimpleType> prop;
        SimpleType2<Amount<SimpleType>> prop2;        
        SimpleType2<Amount> prop3;
        SimpleType2<?> prop4;
    }

    public static class Amount<T>{

    }

}
