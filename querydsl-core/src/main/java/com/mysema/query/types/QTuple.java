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
package com.mysema.query.types;

import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;
import com.mysema.query.Tuple;

/**
 * QTuple represents a projection of type Tuple
 *
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * List<Tuple> result = query.from(employee).list(new QTuple(employee.firstName, employee.lastName));
 * for (Tuple row : result) {
 *     System.out.println("firstName " + row.get(employee.firstName));
 *     System.out.println("lastName " + row.get(employee.lastName));
 * }}
 * </pre>
 *
 * <p>Since Tuple projection is the default for multi column projections, the above is equivalent to this code</p>
 *
 * <pre>
 * {@code
 * List<Tuple> result = query.from(employee).list(employee.firstName, employee.lastName);
 * for (Tuple row : result) {
 *     System.out.println("firstName " + row.get(employee.firstName));
 *     System.out.println("lastName " + row.get(employee.lastName));
 * }}
 * </pre>
 *
 * @author tiwe
 *
 */
@Immutable
public class QTuple extends ExpressionBase<Tuple> implements FactoryExpression<Tuple> {

    private static ImmutableList<Expression<?>> createBindings(List<Expression<?>> exprs) {
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (Expression<?> e : exprs) {
            if (e instanceof Operation && ((Operation<?>)e).getOperator() == Ops.ALIAS) {
                builder.add(((Operation<?>)e).getArg(1));
            } else {
                builder.add(e);
            }
        }
        return builder.build();
    }

    private final class TupleImpl implements Tuple {
        private final Object[] a;

        private TupleImpl(Object[] a) {
            this.a = a;
        }

        @Override
        public <T> T get(int index, Class<T> type) {
            return (T) a[index];
        }

        @Override
        public <T> T get(Expression<T> expr) {
            int index = QTuple.this.bindings.indexOf(expr);
            return index != -1 ? (T) a[index] : null;
        }

        @Override
        public int size() {
            return a.length;
        }

        @Override
        public Object[] toArray() {
            return a;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof Tuple) {
                return Arrays.equals(a, ((Tuple) obj).toArray());
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(a);
        }

        @Override
        public String toString() {
            return Arrays.toString(a);
        }
    }

    private static final long serialVersionUID = -2640616030595420465L;

    private final ImmutableList<Expression<?>> args;

    private final ImmutableList<Expression<?>> bindings;

    /**
     * Create a new QTuple instance
     *
     * @param args
     */
    public QTuple(Expression<?>... args) {
        super(Tuple.class);
        this.args = ImmutableList.copyOf(args);
        this.bindings = createBindings(this.args);
    }

    /**
     * Create a new QTuple instance
     *
     * @param args
     */
    public QTuple(ImmutableList<Expression<?>> args) {
        super(Tuple.class);
        this.args = args;
        this.bindings = createBindings(this.args);
    }

    /**
     * Create a new QTuple instance
     *
     * @param args
     */
    public QTuple(Expression<?>[]... args) {
        super(Tuple.class);
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (Expression<?>[] exprs: args) {
            builder.add(exprs);
        }
        this.args = builder.build();
        this.bindings = createBindings(this.args);
    }

    @Override
    public Tuple newInstance(Object... a) {
        return new TupleImpl(a);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof FactoryExpression) {
            FactoryExpression<?> c = (FactoryExpression<?>)obj;
            return args.equals(c.getArgs()) && getType().equals(c.getType());
        } else {
            return false;
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

}
