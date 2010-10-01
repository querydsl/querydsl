/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.hibernate;

import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.AbstractJPQLSubQuery;
import com.mysema.query.jpa.JPQLCommonQuery;

/**
 * HibernateSubQuery is a subquery class for Hibernate
 *
 * @author tiwe
 *
 */
public final class HibernateSubQuery extends AbstractJPQLSubQuery<HibernateSubQuery> implements JPQLCommonQuery<HibernateSubQuery>{

    public HibernateSubQuery() {
        super();
    }

    public HibernateSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

}
