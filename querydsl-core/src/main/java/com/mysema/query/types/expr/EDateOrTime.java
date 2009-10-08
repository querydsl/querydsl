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
    
    public EBoolean after(D right) {
        return gt(right);
    }    

    public EBoolean after(Expr<D> right) {
        return gt(right);
    }
    
    /**
     * Use goe instead
     */
    @Deprecated
    public EBoolean aoe(D right) {
        return goe(right);
    }    

    /**
     * Use goe instead
     */
    @Deprecated
    public EBoolean aoe(Expr<D> right) {
        return goe(right);
    }
    
    public EBoolean before(D right) {
        return lt(right);
    }    

    public EBoolean before(Expr<D> right) {
        return lt(right);
    }
    
    /**
     * Use loe instead
     */
    @Deprecated
    public EBoolean boe(D right) {
        return loe(right);
    }    

    /**
     * Use loe instead
     */
    @Deprecated
    public EBoolean boe(Expr<D> right) {
        return loe(right);
    }
    
}
