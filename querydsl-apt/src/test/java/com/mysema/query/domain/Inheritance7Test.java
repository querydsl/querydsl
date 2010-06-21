package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;

public class Inheritance7Test {

    @QueryEntity
    public static class User{

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
    public void parent(){
        // FIXME
        assertEquals(Category.class, QInheritance7Test_Category.category.parent.getType());
        assertEquals(SubCategory.class, QInheritance7Test_SubCategory.subCategory.parent.getType());
        assertEquals(SubCategory2.class, QInheritance7Test_SubCategory2.subCategory2.parent.getType());
    }

    @Test
    @Ignore
    public void children(){
        // FIXME
        assertEquals(Category.class, QInheritance7Test_Category.category.children.getElementType());
        assertEquals(SubCategory.class, QInheritance7Test_SubCategory.subCategory.children.getElementType());
        assertEquals(SubCategory2.class, QInheritance7Test_SubCategory2.subCategory2.children.getElementType());
    }

}
