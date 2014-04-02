/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.codegen.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;
import java.sql.Time;

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

    @Test
    public void Get() {
        assertEquals(TypeCategory.BOOLEAN, TypeCategory.get(Boolean.class.getName()));
        assertEquals(TypeCategory.STRING, TypeCategory.get(String.class.getName()));
        assertEquals(TypeCategory.DATE, TypeCategory.get(Date.class.getName()));
        assertEquals(TypeCategory.TIME, TypeCategory.get(Time.class.getName()));
    }

    @Test
    public void Java8() {
        assertEquals(TypeCategory.DATETIME, TypeCategory.get("java.time.Instant"));
        assertEquals(TypeCategory.DATE, TypeCategory.get("java.time.LocalDate"));
        assertEquals(TypeCategory.DATETIME, TypeCategory.get("java.time.LocalDateTime"));
        assertEquals(TypeCategory.TIME, TypeCategory.get("java.time.LocalTime"));
        assertEquals(TypeCategory.DATETIME, TypeCategory.get("java.time.OffsetDateTime"));
        assertEquals(TypeCategory.TIME, TypeCategory.get("java.time.OffsetTime"));
        assertEquals(TypeCategory.DATETIME, TypeCategory.get("java.time.ZonedDateTime"));
    }

}
