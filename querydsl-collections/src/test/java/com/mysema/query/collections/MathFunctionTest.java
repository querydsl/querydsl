/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.alias.GrammarWithAlias.alias;
import static com.mysema.query.collections.MiniApi.from;

import org.junit.Test;

import com.mysema.query.collections.domain.Cat;
import com.mysema.query.functions.MathFunctions;
import com.mysema.query.types.expr.EConstant;
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
        Expr<Integer> i = EConstant.create(1);
        Expr<Double> d = EConstant.create(1.0);
        from(c, cats).list(MathFunctions.abs(i), MathFunctions.acos(d),
                MathFunctions.asin(d), MathFunctions.atan(d),
                MathFunctions.ceil(d), MathFunctions.cos(d),
                MathFunctions.tan(d), MathFunctions.sqrt(i),
                MathFunctions.sin(d), MathFunctions.round(d),
                MathFunctions.random(), MathFunctions.pow(d, d),
                MathFunctions.min(i, i), MathFunctions.max(i, i),
                // QMath.mod(i,i),
                MathFunctions.log10(d), MathFunctions.log(d),
                MathFunctions.floor(d), MathFunctions.exp(d)).iterator();

    }

}
