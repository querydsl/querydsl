/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Expression;

/**
 * TemporalExpression is a supertype for Date/Time related types
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@SuppressWarnings({"unchecked"})
public abstract class TemporalExpression<T extends Comparable> extends ComparableExpression<T> {

    private static final long serialVersionUID = 1137918766051524298L;

    public TemporalExpression(Class<? extends T> type) {
        super(type);
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression after(T right) {
        return gt(right);
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression after(Expression<T> right) {
        return gt(right);
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression before(T right) {
        return lt(right);
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression before(Expression<T> right) {
        return lt(right);
    }

}
