/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import static com.mysema.query.alias.Alias.alias;
import static com.mysema.query.collections.MiniApi.from;

import org.junit.Test;

import com.mysema.query.collections.domain.Cat;
import com.mysema.query.functions.MathFunctions;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.Expr;

/**
 * MathFunctionTest provides
 * 
 * @author tiwe
 * @version $Id$
 */
public class MathFunctionTest extends AbstractQueryTest {

    @Test
    public void test() {
        Cat c = alias(Cat.class, "c");
        Expr<Integer> i = ENumber.create(1);
        Expr<Double> d = ENumber.create(1.0);
        from(c, cats).list( 
                MathFunctions.acos(d),
                MathFunctions.asin(d), 
                MathFunctions.atan(d), 
                MathFunctions.cos(d),
                MathFunctions.sin(d), 
                ENumber.random(), 
                MathFunctions.pow(d, d),
                ENumber.min(i, i), 
                ENumber.max(i, i),
                MathFunctions.log10(d), 
                MathFunctions.log(d), 
                MathFunctions.exp(d)).iterator();

    }

}
