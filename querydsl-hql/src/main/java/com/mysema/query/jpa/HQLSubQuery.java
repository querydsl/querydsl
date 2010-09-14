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
public final class HQLSubQuery extends AbstractHQLSubQuery<HQLSubQuery> implements HQLCommonQuery<HQLSubQuery>{

    public HQLSubQuery() {
        super();
    }

    public HQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

}
