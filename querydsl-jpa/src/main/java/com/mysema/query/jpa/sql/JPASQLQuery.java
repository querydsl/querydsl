/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.sql;

import javax.persistence.EntityManager;

import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.impl.DefaultSessionHolder;
import com.mysema.query.jpa.impl.JPASessionHolder;
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLTemplates;

/**
 * JPASQLQuery is an SQLQuery implementation that uses Hibernate's Native SQL functionality
 * to execute queries
 *
 * @author tiwe
 *
 */
public final class JPASQLQuery extends AbstractJPASQLQuery<JPASQLQuery> implements SQLCommonQuery<JPASQLQuery> {

    public JPASQLQuery(EntityManager entityManager, SQLTemplates sqlTemplates) {
        super(entityManager, sqlTemplates);
    }

    public JPASQLQuery(JPASessionHolder session, SQLTemplates sqlTemplates, QueryMetadata metadata) {
        super(session, sqlTemplates, metadata);
    }
    
    public JPASQLQuery clone(EntityManager entityManager) {
        JPASQLQuery q = new JPASQLQuery(new DefaultSessionHolder(entityManager), sqlTemplates, getMetadata().clone());
        q.flushMode = flushMode;
        q.hints.putAll(hints);
        q.lockMode = lockMode;
        return q;
    }

}
