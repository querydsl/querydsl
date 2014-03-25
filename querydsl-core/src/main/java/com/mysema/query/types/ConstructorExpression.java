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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;
import com.google.common.primitives.Primitives;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * ConstructorExpression represents a constructor invocation
 *
 * <p>Example</p>
 *
 * <pre>
 * {@code
 * QEmployee employee = QEmployee.employee;
 * List<EmployeeInfo> result = query.from(employee)
 *   .where(employee.valid.eq(true))
 *   .list(ConstructorExpression.create(EmployeeInfo.class, employee.firstName, employee.lastName));
 * }
 * </pre>
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
@Immutable
public class ConstructorExpression<T> extends ExpressionBase<T> implements FactoryExpression<T> {

    private static final long serialVersionUID = -602747921848073175L;

    private static Class<?> normalize(Class<?> clazz) {
        return Primitives.wrap(clazz);
    }

    private static final Map<Class<?>, Object> defaultPrimitives = new HashMap<Class<?>, Object>();

    static {
        defaultPrimitives.put(Boolean.TYPE, false);
        defaultPrimitives.put(Byte.TYPE, (byte) 0);
        defaultPrimitives.put(Character.TYPE, (char) 0);
        defaultPrimitives.put(Short.TYPE, (short) 0);
        defaultPrimitives.put(Integer.TYPE, 0);
        defaultPrimitives.put(Long.TYPE, 0L);
        defaultPrimitives.put(Float.TYPE, 0.0F);
        defaultPrimitives.put(Double.TYPE, 0.0);
    }

    private static Class<?>[] getRealParameters(Class<?> type, Class<?>[] givenTypes) {
        for (Constructor<?> c : type.getConstructors()) {
            Class<?>[] paramTypes = c.getParameterTypes();
            if (c.isVarArgs()) {
                return paramTypes;
            } else if (paramTypes.length == givenTypes.length) {
                boolean found = true;
                for (int i = 0; i < paramTypes.length; i++) {
                    if (!normalize(paramTypes[i]).isAssignableFrom(normalize(givenTypes[i]))) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    return paramTypes;
                }
            }
        }
        // prepare error message
        StringBuilder formattedTypes = new StringBuilder();
        for (Class<?> typ : givenTypes) {
            if (formattedTypes.length() > 0) {
                formattedTypes.append(", ");
            }
            formattedTypes.append(typ.getName());
        }
        throw new ExpressionException("Got no matching constructor. Class: " +
                type.getName() +", parameters: " + formattedTypes.toString());
    }

