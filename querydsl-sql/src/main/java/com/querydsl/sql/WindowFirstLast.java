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
package com.querydsl.sql;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.MutableExpressionBase;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.TemplateFactory;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.ComparableExpressionBase;
import com.querydsl.core.types.expr.SimpleExpression;
import com.querydsl.core.types.template.SimpleTemplate;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class WindowFirstLast<T> extends MutableExpressionBase<T> {

    private static final long serialVersionUID = 4107262569593794721L;

    private static final String ORDER_BY = "order by ";

    private final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();

    private volatile SimpleExpression<T> value;

    private final Expression<T> target;

    private final boolean first;

    public WindowFirstLast(WindowOver<T> target, boolean first) {
        super(target.getType());
        this.target = target;
        this.first = first;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return getValue().accept(v, context);
    }

    public WindowFirstLast<T> orderBy(ComparableExpressionBase<?> orderBy) {
        value = null;
        this.orderBy.add(orderBy.asc());
        return this;
    }

    public WindowFirstLast<T> orderBy(ComparableExpressionBase<?>... orderBy) {
        value = null;
        for (ComparableExpressionBase<?> e : orderBy) {
            this.orderBy.add(e.asc());
        }
        return this;
    }

    public WindowFirstLast<T> orderBy(OrderSpecifier<?> orderBy) {
        value = null;
        this.orderBy.add(orderBy);
        return this;
    }

    public WindowFirstLast<T> orderBy(OrderSpecifier<?>... orderBy) {
        value = null;
        this.orderBy.addAll(Arrays.asList(orderBy));
        return this;
    }

    SimpleExpression<T> getValue() {
        if (value == null) {
            if (orderBy.isEmpty()) {
                // TODO this check should be static
                throw new IllegalStateException("No order by arguments given");
            }
            ImmutableList.Builder<Expression<?>> args = ImmutableList.builder();
            StringBuilder builder = new StringBuilder();
            builder.append("{0} keep (dense_rank ");
            args.add(target);
            builder.append(first ? "first " : "last ");
            builder.append(ORDER_BY);
            boolean first = true;
            int size = 1;
            for (OrderSpecifier<?> expr : orderBy) {
                if (!first) {
                    builder.append(", ");
                }
                builder.append("{" + size + "}");
                if (!expr.isAscending()) {
                    builder.append(" desc");
                }
                args.add(expr.getTarget());
                size++;
                first = false;
            }
            builder.append(")");
            value = new SimpleTemplate<T>(
                    target.getType(),
                    TemplateFactory.DEFAULT.create(builder.toString()),
                    args.build());
        }
        return value;
    }

    public WindowFunction<T> over() {
        return new WindowFunction<T>(getValue());
    }

}
