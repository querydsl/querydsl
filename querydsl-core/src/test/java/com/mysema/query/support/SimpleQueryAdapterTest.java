package com.mysema.query.support;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import com.mysema.query.types.path.PString;


public class SimpleQueryAdapterTest {
    
    @Test
    public void test(){
        DummyQuery query = new DummyQuery();
        DummyProjectable projectable = new DummyProjectable();
        SimpleQueryAdapter simpleQuery = new SimpleQueryAdapter(query, projectable, new PString("a"));
        
        simpleQuery.count();
        simpleQuery.countDistinct();
        assertNotNull(simpleQuery.list());
        assertNotNull(simpleQuery.listDistinct());
        assertNotNull(simpleQuery.listDistinctResults());
        assertNotNull(simpleQuery.listResults());
        assertNull(simpleQuery.uniqueResult());
    }

}
