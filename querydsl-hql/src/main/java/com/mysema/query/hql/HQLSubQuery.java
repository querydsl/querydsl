/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.hql;

import com.mysema.query.QueryMetadata;

/**
 * HQLSubQuery is a subquery builder class for HQL/JPAQL
 *
 * @author tiwe
 *
 */
public final class HQLSubQuery extends AbstractHQLSubQuery<HQLSubQuery>{

    public HQLSubQuery() {
        super();
    }

    public HQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

}
