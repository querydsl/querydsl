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
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.hql.HQLSerializer;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * DeleteClause implementation for JPA
 * 
 * @author tiwe
 *
 */
public class JPADeleteClause implements DeleteClause<JPADeleteClause>{

    private final QueryMetadata metadata = new DefaultQueryMetadata();
    
    private final EntityManager entityManager;
    
    private final HQLTemplates templates;
    
    public JPADeleteClause(EntityManager em, PEntity<?> entity){
        this(em, entity, HQLTemplates.DEFAULT);
    }
    
    public JPADeleteClause(EntityManager entityManager, PEntity<?> entity, HQLTemplates templates){
        this.entityManager = entityManager;
        this.templates = templates;
        metadata.addFrom(entity);        
    }
    
    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForDelete(metadata);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants);
        return query.executeUpdate();
    }

    @Override
    public JPADeleteClause where(EBoolean... o) {
        metadata.addWhere(o);
        return this;
    }

}
