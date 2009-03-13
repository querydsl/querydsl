/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.utils;

import java.util.List;

import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.eval.JaninoEvaluator;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;

/**
 * EvaluatorUtils provides
 *
 * @author tiwe
 * @version $Id$
 */
public class EvaluatorUtils {
    
    public static Evaluator create(JavaOps ops, List<? extends Expr<?>> sources, Expr<?> expr){
        return new JaninoEvaluator(ops, sources, expr);
    }
}
