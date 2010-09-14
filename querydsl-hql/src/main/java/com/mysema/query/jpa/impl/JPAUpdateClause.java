/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
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
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NullExpr;

/**
 * UpdateClause implementation for JPA
 *
 * @author tiwe
 *
 */
public class JPAUpdateClause implements UpdateClause<JPAUpdateClause>{

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final EntityManager entityManager;

    private final JPQLTemplates templates;

    public JPAUpdateClause(EntityManager session, EntityPath<?> entity){
        this(session, entity, HQLTemplates.DEFAULT);
    }

    public JPAUpdateClause(EntityManager em, EntityPath<?> entity, JPQLTemplates templates){
        this.entityManager = em;
        this.templates = templates;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        JPQLSerializer serializer = new JPQLSerializer(templates);
        serializer.serializeForUpdate(metadata);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants, metadata.getParams());
        return query.executeUpdate();
    }

    @Override
    public <T> JPAUpdateClause set(Path<T> path, T value) {
        if (value != null){
            metadata.addProjection(ExpressionUtils.eqConst(path, value));
        }else{
            metadata.addProjection(ExpressionUtils.eq(path, new NullExpr<T>(path.getType())));
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JPAUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++){
            if (values.get(i) != null){
                metadata.addProjection(ExpressionUtils.eqConst((Expression)paths.get(i), values.get(i)));
            }else{
                metadata.addProjection(ExpressionUtils.eq((Expression)paths.get(i), new NullExpr(paths.get(i).getType())));
            }
        }
        return this;
    }

    @Override
    public JPAUpdateClause where(Predicate... o) {
        metadata.addWhere(o);
        return this;
    }
    
    @Override
    public String toString(){
        JPQLSerializer serializer = new JPQLSerializer(templates);
        serializer.serializeForUpdate(metadata);
        return serializer.toString();
    }

}
