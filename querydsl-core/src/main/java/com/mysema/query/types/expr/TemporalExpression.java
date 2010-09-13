/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Expression;

/**
 * EDateOrTime is a supertype for Date/Time related types
 *
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked"})
public abstract class TemporalExpression<D extends Comparable> extends ComparableExpression<D> {

    private static final long serialVersionUID = 1137918766051524298L;

    public TemporalExpression(Class<? extends D> type) {
        super(type);
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression after(D right) {
        return gt(right);
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression after(Expression<D> right) {
        return gt(right);
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression before(D right) {
        return lt(right);
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right
     * @return
     */
    public BooleanExpression before(Expression<D> right) {
        return lt(right);
    }

}
