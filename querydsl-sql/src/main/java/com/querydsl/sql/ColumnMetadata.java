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
package com.querydsl.sql;

import java.io.Serializable;

import com.google.common.base.Objects;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;

/**
 * Provides metadata like the column name, JDBC type and constraints
 */
public final class ColumnMetadata implements Serializable {

    private static final long serialVersionUID = -5678865742525938470L;

    /**
     * Returns this path's column metadata if present. Otherwise returns default
     * metadata where the column name is equal to the path's name.
     */
    public static ColumnMetadata getColumnMetadata(Path<?> path) {
        Path<?> parent = path.getMetadata().getParent();
        if (parent instanceof EntityPath) {
            Object columnMetadata = ((EntityPath<?>) parent).getMetadata(path);
            if (columnMetadata instanceof ColumnMetadata) {
                return (ColumnMetadata)columnMetadata;
            }
        }
        return ColumnMetadata.named(path.getMetadata().getName());
    }

    public static String getName(Path<?> path) {
        Path<?> parent = path.getMetadata().getParent();
        if (parent instanceof EntityPath) {
            Object columnMetadata = ((EntityPath<?>) parent).getMetadata(path);
            if (columnMetadata instanceof ColumnMetadata) {
                return ((ColumnMetadata)columnMetadata).getName();
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
        return new ColumnMetadata(null, name, null, true, UNDEFINED, UNDEFINED);
    }

    private static final int UNDEFINED = -1;

    private final String name;
    
    private final Integer index;

    private final Integer jdbcType;

    private final boolean nullable;

    private final int size;

    private final int decimalDigits;

    private ColumnMetadata(Integer index, String name, Integer jdbcType, boolean nullable, int size,
            int decimalDigits) {
        this.index = index;
        this.name = name;
        this.jdbcType = jdbcType;
        this.nullable = nullable;
        this.size = size;
        this.decimalDigits = decimalDigits;
    }

    public String getName() {
        return name;
    }
    
    public int getIndex() {
        return index;
    }
    
    public ColumnMetadata withIndex(int index) {
        return new ColumnMetadata(index, name, jdbcType, nullable, size, decimalDigits);
    }

    public int getJdbcType() {
        return jdbcType;
    }

    public boolean hasJdbcType() {
        return jdbcType != null;
    }

    public ColumnMetadata ofType(int jdbcType) {
        return new ColumnMetadata(index, name, jdbcType, nullable, size, decimalDigits);
    }

    public boolean isNullable() {
        return nullable;
    }

    public ColumnMetadata notNull() {
        return new ColumnMetadata(index, name, jdbcType, false, size, decimalDigits);
    }

    /**
     * For char or date types this is the maximum number of characters, for numeric or decimal types this is precision.
     *
     * @return
     */
    public int getSize() {
        return size;
    }

    public boolean hasSize() {
        return size != UNDEFINED;
    }

    public ColumnMetadata withSize(int size) {
        return new ColumnMetadata(index, name, jdbcType, nullable, size, decimalDigits);
    }

    /**
     * the number of fractional digits
     *
     * @return
     */
    public int getDigits() {
        return decimalDigits;
    }

    public boolean hasDigits() {
        return decimalDigits != UNDEFINED;
    }

    public ColumnMetadata withDigits(int decimalDigits) {
        return new ColumnMetadata(index, name, jdbcType, nullable, size, decimalDigits);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof ColumnMetadata) {
            ColumnMetadata md = (ColumnMetadata)o;
            return name.equals(md.name)
                && Objects.equal(jdbcType, md.jdbcType)
                && nullable == md.nullable
                && size == md.size
                && decimalDigits == md.decimalDigits;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


}
