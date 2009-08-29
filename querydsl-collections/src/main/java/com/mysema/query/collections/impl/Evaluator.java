/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.util.List;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.collections.ColQueryTemplates;
import com.mysema.query.types.expr.Expr;

/**
 * Evaluator defines an interface for evaluating Querydsl expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Evaluator {
    
    public static Evaluator create(ExpressionEvaluator ev, List<? extends Expr<?>> sources, Expr<?> projection) {
        return new JaninoEvaluator(ev, sources, projection);
    }

    public static Evaluator create(ColQueryTemplates patterns, List<? extends Expr<?>> sources, Expr<?> projection) {
        return new JaninoEvaluator(patterns, sources, projection);
    }

    public abstract <T> T evaluate(Object... args);

}
