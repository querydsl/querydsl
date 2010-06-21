/*
 * Copyright (c) 2010 Mysema Ltd.
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
     * Convert the given tableName to a simple class name with the given name prefix
     *
     * @param namePrefix
     * @param tableName
     * @return
     */
    String getClassName(String namePrefix, String tableName);

    /**
     * Get the default alias for the given EntityType
     *
     * @param namePrefix
     * @param entityType
     * @return
     */
    String getDefaultAlias(String namePrefix, EntityType entityType);

    /**
     * Get the default variable name for the given EntityType
     *
     * @param entityType
     * @return
     */
    String getDefaultVariableName(String namePrefix, EntityType entityType);

    /**
     * Convert the given column name to a property name
     *
     * @param columnName
     * @param namePrefix NOTE : is used in some custom NamingStrategy implementations
     * @param entityType
     * @return
     */
    String getPropertyName(String columnName, String namePrefix, EntityType entityType);

    /**
     * Convert the given foreign key name to a foreign key property name
     *
     * @param foreignKeyName
     * @param entityType
     * @return
     */
    String getPropertyNameForForeignKey(String foreignKeyName, EntityType entityType);

    /**
     * Convert the given primary key name to a primary key property name
     *
     * @param name
     * @param model
     * @return
     */
    String getPropertyNameForPrimaryKey(String name, EntityType model);

    /**
     * Convert the given column name and provide the opportunity to add quoted identifiers
     *
     * @param columnName
     * @return
     */
    String normalizeColumnName(String columnName);

    /**
     * Convert the given table name and provide the opportunity to add quoted identifiers
     *
     * @param tableName
     * @return
     */
    String normalizeTableName(String tableName);

}
