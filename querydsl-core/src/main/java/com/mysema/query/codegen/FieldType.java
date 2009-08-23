/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import javax.annotation.Nullable;


/**
 * FieldType defines the expression type used for a Field
 * 
 * @author tiwe
 *
 */
public enum FieldType {
    /**
     * Simple non-entity fields
     */
    SIMPLE(null),
    /**
     * Comparable literal fields
     */
    COMPARABLE(SIMPLE),
    /**
     * Boolean files
     */
    BOOLEAN(COMPARABLE),     
    /**
     * Date fields
     */
    DATE(COMPARABLE),
    /**
     * Date/Time fields
     */
    DATETIME(COMPARABLE),    
    /**
     * Entity fields
     */
    ENTITY(SIMPLE),
    /**
     * Entity collection fields
     */
    ENTITYCOLLECTION(SIMPLE),
    /**
     * Entity list fields
     */
    ENTITYLIST(ENTITYCOLLECTION),      
    /**
     * Entity map fields
     */
    ENTITYMAP(SIMPLE), 
    /**
     * Numeric fields
     */
    NUMERIC(COMPARABLE),     
    /**
     * Simple collection fields
     */
    SIMPLECOLLECTION(SIMPLE), 
    /**
     * Simple list fields
     */
    SIMPLELIST(SIMPLECOLLECTION),
    /**
     * Simple map fields
     */
    SIMPLEMAP(SIMPLE), 
    /**
     * String fields
     */
    STRING(COMPARABLE),
    /**
     * Time fields
     */
    TIME(COMPARABLE);
    
    @Nullable
    private final FieldType superType;
    
    FieldType(@Nullable FieldType superType){
        this.superType = superType;
    }

    @Nullable
    public FieldType getSuperType() {
        return superType;
    }
    
    
    
}