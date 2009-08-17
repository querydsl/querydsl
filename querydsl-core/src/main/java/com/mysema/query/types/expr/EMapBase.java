/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Map;

import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.ONumber;
import com.mysema.query.types.operation.Ops;

/**
 * EMapBase is an abstract base class for EMap implementations
 * 
 * @author tiwe
 *
 * @param <K>
 * @param <V>
 */
public abstract class EMapBase<K,V> extends Expr<Map<K,V>> implements EMap<K,V> {

    private ENumber<Integer> size;    
    
    private EBoolean empty;
    
    public EMapBase(Class<? extends Map<K, V>> type) {
        super(type);
    }
    
    @Override
    public final EBoolean contains(Expr<K> key, Expr<V> value) {
        return get(key).eq(value);
    }

    @Override
    public final EBoolean containsKey(Expr<K> key) {
        return new OBoolean(Ops.CONTAINS_KEY, this, key);
    }

    @Override
    public final EBoolean containsKey(K key) {
        return new OBoolean(Ops.CONTAINS_KEY, this, EConstant.create(key));
    }

    @Override
    public final EBoolean containsValue(Expr<V> value) {
        return new OBoolean(Ops.CONTAINS_VALUE, this, value);
    }

    @Override
    public final EBoolean containsValue(V value) {
        return new OBoolean(Ops.CONTAINS_VALUE, this, EConstant.create(value));
    }
        
    @Override
    public final EBoolean isEmpty() {
        if (empty == null){
            empty = new OBoolean(Ops.MAP_ISEMPTY, this);
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
            size = ONumber.create(Integer.class, Ops.MAP_SIZE, this);
        }
        return size;
    }

}
