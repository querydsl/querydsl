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
 * PEntityList represents entity list paths
 * 
 * @author tiwe
 * 
 * @param <E> component type
 */
@SuppressWarnings("serial")
public class PEntityList<E, Q extends PEntity<E>> extends PEntityCollection<E> implements PList<E> {
    
    private final Class<Q> queryType;
    
    private final Map<Integer,Q> cache = new HashMap<Integer,Q>();
    
    public PEntityList(Class<? super E> elementType, Class<Q> queryType, PathMetadata<?> metadata) {
        super(elementType, elementType.getSimpleName(), metadata);
        this.queryType = queryType;
    }
    
    @Override
    public Q get(Expr<Integer> index) {
        PathMetadata<Integer> md = PathMetadata.forListAccess(this, index);
        try {
            return queryType.getConstructor(PathMetadata.class).newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Q get(int index) {
        if (cache.containsKey(index)){
            return cache.get(index);
        }else{
            Q rv = create(index);
            cache.put(index, rv);
            return rv;
        }        
    }
    
    private Q create(int index){
        PathMetadata<Integer> md = PathMetadata.forListAccess(this, index);
        try {
            return queryType.getConstructor(PathMetadata.class).newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}