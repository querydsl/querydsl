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

import java.io.Serializable;

import javax.persistence.Entity;

import org.junit.Ignore;

@Ignore
public class AbstractPropertiesTest {

    public static abstract class GenericEntity<K extends Serializable & Comparable<K>> {

        private static final long serialVersionUID = -3988499137919577054L;

        public abstract K getId();

        public abstract void setId(K id);

    }

    @Entity
    public static class TestEntity extends GenericEntity<Integer> {

        private static final long serialVersionUID = 1803671157183603979L;

        private Integer id;

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public void setId(Integer id) {
            this.id = id;
        }

    }

    // TODO : tests
}
