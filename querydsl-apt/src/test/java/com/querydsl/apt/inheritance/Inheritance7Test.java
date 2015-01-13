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

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.querydsl.core.annotations.QueryEntity;

public class Inheritance7Test {

    @QueryEntity
    public static class User {

    }

    @QueryEntity
    public static class SubCategory extends Category<SubCategory> {

    }

    @QueryEntity
    public static class Category<T extends Category<T>> implements Comparable<T>{

        private User owner;

        private T parent;

        private Set<T> children;

        public User getOwner() {
            return owner;
        }

        @Override
        public int compareTo(T o) {
            return 0;
        }
        
        public boolean equals(Object o) {
            return o == this;
        }
        
        public T getParent() {
            return parent;
        }

        public Set<T> getChildren() {
            return children;
        }

    }

    @QueryEntity
    public static class SubCategory2 extends Category<SubCategory2> {

    }

    @Test
    @Ignore
    public void Parent() {
        // FIXME
        assertEquals(Category.class, QInheritance7Test_Category.category.parent.getType());
        assertEquals(SubCategory.class, QInheritance7Test_SubCategory.subCategory.parent.getType());
        assertEquals(SubCategory2.class, QInheritance7Test_SubCategory2.subCategory2.parent.getType());
    }

    @Test
    @Ignore
    public void Children() {
        // FIXME
        assertEquals(Category.class, QInheritance7Test_Category.category.children.getElementType());
        assertEquals(SubCategory.class, QInheritance7Test_SubCategory.subCategory.children.getElementType());
        assertEquals(SubCategory2.class, QInheritance7Test_SubCategory2.subCategory2.children.getElementType());
    }

}
