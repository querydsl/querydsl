/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jpa.hibernate;

import java.util.HashMap;
import java.util.Map;

import com.querydsl.core.JoinType;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.jpa.HQLTemplates;
import com.querydsl.jpa.JPAQueryMixin;
import com.querydsl.jpa.JPQLSerializer;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

/**
 * DeleteClause implementation for Hibernate
 *
 * @author tiwe
 *
 */
public class HibernateDeleteClause implements DeleteClause<HibernateDeleteClause> {

    private final QueryMixin<?> queryMixin = new JPAQueryMixin<Void>();

    private final SessionHolder session;

    private final JPQLTemplates templates;

    private final Map<Path<?>,LockMode> lockModes = new HashMap<Path<?>,LockMode>();

    public HibernateDeleteClause(Session session, EntityPath<?> entity) {
        this(new DefaultSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateDeleteClause(StatelessSession session, EntityPath<?> entity) {
        this(new StatelessSessionHolder(session), entity, HQLTemplates.DEFAULT);
    }

    public HibernateDeleteClause(Session session, EntityPath<?> entity, JPQLTemplates templates) {
        this(new DefaultSessionHolder(session), entity, templates);
    }
    
    public HibernateDeleteClause(SessionHolder session, EntityPath<?> entity, JPQLTemplates templates) {
        this.session = session;
        this.templates = templates;
        queryMixin.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        JPQLSerializer serializer = new JPQLSerializer(templates, null);
        serializer.serializeForDelete(queryMixin.getMetadata());
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = session.createQuery(serializer.toString());
        for (Map.Entry<Path<?>, LockMode> entry : lockModes.entrySet()) {
            query.setLockMode(entry.getKey().toString(), entry.getValue());
        }
        HibernateUtil.setConstants(query, constants, queryMixin.getMetadata().getParams());
        return query.executeUpdate();
    }
    
    @Override
    public HibernateDeleteClause where(Predicate... o) {
        for (Predicate p : o) {
            queryMixin.where(p);
        }        
        return this;
    }

    /**
     * Set the lock mode for the given path.
     */
    @SuppressWarnings("unchecked")
    public HibernateDeleteClause setLockMode(Path<?> path, LockMode lockMode) {
        lockModes.put(path, lockMode);
        return this;
    }

    @Override
    public String toString() {
        JPQLSerializer serializer = new JPQLSerializer(templates, null);
        serializer.serializeForDelete(queryMixin.getMetadata());
        return serializer.toString();
    }

}
