/*
 * Copyright 2013, Mysema Ltd
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
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.MutableExpressionBase;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.TemplateFactory;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.ComparableExpressionBase;
import com.querydsl.core.types.expr.SimpleExpression;
import com.querydsl.core.types.expr.SimpleOperation;
import com.querydsl.core.types.template.SimpleTemplate;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class WithinGroup<T> extends SimpleOperation<T> {

    private static final long serialVersionUID = 464583892898579544L;

    private static Expression<?> merge(Expression<?>... args) {
        if (args.length == 1) {
            return args[0];
        } else {
            return ExpressionUtils.list(Object.class, args);
        }
    }

    public class OrderBy extends MutableExpressionBase<T> {

        private static final long serialVersionUID = -4936481493030913621L;

        private static final String ORDER_BY = "order by ";

        private volatile SimpleExpression<T> value;

        private final List<OrderSpecifier<?>> orderBy = new ArrayList<OrderSpecifier<?>>();

        public OrderBy() {
            super(WithinGroup.this.getType());
        }

        public SimpleExpression<T> getValue() {
            if (value == null) {
                int size = 0;
                ImmutableList.Builder<Expression<?>> args = ImmutableList.builder();
                StringBuilder builder = new StringBuilder();
                builder.append("{0} within group (");
                args.add(WithinGroup.this);
                size++;
                if (!orderBy.isEmpty()) {
                    builder.append(ORDER_BY);
                    boolean first = true;
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
                }
                builder.append(")");
                value = new SimpleTemplate<T>(
                        WithinGroup.this.getType(),
                        TemplateFactory.DEFAULT.create(builder.toString()),
                        args.build());
            }
            return value;
        }

        @Override
        public <R, C> R accept(Visitor<R, C> v, C context) {
            return getValue().accept(v, context);
        }

        public OrderBy orderBy(ComparableExpressionBase<?> orderBy) {
            value = null;
            this.orderBy.add(orderBy.asc());
            return this;
        }

        public OrderBy orderBy(ComparableExpressionBase<?>... orderBy) {
            value = null;
            for (ComparableExpressionBase<?> e : orderBy) {
                this.orderBy.add(e.asc());
            }
            return this;
        }
    }

    public WithinGroup(Class<T> type, Operator<? super T> op) {
        super(type, op, ImmutableList.<Expression<?>>of());
    }

    public WithinGroup(Class<T> type, Operator<? super T> op, Expression<?> arg) {
        super(type, op, ImmutableList.<Expression<?>>of(arg));
    }

    public WithinGroup(Class<T> type, Operator<? super T> op, Expression<?> arg1, Expression<?> arg2) {
        super(type, op, ImmutableList.<Expression<?>>of(arg1, arg2));
    }

    public WithinGroup(Class<T> type, Operator<? super T> op, Expression<?>... args) {
        super(type, op, merge(args));
    }

    /**
     * @return
     */
    public OrderBy withinGroup() {
        return new OrderBy();
    }

}
