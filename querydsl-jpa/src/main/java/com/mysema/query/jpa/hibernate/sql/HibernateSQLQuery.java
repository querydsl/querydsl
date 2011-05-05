/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.hibernate.sql;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.hibernate.DefaultSessionHolder;
import com.mysema.query.jpa.hibernate.SessionHolder;
import com.mysema.query.sql.SQLTemplates;

/**
 * HibernateSQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public final class HibernateSQLQuery extends AbstractHibernateSQLQuery<HibernateSQLQuery> {

    public HibernateSQLQuery(Session session, SQLTemplates sqlTemplates) {
        super(session, sqlTemplates);
    }

    public HibernateSQLQuery(StatelessSession session, SQLTemplates sqlTemplates){
        super(session, sqlTemplates);
    }
    
    protected HibernateSQLQuery(SessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(session, sqlTemplates, metadata);
    }
    
    public HibernateSQLQuery clone(Session session){
        return new HibernateSQLQuery(new DefaultSessionHolder(session), sqlTemplates, getMetadata().clone());
    }

}
