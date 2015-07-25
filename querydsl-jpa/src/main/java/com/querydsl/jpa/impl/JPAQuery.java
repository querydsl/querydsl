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
package com.querydsl.jpa.impl;

import javax.persistence.EntityManager;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLTemplates;

/**
 * {@code JPAQuery} is the default implementation of the {@link JPQLQuery} interface for JPA
 *
 * @param <T> result type
 *
 * @author tiwe
 */
public class JPAQuery<T> extends AbstractJPAQuery<T, JPAQuery<T>> {

    /**
     * Creates a new detached query
     * The query can be attached via the clone method
     */
    public JPAQuery() {
        super(null, JPQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param em entity manager
     */
    public JPAQuery(EntityManager em) {
        super(em, JPAProvider.getTemplates(em), new DefaultQueryMetadata());
    }

    /**
     * Creates a new EntityManager bound query
     *
     * @param em entity manager
     * @param metadata query metadata
     */
    public JPAQuery(EntityManager em, QueryMetadata metadata) {
        super(em, JPAProvider.getTemplates(em), metadata);
    }

    /**
     * Creates a new query
     *
     * @param em entity manager
     * @param templates templates
     */
    public JPAQuery(EntityManager em, JPQLTemplates templates) {
        super(em, templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new query
     *
     * @param em entity manager
     * @param templates templates
     * @param metadata query metadata
     */
    public JPAQuery(EntityManager em, JPQLTemplates templates, QueryMetadata metadata) {
        super(em, templates, metadata);
    }

    @Override
    public JPAQuery<T> clone(EntityManager entityManager, JPQLTemplates templates) {
        JPAQuery<T> q = new JPAQuery<T>(entityManager, templates, getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public JPAQuery<T> clone(EntityManager entityManager) {
        return clone(entityManager, JPAProvider.getTemplates(entityManager));
    }

    @Override
    public <U> JPAQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        return (JPAQuery<U>) this;
    }

    @Override
    public JPAQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (JPAQuery<Tuple>) this;
    }
}
