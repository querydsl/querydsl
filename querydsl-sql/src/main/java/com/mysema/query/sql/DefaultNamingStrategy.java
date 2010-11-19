/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.util.Locale;

import com.mysema.query.codegen.EntityType;
import static com.mysema.util.JavaSyntaxUtils.*;

/**
 * DefaultNamingStrategy is the default implementation of the NamingStrategy
 * interface
 *
 * @author tiwe
 *
 */
public class DefaultNamingStrategy implements NamingStrategy {

    private String foreignKeysClassName = "ForeignKeys";
    
    private String foreignKeysVariable = "fk";
    
    private String primaryKeysClassName = "PrimaryKeys";
    
    private String primaryKeysVariable = "pk";
    
    private String reservedSuffix = "Col";
    
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
        return escape(entityType, toCamelCase(entityType.getAnnotation(Table.class).value()));    
    }
    
    @Override
    public String getForeignKeysClassName() {
        return foreignKeysClassName;
    }
    
    @Override
    public String getForeignKeysVariable(EntityType entityType) {
        return escape(entityType, foreignKeysVariable);
    }
    
    @Override
    public String getPrimaryKeysClassName() {
        return primaryKeysClassName;
    }

    @Override
    public String getPrimaryKeysVariable(EntityType entityType) {
        return escape(entityType, primaryKeysVariable);
    }

    @Override
    public String getPropertyName(String columnName, String namePrefix, EntityType entityType) {
        return normalizePropertyName(
            columnName.substring(0, 1).toLowerCase(Locale.ENGLISH) 
            + toCamelCase(columnName.substring(1)));        
    }

    @Override
    public String getPropertyNameForForeignKey(String foreignKeyName, EntityType entityType) {
        if (foreignKeyName.toLowerCase().startsWith("fk_")){
            foreignKeyName = foreignKeyName.substring(3) + "_" + foreignKeyName.substring(0,2);
        }
        if (foreignKeyName.length() > 1){
            return normalizePropertyName(
                foreignKeyName.substring(0,1).toLowerCase(Locale.ENGLISH) 
                + toCamelCase(foreignKeyName.substring(1)));    
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
            return normalizePropertyName(
                primaryKeyName.substring(0,1).toLowerCase(Locale.ENGLISH)  
                + toCamelCase(primaryKeyName.substring(1)));    
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

    protected String normalizePropertyName(String name){
        return isReserved(name) ? name + reservedSuffix : name;   
    }
    
    protected String escape(EntityType entityType, String name){
        int suffix = 0;
        while (true){
            String candidate = suffix > 0 ? name + suffix : name;
            if (entityType.getEscapedPropertyNames().contains(candidate)){
                suffix++;
            }else{
                return candidate;
            }
        }      
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

    public void setForeignKeysClassName(String foreignKeysClassName) {
        this.foreignKeysClassName = foreignKeysClassName;
    }

    public void setForeignKeysVariable(String foreignKeysVariable) {
        this.foreignKeysVariable = foreignKeysVariable;
    }

    public void setPrimaryKeysClassName(String primaryKeysClassName) {
        this.primaryKeysClassName = primaryKeysClassName;
    }

    public void setPrimaryKeysVariable(String primaryKeysVariable) {
        this.primaryKeysVariable = primaryKeysVariable;
    }

    public void setReservedSuffix(String reservedSuffix) {
        this.reservedSuffix = reservedSuffix;
    }


    
}
