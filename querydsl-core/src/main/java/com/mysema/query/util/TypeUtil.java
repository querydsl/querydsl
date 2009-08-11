/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;

import javax.annotation.Nullable;

// TODO : move to mysema-commons
public class TypeUtil {
    
    @Nullable
    public static Class<?> safeForName(String name){
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
    
    @SuppressWarnings("unchecked")
    @Nullable
    public static Class<?> getTypeParameter(java.lang.reflect.Type type, int index) {
        if (type instanceof ParameterizedType) {
            ParameterizedType ptype = (ParameterizedType) type;
            java.lang.reflect.Type[] targs = ptype.getActualTypeArguments();
            if (targs[index] instanceof WildcardType) {
                WildcardType wildcardType = (WildcardType) targs[index];
                return (Class<?>) wildcardType.getUpperBounds()[0];
            } else if (targs[index] instanceof TypeVariable) {
                return (Class<?>) ((TypeVariable) targs[index]).getGenericDeclaration();
            } else if (targs[index] instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) targs[index]).getRawType();
            } else {
                try {
                    return (Class<?>) targs[index];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
