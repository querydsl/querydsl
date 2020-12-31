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

import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.ProjectionRole;

/**
 * RelationalPath extends {@link EntityPath} to provide access to relational
 * metadata
 *
 * @param <T> expression type
 *
 * @author tiwe
 */
public interface RelationalPath<T> extends EntityPath<T>, ProjectionRole<T> {

    /**
     * Get the schema and table name
     *
     * @return schema and table
     */
    SchemaAndTable getSchemaAndTable();

    /**
     * Get the schema name
     *
     * @return schema
     */
    String getSchemaName();

    /**
     * Get the table name
     *
     * @return table
     */
    String getTableName();

    /**
     * Get all columns
     *
     * @return columns
     */
    List<Path<?>> getColumns();

    /**
     * Get the primary key for this relation or null if none exists
     *
     * @return primary key
     */
    @Nullable
    PrimaryKey<T> getPrimaryKey();

    /**
     * Get the foreign keys for this relation
     *
     * @return foreign keys
     */
    Collection<ForeignKey<?>> getForeignKeys();

    /**
     * Get the inverse foreign keys for this relation
     *
     * @return inverse foreign keys
     */
    Collection<ForeignKey<?>> getInverseForeignKeys();

    /**
     * Returns the metadata for this path or null if none was assigned. See
     * {@link ColumnMetadata#getColumnMetadata(Path)} for a null safe
     * alternative
     *
     * @return column metadata for path
     */
    @Override
    @Nullable
    ColumnMetadata getMetadata(Path<?> column);
}