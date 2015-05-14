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
package com.querydsl.sql.dml;

import java.util.Map;

import com.querydsl.core.types.Path;
import com.querydsl.sql.RelationalPath;

/**
 * Create a Map of updates for a given domain object
 *
 * @param <T> object type
 *
 * @author tiwe
 */
public interface Mapper<T> {

    /**
     * Create a map of updates for the given path and instance
     *
     * @param path path
     * @param object instance
     * @return bindings
     */
    Map<Path<?>, Object> createMap(RelationalPath<?> path, T object);

}
