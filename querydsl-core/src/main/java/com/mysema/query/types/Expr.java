/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollection;
import com.mysema.query.types.expr.ENumber;

/**
 * Expr represents a general typed expression in a Query instance. The generic type parameter
 * is a reference to the type the expression is bound to.
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class Expr<D> implements Serializable{

    private static final long serialVersionUID = 8049453060731070043L;

    protected final boolean primitive;
    
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
    public abstract ENumber<Long> count();

    /**
     * Get the <code>count(distinct this)</code> expression
     *
     * @return count(distinct this)
     */
    public abstract ENumber<Long> countDistinct();

    /**
     * Get a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public abstract EBoolean eq(D right);

    /**
     * Get a <code>this == right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public abstract EBoolean eq(Expr<? super D> right);

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
    public abstract EBoolean in(Collection<? extends D> right);

    /**
     * Get a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public abstract EBoolean in(D... right);

    /**
     * Get a <code>this in right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public abstract EBoolean in(ECollection<?,? extends D> right);

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public abstract EBoolean ne(D right);

    /**
     * Get a <code>this &lt;&gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     */
    public abstract EBoolean ne(Expr<? super D> right);

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
    public EBoolean notIn(D... right) {
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
    
}
