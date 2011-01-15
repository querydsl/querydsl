/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import javax.annotation.Nullable;

import com.mysema.query.types.Operator;
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

    /**
     * Return the intersection of the given Boolean expressions
     * 
     * @param exprs
     * @return
     */
    @Nullable
    public static BooleanExpression allOf(BooleanExpression... exprs){
        BooleanExpression rv = null;
        for (BooleanExpression b : exprs){
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
        for (BooleanExpression b : exprs){
            rv = rv == null ? b : rv.or(b);
        }
        return rv;
    }

    @Nullable
    private volatile BooleanExpression not;

    public BooleanExpression() {
        super(Boolean.class);
    }

    @SuppressWarnings("unchecked")
    @Override
    public BooleanExpression as(Path<Boolean> alias) {
        return BooleanOperation.create((Operator)Ops.ALIAS, this, alias);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public BooleanExpression as(String alias) {
        return BooleanOperation.create((Operator)Ops.ALIAS, this, new PathImpl(getType(), alias));
    }

    /**
     * Get an intersection of this and the given expression
     *
     * @param right right hand side of the union
     * @return this && right
     */
    public BooleanExpression and(@Nullable Predicate right) {
        if (right != null){
            return BooleanOperation.create(Ops.AND, this, right);
        }else{
            return this;
        }
    }

    /**
     * Get a negation of this boolean expression
     *
     * @return !this
     */
    public BooleanExpression not() {
        if (not == null){
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
        if (right != null){
            return BooleanOperation.create(Ops.OR, this, right);
        }else{
            return this;
        }

    }
}
