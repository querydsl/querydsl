/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.jdo;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.NumberOperation;
import com.querydsl.core.types.query.NumberSubQuery;

/**
 * JDOSubQuery is subquery implementation for JDOQL
 *
 * @author tiwe
 *
 */
public class JDOSubQuery extends AbstractJDOSubQuery<JDOSubQuery> {
    
    public JDOSubQuery() {
        super();
    }

    public JDOSubQuery(QueryMetadata metadata) {
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
