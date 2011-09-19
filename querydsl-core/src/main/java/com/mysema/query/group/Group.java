/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.query.types.Expression;

public interface Group {
    
    Object[] toArray();
    
    <T, R> R getGroup(GroupColumnDefinition<T, R> coldef);
    
    <T> T getOne(Expression<T> expr);
    
    <T> Set<T> getSet(Expression<T> expr);
    
    <T> List<T> getList(Expression<T> expr);
    
    <K, V> Map<K, V> getMap(Expression<K> key, Expression<V> value);
    
    <K, V> Map<K, V> getMap(Expression<K> key, Class<V> valueType);
    
}