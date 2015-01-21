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

/**
 * PathFactory defines a factory interface for Path creation
 * 
 * @author tiwe
 *
 */
public interface PathFactory {
        
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T> Path<T[]> createArrayPath(Class<T[]> type, PathMetadata<?> metadata);
    
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T> Path<T> createEntityPath(Class<T> type, PathMetadata<?> metadata);

    /**
     * @param type
     * @param metadata
     * @return
     */
    <T> Path<T> createSimplePath(Class<T> type, PathMetadata<?> metadata);
    
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T extends Comparable<?>> Path<T> createComparablePath(Class<T> type, PathMetadata<?> metadata);
    
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T extends Enum<T>> Path<T> createEnumPath(Class<T> type, PathMetadata<?> metadata);
    
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T extends Comparable<?>> Path<T> createDatePath(Class<T> type, PathMetadata<?> metadata);
    
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T extends Comparable<?>> Path<T> createTimePath(Class<T> type, PathMetadata<?> metadata);
    
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T extends Comparable<?>> Path<T> createDateTimePath(Class<T> type, PathMetadata<?> metadata);
    
    /**
     * @param type
     * @param metadata
     * @return
     */
    <T extends Number & Comparable<T>> Path<T> createNumberPath(Class<T> type, PathMetadata<?> metadata);
    
    /**
     * @param metadata
     * @return
     */
    Path<Boolean> createBooleanPath(PathMetadata<?> metadata);
    
    /**
     * @param metadata
     * @return
     */
    Path<String> createStringPath(PathMetadata<?> metadata);

    /**
     * @param elementType
     * @param metadata
     * @return
     */
    <E> Path<List<E>> createListPath(Class<E> elementType, PathMetadata<?> metadata);
    
    /**
     * @param elementType
     * @param metadata
     * @return
     */
    <E> Path<Set<E>> createSetPath(Class<E> elementType, PathMetadata<?> metadata);
    
    /**
     * @param elementType
     * @param metadata
     * @return
     */
    <E> Path<Collection<E>> createCollectionPath(Class<E> elementType, PathMetadata<?> metadata);
    
    /**
     * @param keyType
     * @param valueType
     * @param metadata
     * @return
     */
    <K,V> Path<Map<K,V>> createMapPath(Class<K> keyType, Class<V> valueType, PathMetadata<?> metadata);
}
