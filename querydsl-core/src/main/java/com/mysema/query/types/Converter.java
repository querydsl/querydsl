/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <D>
 */
interface Converter<Source extends Expr<?>, Target extends Expr<?>>{
    
    /**
     * @param arg
     * @return
     */
    Target convert(Source arg); 
}
