/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import javax.annotation.Nonnegative;

/**
 * @author tiwe
 *
 * @param <EA>
 */
public interface EArray<E> {
    
    ENumber<Integer> size();
    
    Expr<E> get(Expr<Integer> index);

    Expr<E> get(@Nonnegative int index);

}
