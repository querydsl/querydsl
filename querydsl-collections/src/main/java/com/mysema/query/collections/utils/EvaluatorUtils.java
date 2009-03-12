/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.utils;

import java.util.List;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.JavaSerializer;
import com.mysema.query.grammar.types.Expr;

/**
 * EvaluatorUtils provides
 *
 * @author tiwe
 * @version $Id$
 */
public class EvaluatorUtils {

    public static ExpressionEvaluator create(JavaOps ops, List<Expr<?>> sources, Expr<?> expr){
        try {
            return new JavaSerializer(ops).handle(expr).createExpressionEvaluator(sources, expr);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
