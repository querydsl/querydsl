package com.mysema.query.apt;

/**
 * @author tiwe
 *
 */
public enum VisitorConfig {
    /**
     * 
     */
    ALL(true,true),
    /**
     * 
     */
    FIELDS_ONLY(true,false),
    /**
     * 
     */
    METHODS_ONLY(false,true);
    
    private final boolean fields, methods;
    
    VisitorConfig(boolean fields, boolean methods){
        this.fields = fields;
        this.methods = methods;
    }
    
    public boolean isVisitFields(){
        return fields;
    }
    
    public boolean isVisitMethods(){
        return methods;
    }
    
    
}
