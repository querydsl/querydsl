/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import com.mysema.query.codegen.EntityType;

/**
 * NamingStrategy defines a conversion strategy from table to class and column
 * to property names
 * 
 * @author tiwe
 */
public interface NamingStrategy {
    
    /**
     * Get the default variable name for the given EntityType
     * 
     * @param entityType
     * @return
     */
    String getDefaultVariableName(String namePrefix, EntityType entityType);
    
    /**
     * Get the default alias for the given EntityType
     * 
     * @param namePrefix
     * @param entityType
     * @return
     */
    String getDefaultAlias(String namePrefix, EntityType entityType);

    /**
     * Convert the given tableName to a simple class name with the given name prefix
     * 
     * @param namePrefix
     * @param tableName 
     * @return
     */
    String getClassName(String namePrefix, String tableName);

    /**
     * Convert the given column name to a property name
     * 
     * @param columnName
     * @return
     */
    String getPropertyName(String columnName);

}