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
package com.querydsl.core.util;

import java.lang.reflect.*;
import java.util.*;

import javax.annotation.Nullable;


/**
 * ReflectionUtils provides Reflection related functionality
 * 
 * @author tiwe
 *
 */
public final class ReflectionUtils {

    private static final AnnotatedElement EMPTY = new Annotations();

    private ReflectionUtils() {}

    public static AnnotatedElement getAnnotatedElement(Class<?> beanClass, String propertyName, Class<?> propertyClass) {
        Field field = getFieldOrNull(beanClass, propertyName);
        Method method = getGetterOrNull(beanClass, propertyName, propertyClass);
        if (field == null || field.getAnnotations().length == 0) {
            return (method != null && method.getAnnotations().length > 0) ? method : EMPTY;
        } else if (method == null || method.getAnnotations().length == 0) {
            return field;
        } else {
            return new Annotations(field, method);
        }
    }

    @Nullable
    public static Field getFieldOrNull(Class<?> beanClass, String propertyName) {
        while (beanClass != null && !beanClass.equals(Object.class)) {
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
    public static Method getGetterOrNull(Class<?> beanClass, String name) {
        Method method = getGetterOrNull(beanClass, name, Object.class);
        if (method != null) {
            return method;
        } else {
            return getGetterOrNull(beanClass, name, Boolean.class);
        }
    }
    
    @Nullable
    public static Method getGetterOrNull(Class<?> beanClass, String name, Class<?> type) {
        String methodName = ((type.equals(Boolean.class) || type.equals(boolean.class)) ? "is" : "get") + BeanUtils.capitalize(name);
        while (beanClass != null && !beanClass.equals(Object.class)) {
            try {
                return beanClass.getDeclaredMethod(methodName);
            } catch (SecurityException e) { // skip
            } catch (NoSuchMethodException e) { // skip
            }
            beanClass = beanClass.getSuperclass();
        }
        return null;

    }

    public static int getTypeParameterCount(java.lang.reflect.Type type) {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments().length;
        } else if (type instanceof TypeVariable) {
            return getTypeParameterCount(((TypeVariable) type).getBounds()[0]);            
        } else {
            return 0;
        }
    }

    public static Class<?> getTypeParameterAsClass(java.lang.reflect.Type type, int index) {
        Type parameter = getTypeParameter(type, index);
        if (parameter != null) {
            return asClass(parameter);
        } else {
            return null;
        }
    }
    
    @Nullable
    public static Type getTypeParameter(java.lang.reflect.Type type, int index) {
        if (type instanceof ParameterizedType) {
            return ((ParameterizedType) type).getActualTypeArguments()[index];
        } else if (type instanceof TypeVariable) {    
            return getTypeParameter(((TypeVariable) type).getBounds()[0], index);
        } else {
            return null;    
        }        
    }
    
    private static Class<?> asClass(Type type) {
        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;
            if (wildcardType.getUpperBounds()[0] instanceof Class) {
                return (Class<?>) wildcardType.getUpperBounds()[0];
            } else if (wildcardType.getUpperBounds()[0] instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) wildcardType.getUpperBounds()[0]).getRawType();
            } else {
                return Object.class;
            }
        } else if (type instanceof TypeVariable) {
            return asClass(((TypeVariable)type).getBounds()[0]);
        } else if (type instanceof ParameterizedType) {
            return (Class<?>) ((ParameterizedType) type).getRawType();
        } else if (type instanceof GenericArrayType) {    
            Type component = ((GenericArrayType)type).getGenericComponentType();
            return Array.newInstance(asClass(component), 0).getClass();
        } else if (type instanceof Class) {
            return (Class<?>) type;
        } else {
            throw new IllegalArgumentException(type.getClass().toString());
        }
    }
    
    public static Set<Class<?>> getSuperClasses(Class<?> cl) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        Class<?> c = cl;
        while (c != null) {
            classes.add(c);
            c = c.getSuperclass();
        }
        return classes;
    }
    
    public static Set<Field> getFields(Class<?> cl) {
        Set<Field> fields = new HashSet<Field>();
        Class<?> c = cl;
        while (c != null) {
            fields.addAll(Arrays.asList(c.getDeclaredFields()));
            c = c.getSuperclass();
        }
        return fields;
    }
    
    public static Set<Class<?>> getImplementedInterfaces(Class<?> cl) {
        Set<Class<?>> interfaces = new HashSet<Class<?>>();
        Deque<Class<?>> classes = new ArrayDeque<Class<?>>();
        classes.add(cl);
        while (!classes.isEmpty()) {
            Class<?> c = classes.pop();
            interfaces.addAll(Arrays.asList(c.getInterfaces()));
            if (c.getSuperclass() != null) {
                classes.add(c.getSuperclass());
            }
            classes.addAll(Arrays.asList(c.getInterfaces()));
        }
        return interfaces;
    }

}
