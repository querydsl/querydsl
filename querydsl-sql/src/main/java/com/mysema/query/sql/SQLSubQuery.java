/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;
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
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> SimpleExpression<T> union(List<? extends SubQueryExpression<T>> sq) {
        Expression<T> rv = sq.get(0);
        for (int i = 1; i < sq.size(); i++) {
            rv = SimpleOperation.create((Class)rv.getType(), SQLTemplates.UNION, rv, sq.get(i));
        }
        if (rv instanceof SimpleExpression) {
            return (SimpleExpression<T>)rv;
        } else {
            return SimpleOperation.create((Class)rv.getType(), Ops.DELEGATE, rv);
        }
    }
    
    public <T> SimpleExpression<T> union(SubQueryExpression<T>... sq) {
        return union(Arrays.asList(sq));
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
