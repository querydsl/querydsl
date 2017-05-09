/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.sql.codegen;

import com.querydsl.codegen.EntityType;
import com.querydsl.sql.SchemaAndTable;
import com.querydsl.sql.codegen.support.ForeignKeyData;
import com.querydsl.sql.codegen.support.InverseForeignKeyData;

/**
 * {@code NamingStrategy} defines a conversion strategy from table to class and column
 * to property names
 *
 * @author tiwe
 */
public interface NamingStrategy {

    /**
     * Normalizes and appends the given schema name to the package name
     *
     * @deprecated Use {@link #getPackage(String, SchemaAndTable)} instead.
     *
     * @param packageName
     * @param schema
     * @return
     */
    @Deprecated
    String appendSchema(String packageName, String schema);

    /**
     * Convert the given tableName to a simple class name
     *
     * @deprecated Use {@link #getClassName(SchemaAndTable)} instead.
     *
     * @return
     */
    @Deprecated
    String getClassName(String tableName);

    /**
     * Convert the given schema and table name to a simple class name.
     */
    String getClassName(SchemaAndTable schemaAndTable);

    /**
     * Get the default alias for the given EntityType
     *
     * @param entityType
     * @return
     */
    String getDefaultAlias(EntityType entityType);

    /**
     * Get the default variable name for the given EntityType
     *
     * @param entityType
     * @return
     */
    String getDefaultVariableName(EntityType entityType);

    /**
     * Get the class name for the foreign keys inner class
     *
     * @return
     */
    String getForeignKeysClassName();

    /**
     * Get the field name for the foreign keys class instance
     *
     * @return
     */
    String getForeignKeysVariable(EntityType entityType);

    /**
     * Get the class name for the primary keys inner class
     *
     * @return
     */
    String getPrimaryKeysClassName();

    /**
     * Get the field name for the primary keys class instance
     *
     * @return
     */
    String getPrimaryKeysVariable(EntityType entityType);

    /**
     * Convert the given column name to a property name
     *
     * @param columnName
     * @param entityType
     * @return
     */
    String getPropertyName(String columnName, EntityType entityType);

    /**
     * Convert the given foreign key name to a foreign key property name
     *
     * @param foreignKeyName
     * @param entityType
     * @return
     */
    String getPropertyNameForForeignKey(String foreignKeyName, EntityType entityType);

    /**
     * Convert the given foreign key name to a foreign key property name
     *
     * @param name
     * @param model
     * @return
     */
    String getPropertyNameForInverseForeignKey(String name, EntityType model);

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


    /**
     * Convert the given schema name and provide the opportunity to add quoted identifiers
     *
     * @param schemaName
     * @return
     */
    String normalizeSchemaName(String schemaName);

    /**
     * Returns <code>true</code> if the class generation of the table is required, otherwise
     * <code>false</code>.
     *
     * @param schemaAndTable the schema and table
     * @return
     */
    boolean shouldGenerateClass(SchemaAndTable schemaAndTable);

    /**
     * Returns <code>true</code> if the foreign key reference should be generated in the table,
     * otherwise <code>false</code>.
     *
     * @param schemaAndTable the schema and table
     * @param foreignKeyData the foreign key in the table
     * @return
     */
    boolean shouldGenerateForeignKey(SchemaAndTable schemaAndTable, ForeignKeyData foreignKeyData);

    /**
     * Returns the package where the class of the table will be generated.
     *
     * @param basePackage the base package of the class generation
     * @param schemaAndTable the schema and table
     * @return
     */
    String getPackage(String basePackage, SchemaAndTable schemaAndTable);

    /**
     * Returns <code>true</code> if the inverse foreign key reference should be generated in the table,
     * otherwise <code>false</code>.
     *
     * @param schemaAndTable the schema and table
     * @param inverseForeignKeyData the inverse foreign key in the table
     * @return
     */
    boolean shouldGenerateInverseForeignKeys(SchemaAndTable schemaAndTable, InverseForeignKeyData inverseForeignKeyData);

    }
