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

import com.querydsl.core.util.PrimitiveUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Path;
import com.querydsl.sql.namemapping.ChainedNameMapping;
import com.querydsl.sql.namemapping.NameMapping;
import com.querydsl.sql.namemapping.PreConfiguredNameMapping;
import com.querydsl.sql.types.ArrayType;
import com.querydsl.sql.types.Null;
import com.querydsl.sql.types.Type;

/**
 * Configuration for SQLQuery instances
 *
 * @author tiwe
 *
 */
public final class Configuration {

    private static final Logger logger = LoggerFactory.getLogger(Configuration.class);

    static final Configuration DEFAULT = new Configuration(SQLTemplates.DEFAULT);

    private final JDBCTypeMapping jdbcTypeMapping = new JDBCTypeMapping();

    private final JavaTypeMapping javaTypeMapping = new JavaTypeMapping();

    private final PreConfiguredNameMapping internalNameMapping = new PreConfiguredNameMapping();

    private NameMapping nameMapping = internalNameMapping;

    private final Map<String, String> schemaMapping = new HashMap<>();

    private final Map<String, Class<?>> typeToName = new HashMap<>();

    private SQLTemplates templates;

    private SQLExceptionTranslator exceptionTranslator = DefaultSQLExceptionTranslator.DEFAULT;

    private final SQLListeners listeners = new SQLListeners();

    private boolean hasTableColumnTypes = false;

    private boolean useLiterals = false;

