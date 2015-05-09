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
package com.querydsl.collections;

import java.util.List;
import java.util.Map;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;

/**
 * {@code QueryEngine} defines an interface for the evaluation of ColQuery queries
 *
 * @author tiwe
 *
 */
public interface QueryEngine {

    /**
     * Evaluate the given query and return the count of matched rows
     *
     * @param metadata query metadata
     * @param iterables source contents
     * @return matching row count
     */
    long count(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables);

    /**
     * Evaluate the given query and return the projection as a list
     *
     * @param metadata query metadata
     * @param iterables source contents
     * @return matching rows
     */
    <T> List<T> list(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables,
            Expression<T> projection);

    /**
     * Evaluate the given query return whether rows where matched
     *
     * @param metadata query metadata
     * @param iterables source contents
     * @return true, if at least one row was matched
     */
    boolean exists(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables);

}
