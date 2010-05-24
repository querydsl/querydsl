/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.hql.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.hql.HQLSerializer;
import com.mysema.query.hql.HQLTemplates;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;

/**
 * UpdateClause implementation for JPA
 * 
 * @author tiwe
 * 
 */
public class HibernateUpdateClause implements
        UpdateClause<HibernateUpdateClause> {

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final SessionHolder session;

    private final HQLTemplates templates;

    public HibernateUpdateClause(Session session, PEntity<?> entity) {
        this(new DefaultSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateUpdateClause(StatelessSession session, PEntity<?> entity) {
        this(new StatelessSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateUpdateClause(SessionHolder session, PEntity<?> entity,
            HQLTemplates templates) {
        this.session = session;
        this.templates = templates;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        HQLSerializer serializer = new HQLSerializer(templates);
        serializer.serializeForUpdate(metadata);
        Map<Object, String> constants = serializer.getConstantToLabel();

        Query query = session.createQuery(serializer.toString());
        HibernateUtil.setConstants(query, constants);
        return query.executeUpdate();
    }

    @Override
    public <T> HibernateUpdateClause set(Path<T> path, T value) {
        metadata.addProjection(path.asExpr().eq(value));
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HibernateUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++){
            metadata.addProjection(((Expr)paths.get(i).asExpr()).eq(values.get(i)));
        }
        return this;
    }

    @Override
    public HibernateUpdateClause where(EBoolean... o) {
        metadata.addWhere(o);
        return this;
    }

}
