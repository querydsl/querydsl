/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.jpa;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.hql.HQLSerializer;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.JPQLTemplates;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
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
        metadata.addJoin(JoinType.DEFAULT, entity.asExpr());
    }

    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForUpdate(metadata);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants, metadata.getParams());
        return query.executeUpdate();
    }

    @Override
    public <T> JPAUpdateClause set(Path<T> path, T value) {
        if (value != null){
            metadata.addProjection(path.asExpr().eq(value));
        }else{
            metadata.addProjection(path.asExpr().eq(new NullExpr<T>(path.getType())));
        }

        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public JPAUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++){
            if (values.get(i) != null){
                metadata.addProjection(((Expr)paths.get(i).asExpr()).eq(values.get(i)));
            }else{
                metadata.addProjection(((Expr)paths.get(i).asExpr()).eq(new NullExpr(paths.get(i).getType())));
            }
        }
        return this;
    }

    @Override
    public JPAUpdateClause where(EBoolean... o) {
        metadata.addWhere(o);
        return this;
    }
    
    @Override
    public String toString(){
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForUpdate(metadata);
        return serializer.toString();
    }

}
