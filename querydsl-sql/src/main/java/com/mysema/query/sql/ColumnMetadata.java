/*
 * Copyright 2013, Mysema Ltd
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
package com.mysema.query.sql;

import java.sql.Types;

import com.mysema.query.types.Path;

/**
 * Provides metadata like the column name, JDBC type and constraints
 */
public class ColumnMetadata {

    /**
     * Returns this path's column metadata if present. Otherwise returns default
     * metadata where the column name is equal to the path's name.
     */
    public static ColumnMetadata getColumnMetadata(Path<?> path) {
        Path<?> parent = path.getMetadata().getParent();
        if (parent != null && parent instanceof RelationalPath) {
            ColumnMetadata columnMetadata = ((RelationalPath<?>) parent).getColumnMetadata(path);
            if (columnMetadata != null) {
                return columnMetadata;
            }
        }
        return ColumnMetadata.named(path.getMetadata().getName());
    }

    public static String getName(Path<?> path) {
        Path<?> parent = path.getMetadata().getParent();
        if (parent != null && parent instanceof RelationalPath) {
            ColumnMetadata columnMetadata = ((RelationalPath<?>) parent).getColumnMetadata(path);
            if (columnMetadata != null) {
                return columnMetadata.getName();
            }
        }
        return path.getMetadata().getName();
    }

    /**
     * Creates default column meta data with the given column name, but without
     * any type or constraint information. Use the fluent builder methods to
     * further configure it.
     *
     * @throws NullPointerException
     *             if the name is null
     */
    public static ColumnMetadata named(String name) {
        return new ColumnMetadata(name, null, true, UNDEFINED, UNDEFINED, UNDEFINED, true, true);
    }

    private static int UNDEFINED = -1;

    private final String name;

    private final Integer jdbcType;

    private final boolean nullable;

    private final int length;

    private final int precision;

    private final int scale;

    private final boolean updateable;

    private final boolean insertable;

    private ColumnMetadata(String name, Integer jdbcType, boolean nullable, int length,
            int precision, int scale, boolean updateable, boolean insertable) {
        this.name = name;
        this.jdbcType = jdbcType;
        this.nullable = nullable;
        this.length = length;
        this.precision = precision;
        this.scale = scale;
        this.updateable = updateable;
        this.insertable = insertable;
    }

    public String getName() {
        return name;
    }

    public boolean hasJdbcType() {
        return jdbcType != null;
    }

    /**
     * The JDBC type of this column.
     *
     * @see Types
     * @see ColumnMetadata#hasJdbcType()
     */
    public int getJdbcType() {
        return jdbcType;
    }

    /**
     * Returns a new column with the given type information
     *
     * @see Types
     */
    public ColumnMetadata ofType(int jdbcType) {
        return new ColumnMetadata(name, jdbcType, nullable, length, precision, scale, updateable,
                insertable);
    }

    public boolean isNullable() {
        return nullable;
    }

    /**
     * Returns a new column with a not null constraint
     */
    public ColumnMetadata notNull() {
        return new ColumnMetadata(name, jdbcType, false, length, precision, scale, updateable,
                insertable);
    }

    /**
     * The length constraint of this column.
     *
     * @see ColumnMetadata#hasLength()
     */
    public int getLength() {
        return length;
    }

    public boolean hasLength() {
        return length != UNDEFINED;
    }

    /**
     * Returns a new column with the given length constraint. The length must be > 0.
     */
    public ColumnMetadata withlength(int length) {
        return new ColumnMetadata(name, jdbcType, nullable, length, precision, scale, updateable,
                insertable);
    }

    /**
     * Returns the precision of this numeric column.
     *
     * @see ColumnMetadata#hasPrecisionAndScale()
     */
    public int getPrecision() {
        return precision;
    }

    /**
     * Returns the scale of this numeric column
     *
     * @see ColumnMetadata#hasPrecisionAndScale()
     */
    public int getScale() {
        return scale;
    }

    public boolean hasPrecisionAndScale() {
        return scale != UNDEFINED && precision != UNDEFINED;
    }

    /**
     * Returns a new column with the given precision and scale constraint. Both
     * must be > 0.
     */
    public ColumnMetadata withPrecisionAndScale(int precision, int scale) {
        return new ColumnMetadata(name, jdbcType, nullable, length, precision, scale, updateable,
                insertable);
    }

    public boolean isUpdateable() {
        return updateable;
    }

    /**
     * Returns a new column with a no-update constraint.
     */
    public ColumnMetadata nonUpdateable() {
        return new ColumnMetadata(name, jdbcType, nullable, length, precision, scale, false,
                insertable);
    }

    public boolean isInsertable() {
        return insertable;
    }

    /**
     * Returns a new column with a no-insert constraint.
     */
    public ColumnMetadata nonInsertable() {
        return new ColumnMetadata(name, jdbcType, nullable, length, precision, scale, updateable,
                false);
    }
}
