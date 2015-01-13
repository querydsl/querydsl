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
package com.querydsl.collections;

import java.util.List;
import java.util.Map;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;

/**
 * QueryEngine defines an interface for the evaluation of ColQuery queries
 *
 * @author tiwe
 *
 */
public interface QueryEngine {

    /**
     * Evaluate the given querydsl and return the count of matched rows
     *
     * @param metadata
     * @param iterables
     * @return
     */
    long count(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables);

    /**
     * Evaluate the given querydsl and return the projection as a list
     *
     * @param metadata
     * @param iterables
     * @return
     */
    <T> List<T> list(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables, 
            Expression<T> projection);

    /**
     * @param metadata
     * @param iterables
     * @return
     */
    boolean exists(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables);

}
