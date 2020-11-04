package com.querydsl.core.util;

import com.google.common.primitives.Primitives;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Primitive utility methods
 *
 * @author Jan-Willem
 */
public final class PrimitiveUtils {

    private PrimitiveUtils() {
    }

    private static final Set<Class> WRAPPER_TYPES = Collections.unmodifiableSet(new HashSet(Arrays.asList(
            Boolean.class, Character.class, Byte.class, Short.class, Integer.class, Long.class, Float.class, Double.class, Void.class)));

    public static boolean isWrapperType(Class clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    public static <T> Class<T> wrap(Class<T> type) {
        return Primitives.wrap(type);
    }

    public static <T> Class<T> unwrap(Class<T> type) {
        return Primitives.unwrap(type);
    }

}
