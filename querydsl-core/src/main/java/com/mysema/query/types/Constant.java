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
 * @param <D> constant type
 */
public interface Constant<D> extends Expression<D>{

    /**
     * Get the constant
     *
     * @return
     */
    D getConstant();

}
