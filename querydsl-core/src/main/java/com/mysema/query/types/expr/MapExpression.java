/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.expr;

import com.mysema.query.types.Expression;

/**
 * EMap represents java.util.Map typed expressions
 *
 * @author tiwe
 *
 * @param <K> key type
 * @param <V> value type
 * @see java.util.Map
 */
public interface MapExpression<K, V> {

    /**
     * @param key
     * @param value
     * @return this.get(key).equals(value)
     */
    BooleanExpression contains(Expression<K> key, Expression<V> value);

    /**
     * Get a <code>this.containsKey(key)</code> expression
     *
     * @param key
     * @return this.containsKey(key)
     * @see java.util.Map#containsKey(Object)
     */
    BooleanExpression containsKey(Expression<K> key);

    /**
     * Get a <code>this.containsKey(key)</code> expression
     *
     * @param key
     * @return this.containsKey(key)
     * @see java.util.Map#containsKey(Object)
     */
    BooleanExpression containsKey(K key);

    /**
     * Get a <code>this.containsValue(value)</code> expression
     *
     * @param value
     * @return this.containsValue(value)
     * @see java.util.Map#containsValue(Object)
     */
    BooleanExpression containsValue(Expression<V> value);

    /**
     * Get a <code>this.containsValue(value)</code> expression
     *
     * @param value
     * @return this.containsValue(value)
     * @see java.util.Map#containsValue(Object)c
     */
    BooleanExpression containsValue(V value);

    /**
     * Get a <code>this.get(key)</code> expression
     *
     * @param key
     * @return this.get(key)
     * @see java.util.Map#get(Object)
     */
    SimpleExpression<V> get(Expression<K> key);

    /**
     * Get a <code>this.get(key)</code> expression
     *
     * @param key
     * @return this.get(key)
     * @see java.util.Map#get(Object)
     */
    SimpleExpression<V> get(K key);

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
    BooleanExpression isEmpty();

    /**
     * Get the <code>!this.isEmpty()</code> expression
     *
     * @return !this.isEmpty()
     * @see java.util.Map#isEmpty()
     */
    BooleanExpression isNotEmpty();

    /**
     * Get the size of the Map instance
     *
     * @return this.size()
     * @see java.util.Map#size()
     */
    NumberExpression<Integer> size();
}
