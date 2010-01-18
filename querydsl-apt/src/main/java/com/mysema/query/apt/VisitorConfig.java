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
    ALL(true,true),
    
    /**
     * visit fields only
     */
    FIELDS_ONLY(true,false),
        
    /**
     * visit methods only
     */
    METHODS_ONLY(false,true),    

    /**
     * visit none
     */
    NONE(false,false);
    
    private final boolean visitFieldProperties, visitMethodProperties;
    
    VisitorConfig(boolean fields, boolean methods){
        this.visitFieldProperties = fields;
        this.visitMethodProperties = methods;
    }
    
    public boolean visitConstructors() {
        // TODO : parametrize!
        return true;
    }
    
    public boolean visitFieldProperties(){
        return visitFieldProperties;
    }

    public boolean visitMethodProperties(){
        return visitMethodProperties;
    }
    
}
