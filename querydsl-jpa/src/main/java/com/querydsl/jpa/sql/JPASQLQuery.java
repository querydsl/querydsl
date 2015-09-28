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
package com.querydsl.jpa.sql;

import javax.persistence.EntityManager;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.QueryHandler;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code JPASQLQuery} is an SQLQuery implementation that uses JPA Native SQL functionality
 * to execute queries
 *
 * @param <T> result type
 *
 * @author tiwe
 *
 */
public class JPASQLQuery<T> extends AbstractJPASQLQuery<T, JPASQLQuery<T>> {

    public JPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates) {
        super(entityManager, new Configuration(sqlTemplates));
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf) {
        super(entityManager, conf);
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf, QueryHandler queryHandler) {
        super(entityManager, conf, queryHandler);
    }

    public JPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(entityManager, new Configuration(sqlTemplates), metadata);
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf, QueryMetadata metadata) {
        super(entityManager, conf, metadata);
    }

    public JPASQLQuery(EntityManager entityManager, Configuration conf, QueryHandler queryHandler, QueryMetadata metadata) {
        super(entityManager, conf, queryHandler, metadata);
    }

    @Override
    public JPASQLQuery<T> clone(EntityManager entityManager) {
        JPASQLQuery<T> q = new JPASQLQuery<T>(entityManager, configuration, queryHandler, getMetadata().clone());
        q.clone(this);
        return q;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> JPASQLQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        return (JPASQLQuery<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JPASQLQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (JPASQLQuery<Tuple>) this;
    }

}
