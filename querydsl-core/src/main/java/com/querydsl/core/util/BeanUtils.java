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

import java.beans.Introspector;
import java.lang.reflect.Method;

/**
 * BeanUtils provides JavaBean compliant property de/capitalization functionality
 * 
 * @author tiwe
 *
 */
public final class BeanUtils {

    public static String capitalize(String name) {
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            return name;
        } else if (name.length() > 1) {
            return Character.toUpperCase(name.charAt(0)) + name.substring(1);
        } else {
            return name.toUpperCase();
        }
    }

    public static String uncapitalize(String name) {
        return Introspector.decapitalize(name);
    }

    public static boolean isAccessorPresent(String prefix, String property, Class<?> bean) {
        try {
            bean.getMethod(prefix + capitalize(property));
            return true;
        } catch (NoSuchMethodException ex) {
            return false;
        }
    }

    public static Method getAccessor(String prefix, String property, Class<?> bean) {
        try {
            return bean.getMethod(prefix + capitalize(property));
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private BeanUtils() {}
}
