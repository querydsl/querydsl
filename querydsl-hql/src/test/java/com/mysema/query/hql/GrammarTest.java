/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.mysema.query.functions.AggregationFunctions;

/**
 * HqlGrammarTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class GrammarTest {

    @Test
    public void noDuplicates() {
        Set<String> names = new HashSet<String>();
        for (Method method : AggregationFunctions.class.getDeclaredMethods()) {
            names.add(method.getName());
        }

        for (Method method : HQLGrammar.class.getDeclaredMethods()) {
            if (names.contains(method.getName())) {
                System.out.println(method.getName() + " already available.");
            }
        }
    }

}
