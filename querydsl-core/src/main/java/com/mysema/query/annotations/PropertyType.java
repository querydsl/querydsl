/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.annotations;

import com.mysema.codegen.model.TypeCategory;

/**
 * PropertyType defines the Path type to be used for a Domain property
 *
 * @author tiwe
 *
 */
public enum PropertyType {
    /**
     * for PComparable fields
     */
    COMPARABLE(TypeCategory.COMPARABLE),
    /**
     * for custom PDate fields
     */
    DATE(TypeCategory.DATE),
    /**
     * for custom PDateTime fields
     */
    DATETIME(TypeCategory.DATETIME),
    /**
     * to skip properties
     */
    NONE(null),
    /**
     * for PSimple fields
     */
    SIMPLE(TypeCategory.SIMPLE),
    /**
     * for custom PTime fields
     */
    TIME(TypeCategory.TIME),
    /**
     *
     */
    ENTITY(TypeCategory.ENTITY);
    
    private final TypeCategory category;
    
    PropertyType(TypeCategory category){
        this.category = category;
    }
    
    public TypeCategory getCategory(){
        return category;
    }

}
