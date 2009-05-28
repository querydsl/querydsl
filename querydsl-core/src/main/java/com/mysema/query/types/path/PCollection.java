/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

/**
 * PCollection represents collection typed paths
 * 
 * @author tiwe
 * 
 * @param <D>
 * @see java.util.Collection
 */
public interface PCollection<D> extends Path<java.util.Collection<D>>,
        CollectionType<D> {
   
    /**
     * Create an expression for <code>this.contains(child)</code>
     * 
     * @param child
     * @return
     * @see java.util.Collection#contains(Object)
     */
    EBoolean contains(D child);

    /**
     * Create an expression for <code>this.contains(child)</code>
     * 
     * @param child
     * @return
     * @see java.util.Collection#contains(Object)
     */
    EBoolean contains(Expr<D> child);

    /**
     * Get the element type of this path
     * 
     */
    Class<D> getElementType();

    /**
     * Create an expression for <code>this.isEmpty()</code>
     * 
     * @return
     * @see java.util.Collection#isEmpty()
     */
    EBoolean isEmpty();

    /**
     * Create an expression for <code>!this.isEmpty()</code>
     * 
     * @return
     * @see java.util.Collection#isEmpty()
     */
    EBoolean isNotEmpty();

    /**
     * Create an expression for <code>this.size()</code>
     * 
     * @return
     * @see java.util.Collection#size()
     */
    EComparable<Integer> size();
}