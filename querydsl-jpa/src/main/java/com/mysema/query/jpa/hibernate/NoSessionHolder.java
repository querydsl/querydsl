/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.hibernate;

import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * NoSessionHolder is a session holder for detached HibernateQuery usage
 *
 * @author tiwe
 *
 */
public final class NoSessionHolder implements SessionHolder {

    public static final SessionHolder DEFAULT = new NoSessionHolder();

    private NoSessionHolder(){}

    @Override
    public Query createQuery(String queryString) {
        throw new UnsupportedOperationException("No session in detached Query available");
    }

    @Override
    public SQLQuery createSQLQuery(String queryString) {
        throw new UnsupportedOperationException("No session in detached Query available");
    }

}
