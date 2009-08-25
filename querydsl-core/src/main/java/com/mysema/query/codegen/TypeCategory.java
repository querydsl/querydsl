/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Blob;
import java.sql.Clob;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;


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
    SIMPLE(null,
        Number.class, // Number is itself simple, since it is not comparable 
        Locale.class, 
        Clob.class, 
        Blob.class, 
        Class.class, 
        Object.class, 
        Serializable.class),
    /**
     * Comparable literal fields
     */
    COMPARABLE(SIMPLE, 
        Comparable.class, 
        Character.class),
    /**
     * Boolean files
     */
    BOOLEAN(COMPARABLE, 
        Boolean.class),     
    /**
     * Date fields
     */
    DATE(COMPARABLE, 
        java.sql.Date.class),
    /**
     * Date/Time fields
     */
    DATETIME(COMPARABLE, 
        java.util.Date.class, 
        java.sql.Timestamp.class),    
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
    STRING(COMPARABLE, 
        String.class),
    /**
     * Time fields
     */
    TIME(COMPARABLE, 
        java.sql.Time.class);
    
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
    
    public boolean isChildOf(TypeCategory ancestor){
        return superType != null && (superType == ancestor || superType.isChildOf(ancestor));
    }
    
    public static TypeCategory get(String className){
        for (TypeCategory category : values()){
            if (category.supports(className)){
                return category;
            }
        }
        return ENTITY;
    }
    
}