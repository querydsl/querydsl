package com.mysema.query.codegen;

import static org.junit.Assert.*;

import org.junit.Test;


public class TypeCategoryTest {

    @Test
    public void test(){
        assertFalse(TypeCategory.ENTITY.isSubCategoryOf(TypeCategory.SIMPLE));
    }
}
