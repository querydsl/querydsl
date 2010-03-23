/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Expr;


/**
 * EMap represents java.util.Map typed expressions
 * 
 * @author tiwe
 * 
 * @param <K> key type
 * @param <V> value type
 * @see java.util.Map
 */
public interface EMap<K, V> {
    
    /**
     * @param key
     * @param value
     * @return this.get(key).equals(value)
     */
    EBoolean contains(Expr<K> key, Expr<V> value);
    
    /**
     * Get a <code>this.containsKey(key)</code> expression
     * 
     * @param key
     * @return this.containsKey(key)
     * @see java.util.Map#containsKey(Object)
     */
    EBoolean containsKey(Expr<K> key);

    /**
     * Get a <code>this.containsKey(key)</code> expression
     * 
     * @param key
     * @return this.containsKey(key)
     * @see java.util.Map#containsKey(Object)
     */
    EBoolean containsKey(K key);

    /**
     * Get a <code>this.containsValue(value)</code> expression
     * 
     * @param value
     * @return this.containsValue(value)
     * @see java.util.Map#containsValue(Object)
     */
    EBoolean containsValue(Expr<V> value);

    /**
     * Get a <code>this.containsValue(value)</code> expression
     * 
     * @param value
     * @return this.containsValue(value)
     * @see java.util.Map#containsValue(Object)c
     */
    EBoolean containsValue(V value);
    
    /**
     * Get a <code>this.get(key)</code> expression
     * 
     * @param key
     * @return this.get(key)
     * @see java.util.Map#get(Object)
     */
    Expr<V> get(Expr<K> key);
    
    /**
     * Get a <code>this.get(key)</code> expression
     * 
     * @param key
     * @return this.get(key)
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
     * @return this.isEmpty()
     * @see java.util.Map#isEmpty()
     */
    EBoolean isEmpty();

    /**
     * Get the <code>!this.isEmpty()</code> expression
     * 
     * @return !this.isEmpty()
     * @see java.util.Map#isEmpty()
     */
    EBoolean isNotEmpty();
    
    /**
     * Get the size of the Map instance
     * 
     * @return this.size()
     * @see java.util.Map#size()
     */
    ENumber<Integer> size();
}