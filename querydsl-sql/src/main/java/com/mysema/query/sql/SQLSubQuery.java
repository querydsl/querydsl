/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expr;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.OSimple;

/**
 * SQLSubQuery is a subquery implementation for SQL queries
 *
 * @author tiwe
 *
 */
public class SQLSubQuery extends AbstractSQLSubQuery<SQLSubQuery> implements SQLCommonQuery<SQLSubQuery>{

    public SQLSubQuery() {
        super();
    }

    public SQLSubQuery(QueryMetadata metadata) {
        super(metadata);
    }

    public Expr<?> union(SubQuery<?>... sq){
        Expr<?> rv = sq[0].asExpr();
        for (int i = 1; i < sq.length; i++){
            rv = OSimple.create(rv.getType(), SQLTemplates.UNION, rv, sq[i].asExpr());
        }
        return rv;
    }
}
