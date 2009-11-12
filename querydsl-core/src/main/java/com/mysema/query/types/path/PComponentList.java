/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.expr.Expr;

/**
 * PComponentList represents component list paths
 * 
 * @author tiwe
 * 
 * @param <E> component type
 */
@SuppressWarnings("serial")
public class PComponentList<E> extends PComponentCollection<E> implements PList<E> {
    
    private final Map<Integer,PSimple<E>> cache = new HashMap<Integer,PSimple<E>>();
    
    public PComponentList(Class<? super E> type, PathMetadata<?> metadata) {
        super(type, metadata);
    }

    @Override
    public PSimple<E> get(Expr<Integer> index) {
        return new PSimple<E>(type, PathMetadata.forListAccess(this, index));
    }

    @Override
    public PSimple<E> get(int index) {
        if (cache.containsKey(index)){
            return cache.get(index);
        }else{
            PSimple<E> rv = create(index);
            cache.put(index, rv);
            return rv;
        }
    }
    
    private PSimple<E> create(int index){
        return new PSimple<E>(type, PathMetadata.forListAccess(this, index));
    }
}