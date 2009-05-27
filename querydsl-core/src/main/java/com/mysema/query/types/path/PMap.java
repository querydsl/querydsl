/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 * 
 * @param <K>
 * @param <V>
 */
public interface PMap<K, V> extends Path<java.util.Map<K, V>> {
    EBoolean containsKey(Expr<K> key);

    EBoolean containsKey(K key);

    EBoolean containsValue(Expr<V> value);

    EBoolean containsValue(V value);
    
    Expr<V> get(Expr<K> key);
    
    Expr<V> get(K key);
    
    Class<K> getKeyType();
    
    Class<V> getValueType();
    
    EBoolean isEmpty();

    EBoolean isNotEmpty();
}