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
package com.querydsl.core.group;

import com.querydsl.core.types.Expression;

/**
 * Defines the way results of a given expression are grouped. GroupExpressions are also used
 * to access values of a given GroupExpression within a Group.
 * GroupExpressions are stateless wrappers for Expressions that know how to
 * collect row values into a group.
 *
 * @param <T> Element type
 * @param <R> Target type (e.g. List, Set)
 *
 * @author sasa
 * @author tiwe
 */
public interface GroupExpression<T, R> extends Expression<R> {

    /**
     * Get the expression wrapped by this group definition
     *
     * @return wrapped expression
     */
    Expression<T> getExpression();

    /**
     * Create a new GroupCollector to collect values belonging to this group.
     *
     * @return new GroupCollector
     */
    GroupCollector<T, R> createGroupCollector();


}
