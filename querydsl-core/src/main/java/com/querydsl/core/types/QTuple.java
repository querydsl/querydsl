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
package com.querydsl.core.types;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import com.querydsl.core.Tuple;
import com.querydsl.core.util.CollectionUtils;

/**
 * {@code QTuple} represents a projection of type {@link Tuple}
 *
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * List<Tuple> result = query.from(employee).select(Projections.tuple(employee.firstName, employee.lastName)).fetch();
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
 * List<Tuple> result = query.from(employee).select(employee.firstName, employee.lastName).fetch();
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
public class QTuple extends FactoryExpressionBase<Tuple> {

    private static Map<Expression<?>, Integer> createBindings(List<Expression<?>> exprs) {
        Map<Expression<?>, Integer> map = new LinkedHashMap<>();
        for (int i = 0; i < exprs.size(); i++) {
            Expression<?> e = exprs.get(i);
            if (e instanceof Operation && ((Operation<?>) e).getOperator() == Ops.ALIAS) {
                map.put(((Operation<?>) e).getArg(1), i);
            }
            map.put(e, i);
        }
        return Collections.unmodifiableMap(map);
    }

    private final class TupleImpl implements Tuple, Serializable {

        private static final long serialVersionUID = 6635924689293325950L;

        private final Object[] a;

        private TupleImpl(Object[] a) {
            this.a = a;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(int index, Class<T> type) {
            return (T) a[index];
        }

        @SuppressWarnings("unchecked")
        @Override
        public <T> T get(Expression<T> expr) {
            Integer idx = QTuple.this.bindings.get(expr);
            if (idx != null) {
                return (T) a[idx];
            } else {
                return null;
            }
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

    private final List<Expression<?>> args;

    private final Map<Expression<?>, Integer> bindings;

    /**
     * Create a new QTuple instance
     *
     * @param args
     */
    protected QTuple(Expression<?>... args) {
        this(Arrays.asList(args));
    }

    /**
     * Create a new QTuple instance
     *
     * @param args
     */
    protected QTuple(List<Expression<?>> args) {
        super(Tuple.class);
        this.args = CollectionUtils.unmodifiableList(args);
        this.bindings = createBindings(this.args);
    }

    /**
     * Create a new QTuple instance
     *
     * @param args
     */
    protected QTuple(Expression<?>[]... args) {
        super(Tuple.class);
        ArrayList<Expression<?>> builder = new ArrayList<>();
        for (Expression<?>[] exprs: args) {
            Collections.addAll(builder, exprs);
        }
        this.args = CollectionUtils.unmodifiableList(builder);
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
            FactoryExpression<?> c = (FactoryExpression<?>) obj;
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
