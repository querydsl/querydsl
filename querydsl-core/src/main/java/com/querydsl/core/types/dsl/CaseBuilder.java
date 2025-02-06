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

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code CaseBuilder} enables the construction of typesafe case-when-then-else
 * constructs :
 * e.g.
 *
 * <pre>{@code
 * Expression<String> cases = new CaseBuilder()
 *     .when(c.annualSpending.gt(10000)).then("Premier")
 *     .when(c.annualSpending.gt(5000)).then("Gold")
 *     .when(c.annualSpending.gt(2000)).then("Silver")
 *     .otherwise("Bronze");
 * }</pre>
 *
 * @author tiwe
 *
 */
public final class CaseBuilder {

    private static final class CaseElement<A> {

        @Nullable
        private final Predicate condition;

        private final Expression<A> target;

        CaseElement(@Nullable Predicate condition, Expression<A> target) {
            this.condition = condition;
            this.target = target;
        }

        public Predicate getCondition() {
            return condition;
        }

        public Expression<A> getTarget() {
            return target;
        }

    }

    /**
     * Cascading typesafe Case builder
     *
     * @author tiwe
     *
     * @param <A>
     * @param <Q>
     */
    public abstract static class Cases<A, Q extends Expression<A>> {

        private final List<CaseElement<A>> cases = new ArrayList<CaseElement<A>>();

        private final Class<? extends A> type;

        public Cases(Class<? extends A> type) {
            this.type = type;
        }

        Cases<A,Q> addCase(Predicate condition, Expression<A> expr) {
            cases.add(0, new CaseElement<A>(condition, expr));
            return this;
        }

        protected abstract Q createResult(Class<? extends A> type, Expression<A> last);

        @SuppressWarnings("unchecked")
        public Q otherwise(A constant) {
            if (constant != null) {
                return otherwise(ConstantImpl.create(constant));
            } else {
                return otherwise((Q) NullExpression.DEFAULT);
            }
        }

        @SuppressWarnings("unchecked")
        public Q otherwise(Expression<A> expr) {
            if (expr == null) {
                expr = (Expression) NullExpression.DEFAULT;
            }
            cases.add(0, new CaseElement<A>(null, expr));
            Expression<A> last = null;
            for (CaseElement<A> element : cases) {
                if (last == null) {
                    last = Expressions.operation(type, Ops.CASE_ELSE,
                            element.getTarget());
                } else {
                    last = Expressions.operation(type, Ops.CASE_WHEN,
                            element.getCondition(),
                            element.getTarget(),
                            last);
                }
            }
            return createResult(type, last);
        }

        public CaseWhen<A,Q> when(Predicate b) {
            return new CaseWhen<A,Q>(this, b);
        }

    }

    /**
     * Intermediate When state
     *
     * @author tiwe
     *
     * @param <A>
     * @param <Q>
     */
    public static class CaseWhen<A,Q extends Expression<A>> {

        private final Predicate b;

        private final Cases<A,Q> cases;

        public CaseWhen(Cases<A,Q> cases, Predicate b) {
            this.cases = cases;
            this.b = b;
        }

        public Cases<A,Q> then(A constant) {
            return then(ConstantImpl.create(constant));
        }

        public Cases<A,Q> then(Expression<A> expr) {
            return cases.addCase(b, expr);
        }
    }

    /**
     * Initial state of Case construction
     *
     * @author tiwe
     *
     */
    public static class Initial {

        private final Predicate when;

        public Initial(Predicate b) {
            this.when = b;
        }

        @SuppressWarnings("unchecked")
        public <A> Cases<A, SimpleExpression<A>> then(Expression<A> expr) {
            if (expr instanceof Predicate) {
                return (Cases) then((Predicate) expr);
            } else if (expr instanceof StringExpression) {
                return (Cases) then((StringExpression) expr);
            } else if (expr instanceof NumberExpression) {
                return then((NumberExpression) expr);
            } else if (expr instanceof DateExpression) {
                return then((DateExpression) expr);
            } else if (expr instanceof DateTimeExpression) {
                return then((DateTimeExpression) expr);
            } else if (expr instanceof TimeExpression) {
                return then((TimeExpression) expr);
            } else if (expr instanceof ComparableExpression) {
                return then((ComparableExpression) expr);
            } else {
                return thenSimple(expr);
            }
        }

