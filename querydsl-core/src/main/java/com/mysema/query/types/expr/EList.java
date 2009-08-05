/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;


/**
 * EList represents java.util.List typed expressions
 * 
 * @author tiwe
 * 
 * @param <D> component type
 * @see java.util.List
 */
public interface EList<D> extends ECollection<D> {
    
    /**
     * Indexed access
     * 
     * @param index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Expr<D> get(Expr<Integer> index);

    /**
     * Indexed access
     * 
     * @param index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Expr<D> get(int index);
}