/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.custom;

import java.util.List;

import com.mysema.query.types.expr.Expr;

/**
 * Custom provides base types for custom expresions with integrated
 * serialization patterns
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Custom<T> {

    List<Expr<?>> getArgs();
    
    Expr<?> getArg(int index);

    String getPattern();

}
