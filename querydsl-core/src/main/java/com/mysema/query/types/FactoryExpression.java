/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;

/**
 * @author tiwe
 *
 * @param <D>
 */
public interface FactoryExpression<D> {
    /**
     * Cast to {@link Expr}
     *
     * @return
     */
    Expr<D> asExpr();

    /**
     * Get the type of this path
     *
     * @return
     */
    Class<? extends D> getType();

    /**
     * Get the constructor invocation arguments
     *
     * @return
     */
    List<Expr<?>> getArgs();

    /**
     * Create a projection with the given arguments
     *
     * @param args
     * @return
     */
    D newInstance(Object... args);

}