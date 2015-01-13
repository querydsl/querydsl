/*
 * Copyright 2011, Mysema Ltd
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

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.codegen.model.SimpleType;
import com.mysema.codegen.model.Type;
import com.querydsl.sql.codegen.support.ForeignKeyData;
import com.querydsl.sql.codegen.support.InverseForeignKeyData;
import com.querydsl.sql.codegen.support.PrimaryKeyData;

/**
 * @author tiwe
 *
 */
public class KeyDataFactory {

    private static final int FK_FOREIGN_COLUMN_NAME = 8;

    private static final int FK_FOREIGN_TABLE_NAME = 7;

    private static final int FK_FOREIGN_SCHEMA_NAME = 6;

    private static final int FK_NAME = 12;

    private static final int FK_PARENT_COLUMN_NAME = 4;

    private static final int FK_PARENT_TABLE_NAME = 3;

    private static final int FK_PARENT_SCHEMA_NAME = 2;

    private static final int PK_COLUMN_NAME = 4;

    private static final int PK_NAME = 6;

    private final NamingStrategy namingStrategy;

    private final String packageName, prefix, suffix;

    private final boolean schemaToPackage;

    public KeyDataFactory(NamingStrategy namingStrategy, String packageName,
            String prefix, String suffix, boolean schemaToPackage) {
        this.namingStrategy = namingStrategy;
        this.packageName = packageName;
        this.prefix = prefix;
        this.suffix = suffix;
        this.schemaToPackage = schemaToPackage;
    }

    public Map<String, InverseForeignKeyData> getExportedKeys(DatabaseMetaData md,
            String catalog, String schema, String tableName) throws SQLException{
        ResultSet foreignKeys = md.getExportedKeys(catalog, schema, tableName);
        Map<String,InverseForeignKeyData> inverseForeignKeyData = new HashMap<String,InverseForeignKeyData>();
        try{
            while (foreignKeys.next()) {
                String name = foreignKeys.getString(FK_NAME);
                String parentColumnName = namingStrategy.normalizeColumnName(foreignKeys.getString(FK_PARENT_COLUMN_NAME));
                String foreignSchemaName = namingStrategy.normalizeSchemaName(foreignKeys.getString(FK_FOREIGN_SCHEMA_NAME));
                String foreignTableName = namingStrategy.normalizeTableName(foreignKeys.getString(FK_FOREIGN_TABLE_NAME));
                String foreignColumn = namingStrategy.normalizeColumnName(foreignKeys.getString(FK_FOREIGN_COLUMN_NAME));
                if (name == null || name.isEmpty()) {
                    name = tableName + "_" + foreignTableName + "_IFK";
                }

                InverseForeignKeyData data = inverseForeignKeyData.get(name);
                if (data == null) {
                    data = new InverseForeignKeyData(name, foreignSchemaName,
                            foreignTableName, createType(foreignSchemaName, foreignTableName));
                    inverseForeignKeyData.put(name, data);
                }
                data.add(parentColumnName, foreignColumn);
            }
            return inverseForeignKeyData;
        }finally{
            foreignKeys.close();
        }
    }

    public Map<String, ForeignKeyData> getImportedKeys(DatabaseMetaData md,
            String catalog, String schema, String tableName) throws SQLException {
        ResultSet foreignKeys = md.getImportedKeys(catalog, schema, tableName);
        Map<String,ForeignKeyData> foreignKeyData = new HashMap<String,ForeignKeyData>();
        try{
            while (foreignKeys.next()) {
                String name = foreignKeys.getString(FK_NAME);
                String parentSchemaName = namingStrategy.normalizeSchemaName(foreignKeys.getString(FK_PARENT_SCHEMA_NAME));
                String parentTableName = namingStrategy.normalizeTableName(foreignKeys.getString(FK_PARENT_TABLE_NAME));
                String parentColumnName = namingStrategy.normalizeColumnName(foreignKeys.getString(FK_PARENT_COLUMN_NAME));
                String foreignColumn = namingStrategy.normalizeColumnName(foreignKeys.getString(FK_FOREIGN_COLUMN_NAME));
                if (name == null || name.isEmpty()) {
                    name = tableName + "_" + parentTableName + "_FK";
                }

                ForeignKeyData data = foreignKeyData.get(name);
                if (data == null) {
                    data = new ForeignKeyData(name, parentSchemaName, parentTableName,
                            createType(parentSchemaName, parentTableName));
                    foreignKeyData.put(name, data);
                }
                data.add(foreignColumn, parentColumnName);
            }
            return foreignKeyData;
        }finally{
            foreignKeys.close();
        }
    }

    public Map<String, PrimaryKeyData> getPrimaryKeys(DatabaseMetaData md,
            String catalog, String schema, String tableName) throws SQLException {
        ResultSet primaryKeys = md.getPrimaryKeys(catalog, schema, tableName);
        Map<String,PrimaryKeyData> primaryKeyData = new HashMap<String,PrimaryKeyData>();
        try{
            while (primaryKeys.next()) {
                String name = primaryKeys.getString(PK_NAME);
                String columnName = primaryKeys.getString(PK_COLUMN_NAME);
                if (name == null || name.isEmpty()) {
                    name = tableName + "_PK";
                }

                PrimaryKeyData data = primaryKeyData.get(name);
                if (data == null) {
                    data = new PrimaryKeyData(name);
                    primaryKeyData.put(name, data);
                }
                data.add(columnName);
            }
            return primaryKeyData;
        }finally{
            primaryKeys.close();
        }
    }

    private Type createType(@Nullable String schemaName, String table) {
        String packageName = this.packageName;
        if (schemaToPackage && schemaName != null) {
            packageName = namingStrategy.appendSchema(packageName, schemaName);
        }
        String simpleName = prefix + namingStrategy.getClassName(table) + suffix;
        return new SimpleType(packageName + "." + simpleName, packageName, simpleName);
    }

}
