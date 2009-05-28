/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * PMap represents Map typed paths
 * 
 * @author tiwe
 * 
 * @param <K> key type
 * @param <V> value type
 * @see java.util.Map
 */
public interface PMap<K, V> extends Path<java.util.Map<K, V>> {
    
    /**
     * Create a <code>this.containsKey(key)</code> expression
     * 
     * @param key
     * @return
     * @see java.util.Map#containsKey(Object)
     */
    EBoolean containsKey(Expr<K> key);

    /**
     * Create a <code>this.containsKey(key)</code> expression
     * 
     * @param key
     * @return
     * @see java.util.Map#containsValue(Object)
     */
    EBoolean containsKey(K key);

    /**
     * Create a <code>this.containsValue(value)</code> expression
     * 
     * @param value
     * @return
     * @see java.util.Map#containsValue(Object)
     */
    EBoolean containsValue(Expr<V> value);

    /**
     * Create a <code>this.containsValue(value)</code> expression
     * 
     * @param value
     * @return
     * @see java.util.Map#containsValue(Object)c
     */
    EBoolean containsValue(V value);
    
    /**
     * Create a <code>this.get(key)</code> expression
     * 
     * @param key
     * @return
     * @see java.util.Map#get(Object)
     */
    Expr<V> get(Expr<K> key);
    
    /**
     * Create a <code>this.get(key)</code> expression
     * 
     * @param key
     * @return
     * @see java.util.Map#get(Object)
     */
    Expr<V> get(K key);
    
    /**
     * Get the key type for this path
     * 
     * @return
     */
    Class<K> getKeyType();
    
    /**
     * Get the value type for this path
     * 
     * @return
     */
    Class<V> getValueType();
    
    /**
     * Get the <code>this.isEmpty()</code> expression
     * 
     * @return
     * @see java.util.Map#isEmpty()
     */
    EBoolean isEmpty();

    /**
     * Get the <code>!this.isEmpty()</code> expression
     * 
     * @return
     * @see java.util.Map#isEmpty()
     */
    EBoolean isNotEmpty();
}