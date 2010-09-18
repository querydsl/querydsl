/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo;

import com.mysema.query.QueryMetadata;

/**
 * JDOQLSubQuery is subquery implementation for JDOQL
 *
 * @author tiwe
 *
 */
public class JDOQLSubQuery extends AbstractJDOQLSubQuery<JDOQLSubQuery> implements JDOQLCommonQuery<JDOQLSubQuery>{

    public JDOQLSubQuery() {
        super();
    }

    public JDOQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

}
