/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.mysema.query.types.Expression;

/**
 * Default implementation of the Group interface
 * 
 * @author sasa
 * @author tiwe
 * 
 */
class GroupImpl implements Group {
    
    private final Map<Expression<?>, GroupCollector<?,?>> groupCollectorMap = new LinkedHashMap<Expression<?>, GroupCollector<?,?>>();
    
    private final List<GroupDefinition<?, ?>> columnDefinitions;
    
    private final List<GroupCollector<?,?>> groupCollectors = new ArrayList<GroupCollector<?,?>>();
    
    private final List<QPair<?, ?>> maps;
    
    public GroupImpl(List<GroupDefinition<?, ?>> columnDefinitions,  List<QPair<?, ?>> maps) {
        this.columnDefinitions = columnDefinitions;
        this.maps = maps;
        for (int i=0; i < columnDefinitions.size(); i++) {
            GroupDefinition<?, ?> coldef = columnDefinitions.get(i);
            GroupCollector<?,?> collector = groupCollectorMap.get(coldef.getExpression());
            if (collector == null) {
                collector = coldef.createGroupCollector();                
                groupCollectorMap.put(coldef.getExpression(), collector);    
            }            
            groupCollectors.add(collector);
        }
    }

    void add(Object[] row) {
        int i=0;
        for (GroupCollector groupCollector : groupCollectors) {
            groupCollector.add(row[i]);
            i++;
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T, R> R get(Expression<T> expr) {
        GroupCollector<T,R> col = (GroupCollector<T,R>) groupCollectorMap.get(expr);
        if (col != null) {
            return col.get();
        }
        throw new NoSuchElementException(expr.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, R> R getGroup(GroupDefinition<T, R> definition) {
        for (GroupDefinition<?, ?> def : columnDefinitions) {            
            if (def.equals(definition)) {
                return (R) groupCollectorMap.get(def.getExpression()).get();
            }
        }
        throw new NoSuchElementException(definition.toString());
    }
    
    @Override
    public <T> List<T> getList(Expression<T> expr) {
        return this.<T, List<T>>get(expr);
    }

    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(Expression<K> key, Expression<V> value) {
        for (QPair<?, ?> pair : maps) {
            if (pair.equals(key, value)) {
                return (Map<K, V>) groupCollectorMap.get(pair).get();
            }
        }
        throw new NoSuchElementException("GMap(" + key + ", " + value + ")");
    }

    @Override
    public <T> T getOne(Expression<T> expr) {
        return this.<T, T>get(expr);
    }

    @Override
    public <T> Set<T> getSet(Expression<T> expr) {
        return this.<T, Set<T>>get(expr);
    }

    @Override
    public Object[] toArray() {
        List<Object> arr = new ArrayList<Object>(groupCollectors.size());
        for (GroupCollector<?,?> col : groupCollectors) {
            arr.add(col.get());
        }
        return arr.toArray();
    }

}