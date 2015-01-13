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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Primitives;

/**
 * QBean is a JavaBean populating projection type
 *
 * <p>Example</p>
 *
 * <pre>
 * {@code
 * QEmployee employee = QEmployee.employee;
 * List<EmployeeInfo> result = querydsl.from(employee)
 *      .where(employee.valid.eq(true))
 *      .list(new QBean<EmployeeInfo>(EmployeeInfo.class, employee.firstName, employee.lastName));
 * }
 * </pre>
 *
 * @author tiwe
 *
 * @param <T> bean type
 */
public class QBean<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = -8210214512730989778L;

    private static ImmutableMap<String,Expression<?>> createBindings(Expression<?>... args) {
        ImmutableMap.Builder<String, Expression<?>> rv = ImmutableMap.builder();
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

    private static Class<?> normalize(Class<?> cl) {
        return cl.isPrimitive() ? Primitives.wrap(cl) : cl;
    }

    private final ImmutableMap<String, Expression<?>> bindings;

    private final List<Field> fields;

    private final List<Method> setters;

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
            this.fields = initFields(bindings);
            this.setters = ImmutableList.of();
        } else {
            this.fields = ImmutableList.of();
            this.setters = initMethods(bindings);
        }
    }

    private List<Field> initFields(Map<String, ? extends Expression<?>> args) {
        List<Field> fields = new ArrayList<Field>(args.size());
        for (Map.Entry<String,? extends Expression<?>> entry : args.entrySet()) {
            String property = entry.getKey();
            Expression<?> expr = entry.getValue();
            Class<?> beanType = getType();
            Field field = null;
            while (!beanType.equals(Object.class)) {
                try {
                    field = beanType.getDeclaredField(property);
                    field.setAccessible(true);
                    if (!normalize(field.getType()).isAssignableFrom(expr.getType())) {
                        typeMismatch(field.getType(), expr);
                    }
                    beanType = Object.class;
                } catch (SecurityException e) {
                    // do nothing
                } catch (NoSuchFieldException e) {
                    beanType = beanType.getSuperclass();
                }
            }
            if (field == null) {
                propertyNotFound(expr, property);
            }
            fields.add(field);
        }
        return fields;
    }

    private List<Method> initMethods(Map<String, ? extends Expression<?>> args) {
        try {
            List<Method> methods = new ArrayList<Method>(args.size());
            BeanInfo beanInfo = Introspector.getBeanInfo(getType());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (Map.Entry<String, ? extends Expression<?>> entry : args.entrySet()) {
                String property = entry.getKey();
                Expression<?> expr = entry.getValue();
                Method setter = null;
                for (PropertyDescriptor prop : propertyDescriptors) {
                    if (prop.getName().equals(property)) {
                        setter = prop.getWriteMethod();
                        if (!normalize(prop.getPropertyType()).isAssignableFrom(expr.getType())) {
                            typeMismatch(prop.getPropertyType(), expr);
                        }
                        break;
                    }
                }
                if (setter == null) {
                    propertyNotFound(expr, property);
                }
                methods.add(setter);
            }
            return methods;
        } catch (IntrospectionException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    protected void propertyNotFound(Expression<?> expr, String property) {
        // do nothing
    }

    protected void typeMismatch(Class<?> type, Expression<?> expr) {
        final String msg = expr.getType().getName() + " is not compatible with " + type.getName();
        throw new IllegalArgumentException(msg);
    }

    @Override
    public T newInstance(Object... a) {
        try {
            T rv = create(getType());
            if (fieldAccess) {
                for (int i = 0; i < a.length; i++) {
                    Object value = a[i];
                    if (value != null) {
                        Field field = fields.get(i);
                        if (field != null) field.set(rv, value);
                    }
                }
            } else {
                for (int i = 0; i < a.length; i++) {
                    Object value = a[i];
                    if (value != null) {
                        Method setter = setters.get(i);
                        if (setter != null) setter.invoke(rv, value);
                    }
                }
            }
            return rv;
        } catch (InstantiationException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new ExpressionException(e.getMessage(), e);
        } catch (InvocationTargetException e) {
            throw new ExpressionException(e.getMessage(), e);
        }
    }

    protected <T> T create(Class<T> type) throws IllegalAccessException, InstantiationException {
        return type.newInstance();
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
