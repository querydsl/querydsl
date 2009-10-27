/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

/**
 * PComponentList represents component list paths
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
@SuppressWarnings("serial")
public class PComponentList<D> extends PComponentCollection<D> implements PList<D> {
    
    private volatile PSimple<D> first, second;
    
    public PComponentList(Class<D> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    @Override
    public PSimple<D> get(Expr<Integer> index) {
        return new PSimple<D>(type, PathMetadata.forListAccess(this, index));
    }

    @Override
    public PSimple<D> get(int index) {
        if (index == 0){
            if (first == null) first = create(0);
            return first;
        }else if (index == 1){
            if (second == null) second = create(1);
            return second;
        }else{
            return create(index);
        }
    }
    
    private PSimple<D> create(int index){
        return new PSimple<D>(type, PathMetadata.forListAccess(this, index));
    }
}