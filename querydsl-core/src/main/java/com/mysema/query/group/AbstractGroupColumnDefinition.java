/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.types.Expression;

/**
 * A base class for GroupColumnDefinitions
 * @author sasa
 *
 * @param <T>
 * @param <R>
 */
public abstract class AbstractGroupColumnDefinition<T, R> implements GroupColumnDefinition<T, R> {
    
    private final Expression<T> expr;
    
    public AbstractGroupColumnDefinition(Expression<T> expr) {
        this.expr = expr;
    }
    
    @Override
    public Expression<T> getExpression() {
        return expr;
    }
    
    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + expr + ")";
    }
}