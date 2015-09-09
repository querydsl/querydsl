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
package com.querydsl.jpa.hibernate;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLTemplates;

/**
 * {@code HibernateQuery} is the default implementation of the JPQLQuery interface for Hibernate
 *
 * @param <T> result type
 *
 * @author tiwe
 */
public class HibernateQuery<T> extends AbstractHibernateQuery<T, HibernateQuery<T>> implements JPQLQuery<T> {

    /**
     * Creates a detached query
     * The query can be attached via the clone method
     */
    public HibernateQuery() {
        super(NoSessionHolder.DEFAULT, HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Session bound query
     *
     * @param session session
     */
    public HibernateQuery(Session session) {
        super(new DefaultSessionHolder(session), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Session bound query
     *
     * @param session session
     */
    public HibernateQuery(Session session, QueryMetadata metadata) {
        super(new DefaultSessionHolder(session), HQLTemplates.DEFAULT, metadata);
    }

    /**
     * Creates a new Session bound query
     *
     * @param session session
     * @param templates templates
     */
    public HibernateQuery(Session session, JPQLTemplates templates) {
        super(new DefaultSessionHolder(session), templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Stateless session bound query
     *
     * @param session session
     */
    public HibernateQuery(StatelessSession session) {
        super(new StatelessSessionHolder(session), HQLTemplates.DEFAULT, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Session bound query
     *
     * @param session session
     * @param templates templates
     */
    public HibernateQuery(SessionHolder session, JPQLTemplates templates) {
        super(session, templates, new DefaultQueryMetadata());
    }

    /**
     * Creates a new Session bound query
     *
     * @param session session
     * @param templates templates
     * @param metadata query metadata
     */
    public HibernateQuery(SessionHolder session, JPQLTemplates templates, QueryMetadata metadata) {
        super(session, templates, metadata);
    }

    @Override
    protected HibernateQuery<T> clone(SessionHolder sessionHolder) {
        HibernateQuery<T> q = new HibernateQuery<T>(sessionHolder,
                getTemplates(), getMetadata().clone());
        q.clone(this);
        return q;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <U> HibernateQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        return (HibernateQuery<U>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HibernateQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (HibernateQuery<Tuple>) this;
    }

}
