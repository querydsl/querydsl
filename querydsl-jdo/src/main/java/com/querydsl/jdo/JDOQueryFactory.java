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

import javax.inject.Provider;
import javax.jdo.PersistenceManager;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jdo.dml.JDODeleteClause;

/**
 * Factory class for query and DML clause creation
 *
 * @author tiwe
 *
 */
public class JDOQueryFactory implements QueryFactory<JDOQuery<?>> {

    private final Provider<PersistenceManager> persistenceManager;

    public JDOQueryFactory(Provider<PersistenceManager> persistenceManager) {
        this.persistenceManager = persistenceManager;
    }

    public JDODeleteClause delete(EntityPath<?> path) {
        return new JDODeleteClause(persistenceManager.get(), path);
    }

    /**
     * Create a new {@link JDOQuery} instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(expr)
     */
    public <T> JDOQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    /**
     * Create a new {@link JDOQuery} instance with the given projection
     *
     * @param exprs projection
     * @return select(exprs)
     */
    public JDOQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    /**
     * Create a new {@link JDOQuery} instance with the given projection
     *
     * @param expr projection
     * @param <T>
     * @return select(distinct expr)
     */
    public <T> JDOQuery<T> selectDistinct(Expression<T> expr) {
        return query().select(expr).distinct();
    }

    /**
     * Create a new {@link JDOQuery} instance with the given projection
     *
     * @param exprs projection
     * @return select(distinct exprs)
     */
    public JDOQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return query().select(exprs).distinct();
    }

    /**
     * Create a new {@link JDOQuery} instance with the projection 0
     *
     * @return select(0)
     */
    public JDOQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    /**
     * Create a new {@link JDOQuery} instance with the projection 1
     *
     * @return select(1)
     */
    public JDOQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    /**
     * Create a new {@link JDOQuery} instance with the given projection
     *
     * @param expr projection and source
     * @param <T>
     * @return select(expr).from(expr)
     */
    public <T> JDOQuery<T> selectFrom(EntityPath<T> expr) {
        return select(expr).from(expr);
    }

    public JDOQuery<?> from(EntityPath<?> from) {
        return query().from(from);
    }

    @Override
    public JDOQuery<?> query() {
        return new JDOQuery<Void>(persistenceManager.get());
    }


}