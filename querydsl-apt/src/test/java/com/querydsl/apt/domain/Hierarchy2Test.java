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

import javax.persistence.*;

import org.junit.Ignore;

@Ignore
public class Hierarchy2Test {

    @MappedSuperclass
    public abstract static class SomeMappedSuperClassHavingMyEmbeddable {

        @Embedded
        MyEmbeddable embeddable;
    }

    @Entity
    public static class A {

        @OneToOne
        SomeEntity entry;

        @Embedded
        MyEmbeddable myEmbeddable;
    }

    @Entity
    public static class SomeEntity extends SomeMappedSuperClassHavingMyEmbeddable {
    }

    @Embeddable
    public static class MyEmbeddable implements Comparable<MyEmbeddable> {

        @Basic
        int foo;

        public int compareTo(MyEmbeddable individualToCompare) {
            return -1;
        }

        public boolean equals(Object o) {
            return o == this;
        }
    }

}
