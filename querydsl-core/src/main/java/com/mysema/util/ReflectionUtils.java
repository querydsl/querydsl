/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.util;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang.StringUtils;

/**
 * @author tiwe
 *
 */
public final class ReflectionUtils {

    private static final AnnotatedElement EMPTY = new AnnotatedElementAdapter();

    private ReflectionUtils(){}

    public static AnnotatedElement getAnnotatedElement(Class<?> beanClass, String propertyName, Class<?> propertyClass){
        Field field = getFieldOrNull(beanClass, propertyName);
        Method method = getGetterOrNull(beanClass, propertyName, propertyClass);
        if (field == null || field.getAnnotations().length == 0){
            return (method != null && method.getAnnotations().length > 0) ? method : EMPTY;
        }else if (method == null || method.getAnnotations().length == 0){
            return field;
        }else{
            return new AnnotatedElementAdapter(field, method);
        }
    }

    @Nullable
    private static Field getFieldOrNull(Class<?> beanClass, String propertyName){
        while (beanClass != null && !beanClass.equals(Object.class)){
            try {
                return beanClass.getDeclaredField(propertyName);
            } catch (SecurityException e) {
                // skip
            } catch (NoSuchFieldException e) {
                // skip
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;
    }

    @Nullable
    private static Method getGetterOrNull(Class<?> beanClass, String name, Class<?> type){
        String methodName = (type.equals(Boolean.class) ? "is" : "get") + StringUtils.capitalize(name);
        while(beanClass != null && !beanClass.equals(Object.class)){
            try {
                return beanClass.getDeclaredMethod(methodName);
            } catch (SecurityException e) { // skip
            } catch (NoSuchMethodException e) { // skip
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;

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
                return (Class<?>) targs[index];
            }
        }
        return null;
    }

    public static Set<Class<?>> getImplementedInterfaces(Class<?> cl){
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        Deque<Class<?>> classes = new ArrayDeque<Class<?>>();
        classes.add(cl);
        while (!classes.isEmpty()){
            Class<?> c = classes.pop();
            interfaces.addAll(Arrays.asList(c.getInterfaces()));
            if (c.getSuperclass() != null){
                classes.add(c.getSuperclass());
            }
            classes.addAll(Arrays.asList(c.getInterfaces()));
        }
        return interfaces;
    }

}
