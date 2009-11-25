/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import static org.junit.Assert.assertFalse;

import org.junit.Test;



public class TypeCategoryTest {

    @Test
    public void test(){
        assertFalse(TypeCategory.ENTITY.isSubCategoryOf(TypeCategory.SIMPLE));
    }
}
