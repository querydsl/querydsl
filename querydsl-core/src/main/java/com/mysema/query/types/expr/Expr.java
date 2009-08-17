/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.Collection;

import com.mysema.commons.lang.Assert;
import com.mysema.query.serialization.ToStringVisitor;
import com.mysema.query.types.ValidationVisitor;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * Expr represents a general typed expression in a Query instance. The generic type parameter
 * is a reference to the type the expression instance is bound to.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D> {

    private final Class<? extends D> type;
    
    private String toString;

    private final boolean primitive;
    
    private ENumber<Long> count;
    
    public Expr(Class<? extends D> type) {
        this.type = Assert.notNull(type,"type is null");
        this.primitive = type.isPrimitive() 
            || Number.class.isAssignableFrom(type) 
            || Boolean.class.equals(type) 
            || Character.class.equals(type);
    }
        
    /**
     * @return count(this)
     */
    public ENumber<Long> count(){
        if (count == null){
            count = ONumber.create(Long.class, Ops.AggOps.COUNT_AGG, this);
        }
        return count;
    }

    /**
     * Get the Java type for this expression
     * 
     * @return
     */
    public Class<? extends D> getType() {
        return type;
    }

    /**
     * Create a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean eq(D right) {
        return eq(EConstant.create(right));
    }

    /**
     * Create a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean eq(Expr<? super D> right) {
        if (primitive) {
            return new OBoolean(Ops.EQ_PRIMITIVE, this, right);
        } else {
            return new OBoolean(Ops.EQ_OBJECT, this, right);
        }
    }

    /**
     * Create a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean ne(D right) {
        return ne(EConstant.create(right));
    }

    /**
     * Create a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean ne(Expr<? super D> right) {
        if (primitive) {
            return new OBoolean(Ops.NE_PRIMITIVE, this, right);
        } else {
            return new OBoolean(Ops.NE_OBJECT, this, right);
        }
    }

    /**
     * Create a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(ECollection<? extends D> right) {
        return new OBoolean(Ops.IN, this, (Expr<?>)right);
    }

    /**
     * Create a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(Collection<? extends D> right) {
        return new OBoolean(Ops.IN, this, EConstant.create(right));
    }

    /**
     * Create a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean in(D... right) {
        return new OBoolean(Ops.IN, this, EConstant.create(Arrays.asList(right)));
    }

    /**
     * Create a <code>this not in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean notIn(ECollection<? extends D> right) {
        return in(right).not();
    }

    /**
     * Create a <code>this not in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean notIn(Collection<? extends D> right) {
        return in(right).not();
    }

    /**
     * Create a <code>this not in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public final EBoolean notIn(D... right) {
        return in(right).not();
    }

    /**
     * validate this expression for consistency
     * 
     */
    protected void validate() {
        new ValidationVisitor().handle(this);
    }

    @Override
    public String toString() {
        if (toString == null) {
            toString = new ToStringVisitor().handle(this).toString();
        }
        return toString;
    }
    
    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : super.hashCode();
    }

    public static ENumber<Long> countAll() {
        return Ops.AggOps.COUNT_ALL_AGG_EXPR;
    }
    
}
