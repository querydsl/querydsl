/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import java.util.List;

import javax.annotation.Nonnegative;



/**
 * EList represents java.util.List typed expressions
 * 
 * @author tiwe
 * 
 * @param <E> component type
 * @see java.util.List
 */
public interface EList<E> extends ECollection<List<E>,E> {
    
    /**
     * Indexed access
     * 
     * @param index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Expr<E> get(Expr<Integer> index);

    /**
     * Indexed access
     * 
     * @param index
     * @return this.get(index)
     * @see java.util.List#get(int)
     */
    Expr<E> get(@Nonnegative int index);
}