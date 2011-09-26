/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;

/**
 * A base class for GroupDefinitions
 * @author sasa
 *
 * @param <T>
 * @param <R>
 */
public abstract class AbstractGroupDefinition<T, R> implements GroupDefinition<T, R> {
    
    private final Expression<T> expr;
    
    public AbstractGroupDefinition(Expression<T> expr) {
        Assert.notNull(expr, "expr");
        
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