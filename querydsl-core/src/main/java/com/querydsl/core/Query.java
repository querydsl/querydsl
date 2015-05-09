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
package com.querydsl.core;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;

/**
 * {@code Query} defines the main query interface of the fluent query language.
 *
 * <p>Note that the from method has been left out, since there are implementation
 * specific variants of it.</p>
 *
 * @author tiwe
 * @see SimpleQuery
 */
public interface Query<Q extends Query<Q>> extends SimpleQuery<Q> {

    /**
     * Add grouping/aggregation expressions
     *
     * @param o group by expressions
     * @return the current object
     */
    Q groupBy(Expression<?>... o);

    /**
     * Add filters for aggregation
     *
     * @param o having conditions
     * @return the current object
     */
    Q having(Predicate... o);

}
