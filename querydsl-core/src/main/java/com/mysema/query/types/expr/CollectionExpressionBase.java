/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import java.util.Collection;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;

/**
 * CollectionExpressionBase is an abstract base class for ECollection implementations
 *
 * @author tiwe
 *
 * @param <D>
 */
public abstract class CollectionExpressionBase<C extends Collection<E>, E> extends SimpleExpression<C> implements CollectionExpression<C,E> {

    private static final long serialVersionUID = 691230660037162054L;

    @Nullable
    private volatile BooleanExpression empty;

    @Nullable
    private volatile NumberExpression<Integer> size;

    public CollectionExpressionBase(Class<? extends C> type) {
        super(type);
    }

    @Override
    public final BooleanExpression contains(E child) {
        return contains(SimpleConstant.create(child));
    }

    @Override
    public final BooleanExpression contains(Expression<E> child) {
        return BooleanOperation.create(Ops.IN, child, this);
    }

    @Override
    public final BooleanExpression isEmpty() {
        if (empty == null){
            empty = BooleanOperation.create(Ops.COL_IS_EMPTY, this);
        }
        return empty;
    }

    @Override
    public final BooleanExpression isNotEmpty() {
        return isEmpty().not();
    }

    @Override
    public final NumberExpression<Integer> size() {
        if (size == null) {
            size = NumberOperation.create(Integer.class, Ops.COL_SIZE, this);
        }
        return size;
    }

}
