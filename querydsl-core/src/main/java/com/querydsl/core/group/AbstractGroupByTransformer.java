/*
 * Copyright 2014, Mysema Ltd
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
package com.querydsl.core.group;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.ResultTransformer;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionBase;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Visitor;

/**
 * Base class for GroupBy result transformers
 *
 * @author tiwe
 *
 * @param <K>
 * @param <T>
 */
abstract class AbstractGroupByTransformer<K, T> implements ResultTransformer<T> {

    private static final class FactoryExpressionAdapter<T> extends ExpressionBase<T> implements FactoryExpression<T> {
        private final FactoryExpression<T> expr;

        private final List<Expression<?>> args;

        private FactoryExpressionAdapter(FactoryExpression<T> expr, List<Expression<?>> args) {
            super(expr.getType());
            this.expr = expr;
            this.args = args;
        }

        @Override
        public <R, C> R accept(Visitor<R, C> v, C context) {
            return expr.accept(v, context);
        }

        @Override
        public List<Expression<?>> getArgs() {
            return args;
        }

        @Override
        public T newInstance(Object... args) {
            return expr.newInstance(args);
        }
    }

    protected final List<GroupExpression<?, ?>> groupExpressions = new ArrayList<GroupExpression<?, ?>>();

    protected final List<QPair<?,?>> maps = new ArrayList<QPair<?,?>>();

    protected final Expression<?>[] expressions;

    AbstractGroupByTransformer(Expression<K> key, Expression<?>... expressions) {
        List<Expression<?>> projection = new ArrayList<Expression<?>>(expressions.length);
        groupExpressions.add(new GOne<K>(key));
        projection.add(key);

        for (Expression<?> expr : expressions) {
            if (expr instanceof GroupExpression<?,?>) {
                GroupExpression<?,?> groupExpr = (GroupExpression<?,?>)expr;
                groupExpressions.add(groupExpr);
                Expression<?> colExpression = groupExpr.getExpression();
                if (colExpression instanceof Operation && ((Operation)colExpression).getOperator() == Ops.ALIAS) {
                    projection.add(((Operation)colExpression).getArg(0));
                } else {
                    projection.add(colExpression);
                }
                if (groupExpr instanceof GMap) {
                    maps.add((QPair<?, ?>) colExpression);
                }
            } else {
                groupExpressions.add(new GOne(expr));
                projection.add(expr);
            }
        }

        this.expressions = projection.toArray(new Expression[projection.size()]);
    }

    protected static FactoryExpression<Tuple> withoutGroupExpressions(final FactoryExpression<Tuple> expr) {
        List<Expression<?>> args = new ArrayList<Expression<?>>(expr.getArgs().size());
        for (Expression<?> arg : expr.getArgs()) {
            if (arg instanceof GroupExpression) {
                args.add(((GroupExpression)arg).getExpression());
            } else {
                args.add(arg);
            }
        }
        return new FactoryExpressionAdapter<Tuple>(expr, args);
    }

}
