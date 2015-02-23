/*
 * Copyright 2011, Mysema Ltd
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

import javax.inject.Provider;

import org.hibernate.Session;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.JPQLTemplates;

/**
 * Factory class for query and DML clause creation
 *
 * @author tiwe
 *
 */
public class HibernateQueryFactory implements JPQLQueryFactory {

    private final JPQLTemplates templates;

    private final Provider<Session> session;

    public HibernateQueryFactory(Provider<Session> session) {
        this(HQLTemplates.DEFAULT, session);
    }

    public HibernateQueryFactory(JPQLTemplates templates, Provider<Session> session) {
        this.session = session;
        this.templates = templates;
    }

    public HibernateDeleteClause delete(EntityPath<?> path) {
        return new HibernateDeleteClause(session.get(), path, templates);
    }

    @Override
    public <T> HibernateQuery<T> select(Expression<T> expr) {
        return query().select(expr);
    }

    @Override
    public HibernateQuery<Tuple> select(Expression<?>... exprs) {
        return query().select(exprs);
    }

    public HibernateQuery<Void> from(EntityPath<?> from) {
        return query().from(from);
    }

    public HibernateUpdateClause update(EntityPath<?> path) {
        return new HibernateUpdateClause(session.get(), path, templates);
    }

    public HibernateQuery<Void> query() {
        return new HibernateQuery<Void>(session.get(), templates);
    }

}
