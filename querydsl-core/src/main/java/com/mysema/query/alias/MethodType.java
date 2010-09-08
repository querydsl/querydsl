/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.alias;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.annotation.Nullable;

import com.mysema.query.types.path.EntityPathBase;

/**
 * MethodType defines the supported method types used in the Alias functionality
 * 
 * @author tiwe
 *
 */
public enum MethodType{
    /**
     *
     */
    GET_MAPPED_PATH("__mappedPath", EntityPathBase.class, ManagedObject.class),
    /**
     *
     */
    GETTER("(get|is).+", Object.class, Object.class),
    /**
     *
     */
    HASH_CODE("hashCode", int.class, Object.class),
    /**
     *
     */
    LIST_ACCESS("get", Object.class, List.class, int.class),
    /**
     *
     */
    MAP_ACCESS("get", Object.class, Map.class, Object.class),
    /**
     *
     */
    SIZE("size", int.class, Object.class),
    /**
     *
     */
    TO_STRING("toString",String.class, Object.class),
    /**
     *
     */
    SCALA_GETTER(".+", Object.class, Object.class),;

    private final Pattern pattern;

    private final Class<?> returnType;

    private final Class<?> ownerType;

    private final Class<?>[] paramTypes;

    private MethodType(String namePattern, Class<?> returnType, Class<?> ownerType, Class<?>... paramTypes){
        this.pattern = Pattern.compile(namePattern);
        this.returnType = returnType;
        this.ownerType = ownerType;
        this.paramTypes = paramTypes;
    }

    @Nullable
    public static MethodType get(Method method) {
        for (MethodType methodType : values()){
            if (methodType.pattern.matcher(method.getName()).matches()
                && (methodType.returnType == Object.class || methodType.returnType.isAssignableFrom(method.getReturnType()))
                && (methodType.ownerType == Object.class || methodType.ownerType.isAssignableFrom(method.getDeclaringClass()))
                && Arrays.equals(methodType.paramTypes, method.getParameterTypes())){
                return methodType;
            }
        }
        return null;
    }
}
