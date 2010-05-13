/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.lang.annotation.Annotation;

import com.mysema.query.codegen.EntityType;

/**
 * OriginalNamingStrategy preserves the table and column names in the conversion
 * 
 * @author tiwe
 * 
 */
public class OriginalNamingStrategy implements NamingStrategy {

    @Override
    public String getClassName(String namePrefix, String tableName) {
        return namePrefix + tableName;
    }

    @Override
    public String getPropertyName(String columnName, String namePrefix, EntityType entityType) {
        return columnName;
    }

    @Override
    public String getDefaultVariableName(String namePrefix, EntityType entityType) {
        // TODO : escape
        return entityType.getSimpleName();
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
    public String normalizeColumnName(String columnName) {
        return columnName;
    }

    @Override
    public String normalizeTableName(String tableName) {
        return tableName;
    }

}
