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
package com.querydsl.jdo;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;

/**
 * Common JDO expressions
 */
public final class JDOExpressions {

    /**
     * Create a new detached {@link JDOQuery} instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(expr)
     */
    public static <T> JDOQuery<T> select(Expression<T> expr) {
        return new JDOQuery<Void>().select(expr);
    }

    /**
     * Create a new detached {@link JDOQuery} instance with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    public static JDOQuery<Tuple> select(Expression<?>... exprs) {
        return new JDOQuery<Void>().select(exprs);
    }

    /**
     * Create a new detached {@link JDOQuery} instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(distinct expr)
     */
    public static <T> JDOQuery<T> selectDistinct(Expression<T> expr) {
        return select(expr).distinct();
    }

    /**
     * Create a new detached {@link JDOQuery} instance with the given projection
     *
     * @param exprs projection
     * @return select(distinct exprs)
     */
    public static JDOQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return select(exprs).distinct();
    }


    /**
     * Create a new detached {@link JDOQuery} instance with the given projection 0
     *
     * @return select(0)
     */
    public static JDOQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    /**
     * Create a new detached {@link JDOQuery} instance with the projection 1
     *
     * @return select(1)
     */
    public static JDOQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    /**
     * Create a new detached {@link JDOQuery} instance with the given projection
     *
     * @param expr projection and source
     * @param <T>
     * @return select(expr).from(expr)
     */
    public static <T> JDOQuery<T> selectFrom(EntityPath<T> expr) {
        return select(expr).from(expr);
    }

    private JDOExpressions() {}

}
