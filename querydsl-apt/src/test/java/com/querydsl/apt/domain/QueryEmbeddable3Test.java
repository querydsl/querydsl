/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEntity;

public class QueryEmbeddable3Test {

    @QueryEntity
    public static class User {

        List<Complex<?>> rawList;

        List<Complex<String>> list;

        Set<Complex<String>> set;

        Collection<Complex<String>> collection;

        Map<String, Complex<String>> map;

        Map<String, Complex<String>> rawMap1;

        Map<String, Complex<?>> rawMap2;

        Map<?, Complex<String>> rawMap3;

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

    @Test
    public void user_rawList() {
        Assert.assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.rawList.any().getClass());
    }

    @Test
    public void user_list() {
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.list.any().getClass());
    }

    @Test
    public void user_set() {
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.set.any().getClass());
    }

    @Test
    public void user_collection() {
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.collection.any().getClass());
    }

    @Test
    public void user_map() {
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.map.get("XXX").getClass());
    }

    @Test
    public void user_rawMap1() {
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.rawMap1.get("XXX").getClass());
    }

    @Test
    public void user_rawMap2() {
        assertEquals(QQueryEmbeddable3Test_Complex.class, QQueryEmbeddable3Test_User.user.rawMap2.get("XXX").getClass());
    }

}
