/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

/**
 * PList represents List typed paths
 * 
 * @author tiwe
 * 
 * @param <D> component type
 * @see java.util.List
 */
public interface PList<D> extends PCollection<D> {
    
    /**
     * Indexed access
     * 
     * @param index
     * @return
     * @see java.util.List#get(int)
     */
    Expr<D> get(Expr<Integer> index);

    /**
     * Indexed access
     * 
     * @param index
     * @return
     * @see java.util.List#get(int)
     */
    Expr<D> get(int index);
}