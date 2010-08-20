/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.io.Serializable;

/**
 * Constant represents a general constant expression.
 *
 * @author tiwe
 *
 * @param <D>
 */
public interface Constant<D> extends Serializable{

    /**
     * Get the constant
     *
     * @return
     */
    D getConstant();

    /**
     * Cast to {@link Expr}
     *
     * @return
     */
    Expr<D> asExpr();

}
