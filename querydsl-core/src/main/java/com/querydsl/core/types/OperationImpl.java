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
package com.querydsl.core.types;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.List;

import com.google.common.collect.ImmutableList;


/**
 * OperationImpl is the default implementation of the Operation interface
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@Immutable
public class OperationImpl<T> extends ExpressionBase<T> implements Operation<T> {

    private static final long serialVersionUID = 4796432056083507588L;

    private final ImmutableList<Expression<?>> args;

    private final Operator<? super T> operator;

    public static <RT> Operation<RT> create(Class<? extends RT> type, Operator<? super RT> operator, Expression<?> one) {
        return new OperationImpl<RT>(type, operator, ImmutableList.<Expression<?>>of(one));
    }

    public static <RT> Operation<RT> create(Class<? extends RT> type, Operator<? super RT> operator, Expression<?> one, Expression<?> two) {
        return new OperationImpl<RT>(type, operator, ImmutableList.of(one, two));
    }

    protected OperationImpl(Class<? extends T> type, Operator<? super T> operator, Expression<?>... args) {
        this(type, operator, ImmutableList.copyOf(args));
    }

    public OperationImpl(Class<? extends T> type, Operator<? super T> operator, ImmutableList<Expression<?>> args) {
        super(type);
        this.operator = operator;
        this.args = args;
    }

    @Override
    public final Expression<?> getArg(int i) {
        return args.get(i);
    }

    @Override
    public final List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public final Operator<? super T> getOperator() {
        return operator;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof Operation<?>) {
            Operation<?> op = (Operation<?>)o;
            return op.getOperator() == operator
                && op.getArgs().equals(args)
                && op.getType().equals(getType());
        } else {
            return false;
        }
    }

    @Override
    public final <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    /**
     * Resets operator field to singleton version
     *
     * @param ois
     * @throws ClassNotFoundException
     * @throws IOException
     */
    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        try {
            ois.defaultReadObject();
            Field field = OperationImpl.class.getDeclaredField("operator");
            field.setAccessible(true);
            field.set(this, OperatorImpl.OPS.get(operator.getId()));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

}
