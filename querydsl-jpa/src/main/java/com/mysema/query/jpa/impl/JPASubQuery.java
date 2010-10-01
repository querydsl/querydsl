/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa.impl;

import com.mysema.query.QueryMetadata;
import com.mysema.query.jpa.AbstractJPQLSubQuery;
import com.mysema.query.jpa.JPQLCommonQuery;

/**
 * JPASubQuery is a subquery class for JPA
 *
 * @author tiwe
 *
 */
public final class JPASubQuery extends AbstractJPQLSubQuery<JPASubQuery> implements JPQLCommonQuery<JPASubQuery>{

    public JPASubQuery() {
        super();
    }

    public JPASubQuery(QueryMetadata metadata) {
        super(metadata);
    }

}
