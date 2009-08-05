/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;


/**
 * ECollection represents java.util.Collection typed expressions
 * 
 * @author tiwe
 * 
 * @param <D>
 * @see java.util.Collection
 */
public interface ECollection<D>{
   
    /**
     * Create an expression for <code>this.contains(child)</code>
     * 
     * @param child
     * @return this.contains(child)
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
     * @return this.isEmpty()
     * @see java.util.Collection#isEmpty()
     */
    EBoolean isEmpty();

    /**
     * Create an expression for <code>!this.isEmpty()</code>
     * 
     * @return !this.isEmpty()
     * @see java.util.Collection#isEmpty()
     */
    EBoolean isNotEmpty();

    /**
     * Create an expression for <code>this.size()</code>
     * 
     * @return this.size()
     * @see java.util.Collection#size()
     */
    ENumber<Integer> size();
}