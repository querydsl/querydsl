/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;

/**
 * EnumExpression represents Enum typed expressions
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public abstract class EnumExpression<T extends Enum<T>> extends ComparableExpression<T> {

    private static final long serialVersionUID = 8819222316513862829L;

    public EnumExpression(Class<? extends T> type) {
        super(type);
    }

    @Override
    public EnumExpression<T> as(Path<T> alias) {
        return EnumOperation.create(getType(),Ops.ALIAS, this, alias);
    }

    @Override
    public EnumExpression<T> as(String alias) {
        return EnumOperation.create(getType(),Ops.ALIAS, this, new PathImpl<T>(getType(), alias));
    }

    /**
     * @return
     */
    public NumberExpression<Integer> ordinal(){
        return NumberOperation.create(Integer.class, Ops.ORDINAL, this);
    }

}