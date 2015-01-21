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
package com.querydsl.sql;

import java.lang.reflect.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.google.common.primitives.Primitives;
import com.querydsl.sql.types.ArrayType;
import com.querydsl.sql.types.Null;
import com.querydsl.sql.types.Type;
import com.querydsl.core.types.Path;

/**
 * Configuration for SQLQuery instances
 *
 * @author tiwe
 *
 */
public final class Configuration {

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    public static final Configuration DEFAULT = new Configuration(SQLTemplates.DEFAULT);

    private final JDBCTypeMapping jdbcTypeMapping = new JDBCTypeMapping();

    private final JavaTypeMapping javaTypeMapping = new JavaTypeMapping();

    private final Map<SchemaAndTable, SchemaAndTable> schemaTables = Maps.newHashMap();

    private final Map<String, String> schemas = Maps.newHashMap();

    private final Map<String, String> tables = Maps.newHashMap();

    private final Map<SchemaAndTable, Map<String, String>> schemaTableColumns = Maps.newHashMap();

    private final Map<String, Map<String, String>> tableColumns = Maps.newHashMap();

    private final Map<String, Class<?>> typeToName = Maps.newHashMap();

    private SQLTemplates templates;

    private SQLExceptionTranslator exceptionTranslator = DefaultSQLExceptionTranslator.DEFAULT;

    private final SQLListeners listeners = new SQLListeners();

    private boolean hasTableColumnTypes = false;

    private boolean useLiterals = false;

    /**
     * Create a new Configuration instance
     *
     * @param templates
     */
    public Configuration(SQLTemplates templates) {
        this.templates = templates;
        for (Type<?> customType : templates.getCustomTypes()) {
            javaTypeMapping.register(customType);
        }
        for (Map.Entry<SchemaAndTable, SchemaAndTable> entry : templates.getTableOverrides().entrySet()) {
            schemaTables.put(entry.getKey(), entry.getValue());
        }

        if (templates.isArraysSupported()) {
            // register array types
            List<Class<?>> classes = ImmutableList.<Class<?>>of(String.class, Long.class, Integer.class, Short.class,
                    Byte.class, Boolean.class, java.sql.Date.class, java.sql.Timestamp.class,
                    java.sql.Time.class, Double.class, Float.class);
            for (Class<?> cl : classes) {
                int code = jdbcTypeMapping.get(cl);
                String name = templates.getTypeNameForCode(code);
                Class<?> arrType = Array.newInstance(cl, 0).getClass();
                javaTypeMapping.register(new ArrayType(arrType, name));
                if (Primitives.isWrapperType(cl) && !cl.equals(Byte.class)) {
                    cl = Primitives.unwrap(cl);
                    arrType = Array.newInstance(cl, 0).getClass();
                    javaTypeMapping.register(new ArrayType(arrType, name));
                }
            }
        }

    }

    /**
     * Get the literal representation of the given constant
     *
     * @param o
     * @return
     */
    public String asLiteral(Object o) {
        if (Null.class.isInstance(o)) {
            return "null";
        } else {
            Type type = javaTypeMapping.getType(o.getClass());
            if (type != null) {
                return templates.serialize(type.getLiteral(o), type.getSQLTypes()[0]);
            } else {
                throw new IllegalArgumentException("Unsupported literal type " + o.getClass().getName());
            }
        }                
    }

    public SQLTemplates getTemplates() {
        return templates;
    }

    /**
     * Use the other getJavaType method instead
     *
     * @param sqlType
     * @param size
     * @param digits
     * @param tableName
     * @param columnName
     * @return
     */
    @Deprecated
    public Class<?> getJavaType(int sqlType, int size, int digits, String tableName, String columnName) {
        return getJavaType(sqlType, null, size, digits, tableName, columnName);
    }

    /**
     * Get the java type for the given jdbc type, table name and column name
     *
     * @param sqlType
     * @param typeName
     * @param size
     * @param digits
     * @param tableName
     * @param columnName
     * @return
     */
    public Class<?> getJavaType(int sqlType, String typeName, int size, int digits, String tableName, String columnName) {
        // table.column mapped class
        Type<?> type = javaTypeMapping.getType(tableName, columnName);
        if (type != null) {
            return type.getReturnedClass();
        } else if (typeName != null && !typeName.isEmpty()) {
            typeName = typeName.toLowerCase();
            // typename mapped class
            Class<?> clazz = typeToName.get(typeName);
            if (clazz != null) {
                return clazz;
            }
            if (sqlType == Types.ARRAY) {
                if (typeName.startsWith("_")) {
                    typeName = typeName.substring(1);
                } else if (typeName.endsWith(" array")) {
                    typeName = typeName.substring(0, typeName.length() - 6);
                }
                if (typeName.contains("[")) {
                    typeName = typeName.substring(0, typeName.indexOf("["));
                }
                if (typeName.contains("(")) {
                    typeName = typeName.substring(0, typeName.indexOf("("));
                }

                Integer sqlComponentType = templates.getCodeForTypeName(typeName);
                if (sqlComponentType == null) {
                    logger.warn("Found no JDBC type for " + typeName + " using OTHER instead");
                    sqlComponentType = Types.OTHER;
                }
                Class<?> componentType = jdbcTypeMapping.get(sqlComponentType, size, digits);
                return Array.newInstance(componentType, 0).getClass();
            }
        }
        // sql type mapped class
        return jdbcTypeMapping.get(sqlType, size, digits);
    }

