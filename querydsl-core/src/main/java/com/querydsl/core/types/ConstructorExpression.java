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

import static com.querydsl.core.util.ConstructorUtils.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;

/**
 * {@code ConstructorExpression} represents a constructor invocation
 *
 * <p>Example</p>
 *
 * <pre>
 * {@code
 * QEmployee employee = QEmployee.employee;
 * List<EmployeeInfo> result = query.from(employee)
 *   .where(employee.valid.eq(true))
 *   .select(Projections.constructor(EmployeeInfo.class, employee.firstName, employee.lastName))
 *   .fetch();
 * }
 * </pre>
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@Immutable
public class ConstructorExpression<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = -602747921848073175L;

    private static Class<?>[] getParameterTypes(Expression<?>... args) {
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = args[i].getType();
        }
        return paramTypes;
    }

    private final ImmutableList<Expression<?>> args;

    private final Class<?>[] parameterTypes;

    @Nullable
    private transient Constructor<?> constructor;

    private transient Iterable<Function<Object[], Object[]>> transformers;

    protected ConstructorExpression(Class<? extends T> type, Expression<?>... args) {
        this(type, getParameterTypes(args), ImmutableList.copyOf(args));
    }

    protected ConstructorExpression(Class<? extends T> type, Class<?>[] paramTypes, Expression<?>... args) {
        this(type, paramTypes, ImmutableList.copyOf(args));
    }

    protected ConstructorExpression(Class<? extends T> type, Class<?>[] paramTypes, ImmutableList<Expression<?>> args) {
        super(type);
        this.parameterTypes = getConstructorParameters(type, paramTypes).clone();
        this.args = args;
    }

    /**
     * Create an alias for the expression
     *
     * @return alias expression
     */
    @SuppressWarnings("unchecked")
    public Expression<T> as(Path<T> alias) {
        return ExpressionUtils.operation(getType(), Ops.ALIAS, this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return alias expression
     */
    public Expression<T> as(String alias) {
        return as(ExpressionUtils.path(getType(), alias));
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof ConstructorExpression<?>) {
            ConstructorExpression<?> c = (ConstructorExpression<?>) obj;
            return Arrays.equals(parameterTypes, c.parameterTypes)
                    && args.equals(c.args)
                    && getType().equals(c.getType());
        } else {
            return false;
        }
    }

    @Override
    public final List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T newInstance(Object... args) {
        try {
            if (constructor == null) {
                constructor = getConstructor(getType(), parameterTypes);
                transformers = getTransformers(constructor);
            }
            for (Function<Object[], Object[]> transformer : transformers) {
                args = transformer.apply(args);
            }
            return (T) constructor.newInstance(args);
        } catch (SecurityException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (NoSuchMethodException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (InstantiationException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }

}
