/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;

/**
 * ComparableExpression extends ComparableExpressionBase to provide comparison methods.
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@SuppressWarnings({"unchecked"})
public abstract class ComparableExpression<T extends Comparable> extends ComparableExpressionBase<T> {

    private static final long serialVersionUID = 5761359576767404270L;

    public ComparableExpression(Class<? extends T> type) {
        super(type);
    }
    
    @Override
    public ComparableExpression<T> as(Path<T> alias) {
        return ComparableOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }
    
    @Override
    public ComparableExpression<T> as(String alias) {
        return as(new PathImpl<T>(getType(), alias));
    }

    /**
     * Get a <code>from &lt; this &lt; to</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression between(T from, T to) {
        return BooleanOperation.create(Ops.BETWEEN, this, new ConstantImpl<T>(from), new ConstantImpl<T>(to));
    }

    /**
     * Get a <code>first &lt; this &lt; second</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression between(Expression<T> from, Expression<T> to) {
        return BooleanOperation.create(Ops.BETWEEN, this, from, to);
    }

    /**
     * Get a <code>this not between from and to</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression notBetween(T from, T to) {
        return between(from, to).not();
    }

    /**
     * Get a <code>this not between from and to</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression notBetween(Expression<T> from, Expression<T> to) {
        return between(from, to).not();
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression gt(T right) {
        return gt(new ConstantImpl(right));
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression gt(Expression<T> right) {
        return BooleanOperation.create(Ops.AFTER, this, right);
    }

    /**
     * Get a <code>this &gt;= right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression goe(T right) {
        return goe(new ConstantImpl(right));
    }

    /**
     * Get a <code>this &gt;= right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression goe(Expression<T> right) {
        return BooleanOperation.create(Ops.AOE, this, right);
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression lt(T right) {
        return lt(new ConstantImpl(right));
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression lt(Expression<T> right) {
        return BooleanOperation.create(Ops.BEFORE, this, right);
    }

    /**
     * Get a <code>this &lt;= right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression loe(T right) {
        return BooleanOperation.create(Ops.BOE, this, new ConstantImpl(right));
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression loe(Expression<T> right) {
        return BooleanOperation.create(Ops.BOE, this, right);
    }

}
