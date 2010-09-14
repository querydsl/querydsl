/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.impl;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.jpa.HQLSerializer;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;

/**
 * DeleteClause implementation for JPA
 *
 * @author tiwe
 *
 */
public class JPADeleteClause implements DeleteClause<JPADeleteClause>{

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final EntityManager entityManager;

    private final JPQLTemplates templates;

    public JPADeleteClause(EntityManager em, EntityPath<?> entity){
        this(em, entity, HQLTemplates.DEFAULT);
    }

    public JPADeleteClause(EntityManager entityManager, EntityPath<?> entity, JPQLTemplates templates){
        this.entityManager = entityManager;
        this.templates = templates;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForDelete(metadata);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = entityManager.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants, metadata.getParams());
        return query.executeUpdate();
    }
    
    @Override
    public JPADeleteClause where(Predicate... o) {
        metadata.addWhere(o);
        return this;
    }
    
    @Override
    public String toString(){
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForDelete(metadata);
        return serializer.toString();
    }

}
