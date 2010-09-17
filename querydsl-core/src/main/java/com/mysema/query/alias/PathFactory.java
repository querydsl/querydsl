/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;

/**
 * @author tiwe
 *
 */
public interface PathFactory {
    
    boolean isCollectionType(Class<?> cl);
    
    boolean isSetType(Class<?> cl);
    
    boolean isListType(Class<?> cl);
    
    boolean isMapType(Class<?> cl);
    
    <T> Path<T[]> createArrayPath(Class<T[]> type, PathMetadata<?> metadata);
    
    <T> Path<T> createEntityPath(Class<T> type, PathMetadata<?> metadata);

    <T> Path<T> createSimplePath(Class<T> type, PathMetadata<?> metadata);
    
    <T extends Comparable<?>> Path<T> createComparablePath(Class<T> type, PathMetadata<?> metadata);
    
    <T extends Enum<T>> Path<T> createEnumPath(Class<T> type, PathMetadata<?> metadata);
    
    <T extends Comparable<?>> Path<T> createDatePath(Class<T> type, PathMetadata<?> metadata);
    
    <T extends Comparable<?>> Path<T> createTimePath(Class<T> type, PathMetadata<?> metadata);
    
    <T extends Comparable<?>> Path<T> createDateTimePath(Class<T> type, PathMetadata<?> metadata);
    
    <T extends Number & Comparable<T>> Path<T> createNumberPath(Class<T> type, PathMetadata<?> metadata);
    
    Path<Boolean> createBooleanPath(PathMetadata<?> metadata);
    
    Path<String> createStringPath(PathMetadata<?> metadata);

    <E> Path<List<E>> createListPath(Class<E> elementType, PathMetadata<?> metadata);
    
    <E> Path<Set<E>> createSetPath(Class<E> elementType, PathMetadata<?> metadata);
    
    <E> Path<Collection<E>> createCollectionPath(Class<E> elementType, PathMetadata<?> metadata);
    
    <K,V> Path<Map<K,V>> createMapPath(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata);
}
