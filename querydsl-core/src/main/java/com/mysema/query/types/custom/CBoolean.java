/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * 
 * @author tiwe
 *
 */
public abstract class CBoolean extends EBoolean implements Custom<Boolean> {
    public Expr<?> getArg(int index) {
        return getArgs().get(index);
    }
}