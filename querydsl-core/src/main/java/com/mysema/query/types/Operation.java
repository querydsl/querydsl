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
public interface Operation<RT> extends Expression<RT>{

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
    Operator<? super RT> getOperator();

}
