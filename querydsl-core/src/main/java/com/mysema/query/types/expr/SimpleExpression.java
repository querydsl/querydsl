/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Templates;
import com.mysema.query.types.ToStringVisitor;
import com.mysema.query.types.path.SimplePath;

/**
 * ESimple is the base class for Expr implementations. It provides default implementations
 * for most of the abstract methods in {@link Expression}
 *
 * @author tiwe
 *
 * @param <D>
 */
public abstract class SimpleExpression<D> implements Expression<D> {

    private static final long serialVersionUID = -4405387187738167105L;

    @Nullable
    private volatile NumberExpression<Long> count;

    @Nullable
    private volatile NumberExpression<Long> countDistinct;

    @Nullable
    private volatile BooleanExpression isnull, isnotnull;
    
    protected final boolean primitive;
    
    @Nullable
    private volatile String toString;

    private final Class<? extends D> type;

    public SimpleExpression(Class<? extends D> type) {
        this.type = Assert.notNull(type,"type");
        this.primitive = type.isPrimitive()
            || Number.class.isAssignableFrom(type)
            || Boolean.class.equals(type)
            || Character.class.equals(type);
    }
    
    /**
     * Create a <code>this is not null</code> expression
     *
     * @return
     */
    public BooleanExpression isNotNull() {
        if (isnotnull == null) {
            isnotnull = BooleanOperation.create(Ops.IS_NOT_NULL, this);
        }
        return isnotnull;
    }

    /**
     * Create a <code>this is null</code> expression
     *
     * @return
     */
    public BooleanExpression isNull() {
        if (isnull == null) {
            isnull = BooleanOperation.create(Ops.IS_NULL, this);
        }
        return isnull;
    }


    /**
     * Create an alias for the expression
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public SimpleExpression<D> as(Path<D> alias) {
        return SimpleOperation.create(getType(),(Operator)Ops.ALIAS, this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public SimpleExpression<D> as(String alias) {
        return SimpleOperation.create(getType(),(Operator)Ops.ALIAS, this, new SimplePath<D>(getType(), alias));
    }

    /**
     * Used for safe casts from Path, SubQuery, Operation and Custom to Expr
     *
     * @return
     */
    public final SimpleExpression<D> asExpr(){
        return this;
    }


    /**
     * Get the <code>count(this)</code> expression
     *
     * @return count(this)
     */
    public NumberExpression<Long> count(){
        if (count == null){
            count = NumberOperation.create(Long.class, Ops.AggOps.COUNT_AGG, this);
        }
        return count;
    }


    /**
     * Get the <code>count(distinct this)</code> expression
     *
     * @return count(distinct this)
     */
    public NumberExpression<Long> countDistinct(){
        if (countDistinct == null){
          countDistinct = NumberOperation.create(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, this);
        }
        return countDistinct;
    }

    /**
     * Get a <code>this == right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression eq(D right) {
        return eq(SimpleConstant.create(right));
    }

    /**
     * Get a <code>this == right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression eq(Expression<? super D> right) {
        if (primitive) {
            return BooleanOperation.create(Ops.EQ_PRIMITIVE, this, right);
        } else {
            return BooleanOperation.create(Ops.EQ_OBJECT, this, right);
        }
    }

    /**
     * Get the Java type for this expression
     *
     * @return
     */
    public final Class<? extends D> getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    /**
     * Get a <code>this in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(Collection<? extends D> right) {
        if (right.size() == 1){
            return eq(right.iterator().next());
        }else{
            return BooleanOperation.create(Ops.IN, this, SimpleConstant.create(right));
        }
    }
    
    /**
     * Get a <code>this in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(D... right) {
        if (right.length == 1){
            return eq(right[0]);
        }else{
            return BooleanOperation.create(Ops.IN, this, SimpleConstant.create(Arrays.asList(right)));
        }
    }

    
    /**
     * Get a <code>this in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression in(CollectionExpression<?,? extends D> right) {
        return BooleanOperation.create(Ops.IN, this, (Expression<?>)right);
    }

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression ne(D right) {
        return ne(SimpleConstant.create(right));
    }

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression ne(Expression<? super D> right) {
        if (primitive) {
            return BooleanOperation.create(Ops.NE_PRIMITIVE, this, right);
        } else {
            return BooleanOperation.create(Ops.NE_OBJECT, this, right);
        }
    }

    /**
     * Get a <code>this not in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public final BooleanExpression notIn(Collection<? extends D> right) {
        if (right.size() == 1){
            return ne(right.iterator().next());
        }else{
            return in(right).not();
        }
    }

    /**
     * Get a <code>this not in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public BooleanExpression notIn(D... right) {
        if (right.length == 1){
            return ne(right[0]);
        }else{
            return in(right).not();
        }
    }



    /**
     * Get a <code>this not in right</code> expression
     *
     * @param right rhs of the comparison
     * @return
     */
    public final BooleanExpression notIn(CollectionExpression<?,? extends D> right) {
        return in(right).not();
    }

    @Override
    public final String toString() {
        if (toString == null) {            
            toString = accept(ToStringVisitor.DEFAULT, Templates.DEFAULT);
        }
        return toString;
    }

    /**
     * Get a case expression builder
     *
     * @param other
     * @return
     */
    public CaseForEqBuilder<D> when(D other){
        return new CaseForEqBuilder<D>(this, SimpleConstant.create(other));
    }

    /**
     * Get a case expression builder
     *
     * @param other
     * @return
     */
    public CaseForEqBuilder<D> when(Expression<? extends D> other){
        return new CaseForEqBuilder<D>(this, other);
    }
    
}
