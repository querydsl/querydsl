/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.eval;

import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.Expr;

/**
 * EvaluatorFactory provides factory methods for Evaluator creation
 * 
 * @author tiwe
 * @version $Id$
 */
public final class EvaluatorFactory {
    
    private EvaluatorFactory(){}

    /**
     * 
     * @param patterns
     * @param sources
     * @param expr
     * @return
     */
    public static Evaluator create(ColQueryPatterns patterns,
            List<? extends Expr<?>> sources, Expr<?> expr) {
        if (sources.get(0) == expr) {
            return new Evaluator() {
                @SuppressWarnings("unchecked")
                public <T> T evaluate(Object... args) {
                    return (T) args[0];
                }
            };
        } else {
            return new JaninoEvaluator(
                    Assert.notNull(patterns),
                    Assert.notNull(sources), 
                    Assert.notNull(expr));
        }
    }

}
