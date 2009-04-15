/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import static com.mysema.query.collections.MiniApi.from;
import static com.mysema.query.grammar.GrammarWithAlias.alias;

import org.junit.Test;

import com.mysema.query.collections.Domain.Cat;
import com.mysema.query.grammar.QMath;
import com.mysema.query.grammar.types.Expr;

/**
 * MathFunctionTest provides
 *
 * @author tiwe
 * @version $Id$
 */
public class MathFunctionTest extends AbstractQueryTest {
    
    @Test
    public void test(){
        Cat c = alias(Cat.class,"c");
        Expr<Integer> i = new Expr.EConstant<Integer>(1);
        Expr<Double> d = new Expr.EConstant<Double>(1.0);
        from(c, cats)
        .list(
                QMath.abs(i),
                QMath.acos(d),
                QMath.asin(d),
                QMath.atan(d),
                QMath.ceil(d),
                QMath.cos(d),
                QMath.tan(d),
                QMath.sqrt(i),
                QMath.sin(d),
                QMath.round(d),
                QMath.random(),
                QMath.pow(d,d),
                QMath.min(i,i),
                QMath.max(i,i),
//                QMath.mod(i,i),
                QMath.log10(d),
                QMath.log(d),
                QMath.floor(d),
                QMath.exp(d)).iterator();
          
    }

}
