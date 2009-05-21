/*
 * Copyright (c) 2008 Mysema Ltd.
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
public interface Operation<OP,RT> {
        
    Class<? extends RT> getType();
    List<Expr<?>> getArgs();
    Expr<?> getArg(int index);
    Op<OP> getOperator();
    
}