    /**
     * @param <T>
     * @param rs
     * @param path
     * @param i
     * @param clazz
     * @return
     * @throws SQLException
     */
    @Nullable
    public <T> T get(ResultSet rs, @Nullable Path<?> path, int i, Class<T> clazz) throws SQLException {
        return getType(path, clazz).getValue(rs, i);
    }

    /**
     * Use getOverride instead
     *
     * @param schema
     * @return
     */
    @Deprecated
    public String getSchema(String schema) {
        return schemas.get(schema);
    }

    /**
     * Use getOverride instead
     *
     * @param schema
     * @param table
     * @return
     */
    @Deprecated
    public String getTable(String schema, String table) {
        return getOverride(new SchemaAndTable(schema, table)).getTable();
    }

    /**
     * Get the schema/table override
     *
     * @param key
     * @return
     */
    @Nullable
    public SchemaAndTable getOverride(SchemaAndTable key) {
        if (!schemaTables.isEmpty() && key.getSchema() != null) {
            if (schemaTables.containsKey(key)) {
                return schemaTables.get(key);
            }
        }
        String schema = key.getSchema(), table = key.getTable();
        boolean changed = false;
        if (schemas.containsKey(key.getSchema())) {
            schema = schemas.get(key.getSchema());
            changed = true;
        }

        if (tables.containsKey(key.getTable())) {
            table = tables.get(key.getTable());
            changed = true;
        }
        return changed ? new SchemaAndTable(schema, table) : key;
    }

    /**
     * Get the column override
     *
     * @param key
     * @param column
     * @return
     */
    public String getColumnOverride(SchemaAndTable key, String column) {
        Map<String, String> columnOverrides;
        String newColumn = null;
        columnOverrides = schemaTableColumns.get(key);
        if (columnOverrides != null && (newColumn = columnOverrides.get(column)) != null) {
            return newColumn;
        }
        columnOverrides = tableColumns.get(key.getTable());
        if (columnOverrides != null && (newColumn = columnOverrides.get(column)) != null) {
            return newColumn;
        }
        return column;

    }

    /**
     * @param <T>
     * @param stmt
     * @param path
     * @param i
     * @param value
     * @throws SQLException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> void set(PreparedStatement stmt, Path<?> path, int i, T value) throws SQLException {
        if (Null.class.isInstance(value)) {
            Integer sqlType = path != null ? ColumnMetadata.getColumnMetadata(path).getJdbcType() : null;
            if (sqlType != null) {
                stmt.setNull(i, sqlType);
            } else {
                stmt.setNull(i, Types.NULL);
            }
        } else {
            getType(path, (Class)value.getClass()).setValue(stmt, i, value);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> Type<T> getType(@Nullable Path<?> path, Class<T> clazz) {
        if (hasTableColumnTypes && path != null && !clazz.equals(Null.class)
                && path.getMetadata().getParent() instanceof RelationalPath) {
            String table = ((RelationalPath)path.getMetadata().getParent()).getTableName();
            String column = ColumnMetadata.getName(path);
            Type<T> type = (Type)javaTypeMapping.getType(table, column);
            if (type != null) {
                return type;
            }
        }
        return javaTypeMapping.getType(clazz);
    }

    /**
     * Get the SQL type name for the given java type
     *
     * @param type
     * @return
     */
    public String getTypeName(Class<?> type) {
        Integer jdbcType = jdbcTypeMapping.get(type);
        if (jdbcType == null) {
            jdbcType = javaTypeMapping.getType(type).getSQLTypes()[0];
        }
        return templates.getTypeNameForCode(jdbcType);
    }

    /**
     *
     * @param type
     * @return
     */
    public String getTypeNameForCast(Class<?> type) {
        Integer jdbcType = jdbcTypeMapping.get(type);
        if (jdbcType == null) {
            jdbcType = javaTypeMapping.getType(type).getSQLTypes()[0];
        }
        return templates.getCastTypeNameForCode(jdbcType);
    }

    /**
     * Register a schema override
     *
     * @param oldSchema
     * @param newSchema
     * @return
     */
    public String registerSchemaOverride(String oldSchema, String newSchema) {
        return schemas.put(oldSchema, newSchema);
    }

    /**
     * Register a table override
     *
     * @param oldTable
     * @param newTable
     * @return
     */
    public String registerTableOverride(String oldTable, String newTable) {
        return tables.put(oldTable, newTable);
    }

