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
package com.querydsl.apt.inheritance;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;
import com.querydsl.apt.domain.AbstractTest;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.path.StringPath;

public class Inheritance4Test extends AbstractTest {

    @QueryEntity
    public class EntityWithComparable {
        private Comparable<?> field;

        public Comparable<?> getField() {
            return field;
        }

    }

    @QueryEntity
    public class EntityWithNumber extends EntityWithComparable {
        private Long field;

        public Long getField() {
            return field;
        }

    }

    @QueryEntity
    public class EntityWithString extends EntityWithComparable{
        private String field;

        public String getField() {
            return field;
        }

    }

    @Test
    public void test() throws IllegalAccessException, NoSuchFieldException{
        start(QInheritance4Test_EntityWithComparable.class, QInheritance4Test_EntityWithComparable.entityWithComparable);
        match(SimplePath.class, "field");
        matchType(Comparable.class, "field");

        start(QInheritance4Test_EntityWithNumber.class, QInheritance4Test_EntityWithNumber.entityWithNumber);
        match(NumberPath.class, "field");
        matchType(Long.class, "field");

        start(QInheritance4Test_EntityWithString.class, QInheritance4Test_EntityWithString.entityWithString);
        match(StringPath.class, "field");
        matchType(String.class, "field");

    }
}
