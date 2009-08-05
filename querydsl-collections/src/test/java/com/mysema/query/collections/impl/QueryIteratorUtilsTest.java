/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.junit.Test;

import com.mysema.query.collections.eval.ColQueryPatterns;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.eval.EvaluatorFactory;

/**
 * QueryIteratorUtilsTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class QueryIteratorUtilsTest extends AbstractQueryTest {

    @SuppressWarnings("unchecked")
    @Test
    public void projectToMap() {
        Evaluator ev = EvaluatorFactory.create(ColQueryPatterns.DEFAULT, Arrays
                .asList(cat), cat.name);
        Map<?, ?> map = QueryIteratorUtils.projectToMap(cats.iterator(), ev);
        for (Map.Entry<?, ?> e : map.entrySet()) {
            assertTrue(e.getKey() instanceof String);
            assertTrue(e.getValue() instanceof Collection);
        }
    }
}
