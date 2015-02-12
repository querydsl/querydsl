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
package com.querydsl.jpa.impl;

import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.Query;
import java.util.Map;

import com.querydsl.core.JoinType;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.jpa.JPAQueryMixin;
import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;

/**
 * DeleteClause implementation for JPA
 *
 * @author tiwe
 *
 */
public class JPADeleteClause implements DeleteClause<JPADeleteClause> {

    private final QueryMixin<?> queryMixin = new JPAQueryMixin<Void>();

    private final EntityManager entityManager;

    private final JPQLTemplates templates;

    @Nullable
    private LockModeType lockMode;

    public JPADeleteClause(EntityManager em, EntityPath<?> entity) {
        this(em, entity, JPAProvider.getTemplates(em));
    }

    public JPADeleteClause(EntityManager entityManager, EntityPath<?> entity, JPQLTemplates templates) {
        this.entityManager = entityManager;
        this.templates = templates;
        queryMixin.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForDelete(queryMixin.getMetadata());
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        if (lockMode != null) {
            query.setLockMode(lockMode);
        }
        JPAUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        return query.executeUpdate();
    }
    
    @Override
    public JPADeleteClause where(Predicate... o) {
        for (Predicate p : o) {
            queryMixin.where(p);
        }        
        return this;
    }

    public JPADeleteClause setLockMode(LockModeType lockMode) {
        this.lockMode = lockMode;
        return this;
    }
    
    @Override
    public String toString() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForDelete(queryMixin.getMetadata());
        return serializer.toString();
    }

}
