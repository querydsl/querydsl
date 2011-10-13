/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.template.NumberTemplate;

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

    public Expression<?> union(SubQueryExpression<?>... sq) {
        Expression<?> rv = sq[0];
        for (int i = 1; i < sq.length; i++) {
            rv = OperationImpl.create(rv.getType(), SQLTemplates.UNION, rv, sq[i]);
        }
        return rv;
    }
    
    @Override
    public BooleanExpression exists() {
        return unique(NumberTemplate.ONE).exists();
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

}
