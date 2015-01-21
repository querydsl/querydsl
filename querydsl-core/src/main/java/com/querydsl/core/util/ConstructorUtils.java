/*
 * Copyright 2014, Mysema Ltd
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
package com.querydsl.core.util;

import static com.google.common.collect.Iterables.filter;
import static com.querydsl.core.util.ArrayUtils.isEmpty;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.primitives.Primitives;
import com.querydsl.core.types.ExpressionException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;

/**
 *
 * @author Shredder121
 */
public class ConstructorUtils {

    /**
     * The parameter list for the default constructor;
     */
    private static final Class<?>[] NO_ARGS = {};

    private static final ClassToInstanceMap<Object> defaultPrimitives
            = ImmutableClassToInstanceMap.builder()
            .put(Boolean.TYPE, false)
            .put(Byte.TYPE, (byte) 0)
            .put(Character.TYPE, (char) 0)
            .put(Short.TYPE, (short) 0)
            .put(Integer.TYPE, 0)
            .put(Long.TYPE, 0L)
            .put(Float.TYPE, 0.0F)
            .put(Double.TYPE, 0.0)
            .build();

    /**
     * Returns the constructor where the formal parameter list matches the
     * givenTypes argument.
     *
     * It is advisable to first call
     * {@link #getConstructorParameters(java.lang.Class, java.lang.Class[])}
     * to get the parameters.
     *
     * @param type
     * @param givenTypes
     * @return
     * @throws NoSuchMethodException
     */
    public static <C> Constructor<C> getConstructor(Class<C> type, Class<?>[] givenTypes) throws NoSuchMethodException {
        return type.getConstructor(givenTypes);
    }

    /**
     * Returns the parameters for the constructor that matches the given types.
     *
     * @param type
     * @param givenTypes
     * @return
     */
    public static Class<?>[] getConstructorParameters(Class<?> type, Class<?>[] givenTypes) {
        next_constructor:
        for (Constructor<?> constructor : type.getConstructors()) {
            int matches = 0;
            Class<?>[] parameters = constructor.getParameterTypes();
            Iterator<Class<?>> parameterIterator = Arrays
                    .asList(parameters)
                    .iterator();
            if (!isEmpty(givenTypes)
                    && !isEmpty(parameters)) {
                Class<?> parameter = null;
                for (Class<?> argument : givenTypes) {

                    if (parameterIterator.hasNext()) {
                        parameter = parameterIterator.next();
                        if (!compatible(parameter, argument)) {
                            continue next_constructor;
                        }
                        matches++;
                    } else if (constructor.isVarArgs()) {
                        if (!compatible(parameter, argument)) {
                            continue next_constructor;
                        }
                    } else {
                        continue next_constructor; //default
                    }
                }
                if (matches == parameters.length) {
                    return parameters;
                }
            } else if (isEmpty(givenTypes)
                    && isEmpty(parameters)) {
                return NO_ARGS;
            }
        }
        throw new ExpressionException("No constructor found for " + type.toString()
                + " with parameters: " + Arrays.deepToString(givenTypes));
    }

    /**
     * Returns a list of transformers applicable to the given constructor.
     *
     * @param constructor
     * @return
     */
    public static Iterable<Function<Object[], Object[]>> getTransformers(Constructor<?> constructor) {
        Iterable<ArgumentTransformer> transformers = Lists.newArrayList(
                new PrimitiveAwareVarArgsTransformer(constructor),
                new PrimitiveTransformer(constructor),
                new VarArgsTransformer(constructor));

        return ImmutableList
                .<Function<Object[], Object[]>>copyOf(filter(transformers, applicableFilter));
    }

    private static Class<?> normalize(Class<?> clazz) {
        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return Primitives.wrap(clazz);
    }

    private static boolean compatible(Class<?> parameter, Class<?> argument) {
        return normalize(parameter)
                .isAssignableFrom(normalize(argument));
    }

    private static final Predicate<ArgumentTransformer> applicableFilter
            = new Predicate<ArgumentTransformer>() {

                @Override
                public boolean apply(ArgumentTransformer transformer) {
                    return transformer != null ? transformer.isApplicable() : false;
                }
            };

    protected static abstract class ArgumentTransformer implements Function<Object[], Object[]> {

        @Nullable
        protected Constructor<?> constructor;
        protected final Class<?>[] paramTypes;

        public ArgumentTransformer(Constructor<?> constructor) {
            this(constructor.getParameterTypes());
            this.constructor = constructor;
        }

        public ArgumentTransformer(Class<?>[] paramTypes) {
            this.paramTypes = paramTypes;
        }

        public abstract boolean isApplicable();
    }

    private static class VarArgsTransformer extends ArgumentTransformer {

        protected final Class<?> componentType;

        private VarArgsTransformer(Constructor<?> constructor) {
            super(constructor);

            if (paramTypes.length > 0) {
                componentType = paramTypes[paramTypes.length - 1].getComponentType();
            } else {
                componentType = null;
            }
        }

        @Override
        public boolean isApplicable() {
            return constructor != null ? constructor.isVarArgs() : false;
        }

        @Override
        public Object[] apply(Object[] args) {
            if (isEmpty(args)) {
                return args;
            }
            int current = 0;

            // constructor args
            Object[] cargs = new Object[paramTypes.length];
            for (int i = 0; i < cargs.length - 1; i++) {
                set(cargs, i, args[current++]);
            }
            // array with vargs
            int size = args.length - cargs.length + 1;
            Object vargs = Array.newInstance(
                    componentType, size);
            cargs[cargs.length - 1] = vargs;
            for (int i = 0; i < Array.getLength(vargs); i++) {
                set(vargs, i, args[current++]);
            }
            return cargs;
        }

        private void set(Object array, int index, Object value) throws IllegalArgumentException, ArrayIndexOutOfBoundsException {
            Array.set(array, index, value);
        }

    }

    private static class PrimitiveTransformer extends ArgumentTransformer {

        private final Set<Integer> primitiveLocations;

        private PrimitiveTransformer(Constructor<?> constructor) {
            super(constructor);
            ImmutableSet.Builder<Integer> builder = ImmutableSet.builder();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int location = 0; location < parameterTypes.length; location++) {
                Class<?> parameterType = parameterTypes[location];

                if (parameterType.isPrimitive()) {
                    builder.add(location);
                }
            }
            primitiveLocations = builder.build();
        }

        @Override
        public boolean isApplicable() {
            return !primitiveLocations.isEmpty();
        }

        @Override
        public Object[] apply(Object[] args) {
            if (isEmpty(args)) {
                return args;
            }
            for (Integer location : primitiveLocations) {
                if (args[location] == null) {
                    Class<?> primitiveClass = paramTypes[location];
                    args[location] = defaultPrimitives.getInstance(primitiveClass);
                }
            }
            return args;
        }

    }

    private static class PrimitiveAwareVarArgsTransformer extends VarArgsTransformer {

        private final Object defaultInstance;

        public PrimitiveAwareVarArgsTransformer(Constructor<?> constructor) {
            super(constructor);
            defaultInstance = (componentType != null) ? defaultPrimitives.getInstance(componentType) : null;
        }

        @Override
        public boolean isApplicable() {
            return super.isApplicable()
                    && (componentType != null ? componentType.isPrimitive() : false);
        }

        @Override
        public Object[] apply(Object[] args) {
            if (isEmpty(args)) {
                return args;
            }
            for (int i = paramTypes.length - 1; i < args.length; i++) {
                if (args[i] == null) {
                    args[i] = defaultInstance;
                }
            }
            return args;
        }

    }

}
