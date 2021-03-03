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

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.querydsl.core.annotations.QueryEmbeddable;
import com.querydsl.core.annotations.QueryEntity;

public class ComparableTest {

    @QueryEntity
    public static class CustomComparableHolder {

        private CustomComparable customComparable;

        public CustomComparable getCustomComparable() {
            return customComparable;
        }

        public void setCustomComparable(CustomComparable customComparable) {
            this.customComparable = customComparable;
        }
    }

    @QueryEmbeddable
    public static class CustomComparable2 {


        private CustomComparable customComparable;

        public CustomComparable getCustomComparable() {
            return customComparable;
        }

        public void setCustomComparable(CustomComparable customComparable) {
            this.customComparable = customComparable;
        }

    }

    public static class CustomComparable implements Comparable<CustomComparable> {

        @Override
        public int compareTo(CustomComparable o) {
            return 0;
        }

        public boolean equals(Object o) {
            return o == this;
        }

    }

    @Test
    public void customComparable_is_properly_handled() {
        assertNotNull(QComparableTest_CustomComparableHolder.customComparableHolder.customComparable.asc());
    }

}
