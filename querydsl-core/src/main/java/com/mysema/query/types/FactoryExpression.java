/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;

import javax.annotation.Nullable;

/**
 * FactoryExpression represents factory expressions such as JavaBean or 
 * Constructor projections
 * 
 * @author tiwe
 *
 * @param <T> type of projection
 */
public interface FactoryExpression<T> extends Expression<T>{
    
    /**
     * Get the invocation arguments
     *
     * @return
     */
    List<Expression<?>> getArgs();

    /**
     * Create a projection with the given arguments
     *
     * @param args
     * @return
     */
    @Nullable
    T newInstance(Object... args);

}