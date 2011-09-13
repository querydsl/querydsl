/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.mysema.query.types.path.StringPath;


public class SimpleProjectableAdapterTest {
    
    @Test
    @SuppressWarnings("unchecked")
    public void test(){
        DummyQuery query = new DummyQuery();
        DummyProjectable projectable = new DummyProjectable();
        SimpleProjectableAdapter simpleQuery = new SimpleProjectableAdapter(query, projectable, new StringPath("a"));
        
        simpleQuery.count();
        simpleQuery.countDistinct();
        assertNotNull(simpleQuery.list());
        assertNotNull(simpleQuery.listDistinct());
        assertNotNull(simpleQuery.listDistinctResults());
        assertNotNull(simpleQuery.listResults());
        assertNull(simpleQuery.uniqueResult());
    }

}
