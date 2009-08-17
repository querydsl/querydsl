/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import javax.annotation.Nullable;


/**
 * 
 * @author tiwe
 *
 */
public enum FieldType {
    /**
     * 
     */
    SIMPLE(null),
    /**
     * 
     */
    COMPARABLE(SIMPLE),
    /**
     * 
     */
    BOOLEAN(COMPARABLE),     
    /**
     * 
     */
    DATE(COMPARABLE),
    /**
     * 
     */
    DATETIME(COMPARABLE),    
    /**
     * 
     */
    ENTITY(SIMPLE),
    /**
     * 
     */
    ENTITYCOLLECTION(SIMPLE),
    /**
     * 
     */
    ENTITYLIST(ENTITYCOLLECTION),      
    /**
     * 
     */
    ENTITYMAP(SIMPLE), 
    /**
     * 
     */
    NUMERIC(COMPARABLE),     
    /**
     * 
     */
    SIMPLECOLLECTION(SIMPLE), 
    /**
     * 
     */
    SIMPLELIST(SIMPLECOLLECTION),
    /**
     * 
     */
    SIMPLEMAP(SIMPLE), 
    /**
     * 
     */
    STRING(COMPARABLE),
    /**
     * 
     */
    TIME(COMPARABLE),
    /**
     * 
     */
    DECIMAL(NUMERIC);
    
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