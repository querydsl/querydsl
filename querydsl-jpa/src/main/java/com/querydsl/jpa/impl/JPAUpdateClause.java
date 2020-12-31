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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;

import com.querydsl.core.JoinType;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
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
public class JPAUpdateClause implements UpdateClause<JPAUpdateClause> {

    private final QueryMixin<?> queryMixin = new JPAQueryMixin<Void>();

    private final Map<Path<?>, Expression<?>> updates = new LinkedHashMap<>();

    private final EntityManager entityManager;

    private final JPQLTemplates templates;

    @Nullable
    private LockModeType lockMode;

    public JPAUpdateClause(EntityManager em, EntityPath<?> entity) {
        this(em, entity, JPAProvider.getTemplates(em));
    }

    public JPAUpdateClause(EntityManager em, EntityPath<?> entity, JPQLTemplates templates) {
        this.entityManager = em;
        this.templates = templates;
        queryMixin.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForUpdate(queryMixin.getMetadata(), updates);
        Map<Object,String> constants = serializer.getConstantToAllLabels();

        Query query = entityManager.createQuery(serializer.toString());
        if (lockMode != null) {
            query.setLockMode(lockMode);
        }
        JPAUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        return query.executeUpdate();
    }

    @Override
    public <T> JPAUpdateClause set(Path<T> path, T value) {
        if (value != null) {
            updates.put(path, Expressions.constant(value));
        } else {
            setNull(path);
        }
        return this;
    }

    @Override
    public <T> JPAUpdateClause set(Path<T> path, Expression<? extends T> expression) {
        if (expression != null) {
            updates.put(path, expression);
        } else {
            setNull(path);
        }
        return this;
    }

    @Override
    public <T> JPAUpdateClause setNull(Path<T> path) {
        updates.put(path, Expressions.nullExpression(path));
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public JPAUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++) {
            if (values.get(i) != null) {
                updates.put(paths.get(i), Expressions.constant(values.get(i)));
            } else {
                updates.put(paths.get(i), Expressions.nullExpression(paths.get(i)));
            }
        }
        return this;
    }

    @Override
    public JPAUpdateClause where(Predicate... o) {
        for (Predicate p : o) {
            queryMixin.where(p);
        }
        return this;
    }

    public JPAUpdateClause setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return this;
    }

    @Override
    public String toString() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForUpdate(queryMixin.getMetadata(), updates);
        return serializer.toString();
    }

    @Override
    public boolean isEmpty() {
        return updates.isEmpty();
    }

}
