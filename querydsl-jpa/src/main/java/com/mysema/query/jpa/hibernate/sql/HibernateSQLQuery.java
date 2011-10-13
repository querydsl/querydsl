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
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLTemplates;

/**
 * HibernateSQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public final class HibernateSQLQuery extends AbstractHibernateSQLQuery<HibernateSQLQuery> implements SQLCommonQuery<HibernateSQLQuery> {

    public HibernateSQLQuery(Session session, SQLTemplates sqlTemplates) {
        super(session, sqlTemplates);
    }

    public HibernateSQLQuery(StatelessSession session, SQLTemplates sqlTemplates) {
        super(session, sqlTemplates);
    }
    
    public HibernateSQLQuery(SessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(session, sqlTemplates, metadata);
    }
    
    public HibernateSQLQuery clone(Session session) {
        HibernateSQLQuery q = new HibernateSQLQuery(new DefaultSessionHolder(session), sqlTemplates, getMetadata().clone());
        q.cacheable = cacheable;
        q.cacheRegion = cacheRegion;
        q.fetchSize = fetchSize;
        q.readOnly = readOnly;
        q.timeout = timeout;
        return q;
    }

}
