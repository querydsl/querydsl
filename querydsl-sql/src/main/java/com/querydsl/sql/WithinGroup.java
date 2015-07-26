/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.SimpleOperation;

/**
 * {@code WithinGroup} is a builder for {@code WITHIN GROUP} constructs
 *
 * @param <T> expression type
 *
 * @author tiwe
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

    /**
     * Intermediate step
     */
    public class OrderBy extends MutableExpressionBase<T> {

        private static final long serialVersionUID = -4936481493030913621L;

        private static final String ORDER_BY = "order by ";

        @Nullable
        private transient volatile SimpleExpression<T> value;

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
                    builder.append("{" + size + "}");
                    args.add(ExpressionUtils.orderBy(orderBy));
                }
                builder.append(")");
                value = Expressions.template((Class) WithinGroup.this.getType(), builder.toString(), args.build());
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

    public WithinGroup(Class<? extends T> type, Operator op) {
        super(type, op, ImmutableList.<Expression<?>>of());
    }

    public WithinGroup(Class<? extends T> type, Operator op, Expression<?> arg) {
        super(type, op, ImmutableList.<Expression<?>>of(arg));
    }

    public WithinGroup(Class<? extends T> type, Operator op, Expression<?> arg1, Expression<?> arg2) {
        super(type, op, ImmutableList.of(arg1, arg2));
    }

    public WithinGroup(Class<? extends T> type, Operator op, Expression<?>... args) {
        super(type, op, merge(args));
    }

    public OrderBy withinGroup() {
        return new OrderBy();
    }

}
