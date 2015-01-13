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
package com.querydsl.core.alias;

import org.junit.Test;

public class ComparablePropertyTest {

    public static class Entity{

        private ComparableType property;

        public ComparableType getProperty() {
            return property;
        }

        public void setProperty(ComparableType property) {
            this.property = property;
        }

    }

    public static class ComparableType implements Comparable<ComparableType>{

        @Override
        public int compareTo(ComparableType o) {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof ComparableType) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

    }

    @Test
    public void test() {
        Entity entity = Alias.alias(Entity.class);
        Alias.$(entity.getProperty());
    }

}
