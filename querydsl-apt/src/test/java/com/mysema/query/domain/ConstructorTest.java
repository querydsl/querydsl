/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.mysema.query.annotations.QueryEntity;
import com.mysema.query.annotations.QuerySupertype;

public class ConstructorTest {

    @QuerySupertype
    public static class CategorySuperclass{

    }

    @QueryEntity
    public static class Category<T extends Category<T>> extends CategorySuperclass{

        public Category(int i){}

    }

    @QueryEntity
    public static class ClassWithConstructor{

        public ClassWithConstructor(){}

    }

    @Test
    public void Classes_are_available(){
        assertNotNull(QConstructorTest_CategorySuperclass.class);
        assertNotNull(QConstructorTest_Category.class);
        assertNotNull(QConstructorTest_ClassWithConstructor.class);
    }
    
    @Test
    public void Category_Super_Reference_is_Correct(){
        assertEquals(QConstructorTest_CategorySuperclass.class, QConstructorTest_Category.category._super.getClass());
        assertEquals(Category.class, QConstructorTest_Category.category._super.getType());
    }

}
