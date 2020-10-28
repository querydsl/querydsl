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
package com.querydsl.core.types.dsl;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code ComparableExpression} extends {@link ComparableExpressionBase} to provide comparison methods.
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public abstract class ComparableExpression<T extends Comparable> extends ComparableExpressionBase<T> {

    private static final long serialVersionUID = 5761359576767404270L;

    public ComparableExpression(Expression<T> mixin) {
        super(mixin);
    }

    @Override
    public ComparableExpression<T> as(Path<T> alias) {
        return Expressions.comparableOperation(getType(), Ops.ALIAS, mixin, alias);
    }

    @Override
    public ComparableExpression<T> as(String alias) {
        return as(ExpressionUtils.path(getType(), alias));
    }

    /**
     * Create a {@code this between from and to} expression
     *
     * <p>Is equivalent to {@code from <= this <= to}</p>
     *
     * @param from inclusive start of range
     * @param to inclusive end of range
     * @return this between from and to
     */
    public final BooleanExpression between(@Nullable T from, @Nullable T to) {
        if (from == null) {
            if (to != null) {
                return Expressions.booleanOperation(Ops.LOE, mixin, ConstantImpl.create(to));
            } else {
                throw new IllegalArgumentException("Either from or to needs to be non-null");
            }
        } else if (to == null) {
            return Expressions.booleanOperation(Ops.GOE, mixin, ConstantImpl.create(from));
        } else {
            return Expressions.booleanOperation(Ops.BETWEEN, mixin, ConstantImpl.create(from), ConstantImpl.create(to));
        }
    }

    /**
     * Create a {@code this between from and to} expression
     *
     * <p>Is equivalent to {@code from <= this <= to}</p>
     *
     * @param from inclusive start of range
     * @param to inclusive end of range
     * @return this between from and to
     */
    public final BooleanExpression between(@Nullable Expression<T> from, @Nullable Expression<T> to) {
        if (from == null) {
            if (to != null) {
                return Expressions.booleanOperation(Ops.LOE, mixin, to);
            } else {
                throw new IllegalArgumentException("Either from or to needs to be non-null");
            }
        } else if (to == null) {
            return Expressions.booleanOperation(Ops.GOE, mixin, from);
        } else {
            return Expressions.booleanOperation(Ops.BETWEEN, mixin, from, to);
        }

    }

    /**
     * Create a {@code this not between from and to} expression
     *
     * <p>Is equivalent to {@code this < from || this > to}</p>
     *
     * @param from inclusive start of range
     * @param to inclusive end of range
     * @return this not between from and to
     */
    public final BooleanExpression notBetween(T from, T to) {
        return between(from, to).not();
    }

    /**
     * Create a {@code this not between from and to} expression
     *
     * <p>Is equivalent to {@code this < from || this > to}</p>
     *
     * @param from inclusive start of range
     * @param to inclusive end of range
     * @return this not between from and to
     */
    public final BooleanExpression notBetween(Expression<T> from, Expression<T> to) {
        return between(from, to).not();
    }

    /**
     * Create a {@code this > right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression gt(T right) {
        return gt(ConstantImpl.create(right));
    }

    /**
     * Create a {@code this > right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression gt(Expression<T> right) {
        return Expressions.booleanOperation(Ops.GT, mixin, right);
    }

    /**
     * Create a {@code this > all right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; all right
     */
    public BooleanExpression gtAll(CollectionExpression<?, ? super T> right) {
        return gt(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this > any right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; any right
     */
    public BooleanExpression gtAny(CollectionExpression<?, ? super T> right) {
        return gt(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this > all right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; all right
     */
    public BooleanExpression gtAll(SubQueryExpression<? extends T> right) {
        return gt(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this > any right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt; any right
     */
    public BooleanExpression gtAny(SubQueryExpression<? extends T> right) {
        return gt(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this >= right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt;= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression goe(T right) {
        return goe(ConstantImpl.create(right));
    }

    /**
     * Create a {@code this >= right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt;= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public BooleanExpression goe(Expression<T> right) {
        return Expressions.booleanOperation(Ops.GOE, mixin, right);
    }

    /**
     * Create a {@code this >= all right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt;= all right
     */
    public BooleanExpression goeAll(CollectionExpression<?, ? super T> right) {
        return goe(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this >= any right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt;= any right
     */
    public BooleanExpression goeAny(CollectionExpression<?, ? super T> right) {
        return goe(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this >= all right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt;= all right
     */
    public BooleanExpression goeAll(SubQueryExpression<? extends T> right) {
        return goe(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this >= any right} expression
     *
     * @param right rhs of the comparison
     * @return this &gt;= any right
     */
    public BooleanExpression goeAny(SubQueryExpression<? extends T> right) {
        return goe(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this < right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression lt(T right) {
        return lt(ConstantImpl.create(right));
    }

    /**
     * Create a {@code this < right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression lt(Expression<T> right) {
        return Expressions.booleanOperation(Ops.LT, mixin, right);
    }

    /**
     * Create a {@code this < all right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; all right
     */
    public BooleanExpression ltAll(CollectionExpression<?, ? super T> right) {
        return lt(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this < any right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; any right
     */
    public BooleanExpression ltAny(CollectionExpression<?, ? super T> right) {
        return lt(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this < all right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; all right
     */
    public BooleanExpression ltAll(SubQueryExpression<? extends T> right) {
        return lt(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this < any right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt; any right
     */
    public BooleanExpression ltAny(SubQueryExpression<? extends T> right) {
        return lt(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this <= right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt;= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression loe(T right) {
        return Expressions.booleanOperation(Ops.LOE, mixin, ConstantImpl.create(right));
    }

    /**
     * Create a {@code this <= right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt;= right
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final BooleanExpression loe(Expression<T> right) {
        return Expressions.booleanOperation(Ops.LOE, mixin, right);
    }

    /**
     * Create a {@code this <= all right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt;= all right
     */
    public BooleanExpression loeAll(CollectionExpression<?, ? super T> right) {
        return loe(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this <= any right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt;= any right
     */
    public BooleanExpression loeAny(CollectionExpression<?, ? super T> right) {
        return loe(ExpressionUtils.any(right));
    }

    /**
     * Create a {@code this <= all right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt;= all right
     */
    public BooleanExpression loeAll(SubQueryExpression<? extends T> right) {
        return loe(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this <= any right} expression
     *
     * @param right rhs of the comparison
     * @return this &lt;= any right
     */
    public BooleanExpression loeAny(SubQueryExpression<? extends T> right) {
        return loe(ExpressionUtils.any(right));
    }

}