        private <A> Cases<A, SimpleExpression<A>> thenSimple(Expression<A> expr) {
            return new Cases<A, SimpleExpression<A>>(expr.getType()) {
                @Override
                protected SimpleExpression<A> createResult(Class<? extends A> type, Expression<A> last) {
                    return Expressions.operation(type, Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public <A> Cases<A, SimpleExpression<A>> then(A constant) {
            return thenSimple(ConstantImpl.create(constant));
        }

        // Boolean

        public Cases<Boolean, BooleanExpression> then(Predicate expr) {
            return thenBoolean(expr);
        }

        private Cases<Boolean, BooleanExpression> thenBoolean(Expression<Boolean> expr) {
            return new Cases<Boolean,BooleanExpression>(Boolean.class) {
                @Override
                protected BooleanExpression createResult(Class<? extends Boolean> type, Expression<Boolean> last) {
                    return Expressions.booleanOperation(Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public Cases<Boolean, BooleanExpression> then(boolean b) {
            return thenBoolean(ConstantImpl.create(b));
        }

        // Comparable

        public <T extends Comparable> Cases<T, ComparableExpression<T>> then(ComparableExpression<T> expr) {
            return thenComparable(expr);
        }

        private <T extends Comparable> Cases<T, ComparableExpression<T>> thenComparable(Expression<T> expr) {
            return new Cases<T, ComparableExpression<T>>(expr.getType()) {
                @Override
                protected ComparableExpression<T> createResult(Class<? extends T> type, Expression<T> last) {
                    return Expressions.comparableOperation(type, Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public <A extends Comparable> Cases<A, ComparableExpression<A>> then(A arg) {
            return thenComparable(ConstantImpl.create(arg));
        }

        // Date

        public <T extends Comparable> Cases<T, DateExpression<T>> then(DateExpression<T> expr) {
            return thenDate(expr);
        }

        private <T extends Comparable> Cases<T, DateExpression<T>> thenDate(Expression<T> expr) {
            return new Cases<T, DateExpression<T>>(expr.getType()) {
                @Override
                protected DateExpression<T> createResult(Class<? extends T> type, Expression<T> last) {
                    return Expressions.dateOperation(type, Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public Cases<java.sql.Date, DateExpression<java.sql.Date>> then(java.sql.Date date) {
            return thenDate(ConstantImpl.create(date));
        }

        // DateTime

        public <T extends Comparable> Cases<T, DateTimeExpression<T>> then(DateTimeExpression<T> expr) {
            return thenDateTime(expr);
        }

        private <T extends Comparable> Cases<T, DateTimeExpression<T>> thenDateTime(Expression<T> expr) {
            return new Cases<T, DateTimeExpression<T>>(expr.getType()) {
                @Override
                protected DateTimeExpression<T> createResult(Class<? extends T> type, Expression<T> last) {
                    return Expressions.dateTimeOperation(type, Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public Cases<Timestamp, DateTimeExpression<Timestamp>> then(Timestamp ts) {
            return thenDateTime(ConstantImpl.create(ts));
        }

        public Cases<java.util.Date, DateTimeExpression<java.util.Date>> then(java.util.Date date) {
            return thenDateTime(ConstantImpl.create(date));
        }

        // Enum

        public <T extends Enum<T>> Cases<T,EnumExpression<T>> then(EnumExpression<T> expr) {
            return thenEnum(expr);
        }

        @SuppressWarnings("unchecked")
        private <T extends Enum<T>> Cases<T,EnumExpression<T>> thenEnum(Expression<T> expr) {
            return new Cases<T,EnumExpression<T>>(expr.getType()) {
                @Override
                protected EnumExpression<T> createResult(Class<? extends T> type, Expression<T> last) {
                    return Expressions.enumOperation(type, Ops.CASE, last);
                }
            }.addCase(when, expr);
        }

        public <T extends Enum<T>> Cases<T, EnumExpression<T>> then(T arg) {
            return thenEnum(ConstantImpl.create(arg));
        }

        // Number

        public <A extends Number & Comparable<?>> Cases<A, NumberExpression<A>> then(NumberExpression<A> expr) {
            return thenNumber(expr);
        }

        @SuppressWarnings("unchecked")
        private <A extends Number & Comparable<?>> Cases<A, NumberExpression<A>> thenNumber(Expression<A> expr) {
            return new Cases<A, NumberExpression<A>>(expr.getType()) {
                @Override
                protected NumberExpression<A> createResult(Class<? extends A> type, Expression<A> last) {
                    return Expressions.numberOperation(type, Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public <A extends Number & Comparable<?>> Cases<A, NumberExpression<A>> then(A num) {
            return thenNumber(ConstantImpl.create(num));
        }

        // String

        public Cases<String,StringExpression> then(StringExpression expr) {
            return thenString(expr);
        }

        private Cases<String,StringExpression> thenString(Expression<String> expr) {
            return new Cases<String,StringExpression>(String.class) {
                @SuppressWarnings("unchecked")
                @Override
                protected StringExpression createResult(Class<? extends String> type, Expression<String> last) {
                    return Expressions.stringOperation(Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public Cases<String, StringExpression> then(String str) {
            return thenString(ConstantImpl.create(str));
        }

        // Time

        public <T extends Comparable> Cases<T, TimeExpression<T>> then(TimeExpression<T> expr) {
            return thenTime(expr);
        }

        private <T extends Comparable> Cases<T, TimeExpression<T>> thenTime(Expression<T> expr) {
            return new Cases<T, TimeExpression<T>>(expr.getType()) {
                @Override
                protected TimeExpression<T> createResult(Class<? extends T> type, Expression<T> last) {
                    return Expressions.timeOperation(type, Ops.CASE, last);
                }

            }.addCase(when, expr);
        }

        public Cases<Time, TimeExpression<Time>> then(Time time) {
            return thenTime(ConstantImpl.create(time));
        }

    }

    public Initial when(Predicate b) {
        return new Initial(b);
    }

}