    public static <D> ConstructorExpression<D> create(Class<D> type, Expression<?>... args) {
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < paramTypes.length; i++) {
            paramTypes[i] = args[i].getType();
        }
        return new ConstructorExpression<D>(type, paramTypes, args);
    }

    private final ImmutableList<Expression<?>> args;

    private transient final ConstructorProvider constructorProvider;

    public ConstructorExpression(Class<T> type, Class<?>[] paramTypes, Expression<?>... args) {
        this(type, paramTypes, ImmutableList.copyOf(args));
    }

    public ConstructorExpression(Class<T> type, Class<?>[] paramTypes, ImmutableList<Expression<?>> args) {
        super(type);
        this.constructorProvider = new ConstructorProvider(
                getRealParameters(type, paramTypes).clone());
        this.args = args;
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
        } else if (obj instanceof ConstructorExpression<?>) {
            ConstructorExpression<?> c = (ConstructorExpression<?>)obj;
            return constructorProvider.equals(c.constructorProvider)
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
    public T newInstance(Object... args) {
        try {
            return constructorProvider.provide().newInstance(args);
//            if (ConstructorProvider.getConstructor() == null) {
//                ConstructorProvider.setConstructor(getType().getConstructor(parameterTypes));
//            }
//            if (constructorProvider.getConstructor().isVarArgs()) {
//                Class<?>[] paramTypes = constructorProvider.getConstructor().getParameterTypes();
//                // constructor args
//                Object[] cargs = new Object[paramTypes.length];
//                System.arraycopy(args, 0, cargs, 0, cargs.length - 1);
//                // array with vargs
//                int size = args.length - cargs.length + 1;
//                Object[] array = (Object[]) Array.newInstance(
//                        paramTypes[paramTypes.length - 1].getComponentType(), size);
//                cargs[cargs.length - 1] = array;
//                System.arraycopy(args, cargs.length - 1, array, 0, size);
//                return doNewInstance(cargs);
//            } else {
//                return doNewInstance(args);
//            }

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

    /**
     * A provider for the Constructor.
     *
     * A provider {@link #provide() provides} a provider, with its provider
     * chain.
     *
     * @see VarArgsConstructorProvider
     * @see NullSafePrimitiveConstructorProvider
     */
    private class ConstructorProvider {

        @Nullable
        protected Constructor<?> constructor;
        protected Class<?>[] parameterTypes;
        protected ConstructorProvider providerChain;
        private boolean provided = false;

        public ConstructorProvider(Class<?>[] paramTypes) {
            parameterTypes = paramTypes;
        }

        private ConstructorProvider(ConstructorProvider provider) {
            providerChain = provider;
        }

        /**
         * The provide method fetches the constructor and initializes the
         * provider chain.
         *
         * @return
         * @throws NoSuchMethodException
         */
        public ConstructorProvider provide() throws NoSuchMethodException {
            if (!provided) {
                //initially, only the root constructorprovider is necessary.
                providerChain = this;
                constructor = getType().getConstructor(parameterTypes);
                // check for a primitive parameter.
                for (Class<?> class1 : constructor.getParameterTypes()) {
                    if (class1.isPrimitive()) {
                        providerChain = new NullSafePrimitiveConstructorProvider(providerChain);
                        break;
                    }
                }
                // if there is a varargs constructor.
                if (constructor.isVarArgs()) {
                    providerChain = new VarArgsConstructorProvider(providerChain);
                }
                provided = true;
            }
            return providerChain;

        }

        /**
         * Call the          {@link Constructor constructor's}
         * {@link Constructor#newInstance(java.lang.Object...) newInstance()}
         * method with the arguments provided.
         *
         * @param args the arguments to
         * @return the returned instance from the constructor.
         * @throws InstantiationException
         * @throws IllegalAccessException
         * @throws IllegalArgumentException
         * @throws InvocationTargetException
         */
        @SuppressWarnings("unchecked")
        protected T newInstance(Object[] args) throws
                InstantiationException, IllegalAccessException,
                IllegalArgumentException, InvocationTargetException {

            return (T) constructor.newInstance(args);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj instanceof ConstructorExpression.ConstructorProvider) {
                ConstructorProvider constructorProvider = (ConstructorProvider) obj;
                return Arrays.equals(parameterTypes, constructorProvider.parameterTypes);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 13 * hash + Arrays.deepHashCode(this.parameterTypes);
            return hash;
        }

    }

    private class NullSafePrimitiveConstructorProvider extends ConstructorProvider {

        List<Integer> primitiveLocations = new ArrayList<Integer>();

        public NullSafePrimitiveConstructorProvider(ConstructorProvider parent) {
            super(parent);
            parameterTypes = parent.parameterTypes;
            for (int i = 0; i < parameterTypes.length; i++) {
                Class<?> clazz = parameterTypes[i];
                if (clazz.isPrimitive()) {
                    primitiveLocations.add(i);
                }
            }
        }

        @Override
        protected T newInstance(Object[] args) throws
                InstantiationException, IllegalAccessException,
                IllegalArgumentException, InvocationTargetException {

            for (Integer position : primitiveLocations) {

                // if the parameter at the position of 
                // a primitive type is null, then provide the default value.
                if (args[position] == null) {
                    args[position] = defaultPrimitives.get(parameterTypes[position]);
                }
            }
            return providerChain.newInstance(args);
        }

    }

}
