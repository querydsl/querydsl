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
package com.querydsl.apt.domain;

import static org.junit.Assert.assertNotNull;

import java.util.Date;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryTransient;
import com.querydsl.apt.domain.QGenericTest_GenericType;
import com.querydsl.apt.domain.QGenericTest_GenericType2;
import com.querydsl.apt.domain.QGenericTest_ItemType;
import com.querydsl.apt.domain.rel.SimpleType;
import com.querydsl.apt.domain.rel.SimpleType2;

import org.junit.Test;

public class GenericTest extends AbstractTest {

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

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException {
        assertNotNull(QGenericTest_ItemType.itemType);
        assertNotNull(QGenericTest_GenericType.genericType);
        assertNotNull(QGenericTest_GenericType2.genericType2);

        start(QGenericTest_GenericType.class, QGenericTest_GenericType.genericType);
        matchType(ItemType.class, "itemType");

        start(QGenericTest_GenericType2.class, QGenericTest_GenericType2.genericType2);
        matchType(ItemType.class, "itemType");
        matchType(GenericSimpleType.class, "prop1");
        matchType(GenericSimpleType.class, "prop2");
        matchType(GenericSimpleType.class, "prop3");
        matchType(GenericComparableType.class, "comp1");
        matchType(GenericComparableType.class, "comp2");
        matchType(GenericComparableType.class, "comp3");
        assertMissing("num1");
        matchType(GenericNumberType.class, "num2");
        matchType(GenericNumberType.class, "num3");
    }
}
