/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import javax.annotation.Nullable;

import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Predicate;

/**
 * BooleanExpression represents boolean expressions
 *
 * @author tiwe
 * @see java.lang.Boolean
 *
 */
public abstract class BooleanExpression extends ComparableExpression<Boolean> implements Predicate{

    private static final long serialVersionUID = 3797956062512074164L;

    @Nullable
    private volatile BooleanExpression eqTrue, eqFalse;

    /**
     * Return the intersection of the given Boolean expressions
     *
     * @param exprs
     * @return
     */
    @Nullable
    public static BooleanExpression allOf(BooleanExpression... exprs){
        BooleanExpression rv = null;
        for (BooleanExpression b : exprs) {
            rv = rv == null ? b : rv.and(b);
        }
        return rv;
    }

    /**
     * Return the union of the given Boolean expressions
     *
     * @param exprs
     * @return
     */
    @Nullable
    public static BooleanExpression anyOf(BooleanExpression... exprs){
        BooleanExpression rv = null;
        for (BooleanExpression b : exprs) {
            rv = rv == null ? b : rv.or(b);
        }
        return rv;
    }

    @Nullable
    private volatile BooleanExpression not;

    public BooleanExpression() {
        super(Boolean.class);
    }

    @Override
    public BooleanExpression as(Path<Boolean> alias) {
        return BooleanOperation.create(Ops.ALIAS, this, alias);
    }

    @Override
    public BooleanExpression as(String alias) {
        return as(new PathImpl<Boolean>(Boolean.class, alias));
    }

    /**
     * Get an intersection of this and the given expression
     *
     * @param right right hand side of the union
     * @return this && right
     */
    public BooleanExpression and(@Nullable Predicate right) {
        if (right != null) {
            return BooleanOperation.create(Ops.AND, this, right);
        } else {
            return this;
        }
    }

    /**
     * Get an intersection of this and the union of the given predicates
     *
     * @param predicates
     * @return
     */
    public BooleanExpression andAnyOf(Predicate... predicates){
        return and(ExpressionUtils.anyOf(predicates));
    }

    /**
     * Get a negation of this boolean expression
     *
     * @return !this
     */
    public BooleanExpression not() {
        if (not == null) {
            not = BooleanOperation.create(Ops.NOT, this);
        }
        return not;
    }

    /**
     * Get a union of this and the given expression
     *
     * @param right right hand side of the union
     * @return this || right
     */
    public BooleanExpression or(@Nullable Predicate right) {
        if (right != null) {
            return BooleanOperation.create(Ops.OR, this, right);
        } else {
            return this;
        }
    }

    /**
     * Get a union of this and the intersection of the given predicates
     *
     * @param predicates
     * @return
     */
    public BooleanExpression orAllOf(Predicate... predicates){
        return or(ExpressionUtils.allOf(predicates));
    }

    /**
     * Get a this == true expression
     *
     * @return
     */
    public BooleanExpression isTrue(){
        return eq(true);
    }

    /**
     * Get a this == false expression
     *
     * @return
     */
    public BooleanExpression isFalse(){
        return eq(false);
    }

    @Override
    public BooleanExpression eq(Boolean right) {
        if (right.booleanValue()) {
            if (eqTrue == null) {
                eqTrue = super.eq(true);
            }
            return eqTrue;
        } else {
            if (eqFalse == null) {
                eqFalse = super.eq(false);
            }
            return eqFalse;
        }
    }
}
