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

/**
 * {@code PathFactory} defines a factory interface for {@link Path} creation
 * 
 * @author tiwe
 *
 */
public interface PathFactory {
        
    /**
     * Create an array path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T> Path<T[]> createArrayPath(Class<T[]> type, PathMetadata metadata);
    
    /**
     * Create an entity path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T> Path<T> createEntityPath(Class<T> type, PathMetadata metadata);

    /**
     * Create a simple path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T> Path<T> createSimplePath(Class<T> type, PathMetadata metadata);
    
    /**
     * Create a comparable path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T extends Comparable<?>> Path<T> createComparablePath(Class<T> type, PathMetadata metadata);
    
    /**
     * Create an enum path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T extends Enum<T>> Path<T> createEnumPath(Class<T> type, PathMetadata metadata);
    
    /**
     * Create a date path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T extends Comparable<?>> Path<T> createDatePath(Class<T> type, PathMetadata metadata);
    
    /**
     * Create a time path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T extends Comparable<?>> Path<T> createTimePath(Class<T> type, PathMetadata metadata);
    
    /**
     * Create a datetime path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T extends Comparable<?>> Path<T> createDateTimePath(Class<T> type, PathMetadata metadata);
    
    /**
     * Create a number path
     *
     * @param type type of the path
     * @param metadata metadata of the path
     * @return new path instance
     */
    <T extends Number & Comparable<T>> Path<T> createNumberPath(Class<T> type, PathMetadata metadata);
    
    /**
     * Create a boolean path
     *
     * @param metadata metadata of the path
     * @return new path instance
     */
    Path<Boolean> createBooleanPath(PathMetadata metadata);
    
    /**
     * Create a string path
     *
     * @param metadata metadata of the path
     * @return new path instance
     */
    Path<String> createStringPath(PathMetadata metadata);

    /**
     * Create a list path
     *
     * @param elementType element type
     * @param metadata metadata of the path
     * @return new path instance
     */
    <E> Path<List<E>> createListPath(Class<E> elementType, PathMetadata metadata);
    
    /**
     * Create a set path
     *
     * @param elementType element type
     * @param metadata metadata of the path
     * @return new path instance
     */
    <E> Path<Set<E>> createSetPath(Class<E> elementType, PathMetadata metadata);
    
    /**
     * Create a collection path
     *
     * @param elementType element type
     * @param metadata metadata of the path
     * @return new path instance
     */
    <E> Path<Collection<E>> createCollectionPath(Class<E> elementType, PathMetadata metadata);
    
    /**
     * Create a map path
     *
     * @param keyType key type
     * @param valueType value type
     * @param metadata metadata of the path
     * @return new path instance
     */
    <K,V> Path<Map<K,V>> createMapPath(Class<K> keyType, Class<V> valueType, PathMetadata metadata);
}
