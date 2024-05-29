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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;

/**
 * {@code CaseForEqBuilder} enables the construction of typesafe case-when-then-else constructs
 * for equals-operations :
 * e.g.
 *
 * <pre>{@code
 * QCustomer c = QCustomer.customer;
 * Expression<Integer> cases = c.annualSpending
 *     .when(1000l).then(1)
 *     .when(2000l).then(2)
 *     .when(5000l).then(3)
 *     .otherwise(4);
 * }</pre>
 *
 * @author tiwe
 *
 * @param <D>
 */
public final class CaseForEqBuilder<D> {

    private static final class CaseElement<D> {

        @Nullable
        private final Expression<? extends D> eq;

        private final Expression<?> target;

        CaseElement(@Nullable Expression<? extends D> eq, Expression<?> target) {
            this.eq = eq;
            this.target = target;
        }

        public Expression<? extends D> getEq() {
            return eq;
        }

        public Expression<?> getTarget() {
            return target;
        }

    }

    private final Expression<D> base;

    private final Expression<? extends D> other;

    private final List<CaseElement<D>> caseElements = new ArrayList<CaseElement<D>>();

    private Class<?> type;

    public CaseForEqBuilder(Expression<D> base, Expression<? extends D> other) {
        this.base = base;
        this.other = other;
    }

