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
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;

/**
 * UpdateClause implementation for JPA
 * 
 * @author tiwe
 *
 */
public class JPAUpdateClause implements UpdateClause<JPAUpdateClause>{

    private static final HQLTemplates DEFAULT_TEMPLATES = new HQLTemplates();
    
    private final QueryMetadata md = new DefaultQueryMetadata();
    
    private final EntityManager em;
    
    private final HQLTemplates templates;
    
    public JPAUpdateClause(EntityManager session, PEntity<?> entity){
        this(session, entity, DEFAULT_TEMPLATES);
    }
    
    public JPAUpdateClause(EntityManager em, PEntity<?> entity, HQLTemplates templates){
        this.em = em;
        this.templates = templates;
        md.addFrom(entity);        
    }
    
    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForUpdate(md);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = em.createQuery(serializer.toString());
        JPAUtil.setConstants(query, constants);
        return query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> JPAUpdateClause set(Path<T> path, T value) {
        md.addProjection(((Expr<T>)path).eq(value));
        return this;
    }

    @Override
    public JPAUpdateClause where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }

}
