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
package com.mysema.query.jpa.hibernate;

import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.DeleteClause;
import com.mysema.query.jpa.JPQLSerializer;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLTemplates;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Predicate;

/**
 * DeleteClause implementation for Hibernate
 *
 * @author tiwe
 *
 */
public class HibernateDeleteClause implements DeleteClause<HibernateDeleteClause> {

    private final QueryMetadata md = new DefaultQueryMetadata();

    private final SessionHolder session;

    private final JPQLTemplates templates;

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
        md.addJoin(JoinType.DEFAULT, entity);
    }

    @Override
    public long execute() {
        JPQLSerializer serializer = new JPQLSerializer(templates, null);
        serializer.serializeForDelete(md);
        Map<Object,String> constants = serializer.getConstantToLabel();

        Query query = session.createQuery(serializer.toString());
        HibernateUtil.setConstants(query, constants, md.getParams());
        return query.executeUpdate();
    }
    
    @Override
    public HibernateDeleteClause where(Predicate... o) {
        for (Predicate p : o) {
            md.addWhere(p);    
        }        
        return this;
    }
    
    @Override
    public String toString() {
        JPQLSerializer serializer = new JPQLSerializer(templates, null);
        serializer.serializeForDelete(md);
        return serializer.toString();
    }

}
