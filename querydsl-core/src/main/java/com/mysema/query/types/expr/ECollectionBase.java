/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Collection;

import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;


/**
 * ECollectionBase is an abstract base class for ECollection implementations
 * 
 * @author tiwe
 *
 * @param <D>
 */
public abstract class ECollectionBase<D> extends Expr<java.util.Collection<D>> implements ECollection<D> {

    private volatile EBoolean empty;
   
    private volatile ENumber<Integer> size;    
    
    public ECollectionBase(Class<? extends Collection<D>> type) {
        super(type);
    }
    
    @Override
    public final EBoolean contains(D child) {
        return contains(ExprConst.create(child));        
    }

    @Override
    public final EBoolean contains(Expr<D> child) {
        return OBoolean.create(Ops.IN, child, this);
    }
    
    @Override
    public final EBoolean isEmpty() {
        if (empty == null){
            empty = OBoolean.create(Ops.COL_IS_EMPTY, this); 
        }
        return empty;
    }

    @Override
    public final EBoolean isNotEmpty() {
        return isEmpty().not();  
    }

    @Override
    public final ENumber<Integer> size() {
        if (size == null) {
            size = ONumber.create(Integer.class, Ops.COL_SIZE, this);
        }
        return size;
    }

}
