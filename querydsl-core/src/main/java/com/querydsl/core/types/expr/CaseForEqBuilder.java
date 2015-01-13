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
package com.querydsl.core.types.expr;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Ops;

/**
 * CaseForEqBuilder enables the construction of typesafe case-when-then-else constructs
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

    private static class CaseElement<D> {

        @Nullable
        private final Expression<? extends D> eq;

        private final Expression<?> target;

        public CaseElement(@Nullable Expression<? extends D> eq, Expression<?> target) {
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

    public <T> Cases<T,Expression<T>> then(Expression<T> then) {
        type = then.getType();
        return new Cases<T,Expression<T>>() {
            @Override
            protected Expression<T> createResult(Class<T> type, Expression<T> last) {
                return SimpleOperation.create(type, Ops.CASE_EQ, base, last);
            }
        }.when(other).then(then);
    }

    public <T> Cases<T,Expression<T>> then(T then) {
        return then(ConstantImpl.create(then));
    }

    public <T> Cases<T,Expression<T>> thenNull() {
        return then((Expression<T>)NullExpression.DEFAULT);
    }

    public <T extends Number & Comparable<?>> Cases<T,NumberExpression<T>> then(T then) {
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
                return NumberOperation.create(type, Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    public Cases<String,StringExpression> then(StringExpression then) {
        return thenString(then);
    }

    public Cases<String,StringExpression> then(String then) {
        return thenString(ConstantImpl.create(then));
    }

    private Cases<String,StringExpression> thenString(Expression<String> then) {
        type = then.getType();
        return new Cases<String,StringExpression>() {
            @SuppressWarnings("unchecked")
            @Override
            protected StringExpression createResult(Class<String> type, Expression<String> last) {
                return StringOperation.create(Ops.CASE_EQ, base, last);
            }

        }.when(other).then(then);
    }

    public abstract class Cases<T, Q extends Expression<T>> {

        public CaseWhen<T,Q> when(Expression<? extends D> when) {
            return new CaseWhen<T,Q>(this, when);
        }

        public CaseWhen<T,Q> when(D when) {
            return when(ConstantImpl.create(when));
        }

        @SuppressWarnings("unchecked")
        public Q otherwise(Expression<T> otherwise) {
            caseElements.add(0, new CaseElement<D>(null, otherwise));
            Expression<T> last = null;
            for (CaseElement<D> element : caseElements) {
                if (last == null) {
                    last = SimpleOperation.create((Class<T>)type, Ops.CASE_EQ_ELSE,
                            element.getTarget());
                } else {
                    last = SimpleOperation.create((Class<T>)type, Ops.CASE_EQ_WHEN,
                            base,
                            element.getEq(),
                            element.getTarget(),
                            last);
                }
            }
            return createResult((Class<T>)type, last);
        }

        protected abstract Q createResult(Class<T> type, Expression<T> last);

        public Q otherwise(T otherwise) {
            return otherwise(ConstantImpl.create(otherwise));
        }
    }

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
