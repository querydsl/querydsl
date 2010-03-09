/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

/**
 * DefaultNamingStrategy is the default implementation of the NamingStrategy interface
 * 
 * @author tiwe
 *
 */
public class DefaultNamingStrategy implements NamingStrategy {
            
    @Override
    public String toClassName(String namePrefix, String tableName) {
        return namePrefix + tableName.substring(0,1).toUpperCase() + toCamelCase(tableName.substring(1));
    }
    
    @Override
    public String toPropertyName(String columnName){
        return columnName.substring(0,1).toLowerCase() + toCamelCase(columnName.substring(1));
    }
    
    protected String toCamelCase(String str){
        StringBuilder builder = new StringBuilder(str.length());
        for (int i = 0; i < str.length(); i++){
            if (str.charAt(i) == '_'){
                builder.append(Character.toUpperCase(str.charAt(i+1)));
                i += 1;
            }else{
                builder.append(Character.toLowerCase(str.charAt(i))); 
            }
        }
        return builder.toString();
    }

}
