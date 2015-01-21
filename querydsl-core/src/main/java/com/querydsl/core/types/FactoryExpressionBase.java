package com.querydsl.core.types;

import javax.annotation.Nullable;
import java.util.List;

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

}
