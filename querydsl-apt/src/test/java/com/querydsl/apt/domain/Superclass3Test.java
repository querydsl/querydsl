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

import org.junit.Assert;
import org.junit.Test;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.QSuperclass3Test_Subtype;

public class Superclass3Test {

    @QueryEntity
    public static class Subtype extends DefaultQueryMetadata {

        private static final long serialVersionUID = -218949941713252847L;

    }

    @Test
    public void test() {
        Assert.assertNotNull(QSuperclass3Test_Subtype.subtype.distinct);
    }
}
