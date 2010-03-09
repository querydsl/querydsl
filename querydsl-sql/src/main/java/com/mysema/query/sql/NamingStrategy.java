/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

/**
 * NamingStrategy defines a conversion strategy from table to class and column to property names
 * 
 * @author tiwe
 */
public interface NamingStrategy {

    /**
     * Convert the given tableName to a simple class name with the given name prefix
     * 
     * @param namePrefix
     * @param tableName 
     * @return
     */
    public String toClassName(String namePrefix, String tableName);

    /**
     * Convert the given column name to a property name
     * 
     * @param columnName
     * @return
     */
    public String toPropertyName(String columnName);

}