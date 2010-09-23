/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.io.Serializable;

import javax.annotation.Nullable;

/**
 * Expression defines a general typed expression in a Query instance. The generic type parameter
 * is a reference to the type the expression is bound to.
 *
 * @author tiwe
 * @version $Id$
 */
public interface Expression<D> extends Serializable{
    
    /**
     * Accept the visitor with the given context
     * 
     * @param <R>
     * @param <C>
     * @param v
     * @param context
     * @return
     */
    <R,C> R accept(Visitor<R,C> v, @Nullable C context);
    
    /**
     * Get the java type for this expression
     * 
     * @return
     */
    Class<? extends D> getType();

}
