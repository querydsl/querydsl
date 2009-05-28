/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.utils;

import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.query.collections.JavaPatterns;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.eval.JaninoEvaluator;
import com.mysema.query.types.expr.Expr;

/**
 * EvaluatorUtils provides factory methods for Evaluator creation
 * 
 * @author tiwe
 * @version $Id$
 */
public class EvaluatorUtils {

    /**
     * 
     * @param patterns
     * @param sources
     * @param expr
     * @return
     */
    public static Evaluator create(JavaPatterns patterns,
            List<? extends Expr<?>> sources, Expr<?> expr) {
        if (sources.get(0) == expr) {
            return new Evaluator() {
                public <T> T evaluate(Object... args) {
                    return (T) args[0];
                }
            };
            // TODO : handle Path projection in special way
        } else {
            return new JaninoEvaluator(Assert.notNull(patterns), Assert
                    .notNull(sources), Assert.notNull(expr));
        }
    }

}
