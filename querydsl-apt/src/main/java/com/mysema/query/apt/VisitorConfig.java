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
    
    private final boolean fields, methods;
    
    VisitorConfig(boolean fields, boolean methods){
        this.fields = fields;
        this.methods = methods;
    }
    
    public boolean isVisitConstructors() {
        // TODO : parametrize!
        return true;
    }
    
    public boolean isVisitFields(){
        return fields;
    }

    public boolean isVisitMethods(){
        return methods;
    }
    
}
