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
import com.mysema.query.types.path.*;

/**
 * @author tiwe
 *
 */
public class DefaultPathFactory implements PathFactory{

    @Override
    public <T> Path<T[]> createArrayPath(Class<T[]> arrayType, PathMetadata<?> metadata) {
        return new ArrayPath<T>(arrayType, metadata);
    }

    @Override
    public Path<Boolean> createBooleanPath(PathMetadata<?> metadata) {
        return new BooleanPath(metadata);
    }

    @Override
    public <E> Path<Collection<E>> createCollectionPath(Class<E> elementType, PathMetadata<?> metadata) {
        return new CollectionPath<E>(elementType, elementType.getSimpleName(), metadata);
    }

    @Override
    public <T extends Comparable<?>> Path<T> createComparablePath( Class<T> type, PathMetadata<?> metadata) {
        return new ComparablePath<T>(type, metadata);
    }

    @Override
    public <T extends Comparable<?>> Path<T> createDatePath(Class<T> type, PathMetadata<?> metadata) {
        return new DatePath<T>(type, metadata);
    }
    
    @Override
    public <T extends Comparable<?>> Path<T> createDateTimePath(Class<T> type, PathMetadata<?> metadata) {
        return new DateTimePath<T>(type, metadata);
    }

    @Override
    public <T> Path<T> createEntityPath(Class<T> type, PathMetadata<?> metadata) {
        return new EntityPathBase<T>(type, metadata);
    }

    @Override
    public <T extends Enum<T>> Path<T> createEnumPath( Class<T> type, PathMetadata<?> metadata) {
        return new EnumPath<T>(type, metadata);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public <E> Path<List<E>> createListPath(Class<E> elementType, PathMetadata<?> metadata) {
        return new ListPath<E,EntityPathBase<E>>(elementType, (Class)EntityPathBase.class, metadata);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <K, V> Path<Map<K, V>> createMapPath(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata) {
        return new MapPath<K,V,EntityPathBase<V>>(keyType, valueType, (Class)EntityPathBase.class, metadata);
    }

    @Override
    public <T extends Number & Comparable<T>> Path<T> createNumberPath(Class<T> type, PathMetadata<?> metadata) {
        return new NumberPath<T>(type, metadata);
    }

    @Override
    public <E> Path<Set<E>> createSetPath(Class<E> elementType, PathMetadata<?> metadata) {
        return new SetPath<E>(elementType, elementType.getName(), metadata);
    }

    @Override
    public <T> Path<T> createSimplePath(Class<T> type, PathMetadata<?> metadata) {
        return new SimplePath<T>(type, metadata);
    }
    @Override
    public Path<String> createStringPath(PathMetadata<?> metadata) {
        return new StringPath(metadata);
    }

    @Override
    public <T extends Comparable<?>> Path<T> createTimePath(Class<T> type, PathMetadata<?> metadata) {
        return new TimePath<T>(type, metadata);
    }

}
