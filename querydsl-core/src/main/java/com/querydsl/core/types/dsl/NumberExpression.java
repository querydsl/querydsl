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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;
import com.querydsl.core.types.Ops.MathOps;
import com.querydsl.core.util.MathUtils;

/**
 * {@code NumberExpression} represents a numeric expression
 *
 * @author tiwe
 *
 * @param <T> expression type
 * @see java.lang.Number
 */
public abstract class NumberExpression<T extends Number & Comparable<?>> extends ComparableExpressionBase<T> {

    private static final long serialVersionUID = -5485902768703364888L;

    private static class Constants {
        private static final NumberExpression<Double> RANDOM = Expressions.numberOperation(Double.class, MathOps.RANDOM);
    }

    /**
     * Create a {@code max(left, right)} expression
     *
     * <p>Return the greater of the given values</p>
     *
     * @return max(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> max(Expression<A> left, Expression<A> right) {
        return Expressions.numberOperation(left.getType(), MathOps.MAX, left, right);
    }

    /**
     * Create a {@code min(left, right)} expression
     *
     * <p>Returns the smaller of the given values</p>
     *
     * @return min(left, right)
     */
    public static <A extends Number & Comparable<?>> NumberExpression<A> min(Expression<A> left, Expression<A> right) {
        return Expressions.numberOperation(left.getType(), MathOps.MIN, left, right);
    }

    /**
     * Create a {@code random()} expression
     *
     * <p>Returns the random number</p>
     *
     * @return random()
     */
    public static NumberExpression<Double> random() {
        return Constants.RANDOM;
    }

    @Nullable
    private transient volatile NumberExpression<T> abs, sum, min, max, floor, ceil, round;

    @Nullable
    private transient volatile NumberExpression<Double> avg, sqrt;

    @Nullable
    private transient volatile NumberExpression<T> negation;

    @Nullable
    private transient volatile StringExpression stringCast;

    public NumberExpression(Expression<T> mixin) {
        super(mixin);
    }

    @Override
    public NumberExpression<T> as(Path<T> alias) {
        return Expressions.numberOperation(getType(), Ops.ALIAS, mixin, alias);
    }

    @Override
    public NumberExpression<T> as(String alias) {
        return Expressions.numberOperation(getType(), Ops.ALIAS, mixin, ExpressionUtils.path(getType(), alias));
    }

    /**
     * Create a cast to String expression
     *
     * @see     java.lang.Object#toString()
     * @return string representation
     */
    public StringExpression stringValue() {
        if (stringCast == null) {
            stringCast = Expressions.stringOperation(Ops.STRING_CAST, mixin);
        }
        return stringCast;
    }

    /**
     * Create a {@code abs(this)} expression
     *
     * <p>Returns the absolute value of this expression</p>
     *
     * @return abs(this)
     */
    public NumberExpression<T> abs() {
        if (abs == null) {
            abs = Expressions.numberOperation(getType(), MathOps.ABS, mixin);
        }
        return abs;
    }

    /**
     * Create a {@code this + right} expression
     *
     * <p>Returns the sum of this and right</p>
     *
     * @param right rhs of expression
     * @return this + right
     */
    public <N extends Number & Comparable<?>> NumberExpression<T> add(Expression<N> right) {
        return Expressions.numberOperation(getType(), Ops.ADD, mixin, right);
    }

    /**
     * Create a {@code this + right} expression
     *
     * <p>Get the sum of this and right</p>
     *
     * @param right rhs of expression
     * @return this + right
     */
    public <N extends Number & Comparable<N>> NumberExpression<T> add(N right) {
        return Expressions.numberOperation(getType(), Ops.ADD, mixin, ConstantImpl.create(right));
    }

    /**
     * Create a {@code avg(this)} expression
     *
     * <p>Get the average value of this expression (aggregation)</p>
     *
     *  @return avg(this)
     */
    public NumberExpression<Double> avg() {
        if (avg == null) {
            avg = Expressions.numberOperation(Double.class, Ops.AggOps.AVG_AGG, mixin);
        }
        return avg;
    }

