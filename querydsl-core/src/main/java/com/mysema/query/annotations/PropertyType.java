/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.annotations;

import javax.annotation.Nullable;

import com.mysema.codegen.model.TypeCategory;

/**
 * PropertyType defines the Path type to be used for a Domain property
 *
 * @author tiwe
 *
 */
public enum PropertyType {
    /**
     * 
     */    
    COMPARABLE(TypeCategory.COMPARABLE),
    
    /**
     * 
     */    
    ENUM(TypeCategory.ENUM),
    
    /**
     * 
     */    
    DATE(TypeCategory.DATE),
    
    /**
     * 
     */    
    DATETIME(TypeCategory.DATETIME),
    
    /**
     * 
     */    
    NONE(null),
    
    /**
     * 
     */
    NUMERIC(TypeCategory.NUMERIC),
    
    /**
     * 
     */    
    SIMPLE(TypeCategory.SIMPLE),
    
    /**
     * 
     */
    STRING(TypeCategory.STRING),
    
    /**
     * 
     */
    TIME(TypeCategory.TIME),
    
    /**
     *
     */
    ENTITY(TypeCategory.ENTITY);
    
    @Nullable
    private final TypeCategory category;
    
    PropertyType(@Nullable TypeCategory category){
        this.category = category;
    }
    
    @Nullable
    public TypeCategory getCategory(){
        return category;
    }

}
