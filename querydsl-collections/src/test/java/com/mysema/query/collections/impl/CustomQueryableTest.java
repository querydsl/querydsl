/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.mysema.query.collections.ColQueryTemplates;
import com.mysema.query.collections.CustomQueryable;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PString;

/**
 * CustomQueryableTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class CustomQueryableTest {

    private static final ColQueryTemplates templates = new ColQueryTemplates();
    
    private final List<String> strings = Arrays.asList("1", "2", "3");

    private PString str = new PString("str");

    @SuppressWarnings("unchecked")
    private CustomQueryable<?> query() {
        return new CustomQueryable(templates){
            @Override
            protected Iterable getContent(Expr expr) {
                return strings;
            }                
        };
    }

    @Test
    public void test1() {
        assertEquals(strings, query().from(str).list(str));
        assertEquals("1", query().from(str).where(str.eq("1")).uniqueResult(str));

    }
}
