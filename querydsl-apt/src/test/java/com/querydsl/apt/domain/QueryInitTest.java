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
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.core.annotations.QueryInit;
import com.querydsl.apt.domain.QQueryInitTest_PEntity;
import com.querydsl.apt.domain.QQueryInitTest_PEntity2;

public class QueryInitTest {

    private static final QQueryInitTest_PEntity e1 = QQueryInitTest_PEntity.pEntity;

    private static final QQueryInitTest_PEntity2 e2 = QQueryInitTest_PEntity2.pEntity2;

    @QueryEntity
    public static class PEntity {

        @QueryInit("e3.e4")
        public PEntity2 e2;

        @QueryInit({"e3.*", "e33.e4", "e333"})
        public PEntity2 e22;

        @QueryInit("*")
        public PEntity2 e222;

        public PEntity2 type;

        public PEntity2 inits;
    }

    @QueryEntity
    public static class PEntity2Super{

        public PEntity3 e333;

        @QueryInit("e4")
        public PEntity3 e3333;
    }

    @QueryEntity
    public static class PEntity2 extends PEntity2Super{

        public PEntity3 e3;

        public PEntity3 e33;
    }

    @QueryEntity
    public static class PEntity3{

        public PEntity4 e4;

        public PEntity4 e44;
    }

    @QueryEntity
    public static class PEntity4{

        public PEntity e1;

        public PEntity e11;
    }

    @Test
    public void Basic_Inits() {
        // e2
        assertNotNull(e1.e2);
        assertNotNull(e1.e2.e3.e4);
        assertNull(e1.e2.e33);
        assertNull(e1.e2.e3.e44);

        // e22
        assertNotNull(e1.e22.e33.e4);
        assertNull(e1.e22.e33.e44);
        assertNotNull(e1.e22.e333);
    }

    @Test
    public void Deep_Super_Inits() {
        assertNotNull(e1.e22._super.e333);
    }

    @Test
    public void Root_Super_Inits() {
        assertNotNull(e2.e3333.e4);
        assertNotNull(e2._super.e3333.e4);
    }

}
