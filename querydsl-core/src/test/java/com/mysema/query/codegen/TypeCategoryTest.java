/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.types.TypeCategory;


public class TypeCategoryTest {

    @Test
    public void test(){
        assertFalse(TypeCategory.ENTITY.isSubCategoryOf(TypeCategory.SIMPLE));
    }
}
