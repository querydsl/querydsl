/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.sql.Blob;
import java.sql.Clob;
import java.util.Locale;

import com.mysema.query.util.TypeUtil;


/**
 * @author tiwe
 *
 */
public abstract class InspectingTypeModel extends SimpleTypeModel {
    
    protected TypeCategory getTypeCategory(String fullName){
        if (fullName.equals(String.class.getName())) {
            return TypeCategory.STRING;

        } else if (fullName.equals(Boolean.class.getName())) {
            return TypeCategory.BOOLEAN;

        } else if (fullName.equals(Locale.class.getName())
                || fullName.equals(Clob.class.getName())
                || fullName.equals(Blob.class.getName())
                || fullName.equals(Class.class.getName())
                || fullName.equals(Object.class.getName())) {
            return TypeCategory.SIMPLE;
            
        } else if (fullName.equals(java.sql.Date.class.getName())){
            return TypeCategory.DATE;
            
        }else if (fullName.equals(java.util.Date.class.getName())
                || fullName.equals(java.sql.Timestamp.class.getName())){
            return TypeCategory.DATETIME;
                        
        } else if (fullName.equals(java.sql.Time.class.getName())){
            return TypeCategory.TIME;
                        
        } else if (isComparableSupported(fullName)
                && Number.class.isAssignableFrom(TypeUtil.safeForName(fullName))) {
            return TypeCategory.NUMERIC;
            
        } else if (isComparableSupported(fullName)
                && Comparable.class.isAssignableFrom(TypeUtil.safeForName(fullName))) {
            return TypeCategory.COMPARABLE;

        }else{
            return TypeCategory.ENTITY;
        }
        
    }
    
    protected final void handleArray(TypeModel valueInfo) {
        setNames(valueInfo);
        valueType = valueInfo;
        if (valueInfo.getTypeCategory() == TypeCategory.ENTITY) {
            typeCategory = TypeCategory.ENTITYCOLLECTION;
        } else {
            typeCategory = TypeCategory.SIMPLECOLLECTION;
        }
    }
    
    protected final void handleCollection(TypeModel valueInfo) {
        setNames(valueInfo);  
        valueType = valueInfo;
        if (valueInfo.getTypeCategory() == TypeCategory.ENTITY) {
            typeCategory = TypeCategory.ENTITYCOLLECTION;
        } else {
            typeCategory = TypeCategory.SIMPLECOLLECTION;
        }
    }
    
    protected final void handleList(TypeModel valueInfo) {
        setNames(valueInfo);        
        valueType = valueInfo;
        if (valueInfo.getTypeCategory() == TypeCategory.ENTITY) {
            typeCategory = TypeCategory.ENTITYLIST;
        } else {
            typeCategory = TypeCategory.SIMPLELIST;
        }
    }
    
    protected final void handleMapInterface(TypeModel keyInfo, TypeModel valueInfo) {        
        setNames(valueInfo);
        keyType = keyInfo;
        valueType = valueInfo;
        if (valueInfo.getTypeCategory() == TypeCategory.ENTITY) {
            typeCategory = TypeCategory.ENTITYMAP;
        } else {
            typeCategory = TypeCategory.SIMPLEMAP;
        }
    }
    
    protected final void handlePrimitiveWrapperType(Class<?> cl) {
        setNames(cl);
        if (cl.equals(Boolean.class)) {
            typeCategory = TypeCategory.BOOLEAN;
        } else if (Number.class.isAssignableFrom(cl)) {
            typeCategory = TypeCategory.NUMERIC;
        } else if (Comparable.class.isAssignableFrom(cl)) {
            typeCategory = TypeCategory.COMPARABLE;
        } else {
            typeCategory = TypeCategory.SIMPLE;
        }        
    }
    
    private boolean isComparableSupported(String fullName) {
        return fullName.startsWith("java.") 
            || fullName.startsWith("javax.") 
            || fullName.startsWith("org.joda.time");
    }
    
    protected final void setNames(Class<?> cl){
        if (cl.getPackage() != null){
            packageName = cl.getPackage().getName();    
        }else{
            packageName = "java.lang";
        }        
        name = cl.getName();
        simpleName = cl.getSimpleName();
    }
    
    protected final void setNames(TypeModel valueInfo) {
        packageName = valueInfo.getPackageName();
        name = valueInfo.getName();
        simpleName = valueInfo.getSimpleName();
    }
    
}
