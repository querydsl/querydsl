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
import com.mysema.query.types.path.ArrayPath;
import com.mysema.query.types.path.BooleanPath;
import com.mysema.query.types.path.CollectionPath;
import com.mysema.query.types.path.ComparableEntityPath;
import com.mysema.query.types.path.ComparablePath;
import com.mysema.query.types.path.DatePath;
import com.mysema.query.types.path.DateTimePath;
import com.mysema.query.types.path.EntityPathBase;
import com.mysema.query.types.path.EnumPath;
import com.mysema.query.types.path.ListPath;
import com.mysema.query.types.path.MapPath;
import com.mysema.query.types.path.NumberPath;
import com.mysema.query.types.path.SetPath;
import com.mysema.query.types.path.SimplePath;
import com.mysema.query.types.path.StringPath;
import com.mysema.query.types.path.TimePath;

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

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <E> Path<Collection<E>> createCollectionPath(Class<E> elementType, PathMetadata<?> metadata) {
        return new CollectionPath<E,EntityPathBase<E>>(elementType, (Class)EntityPathBase.class, metadata);
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

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public <T> Path<T> createEntityPath(Class<T> type, PathMetadata<?> metadata) {
        if (Comparable.class.isAssignableFrom(type)) {
            return new ComparableEntityPath(type, metadata);
        } else {
            return new EntityPathBase<T>(type, metadata);    
        }        
    }

    @Override
    public <T extends Enum<T>> Path<T> createEnumPath( Class<T> type, PathMetadata<?> metadata) {
        return new EnumPath<T>(type, metadata);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <E> Path<List<E>> createListPath(Class<E> elementType, PathMetadata<?> metadata) {
        return new ListPath<E,EntityPathBase<E>>(elementType, (Class)EntityPathBase.class, metadata);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <K, V> Path<Map<K, V>> createMapPath(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata) {
        return new MapPath<K,V,EntityPathBase<V>>(keyType, valueType, (Class)EntityPathBase.class, metadata);
    }

    @Override
    public <T extends Number & Comparable<T>> Path<T> createNumberPath(Class<T> type, PathMetadata<?> metadata) {
        return new NumberPath<T>(type, metadata);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <E> Path<Set<E>> createSetPath(Class<E> elementType, PathMetadata<?> metadata) {
        return new SetPath<E,EntityPathBase<E>>(elementType, (Class)EntityPathBase.class, metadata);
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
