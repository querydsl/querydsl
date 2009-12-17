/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

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
public class HibernateUpdateClause implements UpdateClause<HibernateUpdateClause>{

    private static final HQLTemplates DEFAULT_TEMPLATES = new HQLTemplates();
    
    private final QueryMetadata md = new DefaultQueryMetadata();
    
    private final Session session;
    
    private final HQLTemplates templates;
    
    public HibernateUpdateClause(Session session, PEntity<?> entity){
        this(session, entity, DEFAULT_TEMPLATES);
    }
    
    public HibernateUpdateClause(Session session, PEntity<?> entity, HQLTemplates templates){
        this.session = session;
        this.templates = templates;
        md.addFrom(entity);        
    }
    
    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForUpdate(md);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = session.createQuery(serializer.toString());
        HibernateUtil.setConstants(query, constants);
        return query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> HibernateUpdateClause set(Path<T> path, T value) {
        md.addProjection(((Expr<T>)path).eq(value));
        return this;
    }

    @Override
    public HibernateUpdateClause where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }

}
