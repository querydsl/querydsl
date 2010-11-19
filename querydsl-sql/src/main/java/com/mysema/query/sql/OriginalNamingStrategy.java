/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.lang.annotation.Annotation;

import org.apache.commons.lang.StringUtils;

import com.mysema.query.codegen.EntityType;
import com.mysema.util.JavaSyntaxUtils;

/**
 * OriginalNamingStrategy preserves the table and column names in the conversion
 *
 * @author tiwe
 *
 */
public class OriginalNamingStrategy implements NamingStrategy {
    
    private String reservedSuffix = "_col";
    
    private String primaryKeysClassName = "PrimaryKeys";
    
    private String foreignKeysClassName = "ForeignKeys";
    
    private String primaryKeysVariable = "pk";
    
    private String foreignKeysVariable = "fk";
    
    @Override
    public String getClassName(String namePrefix, String tableName) {
        return namePrefix + tableName;
    }

    @Override
    public String getDefaultAlias(String namePrefix, EntityType entityType) {
        for (Annotation ann : entityType.getAnnotations()) {
            if (ann.annotationType().equals(Table.class)) {
                return ((Table) ann).value();
            }
        }
        return getDefaultVariableName(namePrefix, entityType);
    }

    @Override
    public String getDefaultVariableName(String namePrefix, EntityType entityType) {
        return StringUtils.uncapitalize(entityType.getSimpleName());
    }

    @Override
    public String getPropertyName(String columnName, String namePrefix, EntityType entityType) {        
        return getPropertyName(columnName);
    }

    @Override
    public String getPropertyNameForForeignKey(String foreignKeyName, EntityType entityType) {
        return getPropertyName(foreignKeyName);
    }

    @Override
    public String getPropertyNameForInverseForeignKey(String foreignKeyName, EntityType entityType) {
        return "_" + foreignKeyName;
    }

    @Override
    public String getPropertyNameForPrimaryKey(String primaryKeyName, EntityType model) {
        return getPropertyName(primaryKeyName);
    }
    
    private String getPropertyName(String name){
        if (JavaSyntaxUtils.isReserved(name)){
            return name + reservedSuffix;
        }else{
            return name;    
        }
    }

    @Override
    public String normalizeColumnName(String columnName) {
        return columnName;
    }

    @Override
    public String normalizeTableName(String tableName) {
        return tableName;
    }

    public String getReservedSuffix() {
        return reservedSuffix;
    }

    public void setReservedSuffix(String reservedSuffix) {
        this.reservedSuffix = reservedSuffix;
    }

    public String getPrimaryKeysClassName() {
        return primaryKeysClassName;
    }

    public void setPrimaryKeysClassName(String primaryKeysClassName) {
        this.primaryKeysClassName = primaryKeysClassName;
    }

    public String getForeignKeysClassName() {
        return foreignKeysClassName;
    }

    public void setForeignKeysClassName(String foreignKeysClassName) {
        this.foreignKeysClassName = foreignKeysClassName;
    }

    public String getPrimaryKeysVariable() {
        return primaryKeysVariable;
    }

    public void setPrimaryKeysVariable(String primaryKeysVariable) {
        this.primaryKeysVariable = primaryKeysVariable;
    }

    public String getForeignKeysVariable() {
        return foreignKeysVariable;
    }

    public void setForeignKeysVariable(String foreignKeysVariable) {
        this.foreignKeysVariable = foreignKeysVariable;
    }

    
}
