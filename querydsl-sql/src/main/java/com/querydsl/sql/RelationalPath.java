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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.ProjectionRole;

/**
 * RelationalPath extends {@link EntityPath} to provide access to relational
 * metadata
 *
 * @author tiwe
 *
 */
public interface RelationalPath<T> extends EntityPath<T>, ProjectionRole<T> {

    /**
     * Get the schema and table name
     *
     * @return
     */
    SchemaAndTable getSchemaAndTable();

    /**
     * Get the schema name
     *
     * @return
     */
    String getSchemaName();

    /**
     * Get the table name
     *
     * @return
     */
    String getTableName();

    /**
     * Get all columns
     *
     * @return
     */
    List<Path<?>> getColumns();

    /**
     * Get the primary key for this relation or null if none exists
     *
     * @return
     */
    @Nullable
    PrimaryKey<T> getPrimaryKey();

    /**
     * Get the foreign keys for this relation
     *
     * @return
     */
    Collection<ForeignKey<?>> getForeignKeys();

    /**
     * Get the inverse foreign keys for this relation
     *
     * @return
     */
    Collection<ForeignKey<?>> getInverseForeignKeys();

    /**
     * Returns the metadata for this path or null if none was assigned. See
     * {@link ColumnMetadata#getColumnMetadata(Path)} for a null safe
     * alternative
     */
    @Override
    @Nullable
    ColumnMetadata getMetadata(Path<?> column);
}