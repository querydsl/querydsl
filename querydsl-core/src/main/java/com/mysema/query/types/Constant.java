/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

/**
 * Constant represents a general constant expression.
 *
 * @author tiwe
 *
 * @param <T> constant type
 */
public interface Constant<T> extends Expression<T>{

    /**
     * Get the constant
     *
     * @return
     */
    T getConstant();

}
