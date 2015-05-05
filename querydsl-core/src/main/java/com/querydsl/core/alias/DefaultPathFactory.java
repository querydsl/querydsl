/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
import com.querydsl.core.types.dsl.*;

/**
 * {@code DefaultPathFactory} is the default implementation of the {@link PathFactory} interface
 *
 * @author tiwe
 *
 */
public class DefaultPathFactory implements PathFactory {

    @Override
    public <T> Path<T[]> createArrayPath(Class<T[]> arrayType, PathMetadata metadata) {
        return Expressions.arrayPath(arrayType, metadata);
    }

    @Override
    public Path<Boolean> createBooleanPath(PathMetadata metadata) {
        return Expressions.booleanPath(metadata);
    }

    @Override
    public <E> Path<Collection<E>> createCollectionPath(Class<E> elementType, PathMetadata metadata) {
        return Expressions.collectionPath(elementType, EntityPathBase.class, metadata);
    }

    @Override
    public <T extends Comparable<?>> Path<T> createComparablePath( Class<T> type, PathMetadata metadata) {
        return Expressions.comparablePath(type, metadata);
    }

    @Override
    public <T extends Comparable<?>> Path<T> createDatePath(Class<T> type, PathMetadata metadata) {
        return Expressions.datePath(type, metadata);
    }

    @Override
    public <T extends Comparable<?>> Path<T> createDateTimePath(Class<T> type, PathMetadata metadata) {
        return Expressions.dateTimePath(type, metadata);
    }

    @Override
    public <T> Path<T> createEntityPath(Class<T> type, PathMetadata metadata) {
        if (Comparable.class.isAssignableFrom(type)) {
            return Expressions.comparableEntityPath((Class)type, metadata);
        } else {
            return new EntityPathBase<T>(type, metadata);
        }
    }

    @Override
    public <T extends Enum<T>> Path<T> createEnumPath( Class<T> type, PathMetadata metadata) {
        return Expressions.enumPath(type, metadata);
    }

    @Override
    public <E> Path<List<E>> createListPath(Class<E> elementType, PathMetadata metadata) {
        return Expressions.listPath(elementType, EntityPathBase.class, metadata);
    }

    @Override
    public <K, V> Path<Map<K, V>> createMapPath(Class<K> keyType, Class<V> valueType, PathMetadata metadata) {
        return Expressions.mapPath(keyType, valueType, EntityPathBase.class, metadata);
    }

    @Override
    public <T extends Number & Comparable<T>> Path<T> createNumberPath(Class<T> type, PathMetadata metadata) {
        return Expressions.numberPath(type, metadata);
    }

    @Override
    public <E> Path<Set<E>> createSetPath(Class<E> elementType, PathMetadata metadata) {
        return Expressions.setPath(elementType, EntityPathBase.class, metadata);
    }

    @Override
    public <T> Path<T> createSimplePath(Class<T> type, PathMetadata metadata) {
        return Expressions.path(type, metadata);
    }
    @Override
    public Path<String> createStringPath(PathMetadata metadata) {
        return Expressions.stringPath(metadata);
    }

    @Override
    public <T extends Comparable<?>> Path<T> createTimePath(Class<T> type, PathMetadata metadata) {
        return Expressions.timePath(type, metadata);
    }

}
