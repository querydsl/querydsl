/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.CollectionType;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 * 
 * @param <D>
 */
public interface PCollection<D> extends Path<java.util.Collection<D>>,
        CollectionType<D> {
    EBoolean contains(D child);

    EBoolean contains(Expr<D> child);

    Class<D> getElementType();

    EBoolean isEmpty();

    EBoolean isNotEmpty();

    EComparable<Integer> size();
}