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

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mysema.util.BeanMap;

/**
 * QBean is a JavaBean populating projection type
 *
 * <p>Example</p>
 *
 * <pre>
 * {@code
 * QEmployee employee = QEmployee.employee;
 * List<EmployeeInfo> result = query.from(employee)
 *      .where(employee.valid.eq(true))
 *      .list(new QBean<EmployeeInfo>(EmployeeInfo.class, employee.firstName, employee.lastName));
 * }
 * </pre>
 *
 * @author tiwe
 *
 * @param <T> bean type
 */
public class QBean<T> extends ExpressionBase<T> implements FactoryExpression<T> {

    private static final long serialVersionUID = -8210214512730989778L;

    private static ImmutableMap<String,Expression<?>> createBindings(Expression<?>... args) {
        Builder<String, Expression<?>> rv = ImmutableMap.builder();
        for (Expression<?> expr : args) {
            if (expr instanceof Path<?>) {
                Path<?> path = (Path<?>)expr;
                rv.put(path.getMetadata().getName(), expr);
            } else if (expr instanceof Operation<?>) {
                Operation<?> operation = (Operation<?>)expr;
                if (operation.getOperator() == Ops.ALIAS && operation.getArg(1) instanceof Path<?>) {
                    Path<?> path = (Path<?>)operation.getArg(1);
                    rv.put(path.getMetadata().getName(), operation.getArg(0));
                } else {
                    throw new IllegalArgumentException("Unsupported expression " + expr);
                }

            } else {
                throw new IllegalArgumentException("Unsupported expression " + expr);
            }
        }
        return rv.build();
    }

    private final ImmutableMap<String, Expression<?>> bindings;

    private final transient Map<String, Field> fields = new HashMap<String, Field>();

    private final boolean fieldAccess;

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param args
     */
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Expression<?>... args) {
        this((Class)type.getType(), false, args);
    }

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param bindings
     */
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, Map<String, ? extends Expression<?>> bindings) {
        this((Class)type.getType(), false, bindings);
    }

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param fieldAccess
     * @param args
     */
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, boolean fieldAccess, Expression<?>... args) {
        this((Class)type.getType(), fieldAccess, args);
    }

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param fieldAccess
     * @param bindings
     */
    @SuppressWarnings("unchecked")
    public QBean(Path<T> type, boolean fieldAccess, Map<String, ? extends Expression<?>> bindings) {
        this((Class)type.getType(), fieldAccess, bindings);
    }

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param bindings
     */
    public QBean(Class<T> type, Map<String, ? extends Expression<?>> bindings) {
        this(type, false, bindings);
    }

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param args
     */
    public QBean(Class<T> type, Expression<?>... args) {
        this(type, false, args);
    }

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param fieldAccess
     * @param args
     */
    public QBean(Class<T> type, boolean fieldAccess, Expression<?>... args) {
        this(type, fieldAccess, createBindings(args));
    }

    /**
     * Create a new QBean instance
     *
     * @param type
     * @param fieldAccess
     * @param bindings
     */
    public QBean(Class<T> type, boolean fieldAccess, Map<String, ? extends Expression<?>> bindings) {
        super(type);
        this.bindings = ImmutableMap.copyOf(bindings);
        this.fieldAccess = fieldAccess;
        if (fieldAccess) {
            initFields();
        }
    }

    private void initFields() {
        for (String property : bindings.keySet()) {
            Class<?> beanType = getType();
            while (!beanType.equals(Object.class)) {
                try {
                    Field field = beanType.getDeclaredField(property);
                    field.setAccessible(true);
                    fields.put(property, field);
                    beanType = Object.class;
                } catch (SecurityException e) {
                    // do nothing
                } catch (NoSuchFieldException e) {
                    beanType = beanType.getSuperclass();
                }
            }
        }
    }


    @Override
    public T newInstance(Object... a) {
        try {
            T rv = getType().newInstance();
            if (fieldAccess) {
                List<String> keys = bindings.keySet().asList();
                for (int i = 0; i < keys.size(); i++) {
                    Object value = a[i];
                    if (value != null) {
                        fields.get(keys.get(i)).set(rv, value);
                    }
                }
            } else {
                Map<String, Object> beanMap = new BeanMap(rv);
                List<String> keys = bindings.keySet().asList();
                for (int i = 0; i < keys.size(); i++) {
                    Object value = a[i];
                    if (value != null) {
                        beanMap.put(keys.get(i), value);
                    }
                }
            }
            return rv;
        } catch (InstantiationException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    @SuppressWarnings("unchecked")
    public Expression<T> as(Path<T> alias) {
        return OperationImpl.create(getType(),Ops.ALIAS, this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @return
     */
    public Expression<T> as(String alias) {
        return as(new PathImpl<T>(getType(), alias));
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof QBean<?>) {
            QBean<?> c = (QBean<?>)obj;
            return getArgs().equals(c.getArgs()) && getType().equals(c.getType());
        } else {
            return false;
        }
    }

    @Override
    public List<Expression<?>> getArgs() {
        return bindings.values().asList();
    }

}
