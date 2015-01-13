/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.path.ArrayPath;
import com.querydsl.core.types.path.BooleanPath;
import com.querydsl.core.types.path.CollectionPath;
import com.querydsl.core.types.path.ComparableEntityPath;
import com.querydsl.core.types.path.ComparablePath;
import com.querydsl.core.types.path.DatePath;
import com.querydsl.core.types.path.DateTimePath;
import com.querydsl.core.types.path.EntityPathBase;
import com.querydsl.core.types.path.EnumPath;
import com.querydsl.core.types.path.ListPath;
import com.querydsl.core.types.path.MapPath;
import com.querydsl.core.types.path.NumberPath;
import com.querydsl.core.types.path.SetPath;
import com.querydsl.core.types.path.SimplePath;
import com.querydsl.core.types.path.StringPath;
import com.querydsl.core.types.path.TimePath;

/**
 * DefaultPathFactory is the default implementation of the {@link PathFactory} interface
 *
 * @author tiwe
 *
 */
public class DefaultPathFactory implements PathFactory {

    @Override
    public <T> Path<T[]> createArrayPath(Class<T[]> arrayType, PathMetadata<?> metadata) {
        return new ArrayPath<T[], T>(arrayType, metadata);
    }

    @Override
    public Path<Boolean> createBooleanPath(PathMetadata<?> metadata) {
        return new BooleanPath(metadata);
    }

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

    @Override
    public <E> Path<List<E>> createListPath(Class<E> elementType, PathMetadata<?> metadata) {
        return new ListPath<E,EntityPathBase<E>>(elementType, (Class)EntityPathBase.class, metadata);
    }

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
