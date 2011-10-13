/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.jdo;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.query.NumberSubQuery;

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
    
    @Override
    public BooleanExpression exists() {
        return count().gt(0l);
    }
    
    @Override
    public BooleanExpression notExists() {
        return count().eq(0l);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public NumberSubQuery<Long> count() {
        Expression<?> target = queryMixin.getMetadata().getJoins().get(0).getTarget();
        if (target instanceof Operation && ((Operation)target).getOperator() == Ops.ALIAS) {
            target = ((Operation)target).getArg(1);
        }
        return unique(NumberOperation.create(Long.class, Ops.AggOps.COUNT_AGG, target));  
    }

}
