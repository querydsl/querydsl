/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.SimplePath;

/**
 * EComparable extends EComparableBase to provide comparison methods.
 *
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked"})
public abstract class ComparableExpression<D extends Comparable> extends ComparableExpressionBase<D> {

    private static final long serialVersionUID = 5761359576767404270L;

    public ComparableExpression(Class<? extends D> type) {
        super(type);
    }
    
    @Override
    public ComparableExpression<D> as(Path<D> alias) {
        return ComparableOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }
    
    @Override
    public ComparableExpression<D> as(String alias) {
        return ComparableOperation.create(getType(),(Operator)Ops.ALIAS, this, new SimplePath(getType(), alias));
    }

    /**
     * Get a <code>from &lt; this &lt; to</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression between(D from, D to) {
        return BooleanOperation.create(Ops.BETWEEN, this, SimpleConstant.create(from), SimpleConstant.create(to));
    }

    /**
     * Get a <code>first &lt; this &lt; second</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression between(Expression<D> from, Expression<D> to) {
        return BooleanOperation.create(Ops.BETWEEN, this, from, to);
    }

    /**
     * Get a <code>this not between from and to</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression notBetween(D from, D to) {
        return between(from, to).not();
    }

    /**
     * Get a <code>this not between from and to</code> expression
     *
     * @param from
     * @param to
     * @return
     */
    public final BooleanExpression notBetween(Expression<D> from, Expression<D> to) {
        return between(from, to).not();
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression gt(D right) {
        return gt(SimpleConstant.create(right));
    }

    /**
     * Get a <code>this &gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression gt(Expression<D> right) {
        return BooleanOperation.create(Ops.AFTER, this, right);
    }

    /**
     * Get a <code>this &gt;= right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression goe(D right) {
        return goe(SimpleConstant.create(right));
    }

    /**
     * Get a <code>this &gt;= right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression goe(Expression<D> right) {
        return BooleanOperation.create(Ops.AOE, this, right);
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression lt(D right) {
        return lt(SimpleConstant.create(right));
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression lt(Expression<D> right) {
        return BooleanOperation.create(Ops.BEFORE, this, right);
    }

    /**
     * Get a <code>this &lt;= right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression loe(D right) {
        return BooleanOperation.create(Ops.BOE, this, SimpleConstant.create(right));
    }

    /**
     * Get a <code>this &lt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression loe(Expression<D> right) {
        return BooleanOperation.create(Ops.BOE, this, right);
    }

}
