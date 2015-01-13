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
package com.querydsl.apt;

/**
 * VisitorConfig defines the entity type specific visiting configuration
 *
 * @author tiwe
 *
 */
public enum VisitorConfig {
    /**
     * visit both fields and getters
     */
    ALL(true, true, true),

    /**
     * visit fields only
     */
    FIELDS_ONLY(true, false, true),

    /**
     * visit methods only
     */
    METHODS_ONLY(false, true, true),

    /**
     * visit none
     */
    NONE(false, false, false);

    private final boolean visitFieldProperties, visitMethodProperties, visitConstructors;

    public static VisitorConfig get(boolean fields, boolean methods) {
        return get(fields, methods, VisitorConfig.ALL);
    }

    public static VisitorConfig get(boolean fields, boolean methods, VisitorConfig defaultConfig) {
        if (fields && methods) {
            return VisitorConfig.ALL;
        } else if (fields && !methods) {
            return VisitorConfig.FIELDS_ONLY;
        } else if (methods && !fields) {
            return VisitorConfig.METHODS_ONLY;
        } else {
            return defaultConfig;
        }
    }
    
    VisitorConfig(boolean fields, boolean methods, boolean constructors) {
        this.visitFieldProperties = fields;
        this.visitMethodProperties = methods;
        this.visitConstructors = constructors;
    }
    
    public boolean visitConstructors() {
        return visitConstructors;
    }

    public boolean visitFieldProperties() {
        return visitFieldProperties;
    }

    public boolean visitMethodProperties() {
        return visitMethodProperties;
    }

}
