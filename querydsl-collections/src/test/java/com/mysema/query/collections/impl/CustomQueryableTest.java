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

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.collections.ColQueryTemplates;
import com.mysema.query.collections.CustomQueryable;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.Path;

/**
 * CustomQueryableTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class CustomQueryableTest {
    
    private final List<String> strings = Arrays.asList("1", "2", "3");

    private static final EvaluatorFactory evaluatorFactory = EvaluatorFactory.DEFAULT;
    
    private PString str = new PString("str");

    @SuppressWarnings("unchecked")
    private CustomQueryable<?> query() {        
        return new CustomQueryable(new DefaultQueryMetadata(), evaluatorFactory){
            @Override
            protected Iterable getContent(Path expr) {
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
