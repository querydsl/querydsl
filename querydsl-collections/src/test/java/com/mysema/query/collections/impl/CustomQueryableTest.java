/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.mysema.query.collections.eval.ColQueryTemplates;
import com.mysema.query.types.path.PString;

/**
 * CustomQueryableTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class CustomQueryableTest {

    private static final ColQueryTemplates templates = new ColQueryTemplates();
    
    private List<String> strings = Arrays.asList("1", "2", "3");

    private PString str = new PString("str");

    private SimpleIteratorSource source;

    @Before
    public void setUp() {
        source = new SimpleIteratorSource();
        source.add(str, strings);
    }

    @SuppressWarnings("unchecked")
    private CustomQueryable<?> query() {
        return new CustomQueryable(source, templates);
    }

    @Test
    public void test1() {
        assertEquals(strings, query().from(str).list(str));
        assertEquals("1", query().from(str).where(str.eq("1"))
                .uniqueResult(str));

    }
}
