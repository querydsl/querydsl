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
package com.mysema.query.jpa.impl;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.jpa.JPQLSerializer;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.NullExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;

/**
 * UpdateClause implementation for JPA
 *
 * @author tiwe
 *
 */
public class JPAUpdateClause implements UpdateClause<JPAUpdateClause> {

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final EntityManager entityManager;

    private final JPQLTemplates templates;

    public JPAUpdateClause(EntityManager em, EntityPath<?> entity) {
        this(em, entity, JPAProvider.getTemplates(em));
    }

    public JPAUpdateClause(EntityManager em, EntityPath<?> entity, JPQLTemplates templates) {
        this.entityManager = em;
        this.templates = templates;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForUpdate(metadata);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants, metadata.getParams());
        return query.executeUpdate();
    }

    @Override
    public <T> JPAUpdateClause set(Path<T> path, T value) {
        if (value != null) {
            metadata.addProjection(ExpressionUtils.eqConst(path, value));
        } else {
            setNull(path);
        }
        return this;
    }
    
    @Override
    public <T> JPAUpdateClause set(Path<T> path, Expression<? extends T> expression) {
        if (expression != null) {
            metadata.addProjection(ExpressionUtils.eq(path, expression));    
        } else {
            setNull(path);
        }        
        return this;
    }
    
    @Override
    public <T> JPAUpdateClause setNull(Path<T> path) {
        metadata.addProjection(ExpressionUtils.eq(path, new NullExpression<T>(path.getType())));
        return this;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public JPAUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++) {
            if (values.get(i) != null) {
                metadata.addProjection(ExpressionUtils.eqConst((Expression)paths.get(i), values.get(i)));
            } else {
                metadata.addProjection(ExpressionUtils.eq((Expression)paths.get(i), 
                        new NullExpression(paths.get(i).getType())));
            }
        }
        return this;
    }

    @Override
    public JPAUpdateClause where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);   
        }        
        return this;
    }
    
    @Override
    public String toString() {
        JPQLSerializer serializer = new JPQLSerializer(templates, entityManager);
        serializer.serializeForUpdate(metadata);
        return serializer.toString();
    }

    @Override
    public boolean isEmpty() {
        return metadata.getProjection().isEmpty();
    }

}
