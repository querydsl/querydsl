/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Locale;

import com.mysema.query.codegen.EntityType;
import com.mysema.util.JavaSyntaxUtils;

/**
 * DefaultNamingStrategy is the default implementation of the NamingStrategy
 * interface
 *
 * @author tiwe
 *
 */
public class DefaultNamingStrategy implements NamingStrategy {

    private String reservedSuffix = "Col";
    
    private String primaryKeysClassName = "PrimaryKeys";
    
    private String foreignKeysClassName = "ForeignKeys";
    
    private String primaryKeysVariable = "pk";
    
    private String foreignKeysVariable = "fk";
    
    @Override
    public String getClassName(String namePrefix, String tableName) {
        if (tableName.length() > 1){
            return namePrefix
                + tableName.substring(0, 1).toUpperCase(Locale.ENGLISH)
                + toCamelCase(tableName.substring(1));    
        }else{
            return namePrefix + tableName.toUpperCase(Locale.ENGLISH);
        }
        
    }

    @Override
    public String getDefaultAlias(String namePrefix, EntityType entityType) {
        return entityType.getAnnotation(Table.class).value();
    }

    @Override
    public String getDefaultVariableName(String namePrefix, EntityType entityType) {
        String defaultVariable = toCamelCase(entityType.getAnnotation(Table.class).value());
        int suffix = 0;
        while (true){
            String candidate = suffix > 0 ? defaultVariable + suffix : defaultVariable;
            if (entityType.getEscapedPropertyNames().contains(candidate)){
                suffix++;
            }else{
                return candidate;
            }
        }        
    }
    
    @Override
    public String getPropertyName(String columnName, String namePrefix, EntityType entityType) {
        String propertyName = columnName.substring(0, 1).toLowerCase(Locale.ENGLISH) + toCamelCase(columnName.substring(1));
        if (JavaSyntaxUtils.isReserved(propertyName)){
            propertyName += reservedSuffix;
        }
        return propertyName;
    }

    @Override
    public String getPropertyNameForForeignKey(String foreignKeyName, EntityType entityType) {
        if (foreignKeyName.toLowerCase().startsWith("fk_")){
            foreignKeyName = foreignKeyName.substring(3) + "_" + foreignKeyName.substring(0,2);
        }
        if (foreignKeyName.length() > 1){
            String propertyName = foreignKeyName.substring(0,1).toLowerCase(Locale.ENGLISH) 
                + toCamelCase(foreignKeyName.substring(1));
            if (JavaSyntaxUtils.isReserved(propertyName)){
                propertyName += reservedSuffix;
            }
            return propertyName;    
        }else{
            return foreignKeyName.toLowerCase(Locale.ENGLISH);
        }
        
    }
    

    @Override
    public String getPropertyNameForInverseForeignKey(String foreignKeyName, EntityType entityType) {
        return "_" + getPropertyNameForForeignKey(foreignKeyName, entityType);
    }


    @Override
    public String getPropertyNameForPrimaryKey(String primaryKeyName, EntityType entityType) {
        if (primaryKeyName.toLowerCase().startsWith("pk_")){
            primaryKeyName = primaryKeyName.substring(3) + "_" + primaryKeyName.substring(0,2);
        }
        if (primaryKeyName.length() > 1){
            String propertyName = primaryKeyName.substring(0,1).toLowerCase(Locale.ENGLISH)  + toCamelCase(primaryKeyName.substring(1));
            if (JavaSyntaxUtils.isReserved(propertyName)){
                propertyName += reservedSuffix;
            }
            return propertyName;    
        }else{
            return primaryKeyName.toLowerCase(Locale.ENGLISH);
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

    protected String toCamelCase(String str) {
        boolean toLower = str.toUpperCase().equals(str);
        StringBuilder builder = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '_') {
                i += 1;
                if (i < str.length()){
                    builder.append(Character.toUpperCase(str.charAt(i)));    
                }                               
            } else if (toLower){
                builder.append(Character.toLowerCase(str.charAt(i)));
            } else{
                builder.append(str.charAt(i));
            }
        }
        return builder.toString();
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
