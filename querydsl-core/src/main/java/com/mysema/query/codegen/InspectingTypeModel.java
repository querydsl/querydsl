/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.codegen;

import java.util.Locale;

import com.mysema.query.util.TypeUtil;


/**
 * @author tiwe
 *
 */
public abstract class InspectingTypeModel extends SimpleTypeModel {
    
    protected FieldType getFieldType(String fullName){
        if (fullName.equals(String.class.getName())) {
            return FieldType.STRING;

        } else if (fullName.equals(Boolean.class.getName())) {
            return FieldType.BOOLEAN;

        } else if (fullName.equals(Locale.class.getName())
                || fullName.equals(Class.class.getName())
                || fullName.equals(Object.class.getName())) {
            return FieldType.SIMPLE;
            
        } else if (fullName.equals(java.sql.Date.class.getName())){
            return FieldType.DATE;
            
        }else if (fullName.equals(java.util.Date.class.getName())
                || fullName.equals(java.sql.Timestamp.class.getName())){
            return FieldType.DATETIME;
                        
        } else if (fullName.equals(java.sql.Time.class.getName())){
            return FieldType.TIME;
                        
        } else if (isComparableSupported(fullName)
                && Number.class.isAssignableFrom(TypeUtil.safeForName(fullName))) {
            return FieldType.NUMERIC;
            
        } else if (isComparableSupported(fullName)
                && Comparable.class.isAssignableFrom(TypeUtil.safeForName(fullName))) {
            return FieldType.COMPARABLE;

        }else{
            return FieldType.ENTITY;
        }
        
    }
    
    protected final void handleArray(TypeModel valueInfo) {
        setNames(valueInfo);
        valueType = valueInfo;
        if (valueInfo.getFieldType() == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }
    
    protected final void handleCollection(TypeModel valueInfo) {
        setNames(valueInfo);  
        valueType = valueInfo;
        if (valueInfo.getFieldType() == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYCOLLECTION;
        } else {
            fieldType = FieldType.SIMPLECOLLECTION;
        }
    }
    
    protected final void handleList(TypeModel valueInfo) {
        setNames(valueInfo);        
        valueType = valueInfo;
        if (valueInfo.getFieldType() == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYLIST;
        } else {
            fieldType = FieldType.SIMPLELIST;
        }
    }
    
    protected final void handleMapInterface(TypeModel keyInfo, TypeModel valueInfo) {        
        setNames(valueInfo);
        keyType = keyInfo;
        valueType = valueInfo;
        if (valueInfo.getFieldType() == FieldType.ENTITY) {
            fieldType = FieldType.ENTITYMAP;
        } else {
            fieldType = FieldType.SIMPLEMAP;
        }
    }
    
    protected final void handlePrimitiveWrapperType(Class<?> cl) {
        setNames(cl);
        if (cl.equals(Boolean.class)) {
            fieldType = FieldType.BOOLEAN;
        } else if (Number.class.isAssignableFrom(cl)) {
            fieldType = FieldType.NUMERIC;
        } else if (Comparable.class.isAssignableFrom(cl)) {
            fieldType = FieldType.COMPARABLE;
        } else {
            fieldType = FieldType.SIMPLE;
        }        
    }
    
    private boolean isComparableSupported(String fullName) {
        return fullName.startsWith("java.") 
            || fullName.startsWith("javax.") 
            || fullName.startsWith("org.joda.time");
    }
    
    protected final void setNames(Class<?> cl){
        packageName = cl.getPackage().getName();
        name = cl.getName();
        simpleName = cl.getSimpleName();
    }
    
    protected final void setNames(TypeModel valueInfo) {
        packageName = valueInfo.getPackageName();
        name = valueInfo.getName();
        simpleName = valueInfo.getSimpleName();
    }
    
}
