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
package com.querydsl.core.types;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.core.util.ArrayUtils;

/**
 * Utility class to expand FactoryExpression constructor arguments and compress {@link FactoryExpression}
 * invocation arguments
 *
 * @author tiwe
 *
 */
public final class FactoryExpressionUtils {

    public static class FactoryExpressionAdapter<T> extends ExpressionBase<T> implements FactoryExpression<T> {

        private static final long serialVersionUID = -2742333128230913512L;

        private final FactoryExpression<T> inner;

        private final List<Expression<?>> args;

        FactoryExpressionAdapter(FactoryExpression<T> inner) {
            super(inner.getType());
            this.inner = inner;
            this.args = expand(inner.getArgs());
        }


        FactoryExpressionAdapter(FactoryExpression<T> inner, List<Expression<?>> args) {
            super(inner.getType());
            this.inner = inner;
            this.args = args;
        }

        @Override
        public List<Expression<?>> getArgs() {
            return args;
        }

        @Override
        public T newInstance(Object... a) {
            return inner.newInstance(compress(inner.getArgs(), a));
        }

        @Override
        public <R, C> R accept(Visitor<R, C> v, C context) {
            return v.visit(this, context);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof FactoryExpression) {
                FactoryExpression<?> e = (FactoryExpression<?>)o;
                return args.equals(e.getArgs()) && getType().equals(e.getType());
            } else {
                return false;
            }
        }

    }

    /**
     * @param projection
     * @return
     */
    public static FactoryExpression<?> wrap(List<? extends Expression<?>> projection) {
        boolean usesFactoryExpressions = false;
        for (Expression<?> e : projection) {
            usesFactoryExpressions |= e instanceof FactoryExpression;
        }
        if (usesFactoryExpressions) {
            return wrap(new ArrayConstructorExpression(
                    projection.toArray(new Expression[projection.size()])));
        } else {
            return null;
        }
    }

    public static <T> FactoryExpression<T> wrap(FactoryExpression<T> expr, List<Expression<?>> conversions) {
        return new FactoryExpressionAdapter<T>(expr, conversions);
    }

    public static <T> FactoryExpression<T> wrap(FactoryExpression<T> expr) {
        for (Expression<?> arg : expr.getArgs()) {
            if (arg instanceof ProjectionRole) {
                arg = ((ProjectionRole) arg).getProjection();
            }
            if (arg instanceof FactoryExpression<?>) {
                return new FactoryExpressionAdapter<T>(expr);
            }
        }
        return expr;
    }

    private static List<Expression<?>> expand(List<Expression<?>> exprs) {
        List<Expression<?>> rv = new ArrayList<Expression<?>>(exprs.size());
        for (Expression<?> expr : exprs) {
            if (expr instanceof ProjectionRole) {
                expr = ((ProjectionRole) expr).getProjection();
            }
            if (expr instanceof FactoryExpression<?>) {
                rv.addAll(expand(((FactoryExpression<?>)expr).getArgs()));
            } else {
                rv.add(expr);
            }
        }
        return rv;
    }

    private static int countArguments(FactoryExpression<?> expr) {
        int counter = 0;
        for (Expression<?> arg : expr.getArgs()) {
            if (arg instanceof ProjectionRole) {
                arg = ((ProjectionRole)arg).getProjection();
            }
            if (arg instanceof FactoryExpression<?>) {
                counter += countArguments((FactoryExpression<?>)arg);
            } else {
                counter++;
            }
        }
        return counter;
    }

    private static Object[] compress(List<Expression<?>> exprs, Object[] args) {
        Object[] rv = new Object[exprs.size()];
        int offset = 0;
        for (int i = 0; i < exprs.size(); i++) {
            Expression<?> expr = exprs.get(i);
            if (expr instanceof ProjectionRole) {
                expr = ((ProjectionRole) expr).getProjection();
            }
            if (expr instanceof FactoryExpression<?>) {
                FactoryExpression<?> fe = (FactoryExpression<?>)expr;
                int fullArgsLength = countArguments(fe);
                Object[] compressed = compress(fe.getArgs(), ArrayUtils.subarray(args, offset, offset + fullArgsLength));
                rv[i] = fe.newInstance(compressed);
                offset += fullArgsLength;
            } else {
                rv[i] = args[offset];
                offset++;
            }
        }
        return rv;
    }

    private FactoryExpressionUtils() {}

}
