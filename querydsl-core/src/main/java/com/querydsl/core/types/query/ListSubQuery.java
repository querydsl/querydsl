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

import java.util.List;

import javax.annotation.Nullable;

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.SubQueryExpressionImpl;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.BooleanOperation;
import com.querydsl.core.types.expr.CollectionExpressionBase;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.SimpleExpression;
import com.querydsl.core.types.expr.SimpleOperation;
import com.querydsl.core.types.expr.Wildcard;

/**
 * List result subquery
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public final class ListSubQuery<T> extends CollectionExpressionBase<List<T>,T> implements ExtendedSubQueryExpression<List<T>> {

    private static final long serialVersionUID = 3399354334765602960L;

    private final Class<T> elementType;

    private final SubQueryExpressionImpl<List<T>> subQueryMixin;

    @Nullable
    private volatile BooleanExpression exists;

    @Nullable
    private volatile NumberExpression<Long> count;

    @Nullable
    private volatile NumberExpression<Long> countDistinct;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public ListSubQuery(Class<T> elementType, QueryMetadata md) {
        super(new SubQueryExpressionImpl<List<T>>((Class)List.class, md));
        this.elementType = elementType;
        this.subQueryMixin = (SubQueryExpressionImpl<List<T>>)mixin;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(subQueryMixin, context);
    }

    public NumberExpression<Long> count() {
        if (count == null) {
            count = count(Ops.AggOps.COUNT_AGG);
        }
        return count;
    }

    public NumberExpression<Long> countDistinct() {
        if (countDistinct == null) {
            countDistinct = count(Ops.AggOps.COUNT_DISTINCT_AGG);
        }
        return countDistinct;
    }

    private NumberExpression<Long> count(Operator<Long> operator) {
        QueryMetadata md = subQueryMixin.getMetadata().clone();
        Expression<?> e = null;
        if (md.getProjection().size() == 1) {
            e = md.getProjection().get(0);
        } else if (!md.getProjection().isEmpty()) {
            e = ExpressionUtils.list(Object.class, md.getProjection());
        }
        md.clearProjection();
        if (e != null) {
            md.addProjection(OperationImpl.create(Long.class, operator, e));
        } else if (operator == Ops.AggOps.COUNT_AGG) {
            md.addProjection(Wildcard.count);
        } else {
            md.addProjection(Wildcard.countDistinct);
        }

        return new NumberSubQuery<Long>(Long.class, md);
    }

    @Override
    public BooleanExpression exists() {
        if (exists == null) {
            exists = BooleanOperation.create(Ops.EXISTS, mixin);
        }
        return exists;
    }

    @Override
    public Class<T> getElementType() {
        return elementType;
    }

    @Override
    public QueryMetadata getMetadata() {
        return subQueryMixin.getMetadata();
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

    public SimpleExpression<?> as(Expression<?> alias) {
        return SimpleOperation.create(alias.getType(),Ops.ALIAS, this, alias);
    }

    @Override
    public Class<?> getParameter(int index) {
        if (index == 0) {
            return elementType;
        } else {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}
