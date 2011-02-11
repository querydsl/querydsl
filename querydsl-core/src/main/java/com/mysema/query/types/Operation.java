/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.util.List;

/**
 * Operation represents an operation with operator and arguments
 *
 * @author tiwe
 */
public interface Operation<T> extends Expression<T>{

    /**
     * Get the argument with the given index
     *
     * @param index
     * @return
     */
    Expression<?> getArg(int index);

    /**
     * Get the arguments of this operation
     *
     * @return
     */
    List<Expression<?>> getArgs();

    /**
     * Get the operator symbol for this operation
     *
     * @return
     */
    Operator<? super T> getOperator();

}
