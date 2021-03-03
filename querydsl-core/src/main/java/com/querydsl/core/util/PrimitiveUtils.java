/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.querydsl.core.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Primitive utility methods. Adjusted from Guavas {@code Primitives}.
 *
 * @author Kevin Bourrillion
 * @author Jan-Willem Gmelig Meyling
 */
public final class PrimitiveUtils {

    private PrimitiveUtils() {
    }

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_WRAPPER_TYPE;

    private static final Map<Class<?>, Class<?>> WRAPPER_TO_PRIMITIVE_TYPE;

    static {
        Map<Class<?>, Class<?>> primToWrap = new HashMap<>(16);
        Map<Class<?>, Class<?>> wrapToPrim = new HashMap<>(16);

        add(primToWrap, wrapToPrim, boolean.class, Boolean.class);
        add(primToWrap, wrapToPrim, byte.class, Byte.class);
        add(primToWrap, wrapToPrim, char.class, Character.class);
        add(primToWrap, wrapToPrim, double.class, Double.class);
        add(primToWrap, wrapToPrim, float.class, Float.class);
        add(primToWrap, wrapToPrim, int.class, Integer.class);
        add(primToWrap, wrapToPrim, long.class, Long.class);
        add(primToWrap, wrapToPrim, short.class, Short.class);
        add(primToWrap, wrapToPrim, void.class, Void.class);

        PRIMITIVE_TO_WRAPPER_TYPE = Collections.unmodifiableMap(primToWrap);
        WRAPPER_TO_PRIMITIVE_TYPE = Collections.unmodifiableMap(wrapToPrim);
    }

    private static void add(Map<Class<?>, Class<?>> forward, Map<Class<?>, Class<?>> backward, Class<?> key, Class<?> value) {
        forward.put(key, value);
        backward.put(value, key);
    }

    public static boolean isWrapperType(Class<?> clazz) {
        return WRAPPER_TO_PRIMITIVE_TYPE.containsKey(clazz);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> wrap(Class<T> type) {
        return (Class<T>) PRIMITIVE_TO_WRAPPER_TYPE.getOrDefault(type, type);
    }

    @SuppressWarnings("unchecked")
    public static <T> Class<T> unwrap(Class<T> type) {
        return (Class<T>) WRAPPER_TO_PRIMITIVE_TYPE.getOrDefault(type, type);
    }

}
