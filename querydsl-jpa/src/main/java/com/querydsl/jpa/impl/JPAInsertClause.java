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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.google.common.collect.Maps;
import com.querydsl.core.JoinType;
import com.querydsl.core.dml.InsertClause;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAQueryMixin;
import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.JPQLTemplates;

/**
 * UpdateClause implementation for JPA
 *
 * @author tiwe
 *
 */
public class JPAInsertClause implements InsertClause<JPAInsertClause> {

    private final QueryMixin<?> queryMixin = new JPAQueryMixin<Void>();

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final Map<Path<?>, Expression<?>> inserts = Maps.newLinkedHashMap();

    private final List<Expression<?>> values = new ArrayList<Expression<?>>();

    private final EntityManager entityManager;

    private final JPQLTemplates templates;

    private SubQueryExpression<?> subQuery;

    @Nullable
    private LockModeType lockMode;

    public JPAInsertClause(EntityManager em, EntityPath<?> entity) {
        this(em, entity, JPAProvider.getTemplates(em));
    }

    public JPAInsertClause(EntityManager em, EntityPath<?> entity, JPQLTemplates templates) {
        this.entityManager = em;
        this.templates = templates;
        queryMixin.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForInsert(queryMixin.getMetadata(), columns, subQuery, inserts);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        if (lockMode != null) {
            query.setLockMode(lockMode);
        }
        JPAUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        return query.executeUpdate();
    }

    public JPAInsertClause setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    @Override
    public String toString() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForInsert(queryMixin.getMetadata(), columns, subQuery, inserts);
        return serializer.toString();
    }

    @Override
    public JPAInsertClause columns(Path<?>... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    @Override
    public JPAInsertClause select(SubQueryExpression<?> sq) {
        subQuery = sq;
        return this;
    }

    @Override
    public JPAInsertClause values(Object... v) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> JPAInsertClause set(Path<T> path, T value) {
        if (value != null) {
            inserts.put(path, Expressions.constant(value));
        } else {
            setNull(path);
        }
        return this;
    }

    @Override
    public <T> JPAInsertClause set(Path<T> path, Expression<? extends T> expression) {
        if (expression != null) {
            inserts.put(path, expression);
        } else {
            setNull(path);
        }
        return this;
    }

    @Override
    public <T> JPAInsertClause setNull(Path<T> path) {
        inserts.put(path, Expressions.nullExpression(path));
        return this;
    }

}
