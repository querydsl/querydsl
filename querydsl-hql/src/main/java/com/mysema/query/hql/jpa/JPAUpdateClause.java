/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.jpa;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.hql.HQLSerializer;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * UpdateClause implementation for JPA
 * 
 * @author tiwe
 *
 */
public class JPAUpdateClause implements UpdateClause<JPAUpdateClause>{

    private final QueryMetadata metadata = new DefaultQueryMetadata();
    
    private final EntityManager entityManager;
    
    private final HQLTemplates templates;
    
    public JPAUpdateClause(EntityManager session, PEntity<?> entity){
        this(session, entity, HQLTemplates.DEFAULT);
    }
    
    public JPAUpdateClause(EntityManager em, PEntity<?> entity, HQLTemplates templates){
        this.entityManager = em;
        this.templates = templates;
        metadata.addFrom(entity);        
    }
    
    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForUpdate(metadata);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants);
        return query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> JPAUpdateClause set(Path<T> path, T value) {
        metadata.addProjection(path.asExpr().eq(value));
        return this;
    }

    @Override
    public JPAUpdateClause where(EBoolean... o) {
        metadata.addWhere(o);
        return this;
    }

}
