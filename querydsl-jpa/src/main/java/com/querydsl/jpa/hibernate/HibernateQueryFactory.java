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

import com.querydsl.core.Tuple;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPQLQueryFactory;
import com.querydsl.jpa.JPQLTemplates;

import java.util.function.Supplier;

/**
 * Factory class for query and DML clause creation
 *
 * @author tiwe
 *
 */
public class HibernateQueryFactory implements JPQLQueryFactory {

    private final JPQLTemplates templates;

    private final Supplier<Session> session;

    public HibernateQueryFactory(Session session) {
        this(HQLTemplates.DEFAULT, session);
    }

    public HibernateQueryFactory(JPQLTemplates templates, final Session session) {
        this.session = () -> session;
        this.templates = templates;
    }

    public HibernateQueryFactory(Supplier<Session> session) {
        this(HQLTemplates.DEFAULT, session);
    }

    public HibernateQueryFactory(JPQLTemplates templates, Supplier<Session> session) {
        this.session = session;
        this.templates = templates;
    }

    @Override
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

    @Override
    public <T> HibernateQuery<T> selectDistinct(Expression<T> expr) {
        return select(expr).distinct();
    }

    @Override
    public HibernateQuery<Tuple> selectDistinct(Expression<?>... exprs) {
        return select(exprs).distinct();
    }

    @Override
    public HibernateQuery<Integer> selectOne() {
        return select(Expressions.ONE);
    }

    @Override
    public HibernateQuery<Integer> selectZero() {
        return select(Expressions.ZERO);
    }

    @Override
    public <T> HibernateQuery<T> selectFrom(EntityPath<T> from) {
        return select(from).from(from);
    }

    @Override
    public HibernateQuery<?> from(EntityPath<?> from) {
        return query().from(from);
    }

    @Override
    public HibernateQuery<?> from(EntityPath<?>... from) {
        return query().from(from);
    }

    @Override
    public HibernateUpdateClause update(EntityPath<?> path) {
        return new HibernateUpdateClause(session.get(), path, templates);
    }

    @Override
    public HibernateInsertClause insert(EntityPath<?> path) {
        return new HibernateInsertClause(session.get(), path, templates);
    }

    @Override
    public HibernateQuery<?> query() {
        return new HibernateQuery<Void>(session.get(), templates);
    }

}
