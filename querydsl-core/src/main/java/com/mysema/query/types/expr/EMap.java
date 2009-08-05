/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.expr;


/**
 * EMap represents Map typed expressions
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
     * Create a <code>this.containsKey(key)</code> expression
     * 
     * @param key
     * @return this.containsKey(key)
     * @see java.util.Map#containsKey(Object)
     */
    EBoolean containsKey(Expr<K> key);

    /**
     * Create a <code>this.containsKey(key)</code> expression
     * 
     * @param key
     * @return this.containsKey(key)
     * @see java.util.Map#containsKey(Object)
     */
    EBoolean containsKey(K key);

    /**
     * Create a <code>this.containsValue(value)</code> expression
     * 
     * @param value
     * @return this.containsValue(value)
     * @see java.util.Map#containsValue(Object)
     */
    EBoolean containsValue(Expr<V> value);

    /**
     * Create a <code>this.containsValue(value)</code> expression
     * 
     * @param value
     * @return this.containsValue(value)
     * @see java.util.Map#containsValue(Object)c
     */
    EBoolean containsValue(V value);
    
    /**
     * Create a <code>this.get(key)</code> expression
     * 
     * @param key
     * @return this.get(key)
     * @see java.util.Map#get(Object)
     */
    Expr<V> get(Expr<K> key);
    
    /**
     * Create a <code>this.get(key)</code> expression
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