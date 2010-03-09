/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

/**
 * OriginalNamingStrategy preserves the table and column names in the conversion
 * 
 * @author tiwe
 *
 */
public class OriginalNamingStrategy implements NamingStrategy{

    @Override
    public String toClassName(String namePrefix, String tableName) {
        return namePrefix + tableName;
    }

    @Override
    public String toPropertyName(String columnName) {
        return columnName;
    }

}
