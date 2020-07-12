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
package com.querydsl.r2dbc;

import com.querydsl.core.ReactiveFetchable;
import com.querydsl.core.types.*;
import reactor.core.publisher.Flux;

/**
 * {@code Union} defines an interface for Union queries
 *
 * @param <RT> return type of projection
 * @author mc_fish
 */
public interface Union<RT> extends SubQueryExpression<RT>, ReactiveFetchable<RT> {

    /**
     * Get the projection as a typed List
     *
     * @deprecated Use {@link Union#fetch()}
     */
    @Deprecated
    Flux<RT> list();

    /**
     * Defines the grouping/aggregation expressions
     *
     * @param o group by
     * @return the current object
     */
    Union<RT> groupBy(Expression<?>... o);

    /**
     * Defines the filters for aggregation
     *
     * @param o having conditions
     * @return the current object
     */
    Union<RT> having(Predicate... o);


    /**
     * Define the ordering of the query results
     *
     * @param o order
     * @return the current object
     */
    Union<RT> orderBy(OrderSpecifier<?>... o);

    /**
     * Create an alias for the expression
     *
     * @param alias alias
     * @return this as alias
     */
    Expression<RT> as(String alias);

    /**
     * Create an alias for the expression
     *
     * @param alias alias
     * @return this as alias
     */
    Expression<RT> as(Path<RT> alias);
}
