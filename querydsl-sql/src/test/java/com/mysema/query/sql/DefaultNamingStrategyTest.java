/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import static org.junit.Assert.*;

import org.junit.Test;

import com.mysema.query.codegen.ClassType;
import com.mysema.query.codegen.EntityType;
import com.mysema.query.codegen.TypeCategory;

public class DefaultNamingStrategyTest {

    private NamingStrategy namingStrategy = new DefaultNamingStrategy();

    @Test
    public void testGetClassName() {
        assertEquals("QUserData", namingStrategy.getClassName("Q", "user_data"));
    }

    @Test
    public void testGetPropertyName() {
        ClassType typeModel = new ClassType(TypeCategory.ENTITY, Object.class);
        EntityType entityModel = new EntityType("Q", typeModel);
        assertEquals("whileCol", namingStrategy.getPropertyName("while", "Q", entityModel));
        assertEquals("name", namingStrategy.getPropertyName("name", "Q", entityModel));
        assertEquals("userId", namingStrategy.getPropertyName("user_id", "Q", entityModel));
    }

}
