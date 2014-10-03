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
    boolean validate(Class<?> parent, String property, Class<?> propertyType);

    public final PathBuilderValidator DEFAULT = new PathBuilderValidator() {
        @Override
        public boolean validate(Class<?> parent, String property, Class<?> propertyType) {
            return true;
        }
    };

    public final PathBuilderValidator FIELDS = new PathBuilderValidator() {
        @Override
        public boolean validate(Class<?> parent, String property, Class<?> propertyType) {
            while (!parent.equals(Object.class)) {
                try {
                    parent.getDeclaredField(property);
                    return true;
                } catch (NoSuchFieldException e) {
                    parent = parent.getSuperclass();
                }
            }
            return false;
        }
    };

    public final PathBuilderValidator PROPERTIES = new PathBuilderValidator() {
        @Override
        public boolean validate(Class<?> parent, String property, Class<?> propertyType) {
            String accessor = "get" + BeanUtils.capitalize(property);
            while (!parent.equals(Object.class)) {
                try {
                    parent.getDeclaredMethod(accessor);
                    return true;
                } catch (NoSuchMethodException e) {
                    parent = parent.getSuperclass();
                }
            }
            return false;
        }
    };

}
