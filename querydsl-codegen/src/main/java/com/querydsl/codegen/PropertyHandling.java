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
package com.querydsl.codegen;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.querydsl.core.annotations.QueryInit;
import com.querydsl.core.annotations.QueryType;

/**
 * Defines handling of fields and getters in property extractions
 */
public enum PropertyHandling {
    /**
     * Inspect fields and methods
     */
    ALL {
        @Override
        public Config getConfig(Class<?> type) {
            return Config.ALL;
        }
    },
    /**
     * Inspect fields only
     */
    FIELDS {
        @Override
        public Config getConfig(Class<?> type) {
            return Config.FIELDS;
        }
    },
    /**
     * Inspect methods only
     */
    METHODS {
        @Override
        public Config getConfig(Class<?> type) {
            return Config.METHODS;
        }
    },
    /**
     * No member inspection
     */
    NONE {
        @Override
        public Config getConfig(Class<?> type) {
            return Config.NONE;
        }
    },
    /**
     * JDO compatibility
     */
    JDO {
        @Override
        public Config getConfig(Class<?> type) {
            boolean fields = false;
            boolean methods = false;
            for (Field field : type.getDeclaredFields()) {
                fields |= hasAnnotations(field, "javax.jdo.annotations.");
            }
            for (Method method : type.getDeclaredMethods()) {
                methods |= hasAnnotations(method, "javax.jdo.annotations.");
            }
            return Config.of(fields, methods, Config.FIELDS);
        }
    },
    /**
     * JPA compatibility
     */
    JPA {
        @Override
        public Config getConfig(Class<?> type) {
            boolean fields = false;
            boolean methods = false;
            for (Field field : type.getDeclaredFields()) {
                fields |= hasAnnotations(field, "javax.persistence.");
            }
            for (Method method : type.getDeclaredMethods()) {
                methods |= hasAnnotations(method, "javax.persistence.");
            }
            return Config.of(fields, methods, Config.ALL);
        }
    };

    private static boolean hasAnnotations(AnnotatedElement element, String packagePrefix) {
        for (Annotation ann : element.getAnnotations()) {
            if (ann.annotationType().getName().startsWith(packagePrefix)) {
                return true;
            }
        }
        return element.isAnnotationPresent(QueryType.class)
            || element.isAnnotationPresent(QueryInit.class);
    }

    public abstract Config getConfig(Class<?> type);

    /**
     * Property handling options
     */
    public enum Config {
        ALL(true, true),
        FIELDS(true, false),
        METHODS(false, true),
        NONE(false, false);

        private final boolean fields, methods;

        Config(boolean fields, boolean methods) {
            this.fields = fields;
            this.methods = methods;
        }

        public boolean isFields() {
            return fields;
        }

        public boolean isMethods() {
            return methods;
        }

        public static Config of(boolean fields, boolean methods, Config defaultConfig) {
            if (fields && methods) {
                return ALL;
            } else if (fields) {
                return FIELDS;
            } else if (methods) {
                return METHODS;
            } else {
                return defaultConfig;
            }
        }
    }
}
