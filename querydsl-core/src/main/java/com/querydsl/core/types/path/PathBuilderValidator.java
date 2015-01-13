/*
 * Copyright 2014, Timo Westkämper
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
package com.querydsl.core.types.path;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

import com.google.common.primitives.Primitives;
import com.querydsl.core.util.BeanUtils;
import com.querydsl.core.util.ReflectionUtils;

/**
 * PathBuilderValidator validates PathBuilder properties at creation time
 */
public interface PathBuilderValidator {

    /**
     * Validates the given property of given class
     *
     * @param parent
     * @param property
     * @param propertyType
     */
    Class<?> validate(Class<?> parent, String property, Class<?> propertyType);

    public final PathBuilderValidator DEFAULT = new PathBuilderValidator() {
        @Override
        public Class<?> validate(Class<?> parent, String property, Class<?> propertyType) {
            return propertyType;
        }
    };

    public final PathBuilderValidator FIELDS = new PathBuilderValidator() {
        @Override
        public Class<?> validate(Class<?> parent, String property, Class<?> propertyType) {
            while (!parent.equals(Object.class)) {
                try {
                    Field field = parent.getDeclaredField(property);
                    if (Map.class.isAssignableFrom(field.getType())) {
                        return (Class) ReflectionUtils.getTypeParameterAsClass(field.getGenericType(), 1);
                    } else if (Collection.class.isAssignableFrom(field.getType())) {
                        return (Class) ReflectionUtils.getTypeParameterAsClass(field.getGenericType(), 0);
                    } else {
                        return (Class) Primitives.wrap(field.getType());
                    }
                } catch (NoSuchFieldException e) {
                    parent = parent.getSuperclass();
                }
            }
            return null;
        }
    };

    public final PathBuilderValidator PROPERTIES = new PathBuilderValidator() {
        @Override
        public Class<?> validate(Class<?> parent, String property, Class<?> propertyType) {
            Method getter = BeanUtils.getAccessor("get", property, parent);
            if (getter == null && Primitives.wrap(propertyType).equals(Boolean.class)) {
                getter = BeanUtils.getAccessor("is", property, parent);
            }
            if (getter != null) {
                if (Map.class.isAssignableFrom(getter.getReturnType())) {
                    return (Class) ReflectionUtils.getTypeParameterAsClass(getter.getGenericReturnType(), 1);
                } else if (Collection.class.isAssignableFrom(getter.getReturnType())) {
                    return (Class) ReflectionUtils.getTypeParameterAsClass(getter.getGenericReturnType(), 0);
                } else {
                    return (Class) Primitives.wrap(getter.getReturnType());
                }
            } else {
                return null;
            }
        }
    };

}
