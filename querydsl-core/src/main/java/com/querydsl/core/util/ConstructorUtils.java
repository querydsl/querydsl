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
package com.querydsl.core.util;

import com.querydsl.core.types.ExpressionException;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jetbrains.annotations.Nullable;
import static com.querydsl.core.util.ArrayUtils.isEmpty;

/**
 * ConstructorUtils provides constructor resolving functionality
 *
 * @author Shredder121
 */
public final class ConstructorUtils {

    private ConstructorUtils() { }

    /**
     * The parameter list for the default constructor;
     */
    private static final Class<?>[] NO_ARGS = {};

    private static final Map<Class<?>, Object> defaultPrimitives = new HashMap<>();

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

    /**
     * Returns the constructor where the formal parameter list matches the
     * givenTypes argument.
     *
     * It is advisable to first call
     * {@link #getConstructorParameters(java.lang.Class, java.lang.Class[])}
     * to get the parameters.
     *
     * @param type type
     * @param givenTypes parameter types
     * @return matching constructor
     * @throws NoSuchMethodException
     */
    public static <C> Constructor<C> getConstructor(Class<C> type, Class<?>[] givenTypes) throws NoSuchMethodException {
        return type.getConstructor(givenTypes);
    }

    /**
     * Returns the parameters for the constructor that matches the given types.
     *
     * @param type type
     * @param givenTypes parameter types
     * @return constructor parameters
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
     * Returns a fetch of transformers applicable to the given constructor.
     *
     * @param constructor constructor
     * @return transformers
     */
    public static Iterable<Function<Object[], Object[]>> getTransformers(Constructor<?> constructor) {
        return Stream.of(
                new PrimitiveAwareVarArgsTransformer(constructor),
                new PrimitiveTransformer(constructor),
                new VarArgsTransformer(constructor))
                .filter(ArgumentTransformer::isApplicable)
                .collect(Collectors.toList());
    }

    private static Class<?> normalize(Class<?> clazz) {
        if (clazz.isArray()) {
            clazz = clazz.getComponentType();
        }
        return PrimitiveUtils.wrap(clazz);
    }

    private static boolean compatible(Class<?> parameter, Class<?> argument) {
        return normalize(parameter)
                .isAssignableFrom(normalize(argument));
    }

    protected abstract static class ArgumentTransformer implements Function<Object[], Object[]> {

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

        VarArgsTransformer(Constructor<?> constructor) {
            super(constructor);

            if (paramTypes.length > 0) {
                componentType = paramTypes[paramTypes.length - 1].getComponentType();
            } else {
                componentType = null;
            }
        }

        @Override
        public boolean isApplicable() {
            return constructor != null && constructor.isVarArgs();
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

        PrimitiveTransformer(Constructor<?> constructor) {
            super(constructor);
            Set<Integer> builder = new TreeSet<>();
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (int location = 0; location < parameterTypes.length; location++) {
                Class<?> parameterType = parameterTypes[location];

                if (parameterType.isPrimitive()) {
                    builder.add(location);
                }
            }
            primitiveLocations = Collections.unmodifiableSet(builder);
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
                    args[location] = defaultPrimitives.get(primitiveClass);
                }
            }
            return args;
        }

    }

    private static class PrimitiveAwareVarArgsTransformer extends VarArgsTransformer {

        private final Object defaultInstance;

        PrimitiveAwareVarArgsTransformer(Constructor<?> constructor) {
            super(constructor);
            defaultInstance = (componentType != null) ? defaultPrimitives.get(componentType) : null;
        }

        @Override
        public boolean isApplicable() {
            return super.isApplicable()
                    && (componentType != null && componentType.isPrimitive());
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
