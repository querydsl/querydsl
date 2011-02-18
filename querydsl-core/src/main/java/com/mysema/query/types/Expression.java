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
 *
 * @param <T> expression type
 *
 */
public interface Expression<T> extends Serializable{

    /**
     * Accept the visitor with the given context
     *
     * @param <R> return type
     * @param <C> context type
     * @param v visitor
     * @param context context of visit
     * @return
     */
    @Nullable
    <R,C> R accept(Visitor<R,C> v, @Nullable C context);

    /**
     * Get the java type for this expression
     *
     * @return
     */
    Class<? extends T> getType();

}
