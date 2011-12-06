/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.apt;

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
        if (fields && !methods) {
            return VisitorConfig.FIELDS_ONLY;
        } else if (methods && !fields) {
            return VisitorConfig.METHODS_ONLY;
        } else {
            return VisitorConfig.ALL;
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
