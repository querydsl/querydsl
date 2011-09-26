/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.collection.TransformedCollection;
import org.apache.commons.collections15.set.TransformedSet;

/**
 * A read-only view of underlying map with values transformed by a given Transformer.
 * 
 * @author sasa
 *
 * @param <K> Key type of this map
 * @param <V> Original value type; input type for the Transformer
 * @param <T> Target value type; output type of the Transformer
 */
public class ValueTransformerMap<K, V, T> extends AbstractMap<K, T> {

    private final Map<K, V> map;

    private final Transformer<? super V, ? extends T> transformer;
    
    public static <K, V, T> Map<K, T> create(Map<K, V> map, Transformer<? super V, ? extends T> transformer) {
        return new ValueTransformerMap<K, V, T>(map, transformer);
    }
    
    public ValueTransformerMap(Map<K, V> map, Transformer<? super V, ? extends T> transformer) {
        this.map = map;
        this.transformer = transformer;
    }
    
    public int size() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public boolean containsValue(Object value) {
        return map.containsValue(transformer.transform((V) value));
    }

    public T get(Object key) {
        return transformer.transform(map.get(key));
    }

    public T put(K key, T value) {
        throw new UnsupportedOperationException();
    }

    public T remove(Object key) {
        throw new UnsupportedOperationException();
    }

    public void putAll(Map<? extends K, ? extends T> m) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<T> values() {
        return TransformedCollection.decorate(map.values(), transformer);
    }

    public Set<java.util.Map.Entry<K, T>> entrySet() {
        return TransformedSet.decorate(map.entrySet(), new Transformer<Map.Entry<K, V>, Map.Entry<K, T>>() {
            @Override
            public java.util.Map.Entry<K, T> transform(final Map.Entry<K, V> input) {
                return new Map.Entry<K, T>() {

                    @Override
                    public K getKey() {
                        return input.getKey();
                    }

                    @Override
                    public T getValue() {
                        return transformer.transform(input.getValue());
                    }

                    @Override
                    public T setValue(T value) {
                        throw new UnsupportedOperationException();
                    }
                    
                };
            }
        });
    }
    
}
