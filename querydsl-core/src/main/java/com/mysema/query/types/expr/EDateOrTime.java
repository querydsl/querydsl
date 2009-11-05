/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;


/**
 * EDateOrTime is a supertype for Date/Time related types
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked","serial"})
public abstract class EDateOrTime<D extends Comparable> extends EComparable<D> {
    
    public EDateOrTime(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean after(D right) {
        return gt(right);
    }    

    /**
     * Create a <code>this &gt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean after(Expr<D> right) {
        return gt(right);
    }
        
    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean before(D right) {
        return lt(right);
    }    

    /**
     * Create a <code>this &lt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean before(Expr<D> right) {
        return lt(right);
    }
    
    
}
