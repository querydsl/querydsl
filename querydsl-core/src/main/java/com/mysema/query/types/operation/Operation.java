/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.List;

import com.mysema.query.types.expr.Expr;

/**
 * Operation represents an operation with operator and arguments
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Operation<OP, RT> {
    /**
     * Get the type of this operation
     * 
     * @return
     */
    Class<? extends RT> getType();

    /**
     * Get the arguments of this operation
     * 
     * @return
     */
    List<Expr<?>> getArgs();

    /**
     * Get the argument with the given index
     * 
     * @param index
     * @return
     */
    Expr<?> getArg(int index);

    /**
     * Get the operator symbol for this operation
     * 
     * @return
     */
    Operator<OP> getOperator();

}
