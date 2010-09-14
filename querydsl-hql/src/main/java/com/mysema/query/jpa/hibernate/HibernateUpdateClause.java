/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.hibernate;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.jpa.HQLSerializer;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.path.NullExpr;

/**
 * UpdateClause implementation for Hibernate
 *
 * @author tiwe
 *
 */
public class HibernateUpdateClause implements
        UpdateClause<HibernateUpdateClause> {

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final SessionHolder session;

    private final JPQLTemplates templates;

    public HibernateUpdateClause(Session session, EntityPath<?> entity) {
        this(new DefaultSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateUpdateClause(StatelessSession session, EntityPath<?> entity) {
        this(new StatelessSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateUpdateClause(SessionHolder session, EntityPath<?> entity, JPQLTemplates templates) {
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
        HibernateUtil.setConstants(query, constants, metadata.getParams());
        return query.executeUpdate();
    }

    @Override
    public <T> HibernateUpdateClause set(Path<T> path, T value) {
        if (value != null){
            metadata.addProjection(ExpressionUtils.eqConst(path, value));
        }else{
            metadata.addProjection(ExpressionUtils.eq(path, new NullExpr<T>(path.getType())));
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public HibernateUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++){
            if (values.get(i) != null){
                metadata.addProjection(ExpressionUtils.eqConst((Expression)paths.get(i), values.get(i)));
            }else{
                metadata.addProjection(ExpressionUtils.eq(((Expression)paths.get(i)), new NullExpr(paths.get(i).getType())));
            }

        }
        return this;
    }

    @Override
    public HibernateUpdateClause where(Predicate... o) {
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
