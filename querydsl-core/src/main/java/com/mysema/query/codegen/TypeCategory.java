/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import com.mysema.query.annotations.PropertyType;


/**
 * TypeCategory defines the expression type used for a Field
 * 
 * @author tiwe
 *
 */
@Immutable
public enum TypeCategory {
    /**
     * Simple non-entity fields
     */           
    SIMPLE(null),
    /**
     * 
     */
    MAP(null),
    /**
     * 
     */
    COLLECTION(null),
    /**
     * 
     */
    LIST(COLLECTION),
    /**
     * 
     */
    ARRAY(null),
    /**
     * Comparable literal fields (? extends Comparable)
     */
    COMPARABLE(SIMPLE),
    /**
     * Boolean files
     */
    BOOLEAN(COMPARABLE, Boolean.class.getName()),     
    /**
     * Date fields
     */
    DATE(COMPARABLE, java.sql.Date.class.getName(), "org.joda.time.LocalDate"),
    /**
     * Date/Time fields
     */
    DATETIME(COMPARABLE, 
        java.util.Calendar.class.getName(),
        java.util.Date.class.getName(), 
        java.sql.Timestamp.class.getName(), 
        "org.joda.time.LocalDateTime", 
        "org.joda.time.Instant",
        "org.joda.time.DateTime", 
        "org.joda.time.DateMidnight"),    
    /**
     * Entity fields
     */
    ENTITY(null),
    
    /**
     * Numeric fields (? extends Number & Comparable)
     */
    NUMERIC(COMPARABLE),     
    /**
     * String fields
     */
    STRING(COMPARABLE, String.class.getName()),
    /**
     * Time fields
     */
    TIME(COMPARABLE, java.sql.Time.class.getName(), "org.joda.time.LocalTime");
    
    @Nullable
    private final TypeCategory superType;
    
    private final Set<String> types;
        
    TypeCategory(@Nullable TypeCategory superType, String... types){
        this.superType = superType;
        this.types = new HashSet<String>(types.length);
        for (String type : types){
            this.types.add(type);
        }
    }

    @Nullable
    public TypeCategory getSuperType() {
        return superType;
    }
    
    public boolean supports(Class<?> cl){
        return supports(cl.getName());
    }
    
    public boolean supports(String className){
        return types.contains(className);
    }
    
    /**
     * transitive and reflexive subCategoryOf check
     * 
     * @param ancestor
     * @return
     */
    public boolean isSubCategoryOf(TypeCategory ancestor){
        if (this == ancestor)
            return true;
        else if (superType == null)
            return false;
        else 
            return superType == ancestor || superType.isSubCategoryOf(ancestor);
    }
    
    public static TypeCategory get(String className){
        for (TypeCategory category : values()){
            if (category.supports(className)){
                return category;
            }
        }
        return SIMPLE;
    }
    
    @Nullable
    public static TypeCategory get(PropertyType propertyType){
        switch(propertyType){
            case COMPARABLE: return COMPARABLE;
            case DATE: return DATE;
            case DATETIME: return DATETIME;
            case ENTITY: return ENTITY;
            case SIMPLE: return SIMPLE;            
            case TIME: return TIME;    
            case NONE: return null;
        }        
        throw new IllegalArgumentException("Unsupported PropertyType " + propertyType);
    }
    
}