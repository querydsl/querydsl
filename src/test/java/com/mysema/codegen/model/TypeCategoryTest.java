/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeCategoryTest {

    @Test
    public void IsSubCategoryOf() {
        assertTrue(TypeCategory.BOOLEAN.isSubCategoryOf(TypeCategory.COMPARABLE));
        assertTrue(TypeCategory.STRING.isSubCategoryOf(TypeCategory.COMPARABLE));
        assertTrue(TypeCategory.NUMERIC.isSubCategoryOf(TypeCategory.COMPARABLE));
        assertTrue(TypeCategory.COMPARABLE.isSubCategoryOf(TypeCategory.SIMPLE));
        assertFalse(TypeCategory.ENTITY.isSubCategoryOf(TypeCategory.SIMPLE));
    }
    
}
