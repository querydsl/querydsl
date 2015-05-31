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

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;

/**
 * {@code CollQuery} is the default implementation of the {@link FetchableQuery} interface for collections
 *
 * @param <T> result type
 *
 * @author tiwe
 */
public class CollQuery<T> extends AbstractCollQuery<T, CollQuery<T>> implements Cloneable {

    /**
     * Create a new CollQuery instance
     */
    public CollQuery() {
        super(new DefaultQueryMetadata(), DefaultQueryEngine.getDefault());
    }

    /**
     * Creates a new CollQuery instance
     *
     * @param templates serialization templates
     */
    public CollQuery(CollQueryTemplates templates) {
        this(new DefaultQueryEngine(new DefaultEvaluatorFactory(templates)));
    }

    /**
     * Create a new CollQuery instance
     *
     * @param queryEngine query engine for query execution
     */
    public CollQuery(QueryEngine queryEngine) {
        super(new DefaultQueryMetadata(), queryEngine);
    }


    /**
     * Create a new CollQuery instance
     *
     * @param metadata query metadata
     */
    public CollQuery(QueryMetadata metadata) {
        super(metadata, DefaultQueryEngine.getDefault());
    }

    /**
     * Create a new CollQuery instance
     *
     * @param metadata query metadata
     * @param queryEngine query engine for query execution
     */
    public CollQuery(QueryMetadata metadata, QueryEngine queryEngine) {
        super(metadata, queryEngine);
    }

    /**
     * Clone the state of this query to a new CollQuery instance
     */
    @Override
    public CollQuery<T> clone() {
        return new CollQuery<T>(queryMixin.getMetadata().clone(), getQueryEngine());
    }

    @Override
    public <E> CollQuery<E> select(Expression<E> expr) {
        queryMixin.setProjection(expr);
        return (CollQuery<E>) this;
    }

    @Override
    public CollQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (CollQuery<Tuple>) this;
    }
}