    /**
     * Create a {@code cast(this as byte)} expression
     *
     * <p>Get the byte expression of this numeric expression</p>
     *
     * @return this.byteValue()
     * @see java.lang.Number#byteValue()
     */
    public NumberExpression<Byte> byteValue() {
        return castToNum(Byte.class);
    }

    private T cast(Number number) {
        return MathUtils.cast(number, getType());
    }

    @SuppressWarnings("unchecked")
    public <A extends Number & Comparable<? super A>> NumberExpression<A> castToNum(Class<A> type) {
        if (type.equals(getType())) {
            return (NumberExpression<A>) this;
        } else {
            return Expressions.numberOperation(type, Ops.NUMCAST, mixin, ConstantImpl.create(type));
        }
    }

    /**
     * Create a {@code ceil(this)} expression
     *
     * <p>Returns the smallest (closest to negative infinity)
     * {@code double} value that is greater than or equal to the
     * argument and is equal to a mathematical integer</p>
     *
     * @return ceil(this)
     * @see java.lang.Math#ceil(double)
     */
    public NumberExpression<T> ceil() {
        if (ceil == null) {
            ceil = Expressions.numberOperation(getType(), MathOps.CEIL, mixin);
        }
        return ceil;
    }

    private Class<?> getDivisionType(Class<?> left, Class<?> right) {
        if (!left.equals(right)) {
            return Double.class;
        } else {
            return left;
        }
    }

