/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.jcip.annotations.Immutable;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.commons.lang.Assert;
import com.mysema.query.collections.ColQueryTemplates;
import com.mysema.query.types.expr.Expr;

/**
 * JaninoEvaluator is a Janino compiler based Evaluator implementation
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
class JaninoEvaluator extends Evaluator {

    private final ExpressionEvaluator ev;

    private final Expr<?> projection;

    private final List<? extends Expr<?>> sources;

    public JaninoEvaluator(ExpressionEvaluator ev, List<? extends Expr<?>> sources, Expr<?> projection) {
        this.ev = Assert.notNull(ev);
        this.sources = sources;
        this.projection = projection;
    }

    public JaninoEvaluator(ColQueryTemplates patterns, List<? extends Expr<?>> sources, Expr<?> projection) {
        try {
            Class<?> type = projection.getType() != null ? projection.getType() : Object.class;
            this.ev = new ColQuerySerializer(patterns).handle(projection).createExpressionEvaluator(sources, type);
            this.sources = sources;
            this.projection = projection;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T evaluate(Object... args) {
        try {
            return (T) ev.evaluate(args);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof NullPointerException) {
                for (int i = 0; i < args.length; i++) {
                    if (args[i] == null) {
                        throw new IllegalArgumentException("null for " + sources.get(i));
                    }
                }
                throw new IllegalArgumentException("null in " + projection, e);
            } else {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

}