    public <T> Cases<T,Expression<T>> then(Expression<T> expr) {
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

    private <T> Cases<T, Expression<T>> thenSimple(Expression<T> expr) {
        type = expr.getType();
        return new Cases<T,Expression<T>>() {
            @Override
            protected Expression<T> createResult(Class<T> type, Expression<T> last) {
                return Expressions.operation(type, Ops.CASE_EQ, base, last);
            }
        }.when(other).then(expr);
    }

    public <T> Cases<T,Expression<T>> then(T then) {
        return then(ConstantImpl.create(then));
    }

    @SuppressWarnings("unchecked")
    public <T> Cases<T,Expression<T>> thenNull() {
        return then((Expression<T>) NullExpression.DEFAULT);
    }

    // Boolean

    public Cases<Boolean, BooleanExpression> then(Boolean then) {
        return thenBoolean(ConstantImpl.create(then));
    }

    public Cases<Boolean, BooleanExpression> then(BooleanExpression then) {
        return thenBoolean(then);
    }

    private Cases<Boolean, BooleanExpression> thenBoolean(Expression<Boolean> then) {
        type = then.getType();
        return new Cases<Boolean, BooleanExpression>() {
            @Override
            protected BooleanExpression createResult(Class<Boolean> type, Expression<Boolean> last) {
                return Expressions.booleanOperation(Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    // Comparable

    public <T extends Comparable> Cases<T, ComparableExpression<T>> then(T then) {
        return thenComparable(ConstantImpl.create(then));
    }

    public <T extends Comparable> Cases<T, ComparableExpression<T>> then(ComparableExpression<T> then) {
        return thenComparable(then);
    }

    private <T extends Comparable> Cases<T, ComparableExpression<T>> thenComparable(Expression<T> then) {
        type = then.getType();
        return new Cases<T, ComparableExpression<T>>() {
            @Override
            protected ComparableExpression<T> createResult(Class<T> type, Expression<T> last) {
                return Expressions.comparableOperation(type, Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    // Date

    public Cases<java.sql.Date, DateExpression<java.sql.Date>> then(java.sql.Date then) {
        return thenDate(ConstantImpl.create(then));
    }

    public <T extends Comparable> Cases<T, DateExpression<T>> then(DateExpression<T> then) {
        return thenDate(then);
    }

    private <T extends Comparable> Cases<T, DateExpression<T>> thenDate(Expression<T> then) {
        type = then.getType();
        return new Cases<T, DateExpression<T>>() {
            @Override
            protected DateExpression<T> createResult(Class<T> type, Expression<T> last) {
                return Expressions.dateOperation(type, Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    // DateTime

    public Cases<Date, DateTimeExpression<Date>> then(Date then) {
        return thenDateTime(ConstantImpl.create(then));
    }

    public Cases<Timestamp, DateTimeExpression<Timestamp>> then(Timestamp then) {
        return thenDateTime(ConstantImpl.create(then));
    }

    public <T extends Comparable> Cases<T, DateTimeExpression<T>> then(DateTimeExpression<T> then) {
        return thenDateTime(then);
    }

    private <T extends Comparable> Cases<T, DateTimeExpression<T>> thenDateTime(Expression<T> then) {
        type = then.getType();
        return new Cases<T, DateTimeExpression<T>>() {
            @Override
            protected DateTimeExpression<T> createResult(Class<T> type, Expression<T> last) {
                return Expressions.dateTimeOperation(type, Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    // Enum

    public <T extends Enum<T>> Cases<T, EnumExpression<T>> then(T then) {
        return thenEnum(ConstantImpl.create(then));
    }

    public <T extends Enum<T>> Cases<T, EnumExpression<T>> then(EnumExpression<T> then) {
        return thenEnum(then);
    }

    private <T extends Enum<T>> Cases<T, EnumExpression<T>> thenEnum(Expression<T> then) {
        type = then.getType();
        return new Cases<T, EnumExpression<T>>() {
            @Override
            protected EnumExpression<T> createResult(Class<T> type, Expression<T> last) {
                return Expressions.enumOperation(type, Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    // Number

    public <T extends Number & Comparable<?>> Cases<T, NumberExpression<T>> then(T then) {
        return thenNumber(ConstantImpl.create(then));
    }

    public <T extends Number & Comparable<?>> Cases<T,NumberExpression<T>> then(NumberExpression<T> then) {
        return thenNumber(then);
    }

    public <T extends Number & Comparable<?>> Cases<T,NumberExpression<T>> thenNumber(Expression<T> then) {
        type = then.getType();
        return new Cases<T,NumberExpression<T>>() {
            @SuppressWarnings("unchecked")
            @Override
            protected NumberExpression<T> createResult(Class<T> type, Expression<T> last) {
                return Expressions.numberOperation(type, Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    // String

    public Cases<String,StringExpression> then(String then) {
        return thenString(ConstantImpl.create(then));
    }

    public Cases<String,StringExpression> then(StringExpression then) {
        return thenString(then);
    }

    private Cases<String,StringExpression> thenString(Expression<String> then) {
        type = then.getType();
        return new Cases<String,StringExpression>() {
            @SuppressWarnings("unchecked")
            @Override
            protected StringExpression createResult(Class<String> type, Expression<String> last) {
                return Expressions.stringOperation(Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    // Time

    public Cases<java.sql.Time, TimeExpression<java.sql.Time>> then(java.sql.Time then) {
        return thenTime(ConstantImpl.create(then));
    }

    public <T extends Comparable> Cases<T, TimeExpression<T>> then(TimeExpression<T> then) {
        return thenTime(then);
    }

    private <T extends Comparable> Cases<T, TimeExpression<T>> thenTime(Expression<T> then) {
        type = then.getType();
        return new Cases<T, TimeExpression<T>>() {
            @Override
            protected TimeExpression<T> createResult(Class<T> type, Expression<T> last) {
                return Expressions.timeOperation(type, Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    /**
     * Intermediate step
     *
     * @param <T> Result type
     * @param <Q> Parent expression type
     */
    public abstract class Cases<T, Q extends Expression<T>> {

        public CaseWhen<T, Q> when(Expression<? extends D> when) {
            return new CaseWhen<T,Q>(this, when);
        }

        public CaseWhen<T, Q> when(D when) {
            return when(ConstantImpl.create(when));
        }

        @SuppressWarnings("unchecked")
        public Q otherwise(Expression<T> otherwise) {
            caseElements.add(0, new CaseElement<D>(null, otherwise));
            Expression<T> last = null;
            for (CaseElement<D> element : caseElements) {
                if (last == null) {
                    last = Expressions.operation((Class<T>) type, Ops.CASE_EQ_ELSE,
                            element.getTarget());
                } else {
                    last = Expressions.operation((Class<T>) type, Ops.CASE_EQ_WHEN,
                            base,
                            element.getEq(),
                            element.getTarget(),
                            last);
                }
            }
            return createResult((Class<T>) type, last);
        }

        protected abstract Q createResult(Class<T> type, Expression<T> last);

        public Q otherwise(T otherwise) {
            return otherwise(ConstantImpl.create(otherwise));
        }
    }

    /**
     * Intermediate step
     *
     * @param <T> Result type
     * @param <Q> Parent expression type
     */
    public class CaseWhen<T, Q extends Expression<T>> {

        private final Cases<T, Q> cases;

        private final Expression<? extends D> when;

        public CaseWhen(Cases<T, Q> cases, Expression<? extends D> when) {
            this.cases = cases;
            this.when = when;
        }

        public Cases<T, Q> then(Expression<T> then) {
            caseElements.add(0, new CaseElement<D>(when, then));
            return cases;
        }

        public Cases<T, Q> then(T then) {
            return then(ConstantImpl.create(then));
        }

    }

}