    /**
     * Create a {@code this / right} expression
     *
     * <p>Get the result of the operation this / right</p>
     *
     * @param right
     * @return this / right
     */
    public <N extends Number & Comparable<?>> NumberExpression<T> divide(Expression<N> right) {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class<T>) getDivisionType(getType(), right.getType());
        return Expressions.numberOperation(type, Ops.DIV, mixin, right);
    }

    /**
     * Create a {@code this / right} expression
     *
     * <p>Get the result of the operation this / right</p>
     *
     * @param right
     * @return this / right
     */
    public <N extends Number & Comparable<?>> NumberExpression<T> divide(N right) {
        @SuppressWarnings("unchecked")
        Class<T> type = (Class< T>) getDivisionType(getType(), right.getClass());
        return Expressions.numberOperation(type, Ops.DIV, mixin, ConstantImpl.create(right));
    }

    /**
     * Create a {@code cast(this as double)} expression
     *
     * <p>Get the double expression of this numeric expression</p>
     *
     * @return this.doubleValue()
     * @see java.lang.Number#doubleValue()
     */
    public NumberExpression<Double> doubleValue() {
        return castToNum(Double.class);
    }

    /**
     * Create a {@code cast(this as double)} expression
     *
     * <p>Get the float expression of this numeric expression</p>
     *
     * @return this.floatValue()
     * @see java.lang.Number#floatValue()
     */
    public NumberExpression<Float> floatValue() {
        return castToNum(Float.class);
    }

    /**
     * Create a {@code floor(this)} expression
     *
     * <p>Returns the largest (closest to positive infinity)
     * {@code double} value that is less than or equal to the
     * argument and is equal to a mathematical integer.</p>
     *
     * @return floor(this)
     * @see java.lang.Math#floor(double)
     */
    public NumberExpression<T> floor() {
        if (floor == null) {
            floor = Expressions.numberOperation(getType(), MathOps.FLOOR, mixin);
        }
        return floor;
    }

    /**
     * Create a {@code this >= right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this >= right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression goe(A right) {
        return goe(ConstantImpl.create(cast(right)));
    }

    /**
     * Create a {@code this >= right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this >= right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression goe(Expression<A> right) {
        return Expressions.booleanOperation(Ops.GOE, mixin, right);
    }

    /**
     * Create a {@code this >= all right} expression
     *
     * @param right
     * @return this &gt;= all right
     */
    public BooleanExpression goeAll(CollectionExpression<?, ? super T> right) {
        return goe(ExpressionUtils.<T> all(right));
    }

    /**
     * Create a {@code this >= any right} expression
     *
     * @param right
     * @return this &gt;= any right
     */
    public BooleanExpression goeAny(CollectionExpression<?, ? super T> right) {
        return goe(ExpressionUtils.<T> any(right));
    }

    /**
     * Create a {@code this > right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this > right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression gt(A right) {
        return gt(ConstantImpl.create(cast(right)));
    }

    /**
     * Create a {@code this > right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this > right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression gt(Expression<A> right) {
        return Expressions.booleanOperation(Ops.GT, mixin, right);
    }

    /**
     * Create a {@code this > all right} expression
     *
     * @param right
     * @return this &gt; all right
     */
    public BooleanExpression gtAll(CollectionExpression<?, ? super T> right) {
        return gt(ExpressionUtils.<T> all(right));
    }

    /**
     * Create a {@code this > any right} expression
     *
     * @param right
     * @return this &gt; any right
     */
    public BooleanExpression gtAny(CollectionExpression<?, ? super T> right) {
        return gt(ExpressionUtils.<T> any(right));
    }

    /**
     * Create a {@code this > all right} expression
     *
     * @param right
     * @return this &gt; all right
     */
    public BooleanExpression gtAll(SubQueryExpression<? extends T> right) {
        return gt(ExpressionUtils.all(right));
    }

    /**
     * Create a {@code this > any right} expression
     *
     * @param right
     * @return this &gt; any right
     */
    public BooleanExpression gtAny(SubQueryExpression<? extends T> right) {
        return gt(ExpressionUtils.any(right));
    }


    /**
     * Create a {@code this between from and to} expression
     *
     * <p>Is equivalent to {@code from <= this <= to}</p>
     *
     * @param <A>
     * @param from inclusive start of range
     * @param to inclusive end of range
     * @return this between from and to
     */
    public final <A extends Number & Comparable<?>> BooleanExpression between(@Nullable A from, @Nullable A to) {
        if (from == null) {
            if (to != null) {
                return loe(to);
            } else {
                throw new IllegalArgumentException("Either from or to needs to be non-null");
            }
        } else if (to == null) {
            return goe(from);
        } else {
            return between(ConstantImpl.create(cast(from)), ConstantImpl.create(cast(to)));
        }
    }

    /**
     * Create a {@code this between from and to} expression
     *
     * <p>Is equivalent to {@code from <= this <= to}</p>
     *
     * @param <A>
     * @param from inclusive start of range
     * @param to inclusive end of range
     * @return this between from and to
     */
    public final <A extends Number & Comparable<?>> BooleanExpression between(@Nullable Expression<A> from, @Nullable Expression<A> to) {
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
     * <p>Is equivalent to {@code this < from || this > to}</p>
     *
     * @param from inclusive start of range
     * @param to inclusive end of range
     * @return this not between from and to
     */
    public final <A extends Number & Comparable<?>> BooleanExpression notBetween(A from, A to) {
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
    public final <A extends Number & Comparable<?>> BooleanExpression notBetween(Expression<A> from, Expression<A> to) {
        return between(from, to).not();
    }

    /**
     * Create a {@code this.intValue()} expression
     *
     * <p>Get the int expression of this numeric expression</p>
     *
     * @return this.intValue()
     * @see java.lang.Number#intValue()
     */
    public NumberExpression<Integer> intValue() {
        return castToNum(Integer.class);
    }

    /**
     * Create a {@code this like str} expression
     *
     * @param str rhs
     * @return this like str
     */
    public BooleanExpression like(String str) {
        return Expressions.booleanOperation(Ops.LIKE, stringValue(), ConstantImpl.create(str));
    }

    /**
     * Create a {@code this like str} expression
     *
     * @param str
     * @return this like str
     */
    public BooleanExpression like(Expression<String> str) {
        return Expressions.booleanOperation(Ops.LIKE, stringValue(), str);
    }

    /**
     * Create a {@code this <= right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this <= right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression loe(A right) {
        return loe(ConstantImpl.create(cast(right)));
    }

    /**
     * Create a {@code this <= right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this <= right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression loe(Expression<A> right) {
        return Expressions.booleanOperation(Ops.LOE, mixin, right);
    }

    /**
     * Create a {@code this <= all right} expression
     *
     * @param right rhs
     * @return this &lt;= all right
     */
    public BooleanExpression loeAll(CollectionExpression<?, ? super T> right) {
        return loe(ExpressionUtils.<T> all(right));
    }

    /**
     * Create a {@code this <= any right} expression
     *
     * @param right rhs
     * @return this &lt;= any right
     */
    public BooleanExpression loeAny(CollectionExpression<?, ? super T> right) {
        return loe(ExpressionUtils.<T> any(right));
    }

    /**
     * Create a {@code this.longValue()} expression
     *
     * <p>Get the long expression of this numeric expression</p>
     *
     * @return this.longValue()
     * @see java.lang.Number#longValue()
     */
    public NumberExpression<Long> longValue() {
        return castToNum(Long.class);
    }

    /**
     * Create a {@code this < right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this < right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression lt(A right) {
        return lt(ConstantImpl.create(cast(right)));
    }

    /**
     * Create a {@code this < right} expression
     *
     * @param <A>
     * @param right rhs of the comparison
     * @return {@code this < right}
     * @see java.lang.Comparable#compareTo(Object)
     */
    public final <A extends Number & Comparable<?>> BooleanExpression lt(Expression<A> right) {
        return Expressions.booleanOperation(Ops.LT, this, right);
    }

    /**
     * Create a {@code this < all right} expression
     *
     * @param right rhs
     * @return this &lt; all right
     */
    public BooleanExpression ltAll(CollectionExpression<?, ? super T> right) {
        return lt(ExpressionUtils.<T> all(right));
    }

    /**
     * Create a {@code this < any right} expression
     *
     * @param right rhs
     * @return this &lt; any right
     */
    public BooleanExpression ltAny(CollectionExpression<?, ? super T> right) {
        return lt(ExpressionUtils.<T> any(right));
    }

    /**
     * Create a {@code max(this)} expression
     *
     * <p>Get the maximum value of this expression (aggregation)</p>
     *
     * @return max(this)
     */
    @Override
    public NumberExpression<T> max() {
        if (max == null) {
            max = Expressions.numberOperation(getType(), Ops.AggOps.MAX_AGG, mixin);
        }
        return max;
    }

    /**
     * Create a {@code min(this)} expression
     *
     * <p>Get the minimum value of this expression (aggregation)</p>
     *
     * @return min(this)
     */
    @Override
    public NumberExpression<T> min() {
        if (min == null) {
            min = Expressions.numberOperation(getType(), Ops.AggOps.MIN_AGG, mixin);
        }
        return min;
    }

    /**
     * Create a {@code mod(this, num)} expression
     *
     * @param num
     * @return mod(this, num)
     */
    public NumberExpression<T> mod(Expression<T> num) {
        return Expressions.numberOperation(getType(), Ops.MOD, mixin, num);
    }

    /**
     * Create a {@code mod(this, num)} expression
     *
     * @param num
     * @return mod(this, num)
     */
    public NumberExpression<T> mod(T num) {
        return Expressions.numberOperation(getType(), Ops.MOD, mixin, ConstantImpl.create(num));
    }

    /**
     * Create a {@code this * right} expression
     *
     * <p>Get the result of the operation this * right</p>
     *
     * @param right
     * @return this * right
     */
    public <N extends Number & Comparable<?>> NumberExpression<T> multiply(Expression<N> right) {
        return Expressions.numberOperation(getType(), Ops.MULT, mixin, right);
    }

    /**
     * Create a {@code this * right} expression
     *
     * <p>Get the result of the operation this * right</p>
     *
     * @param right
     * @return this * right
     */
    public <N extends Number & Comparable<N>> NumberExpression<T> multiply(N right) {
        return Expressions.numberOperation(getType(), Ops.MULT, mixin, ConstantImpl.create(right));
    }

    /**
     * Create a {@code this * -1} expression
     *
     * <p>Get the negation of this expression</p>
     *
     * @return this * -1
     */
    public NumberExpression<T> negate() {
        if (negation == null) {
            negation = Expressions.numberOperation(getType(), Ops.NEGATE, mixin);
        }
        return negation;
    }

    /**
     * Create a {@code round(this)} expression
     *
     * <p>Returns the closest {@code int} to this.</p>
     *
     * @return round(this)
     * @see java.lang.Math#round(double)
     * @see java.lang.Math#round(float)
     */
    public NumberExpression<T> round() {
        if (round == null) {
            round = Expressions.numberOperation(getType(), MathOps.ROUND, mixin);
        }
        return round;
    }

    /**
     * Create a {@code this.shortValue()} expression
     *
     * <p>Get the short expression of this numeric expression</p>
     *
     * @return this.shortValue()
     * @see java.lang.Number#shortValue()
     */
    public NumberExpression<Short> shortValue() {
        return castToNum(Short.class);
    }

    /**
     * Create a {@code sqrt(this)} expression
     *
     * <p>Get the square root of this numeric expressions</p>
     *
     * @return sqrt(this)
     */
    public NumberExpression<Double> sqrt() {
        if (sqrt == null) {
            sqrt = Expressions.numberOperation(Double.class, MathOps.SQRT, mixin);
        }
        return sqrt;
    }

    /**
     * Create a {@code this - right} expression
     *
     * <p>Get the difference of this and right</p>
     *
     * @param right
     * @return this - right
     */
    public <N extends Number & Comparable<?>> NumberExpression<T> subtract(Expression<N> right) {
        return Expressions.numberOperation(getType(), Ops.SUB, mixin, right);
    }

    /**
     * Create a {@code this - right} expression
     *
     * <p>Get the difference of this and right</p>
     *
     * @param right
     * @return this - right
     */
    public <N extends Number & Comparable<?>> NumberExpression<T> subtract(N right) {
        return Expressions.numberOperation(getType(), Ops.SUB, mixin, ConstantImpl.create(right));
    }

    /**
     * Create a {@code sum(this)} expression
     *
     * <p>Get the sum of this expression (aggregation)</p>
     *
     * @return sum(this)
     */
    public NumberExpression<T> sum() {
        if (sum == null) {
            sum = Expressions.numberOperation(getType(), Ops.AggOps.SUM_AGG, mixin);
        }
        return sum;
    }

    @Override
    public BooleanExpression in(Number... numbers) {
        return super.in(convert(numbers));
    }

    @Override
    public BooleanExpression notIn(Number... numbers) {
        return super.notIn(convert(numbers));
    }

    private List<T> convert(Number... numbers) {
        List<T> list = new ArrayList<T>(numbers.length);
        for (Number number : numbers) {
            list.add(MathUtils.cast(number, getType()));
        }
        return list;
    }

    /**
     * Create a {@code nullif(this, other)} expression
     *
     * @param other
     * @return nullif(this, other)
     */
    @Override
    public NumberExpression<T> nullif(Expression<T> other) {
        return Expressions.numberOperation(getType(), Ops.NULLIF, mixin, other);
    }

    /**
     * Create a {@code nullif(this, other)} expression
     *
     * @param other
     * @return nullif(this, other)
     */
    @Override
    public NumberExpression<T> nullif(T other) {
        return nullif(ConstantImpl.create(other));
    }

    /**
     * Create a {@code coalesce(this, exprs...)} expression
     *
     * @param exprs additional arguments
     * @return coalesce
     */
    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public NumberExpression<T> coalesce(Expression<?>... exprs) {
        Coalesce<T> coalesce = new Coalesce<T>(getType(), mixin);
        for (Expression expr : exprs) {
            coalesce.add(expr);
        }
        return (NumberExpression<T>) coalesce.asNumber();
    }

    /**
     * Create a {@code coalesce(this, args...)} expression
     *
     * @param args additional arguments
     * @return coalesce
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public NumberExpression<T> coalesce(T... args) {
        Coalesce<T> coalesce = new Coalesce<T>(getType(), mixin);
        for (T arg : args) {
            coalesce.add(arg);
        }
        return (NumberExpression<T>) coalesce.asNumber();
    }

}
