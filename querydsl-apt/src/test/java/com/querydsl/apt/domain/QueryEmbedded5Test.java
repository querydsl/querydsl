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

import org.junit.Test;

import com.querydsl.core.annotations.QueryEmbedded;
import com.querydsl.core.annotations.QueryEntity;

public class QueryEmbedded5Test {

    @QueryEntity
    public static class User {

        @QueryEmbedded
        List<Complex<?>> rawList;

        @QueryEmbedded
        List<Complex<String>> list;

        @QueryEmbedded
        Set<Complex<String>> set;

        @QueryEmbedded
        Collection<Complex<String>> collection;

        @QueryEmbedded
        Map<String, Complex<String>> map;

        @QueryEmbedded
        Map<String, Complex<String>> rawMap1;

        @QueryEmbedded
        Map<String, Complex<?>> rawMap2;

        @QueryEmbedded
        Map<?, Complex<String>> rawMap3;

    }


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
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.rawList.any().getClass());
    }

    @Test
    public void user_list() {
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.list.any().getClass());
    }

    @Test
    public void user_set() {
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.set.any().getClass());
    }

    @Test
    public void user_collection() {
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.collection.any().getClass());
    }

    @Test
    public void user_map() {
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.map.get("XXX").getClass());
    }

    @Test
    public void user_rawMap1() {
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.rawMap1.get("XXX").getClass());
    }

    @Test
    public void user_rawMap2() {
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.rawMap2.get("XXX").getClass());
    }

    @Test
    public void user_rawMap3() {
        assertEquals(QQueryEmbedded5Test_Complex.class, QQueryEmbedded5Test_User.user.rawMap3.get("XXX").getClass());
    }
}
