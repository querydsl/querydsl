/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 * 
 * @param <D>
 */
public interface PList<D> extends PCollection<D> {
    Expr<D> get(Expr<Integer> index);

    Expr<D> get(int index);
}