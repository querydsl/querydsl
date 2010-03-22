/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.OBoolean;
import com.mysema.query.types.expr.ONumber;

/**
 * Expr represents a general typed expression in a Query instance. The generic type parameter
 * is a reference to the type the expression instance is bound to.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D> implements Serializable{

    private static final long serialVersionUID = 8049453060731070043L;

    @Nullable
    private volatile ENumber<Long> count;
    
    @Nullable
    private volatile ENumber<Long> countDistinct;

    private final boolean primitive;
    
    @Nullable
    private volatile String toString;
    
    private final Class<? extends D> type;
        
    public Expr(Class<? extends D> type) {
        this.type = Assert.notNull(type,"type is null");
        this.primitive = type.isPrimitive() 
            || Number.class.isAssignableFrom(type) 
            || Boolean.class.equals(type) 
            || Character.class.equals(type);
    }

    public abstract void accept(Visitor v);
    
    /**
     * Used for safe casts from Path, SubQuery, Operation and Custom to Expr
     * 
     * @return
     */
    public final Expr<D> asExpr(){
        return this;
    }
    
    /**
     * Get the <code>count(this)</code> expression
     * 
     * @return count(this)
     */
    public ENumber<Long> count(){
        if (count == null){
            count = ONumber.create(Long.class, Ops.AggOps.COUNT_AGG, this);
        }
        return count;
    }

    /**
     * Get the <code>count(distinct this)</code> expression
     *
     * @return count(distinct this)
     */
    public ENumber<Long> countDistinct(){
        if (countDistinct == null){
          countDistinct = ONumber.create(Long.class, Ops.AggOps.COUNT_DISTINCT_AGG, this);
        }
        return countDistinct;
    }

    /**
     * Get a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public EBoolean eq(D right) {
        return eq(ExprConst.create(right));
    }

    /**
     * Get a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean eq(Expr<? super D> right) {
        if (primitive) {
            return OBoolean.create(Ops.EQ_PRIMITIVE, this, right);
        } else {
            return OBoolean.create(Ops.EQ_OBJECT, this, right);
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

    @Override
    public abstract boolean equals(Object o);
    
    /**
     * Get a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(Collection<? extends D> right) {
        if (right.size() == 1){
            return eq(right.iterator().next());
        }else{
            return OBoolean.create(Ops.IN, this, ExprConst.create(right));    
        }        
    }

    /**
     * Get a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(D... right) {
        if (right.length == 1){
            return eq(right[0]);
        }else{
            return OBoolean.create(Ops.IN, this, ExprConst.create(Arrays.asList(right)));    
        }        
    }

    /**
     * Get a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(ECollection<?,? extends D> right) {
        return OBoolean.create(Ops.IN, this, (Expr<?>)right);
    }

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public EBoolean ne(D right) {
        return ne(ExprConst.create(right));
    }

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean ne(Expr<? super D> right) {
        if (primitive) {
            return OBoolean.create(Ops.NE_PRIMITIVE, this, right);
        } else {
            return OBoolean.create(Ops.NE_OBJECT, this, right);
        }
    }

    /**
     * Get a <code>this not in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean notIn(Collection<? extends D> right) {
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
    public final EBoolean notIn(D... right) {
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
    public final EBoolean notIn(ECollection<?,? extends D> right) {
        return in(right).not();
    }

    @Override
    public final String toString() {
        if (toString == null) {
            Visitor visitor = new ToStringVisitor(Templates.DEFAULT);
            this.accept(visitor);
            toString = visitor.toString();
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
        return new CaseForEqBuilder<D>(this, ExprConst.create(other));
    }
    
    /**
     * Get a case expression builder
     * 
     * @param other
     * @return
     */
    public CaseForEqBuilder<D> when(Expr<? extends D> other){
        return new CaseForEqBuilder<D>(this, other);
    }
    
}
