/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class TypeCategoryTest {

    @Test
    public void testIsSubCategoryOf() {
        assertTrue(TypeCategory.BOOLEAN.isSubCategoryOf(TypeCategory.COMPARABLE));
        assertTrue(TypeCategory.STRING.isSubCategoryOf(TypeCategory.COMPARABLE));
        assertTrue(TypeCategory.NUMERIC.isSubCategoryOf(TypeCategory.COMPARABLE));
        assertTrue(TypeCategory.COMPARABLE.isSubCategoryOf(TypeCategory.SIMPLE));
    }

}
