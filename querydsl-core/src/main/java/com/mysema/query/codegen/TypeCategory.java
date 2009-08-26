/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Instant;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.Partial;


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
     * Comparable literal fields
     */
    COMPARABLE(SIMPLE, Comparable.class, Character.class, Partial.class),
    /**
     * Boolean files
     */
    BOOLEAN(COMPARABLE, Boolean.class),     
    /**
     * Date fields
     */
    DATE(COMPARABLE, java.sql.Date.class, LocalDate.class),
    /**
     * Date/Time fields
     */
    DATETIME(COMPARABLE, 
        java.util.Date.class, 
        java.sql.Timestamp.class, 
        LocalDateTime.class, 
        Instant.class,
        DateTime.class, 
        DateMidnight.class),    
    /**
     * Entity fields
     */
    ENTITY(null),
    /**
     * Entity collection fields
     */
    ENTITYCOLLECTION(null),
    /**
     * Entity list fields
     */
    ENTITYLIST(ENTITYCOLLECTION),      
    /**
     * Entity map fields
     */
    ENTITYMAP(null), 
    /**
     * Numeric fields
     */
    NUMERIC(COMPARABLE, 
        Long.class, 
        Integer.class, 
        Byte.class, 
        Double.class, 
        Float.class, 
        Short.class, 
        BigDecimal.class, 
        BigInteger.class),     
    /**
     * Simple collection fields
     */
    SIMPLECOLLECTION(null), 
    /**
     * Simple list fields
     */
    SIMPLELIST(SIMPLECOLLECTION),
    /**
     * Simple map fields
     */
    SIMPLEMAP(null), 
    /**
     * String fields
     */
    STRING(COMPARABLE, String.class),
    /**
     * Time fields
     */
    TIME(COMPARABLE, java.sql.Time.class, LocalTime.class);
    
    @Nullable
    private final TypeCategory superType;
    
    private final Set<String> types;
        
    TypeCategory(@Nullable TypeCategory superType, Class<?>... types){
        this.superType = superType;
        this.types = new HashSet<String>(types.length);
        for (Class<?> type : types){
        this.types.add(type.getName());
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
    
}