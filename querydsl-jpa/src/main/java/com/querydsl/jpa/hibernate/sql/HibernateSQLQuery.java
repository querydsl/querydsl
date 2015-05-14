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
package com.querydsl.jpa.hibernate.sql;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.hibernate.SessionHolder;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLTemplates;

/**
 * {@code HibernateSQLQuery} is an SQLQuery implementation that uses Hibernate's Native SQL functionality
 * to execute queries
 *
 * @param <T> result type
 *
 * @author tiwe
 *
 */
public class HibernateSQLQuery<T> extends AbstractHibernateSQLQuery<T, HibernateSQLQuery<T>> {

    public HibernateSQLQuery(Session session, SQLTemplates sqlTemplates) {
        super(session, new Configuration(sqlTemplates));
    }

    public HibernateSQLQuery(Session session, Configuration conf) {
        super(session, conf);
    }

    public HibernateSQLQuery(StatelessSession session, SQLTemplates sqlTemplates) {
        super(session, new Configuration(sqlTemplates));
    }

    public HibernateSQLQuery(StatelessSession session, Configuration conf) {
        super(session, conf);
    }

    public HibernateSQLQuery(SessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(session, new Configuration(sqlTemplates), metadata);
    }

    public HibernateSQLQuery(SessionHolder session, Configuration conf, QueryMetadata metadata) {
        super(session, conf, metadata);
    }

    @Override
    protected HibernateSQLQuery<T> clone(SessionHolder sessionHolder) {
        HibernateSQLQuery<T> q = new HibernateSQLQuery<T>(sessionHolder, configuration, getMetadata().clone());
        q.clone(this);
        return q;
    }

    @Override
    public <U> HibernateSQLQuery<U> select(Expression<U> expr) {
        queryMixin.setProjection(expr);
        return (HibernateSQLQuery<U>) this;
    }

    @Override
    public HibernateSQLQuery<Tuple> select(Expression<?>... exprs) {
        queryMixin.setProjection(exprs);
        return (HibernateSQLQuery<Tuple>) this;
    }

}
