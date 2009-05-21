/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.utils;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.collections.AbstractQueryTest;
import com.mysema.query.collections.JavaOps;
import com.mysema.query.collections.eval.Evaluator;


/**
 * QueryIteratorUtilsTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class QueryIteratorUtilsTest extends AbstractQueryTest{

    @Test
    public void projectToMap(){
        Evaluator ev = EvaluatorUtils.create(JavaOps.DEFAULT, Arrays.asList(cat), cat.name);
        Map<?,?> map = QueryIteratorUtils.projectToMap(cats.iterator(), ev); 
        for (Map.Entry<?, ?> e : map.entrySet()){
            assertTrue(e.getKey() instanceof String);
            assertTrue(e.getValue() instanceof Collection);
        }
    }
}
