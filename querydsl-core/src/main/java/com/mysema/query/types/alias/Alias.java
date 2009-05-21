/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.alias;

import com.mysema.query.types.expr.Expr;

/**
 * Alias represents alias expressions
 * 
 * @author tiwe
 * @version $Id$
 */
public interface Alias {
    
    Expr<?> getFrom();

}
