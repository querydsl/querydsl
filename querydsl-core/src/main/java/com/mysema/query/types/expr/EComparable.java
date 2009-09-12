/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;

/**
 * EComparable extends EComparableBase to provide comparison methods. 
 *  
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked","serial"})
public abstract class EComparable<D extends Comparable> extends EComparableBase<D> {
    
    public EComparable(Class<? extends D> type) {
        super(type);
    }

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public EBoolean gt(D right) {
        return gt(ExprConst.create(right));
    }
    
    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public EBoolean gt(Expr<D> right) {
        return OBoolean.create(Ops.AFTER, this, right);
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public EBoolean goe(D right) {
        return goe(ExprConst.create(right));
    }

    /**
     * Create a <code>this &gt;= right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public EBoolean goe(Expr<D> right) {
        return OBoolean.create(Ops.AOE, this, right);
    }
    
    /**
     * Create a <code>this &lt; right</code> expression 
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final EBoolean lt(D right) {
        return lt(ExprConst.create(right));
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final EBoolean lt(Expr<D> right) {
        return OBoolean.create(Ops.BEFORE, this, right);
    }

    /**
     * Create a <code>this &lt;= right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final EBoolean loe(D right) {
        return OBoolean.create(Ops.BOE, this, ExprConst.create(right));
    }

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param right rhs of the comparison
     * @return
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final EBoolean loe(Expr<D> right) {
        return OBoolean.create(Ops.BOE, this, right);
    }

}
