/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Ops;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class EEnum<T extends Enum<T>> extends EComparable<T> {

    private static final long serialVersionUID = 8819222316513862829L;

    public EEnum(Class<? extends T> type) {
        super(type);
    }

    /**
     * @return
     */
    public ENumber<Integer> ordinal(){
        return ONumber.create(Integer.class, Ops.ORDINAL, this);
    }

}