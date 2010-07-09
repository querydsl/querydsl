/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql.hibernate;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.hql.HQLSerializer;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.hql.JPQLTemplates;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.NullExpr;
import com.mysema.query.types.path.PEntity;

/**
 * DeleteClause implementation for Hibernate
 *
 * @author tiwe
 *
 */
public class HibernateDeleteClause implements DeleteClause<HibernateDeleteClause>{

    private final QueryMetadata md = new DefaultQueryMetadata();

    private final SessionHolder session;

    private final JPQLTemplates templates;

    public HibernateDeleteClause(Session session, PEntity<?> entity){
        this(new DefaultSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateDeleteClause(StatelessSession session, PEntity<?> entity){
        this(new StatelessSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateDeleteClause(SessionHolder session, PEntity<?> entity, JPQLTemplates templates){
        this.session = session;
        this.templates = templates;
        md.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForDelete(md);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = session.createQuery(serializer.toString());
        HibernateUtil.setConstants(query, constants, md.getParams());
        return query.executeUpdate();
    }

    @Override
    public <T> HibernateDeleteClause set(Path<T> path, T value) {
        if (value != null){
            md.addWhere(path.asExpr().eq(value));
        }else{
            md.addWhere(path.isNull());
        }
        return this;
    }
    
    @Override
    public HibernateDeleteClause where(EBoolean... o) {
        md.addWhere(o);
        return this;
    }
    
    @Override
    public String toString(){
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForDelete(md);
        return serializer.toString();
    }

}
