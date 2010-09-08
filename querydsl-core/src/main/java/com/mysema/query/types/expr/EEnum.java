/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PSimple;

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
    
    @SuppressWarnings("unchecked")
    @Override
    public EEnum<T> as(Path<T> alias) {
        return OEnum.create(getType(),(Operator)Ops.ALIAS, this, alias.asExpr());
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public EEnum<T> as(String alias) {
        return OEnum.create(getType(),(Operator)Ops.ALIAS, this, new PSimple(getType(), alias));
    }

    /**
     * @return
     */
    public ENumber<Integer> ordinal(){
        return ONumber.create(Integer.class, Ops.ORDINAL, this);
    }

}