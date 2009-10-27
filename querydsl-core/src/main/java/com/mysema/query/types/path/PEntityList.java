/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.Expr;

/**
 * PEntityList represents entity list paths
 * 
 * @author tiwe
 * 
 * @param <D> component type
 */
@SuppressWarnings("serial")
public class PEntityList<D, E extends PEntity<D>> extends PEntityCollection<D> implements PList<D> {
    
    private final Class<E> queryType;
    
    private volatile E first, second;
    
    public PEntityList(Class<? super D> elementType, Class<E> queryType, PathMetadata<?> metadata) {
        super(elementType, elementType.getSimpleName(), metadata);
        this.queryType = queryType;
    }
    
    @Override
    public E get(Expr<Integer> index) {
        PathMetadata<Integer> md = PathMetadata.forListAccess(this, index);
        try {
            return queryType.getConstructor(PathMetadata.class).newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public E get(int index) {
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
    
    private E create(int index){
        PathMetadata<Integer> md = PathMetadata.forListAccess(this, index);
        try {
            return queryType.getConstructor(PathMetadata.class).newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}