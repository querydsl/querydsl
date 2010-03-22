/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;


/**
 * EDateOrTime is a supertype for Date/Time related types
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked"})
public abstract class EDateOrTime<D extends Comparable> extends EComparable<D> {
    
    private static final long serialVersionUID = 1137918766051524298L;

    public EDateOrTime(Class<? extends D> type) {
        super(type);
    }
    
    /**
     * Get a <code>this &gt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean after(D right) {
        return gt(right);
    }    

    /**
     * Get a <code>this &gt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean after(Expr<D> right) {
        return gt(right);
    }
        
    /**
     * Get a <code>this &lt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean before(D right) {
        return lt(right);
    }    

    /**
     * Get a <code>this &lt; right</code> expression
     * 
     * @param right
     * @return
     */
    public EBoolean before(Expr<D> right) {
        return lt(right);
    }
    
    
}
