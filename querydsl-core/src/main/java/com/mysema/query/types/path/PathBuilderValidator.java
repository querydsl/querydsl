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
package com.mysema.query.types.path;

import java.lang.reflect.Method;

import com.google.common.primitives.Primitives;
import com.mysema.util.BeanUtils;

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
    <T> Class<? extends T> validate(Class<?> parent, String property, Class<T> propertyType);

    public final PathBuilderValidator DEFAULT = new PathBuilderValidator() {
        @Override
        public <T> Class<? extends T> validate(Class<?> parent, String property, Class<T> propertyType) {
            return propertyType;
        }
    };

    public final PathBuilderValidator FIELDS = new PathBuilderValidator() {
        @Override
        public <T> Class<? extends T> validate(Class<?> parent, String property, Class<T> propertyType) {
            while (!parent.equals(Object.class)) {
                try {
                    return (Class<? extends T>) parent.getDeclaredField(property).getType();
                } catch (NoSuchFieldException e) {
                    parent = parent.getSuperclass();
                }
            }
            return null;
        }
    };

    public final PathBuilderValidator PROPERTIES = new PathBuilderValidator() {
        @Override
        public <T> Class<? extends T> validate(Class<?> parent, String property, Class<T> propertyType) {
            Method getter = BeanUtils.getAccessor("get", property, parent);
            if (getter == null && Primitives.wrap(propertyType).equals(Boolean.class)) {
                getter = BeanUtils.getAccessor("is", property, parent);
            }
            return getter != null ? (Class<? extends T>) getter.getReturnType() : null;
        }
    };

}
