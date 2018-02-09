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
package com.querydsl.core.alias;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.querydsl.core.types.EntityPath;

/**
 * {@code MethodType} defines the supported method types used in the Alias functionality
 *
 * @author tiwe
 *
 */
public enum MethodType {
    /**
     * __mappedPath invocation
     */
    GET_MAPPED_PATH("__mappedPath", EntityPath.class, ManagedObject.class),
    /**
     * getter invocation
     */
    GETTER("(get|is).+", Object.class, Object.class),
    /**
     * hashCode invocation
     */
    HASH_CODE("hashCode", int.class, Object.class),
    /**
     * List#get(int) invocation
     */
    LIST_ACCESS("get", Object.class, List.class, int.class),
    /**
     * Map#get(Object) invocation
     */
    MAP_ACCESS("get", Object.class, Map.class, Object.class),
    /**
     * Collection#size() invocation
     */
    SIZE("size", int.class, Object.class),
    /**
     * toString invocation
     */
    TO_STRING("toString",String.class, Object.class),
    /**
     * Scala getter invocation
     */
    SCALA_GETTER(".+", Object.class, Object.class),
    /**
     * Scala list apply invocation
     */
    SCALA_LIST_ACCESS("apply", Object.class, Object.class, int.class),
    /**
     * Scala map apply invocation
     */
    SCALA_MAP_ACCESS("apply", Object.class, Object.class, Object.class);

    private final Pattern pattern;

    private final Class<?> returnType;

    private final Class<?> ownerType;

    private final Class<?>[] paramTypes;

    private MethodType(String namePattern, Class<?> returnType, Class<?> ownerType, Class<?>... paramTypes) {
        this.pattern = Pattern.compile(namePattern);
        this.returnType = returnType;
        this.ownerType = ownerType;
        this.paramTypes = paramTypes;
    }

    @Nullable
    public static MethodType get(Method method) {
        for (MethodType methodType : values()) {
            if (methodType.pattern.matcher(method.getName()).matches()
                    && (methodType.returnType == Object.class || methodType.returnType.isAssignableFrom(method.getReturnType()))
                    && (methodType.ownerType == Object.class || methodType.ownerType.isAssignableFrom(method.getDeclaringClass()))
                    && Arrays.equals(methodType.paramTypes, method.getParameterTypes())) {
                return methodType;
            }
        }
        return null;
    }
}
