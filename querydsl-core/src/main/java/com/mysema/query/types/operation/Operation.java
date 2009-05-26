/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.operation;

import java.util.List;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.Ops.Op;

/**
 * Operation represents an operation with operator and arguments
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Operation<OP, RT> {
    /**
     * 
     * @return
     */
    Class<? extends RT> getType();

    /**
     * 
     * @return
     */
    List<Expr<?>> getArgs();

    /**
     * 
     * @param index
     * @return
     */
    Expr<?> getArg(int index);

    /**
     * 
     * @return
     */
    Op<OP> getOperator();

}
