/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * A base class for GroupExpressions
 * @author sasa
 *
 * @param <T>
 * @param <R>
 */
public abstract class AbstractGroupExpression<T, R> extends SimpleExpression<R> implements GroupExpression<T, R> {
    
    private static final long serialVersionUID = 1509709546966783160L;
    
    private final Expression<T> expr;
    
    @SuppressWarnings("unchecked")
    public AbstractGroupExpression(Class<? super R> type, Expression<T> expr) {
        super((Class)type);
        Assert.notNull(expr, "expr");        
        this.expr = expr;
    }
    
    @Override
    public Expression<T> getExpression() {
        return expr;
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return expr.accept(v, context);
    }
    
    @Override
    public boolean equals(Object o) {
        if (getClass().equals(o.getClass())) {
            return ((GroupExpression<?,?>)o).getExpression().equals(expr);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode() {
        return expr.hashCode();
    }
    
}