    /**
     * Create a new Configuration instance
     *
     * @param templates templates for SQL serialization
     */
    @SuppressWarnings("unchecked")
    public Configuration(SQLTemplates templates) {
        this.templates = templates;
        for (Type<?> customType : templates.getCustomTypes()) {
            javaTypeMapping.register(customType);
        }
        for (Map.Entry<SchemaAndTable, SchemaAndTable> entry : templates.getTableOverrides().entrySet()) {
            registerTableOverride(entry.getKey(), entry.getValue());
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
                if (PrimitiveUtils.isWrapperType(cl) && !cl.equals(Byte.class)) {
                    cl = PrimitiveUtils.unwrap(cl);
                    arrType = Array.newInstance(cl, 0).getClass();
                    javaTypeMapping.register(new ArrayType(arrType, name));
                }
            }
        }

    }

    /**
     * Get the literal representation of the given constant
     *
     * @param o object
     * @return literal representation
     */
    @SuppressWarnings("unchecked")
    public String asLiteral(Object o) {
        if (o == null || o instanceof Null) {
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
     * Get the java type for the given jdbc type, table name and column name
     *
     * @param sqlType JDBC type
     * @param typeName JDBC type name
     * @param size size
     * @param digits digits
     * @param tableName table name
     * @param columnName column name
     * @return Java type
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
     * Get the value at the given index from the result set
     *
     * @param <T> type to return
     * @param rs result set
     * @param path path
     * @param i one based index in result set row
     * @param clazz type
     * @return value
     * @throws SQLException
     */
    @Nullable
    public <T> T get(ResultSet rs, @Nullable Path<?> path, int i, Class<T> clazz) throws SQLException {
        return getType(path, clazz).getValue(rs, i);
    }

    /**
     * Get the schema/table override
     *
     * @param key schema and table
     * @return overridden schema and table
     */
    @Nullable
    public SchemaAndTable getOverride(SchemaAndTable key) {
        SchemaAndTable result = nameMapping.getOverride(key).orElse(key);
        if (schemaMapping.containsKey(key.getSchema())) {
            result = new SchemaAndTable(schemaMapping.get(key.getSchema()), result.getTable());
        }
        return result;
    }

    /**
     * Get the column override
     *
     * @param key schema and table
     * @param column column
     * @return overridden column
     */
    public String getColumnOverride(SchemaAndTable key, String column) {
        return nameMapping.getColumnOverride(key, column).orElse(column);
    }

    /**
     * Programmers can specify name mappings by implementing the
     * {@link NameMapping} interface. The mapping rules that are specified by
     * this property are checked if no mapping is specified by any of the
     * <code>register*Override</code> functions.
     *
     * @param nameMapping
     *            The name mapping that is implemented by the user.
     */
    public void setDynamicNameMapping(NameMapping nameMapping) {
        if (nameMapping == null) {
            this.nameMapping = this.internalNameMapping;
        } else {
            this.nameMapping = new ChainedNameMapping(this.internalNameMapping, nameMapping);
        }
    }

    /**
     * Set the value at the given index in the statement
     *
     * @param <T>
     * @param stmt statement
     * @param path path
     * @param i one based index in statement
     * @param value value to bind
     * @throws SQLException
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public <T> void set(PreparedStatement stmt, Path<?> path, int i, T value) throws SQLException {
        if (value == null || value instanceof Null) {
            Integer sqlType = null;
            if (path != null) {
                ColumnMetadata columnMetadata = ColumnMetadata.getColumnMetadata(path);
                if (columnMetadata.hasJdbcType()) {
                    sqlType = columnMetadata.getJdbcType();
                }
            }
            if (sqlType != null) {
                stmt.setNull(i, sqlType);
            } else {
                stmt.setNull(i, Types.NULL);
            }
        } else {
            getType(path, (Class) value.getClass()).setValue(stmt, i, value);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> Type<T> getType(@Nullable Path<?> path, Class<T> clazz) {
        if (hasTableColumnTypes && path != null && !clazz.equals(Null.class)
                && path.getMetadata().getParent() instanceof RelationalPath) {
            String table = ((RelationalPath) path.getMetadata().getParent()).getTableName();
            String column = ColumnMetadata.getName(path);
            Type<T> type = (Type) javaTypeMapping.getType(table, column);
            if (type != null) {
                return type;
            }
        }
        return javaTypeMapping.getType(clazz);
    }

    /**
     * Get the SQL type name for the given java type
     *
     * @param type java type
     * @return SQL type name
     */
    public String getTypeName(Class<?> type) {
        Integer jdbcType = jdbcTypeMapping.get(type);
        if (jdbcType == null) {
            jdbcType = javaTypeMapping.getType(type).getSQLTypes()[0];
        }
        return templates.getTypeNameForCode(jdbcType);
    }

    /**
     * Get the SQL type name for a cast operation
     *
     * @param type java type
     * @return SQL type name
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
     * @param oldSchema schema to override
     * @param newSchema override
     * @return previous override value
     *
     * @deprecated Use {@link #setDynamicNameMapping(NameMapping)} instead.
     */
    @Deprecated
    public String registerSchemaOverride(String oldSchema, String newSchema) {
        return schemaMapping.put(oldSchema, newSchema);
    }

    /**
     * Register a table override
     *
     * @param oldTable table to override
     * @param newTable override
     * @return previous override value
     *
     * @deprecated Use {@link #setDynamicNameMapping(NameMapping)} instead.
     */
    @Deprecated
    public String registerTableOverride(String oldTable, String newTable) {
        return internalNameMapping.registerTableOverride(oldTable, newTable);
    }

    /**
     * Register a schema specific table override
     *
     * @param schema schema of table
     * @param oldTable table to override
     * @param newTable override
     * @return previous override value
     *
     * @deprecated Use {@link #setDynamicNameMapping(NameMapping)} instead.
     */
    @Deprecated
    public String registerTableOverride(String schema, String oldTable, String newTable) {
        SchemaAndTable st = registerTableOverride(schema, oldTable, schema, newTable);
        return st != null ? st.getTable() : null;
    }

    /**
     * Register a schema specific table override
     *
     * @param schema schema of table
     * @param oldTable table to override
     * @param newSchema override schema
     * @param newTable override table
     * @return previous override value
     *
     * @deprecated Use {@link #setDynamicNameMapping(NameMapping)} instead.
     */
    @Deprecated
    public SchemaAndTable registerTableOverride(String schema, String oldTable, String newSchema, String newTable) {
        return registerTableOverride(new SchemaAndTable(schema, oldTable), new SchemaAndTable(newSchema, newTable));
    }

    /**
     * Register a schema specific table override
     *
     * @param from schema and table to override
     * @param to override
     * @return previous override
     *
     * @deprecated Use {@link #setDynamicNameMapping(NameMapping)} instead.
     */
    @Deprecated
    public SchemaAndTable registerTableOverride(SchemaAndTable from, SchemaAndTable to) {
        return internalNameMapping.registerTableOverride(from, to);
    }

    /**
     * Register a column override
     *
     * @param schema schema
     * @param table table
     * @param oldColumn column
     * @param newColumn override
     * @return previous override
     *
     * @deprecated Use {@link #setDynamicNameMapping(NameMapping)} instead.
     */
    @Deprecated
    public String registerColumnOverride(String schema, String table, String oldColumn, String newColumn) {
        return internalNameMapping.registerColumnOverride(schema, table, oldColumn, newColumn);
    }

    /**
     * Register a column override
     *
     * @param table table
     * @param oldColumn column
     * @param newColumn override
     * @return previous override
     *
     * @deprecated Use {@link #setDynamicNameMapping(NameMapping)} instead.
     */
    @Deprecated
    public String registerColumnOverride(String table, String oldColumn, String newColumn) {
        return internalNameMapping.registerColumnOverride(table, oldColumn, newColumn);
    }

    /**
     * Register the given {@link Type} converter
     *
     * @param type type
     */
    public void register(Type<?> type) {
        jdbcTypeMapping.register(type.getSQLTypes()[0], type.getReturnedClass());
        javaTypeMapping.register(type);
    }

    /**
     * Register a typeName to Class mapping
     *
     * @param typeName SQL type name
     * @param clazz java type
     */
    public void registerType(String typeName, Class<?> clazz) {
        typeToName.put(typeName.toLowerCase(), clazz);
    }

    /**
     * Override the binding for the given NUMERIC type
     *
     * @param total total amount of digits
     * @param decimal amount of fractional digits
     * @param javaType java type
     */
    public void registerNumeric(int total, int decimal, Class<?> javaType) {
        jdbcTypeMapping.registerNumeric(total, decimal, javaType);
    }

    /**
     * Override multiple numeric bindings, both begin and end are inclusive
     *
     * @param beginTotal inclusive start of range
     * @param endTotal inclusive end of range
     * @param beginDecimal inclusive start of range
     * @param endDecimal inclusive end of range
     * @param javaType java type
     */
    public void registerNumeric(int beginTotal, int endTotal, int beginDecimal, int endDecimal, Class <?> javaType) {
        for (int total = beginTotal; total <= endTotal; total++) {
            for (int decimal = beginDecimal; decimal <= endDecimal; decimal++) {
                registerNumeric(total, decimal, javaType);
            }
        }
    }

    /**
     * Register the given javaType for the given table and column
     *
     * @param table table
     * @param column column
     * @param javaType java type
     */
    public void register(String table, String column, Class<?> javaType) {
        register(table, column, javaTypeMapping.getType(javaType));
    }

    /**
     * Register the given {@link Type} converter for the given table and column
     *
     * @param table table
     * @param column column
     * @param type type
     */
    public void register(String table, String column, Type<?> type) {
        javaTypeMapping.setType(table, column, type);
        hasTableColumnTypes = true;
    }

    /**
     * Translate the given SQLException
     *
     * @param ex SQLException to translate
     * @return translated exception
     */
    public RuntimeException translate(SQLException ex) {
        return exceptionTranslator.translate(ex);
    }

    /**
     * Translate the given SQLException
     *
     * @param sql SQL string
     * @param bindings bindings
     * @param ex SQLException to translate
     * @return translated exception
     */
    public RuntimeException translate(String sql, List<Object> bindings, SQLException ex) {
        return exceptionTranslator.translate(sql, bindings, ex);
    }

    /**
     * Add a listener
     *
     * @param listener listener
     */
    public void addListener(SQLListener listener) {
        listeners.add(listener);
    }

    /**
     * Get the registered listener
     *
     * @return listeners as single listener instance
     */
    public SQLListeners getListeners() {
        return listeners;
    }

    /**
     * Get whether literals are serialized or prepared statement bindings are used
     *
     * @return true for literals and false for bindings
     */
    public boolean getUseLiterals() {
        return useLiterals;
    }

    /**
     * Set whether literals are used in SQL strings instead of parameter bindings (default: false)
     *
     * <p>Warning: When literals are used, prepared statement won't have any parameter bindings
     * and also batch statements will only be simulated, but not executed as actual batch statements.</p>
     *
     * @param useLiterals true for literals and false for bindings
     */
    public void setUseLiterals(boolean useLiterals) {
        this.useLiterals = useLiterals;
    }

    /**
     * Set the exception translator
     *
     * @param exceptionTranslator exception translator
     */
    public void setExceptionTranslator(SQLExceptionTranslator exceptionTranslator) {
        this.exceptionTranslator = exceptionTranslator;
    }

    /**
     * Set the templates to use for serialization
     *
     * @param templates templates
     */
    public void setTemplates(SQLTemplates templates) {
        this.templates = templates;
    }

}
