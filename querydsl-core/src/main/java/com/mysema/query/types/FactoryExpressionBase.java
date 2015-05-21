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
package com.mysema.query.types;

import java.util.List;

import javax.annotation.Nullable;

/**
 * Common superclass for FactoryExpression implementations
 *
 * @param <T>
 */
public abstract class FactoryExpressionBase<T> extends ExpressionBase<T> implements FactoryExpression<T> {

    private static class FactoryExpressionWrapper<T> extends ExpressionBase<T> implements FactoryExpression<T> {
        private final FactoryExpression<T> expr;

        public FactoryExpressionWrapper(FactoryExpression<T> expr) {
            super(expr.getType());
            this.expr = expr;
        }

        @Override
        public List<Expression<?>> getArgs() {
            return expr.getArgs();
        }

        @Nullable
        @Override
        public T newInstance(Object... args) {
            if (args != null) {
                for (Object arg : args) {
                    if (arg != null) {
                        return expr.newInstance(args);
                    }
                }
            }
            return null;
        }

        @Nullable
        @Override
        public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
            return expr.accept(v, context);
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof FactoryExpressionWrapper) {
                return expr.equals(((FactoryExpressionWrapper)o).expr);
            } else {
                return false;
            }
        }

    }

    public FactoryExpressionBase(Class<? extends T> type) {
        super(type);
    }

    /**
     * Returns a wrapper expression which returns null if all arguments to newInstance are null
     *
     * @return
     */
    public FactoryExpression<T> skipNulls() {
        return new FactoryExpressionWrapper<T>(this);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof FactoryExpression) {
            return getClass().equals(o.getClass())
                    && getArgs().equals(((FactoryExpression) o).getArgs());
        } else {
            return false;
        }
    }

}
