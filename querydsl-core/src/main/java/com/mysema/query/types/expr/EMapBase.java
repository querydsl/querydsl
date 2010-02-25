/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import java.util.Map;

import javax.annotation.Nullable;

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
@SuppressWarnings("serial")
public abstract class EMapBase<K,V> extends Expr<Map<K,V>> implements EMap<K,V> {

    @Nullable 
    private volatile ENumber<Integer> size;    
    
    @Nullable 
    private volatile EBoolean empty;
    
    public EMapBase(Class<? extends Map<K, V>> type) {
        super(type);
    }
    
    @Override
    public final EBoolean contains(Expr<K> key, Expr<V> value) {
        return get(key).eq(value);
    }

    @Override
    public final EBoolean containsKey(Expr<K> key) {
        return OBoolean.create(Ops.CONTAINS_KEY, this, key);
    }

    @Override
    public final EBoolean containsKey(K key) {
        return OBoolean.create(Ops.CONTAINS_KEY, this, ExprConst.create(key));
    }

    @Override
    public final EBoolean containsValue(Expr<V> value) {
        return OBoolean.create(Ops.CONTAINS_VALUE, this, value);
    }

    @Override
    public final EBoolean containsValue(V value) {
        return OBoolean.create(Ops.CONTAINS_VALUE, this, ExprConst.create(value));
    }
        
    @Override
    public final EBoolean isEmpty() {
        if (empty == null){
            empty = OBoolean.create(Ops.MAP_ISEMPTY, this);
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