    /**
     * Register a schema specific table override
     *
     * @param schema
     * @param oldTable
     * @param newTable
     * @return
     */
    public String registerTableOverride(String schema, String oldTable, String newTable) {
        SchemaAndTable st = registerTableOverride(schema, oldTable, schema, newTable);
        return st != null ? st.getTable() : null;
    }

    /**
     * Register a schema specific table override
     *
     * @param schema
     * @param oldTable
     * @param newSchema
     * @param newTable
     * @return
     */
    public SchemaAndTable registerTableOverride(String schema, String oldTable, String newSchema, String newTable) {
        return registerTableOverride(new SchemaAndTable(schema, oldTable), new SchemaAndTable(newSchema, newTable));
    }

    /**
     * Register a schema specific table override
     *
     * @param from
     * @param to
     * @return
     */
    public SchemaAndTable registerTableOverride(SchemaAndTable from, SchemaAndTable to) {
        return schemaTables.put(from, to);
    }

    /**
     * Register a column override
     *
     * @param schema
     * @param table
     * @param oldColumn
     * @param newColumn
     * @return
     */
    public String registerColumnOverride(String schema, String table, String oldColumn, String newColumn) {
        SchemaAndTable key = new SchemaAndTable(schema, table);
        Map<String, String> columnOverrides = schemaTableColumns.get(key);
        if (columnOverrides == null) {
            columnOverrides = new HashMap<String, String>();
            schemaTableColumns.put(key, columnOverrides);
        }
        return columnOverrides.put(oldColumn, newColumn);
    }

    /**
     * Register a column override
     *
     * @param table
     * @param oldColumn
     * @param newColumn
     * @return
     */
    public String registerColumnOverride(String table, String oldColumn, String newColumn) {
        Map<String, String> columnOverrides = tableColumns.get(table);
        if (columnOverrides == null) {
            columnOverrides = new HashMap<String, String>();
            tableColumns.put(table, columnOverrides);
        }
        return columnOverrides.put(oldColumn, newColumn);
    }

    /**
     * Register the given {@link Type} converter
     *
     * @param type
     */
    public void register(Type<?> type) {
        jdbcTypeMapping.register(type.getSQLTypes()[0], type.getReturnedClass());
        javaTypeMapping.register(type);
    }

    /**
     * Register a typeName to Class mapping
     *
     * @param typeName
     * @param clazz
     */
    public void registerType(String typeName, Class<?> clazz) {
        typeToName.put(typeName.toLowerCase(), clazz);
    }

    /**
     * Override the binding for the given NUMERIC type
     *
     * @param total total amount of digits
     * @param decimal amount of fractional digits
     * @param javaType
     */
    public void registerNumeric(int total, int decimal, Class<?> javaType) {
        jdbcTypeMapping.registerNumeric(total, decimal, javaType);
    }

    /**
     * Override multiple numeric bindings, both begin and end are inclusive
     *
     * @param beginTotal
     * @param endTotal
     * @param beginDecimal
     * @param endDecimal
     * @param javaType
     */
    public void registerNumeric (int beginTotal, int endTotal, int beginDecimal, int endDecimal, Class <?> javaType) {
        for (int total = beginTotal; total <= endTotal; total++) {
            for (int decimal = beginDecimal; decimal <= endDecimal; decimal++) {
                registerNumeric(total, decimal, javaType);
            }
        }
    }

    /**
     * Register the given javaType for the given table and column
     *
     * @param table
     * @param column
     * @param javaType
     */
    public void register(String table, String column, Class<?> javaType) {
        register(table, column, javaTypeMapping.getType(javaType));
    }

    /**
     * Register the given {@link Type} converter for the given table and column
     *
     * @param table
     * @param column
     * @param type
     */
    public void register(String table, String column, Type<?> type) {
        javaTypeMapping.setType(table, column, type);
        hasTableColumnTypes = true;
    }

    /**
     * Translate the given SQLException
     *
     * @param ex
     * @return
     */
    public RuntimeException translate(SQLException ex) {
        return exceptionTranslator.translate(ex);
    }

    /**
     * Translate the given SQLException
     *
     * @param sql
     * @param ex
     * @return
     */
    public RuntimeException translate(String sql, List<Object> bindings, SQLException ex) {
        return exceptionTranslator.translate(sql, bindings, ex);
    }

    /**
     * @param listener
     */
    public void addListener(SQLListener listener) {
        listeners.add(listener);
    }

    /**
     * @return
     */
    public SQLListeners getListeners() {
        return listeners;
    }

    /**
     * @return
     */
    public boolean getUseLiterals() {
        return useLiterals;
    }

    /**
     * @param useLiterals
     */
    public void setUseLiterals(boolean useLiterals) {
        this.useLiterals = useLiterals;
    }

    /**
     * @param exceptionTranslator
     */
    public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     * @param templates
     */
    public void setTemplates(SQLTemplates templates) {
        this.templates = templates;
    }

}
