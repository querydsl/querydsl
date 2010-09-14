/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jpa;

import com.mysema.query.QueryMetadata;

/**
 * HQLSubQuery is a subquery class for JPQL
 *
 * @author tiwe
 *
 */
public final class JPQLSubQuery extends AbstractJQLSubQuery<JPQLSubQuery> implements JPQLCommonQuery<JPQLSubQuery>{

    public JPQLSubQuery() {
        super();
    }

    public JPQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

}
