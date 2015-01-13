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
package com.querydsl.core.types.query;

import javax.annotation.Nullable;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.SubQueryExpressionImpl;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.BooleanOperation;

/**
 * Boolean typed single result subquery
 *
 * @author tiwe
 */
public final class BooleanSubQuery extends BooleanExpression implements ExtendedSubQueryExpression<Boolean> {

    private static final long serialVersionUID = -64156984110154969L;

    private final SubQueryExpressionImpl<Boolean> subQueryMixin;

    @Nullable
    private volatile BooleanExpression exists;

    public BooleanSubQuery(QueryMetadata md) {
        super(new SubQueryExpressionImpl<Boolean>(Boolean.class, md));
        subQueryMixin = (SubQueryExpressionImpl<Boolean>)mixin;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public BooleanExpression exists() {
        if (exists == null) {
            exists = BooleanOperation.create(Ops.EXISTS, mixin);
        }
        return exists;
    }

    @Override
    public QueryMetadata getMetadata() {
        return subQueryMixin.getMetadata();
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

}
