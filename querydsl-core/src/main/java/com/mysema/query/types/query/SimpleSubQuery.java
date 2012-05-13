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
package com.mysema.query.types.query;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Ops;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.SubQueryExpressionImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.DslExpression;

/**
 * Object typed single result subquery
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public final class SimpleSubQuery<T> extends DslExpression<T> implements ExtendedSubQueryExpression<T>{

    private static final long serialVersionUID = -64156984110154969L;

    private final SubQueryExpression<T> subQueryMixin;

    @Nullable
    private volatile BooleanExpression exists;

    public SimpleSubQuery(Class<T> type, QueryMetadata md) {
        super(type);
        subQueryMixin = new SubQueryExpressionImpl<T>(type, md);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
       return subQueryMixin.equals(o);
    }

    @Override
    public BooleanExpression exists() {
        if (exists == null) {
            exists = BooleanOperation.create(Ops.EXISTS, this);
        }
        return exists;
    }

    @Override
    public QueryMetadata getMetadata() {
        return subQueryMixin.getMetadata();
    }

    @Override
    public int hashCode(){
        return subQueryMixin.hashCode();
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

}
