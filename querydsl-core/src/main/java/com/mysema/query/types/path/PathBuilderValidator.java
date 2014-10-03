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

import com.sun.xml.internal.ws.util.StringUtils;

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
    void validate(Class<?> parent, String property, Class<?> propertyType);

    public final PathBuilderValidator DEFAULT = new PathBuilderValidator() {
        @Override
        public void validate(Class<?> parent, String property, Class<?> propertyType) {
            // do nothing
        }
    };

    public final PathBuilderValidator FIELDS = new PathBuilderValidator() {
        @Override
        public void validate(Class<?> parent, String property, Class<?> propertyType) {
            boolean found = false;
            while (found || !parent.equals(Object.class)) {
                try {
                    parent.getDeclaredField(property);
                    found = true;
                } catch (NoSuchFieldException e) {
                    parent = parent.getSuperclass();
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Illegal property " + property);
            }
        }
    };

    public final PathBuilderValidator PROPERTIES = new PathBuilderValidator() {
        @Override
        public void validate(Class<?> parent, String property, Class<?> propertyType) {
            boolean found = false;
            String accessor = "get" + StringUtils.capitalize(property);
            while (found || !parent.equals(Object.class)) {
                try {
                    parent.getDeclaredMethod(accessor);
                    found = true;
                } catch (NoSuchMethodException e) {
                    parent = parent.getSuperclass();
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Illegal property " + property);
            }
        }
    };

}
