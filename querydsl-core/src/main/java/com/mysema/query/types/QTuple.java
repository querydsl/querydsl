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
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
 * }
 * } 
 * </pre>
 *
 * Duplicate expressions are removed in the constructor.
 *
 * @author tiwe
 *
 */
public class QTuple extends ExpressionBase<Tuple> implements FactoryExpression<Tuple> {

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
            int index = QTuple.this.args.indexOf(expr);
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

    private final List<Expression<?>> args;

    /**
     * Create a new QTuple instance
     * 
     * @param args
     */
    public QTuple(Expression<?>... args) {
        super(Tuple.class);        
        Set<Expression<?>> set = new HashSet<Expression<?>>(args.length);
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (Expression<?> arg : args) {
            if (set.add(arg)) {
                builder.add(arg);
            }
        }        
        this.args = builder.build();
    }
    
    /**
     * Create a new QTuple instance
     * 
     * @param args
     */
    public QTuple(List<? extends Expression<?>> args) {
        super(Tuple.class);
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (int i = 0; i < args.size(); i++) {
            Expression<?> arg = args.get(i);
            if (args.indexOf(arg) == i) {
                builder.add(arg);
            }
        }
        this.args = builder.build();
    }

    /**
     * Create a new QTuple instance
     * 
     * @param args
     */
    public QTuple(Expression<?>[]... args) {
        super(Tuple.class);
        Set<Expression<?>> argsSet = new LinkedHashSet<Expression<?>>();
        for (Expression<?>[] exprs: args){
            argsSet.addAll(Arrays.asList(exprs));
        }
        this.args = ImmutableList.copyOf(argsSet);
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
        } else if (obj instanceof QTuple) {
            QTuple c = (QTuple)obj;
            return args.equals(c.args) && getType().equals(c.getType());
        } else {
            return false;
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

}
