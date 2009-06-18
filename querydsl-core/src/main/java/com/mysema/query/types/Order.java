/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

/**
 * Order defines ascending and descending order
 * 
 * @author tiwe
 * @version $Id$
 */
public enum Order {
    /**
     * Ascending order
     */
    ASC("asc"),
    /**
     * Descending order
     */
    DESC("desc");
    
    private final String label;    
    Order(String label){
        this.label = label;
    }    
    @Override
    public String toString(){
        return label;
    }
}