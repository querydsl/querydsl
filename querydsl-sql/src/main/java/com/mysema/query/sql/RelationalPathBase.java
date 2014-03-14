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
package com.mysema.query.sql;

import static com.google.common.collect.ImmutableList.copyOf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.mysema.commons.lang.Pair;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.path.BeanPath;

/**
 * RelationalPathBase is a base class for {@link RelationalPath} implementations
 *
 * @author tiwe
 *
 * @param <T>
 *            entity type
 */
public class RelationalPathBase<T> extends BeanPath<T> implements RelationalPath<T> {

    private static final long serialVersionUID = -7031357250283629202L;

    @Nullable
    private PrimaryKey<T> primaryKey;

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final Map<Path<?>, ColumnMetadata> columnMetadata = Maps.newHashMap();

    private final List<ForeignKey<?>> foreignKeys = new ArrayList<ForeignKey<?>>();

    private final List<ForeignKey<?>> inverseForeignKeys = new ArrayList<ForeignKey<?>>();

    private final String schema, table;

    private final Pair<String, String> schemaAndTable;

    private transient FactoryExpression<T> projection;

    public RelationalPathBase(Class<? extends T> type, String variable, String schema, String table) {
        this(type, PathMetadataFactory.forVariable(variable), schema, table);
    }

    public RelationalPathBase(Class<? extends T> type, PathMetadata<?> metadata, String schema,
            String table) {
        super(type, metadata);
        this.schema = schema;
        this.table = table;
        this.schemaAndTable = Pair.of(schema, table);
    }

    protected PrimaryKey<T> createPrimaryKey(Path<?>... columns) {
        primaryKey = new PrimaryKey<T>(this, columns);
        return primaryKey;
    }

    protected <F> ForeignKey<F> createForeignKey(Path<?> local, String foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, local, foreign);
        foreignKeys.add(foreignKey);
        return foreignKey;
    }

    protected <F> ForeignKey<F> createForeignKey(List<? extends Path<?>> local, List<String> foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, copyOf(local), copyOf(foreign));
        foreignKeys.add(foreignKey);
        return foreignKey;
    }

    protected <F> ForeignKey<F> createInvForeignKey(Path<?> local, String foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, local, foreign);
        inverseForeignKeys.add(foreignKey);
        return foreignKey;
    }

    protected <F> ForeignKey<F> createInvForeignKey(List<? extends Path<?>> local,
            List<String> foreign) {
        ForeignKey<F> foreignKey = new ForeignKey<F>(this, copyOf(local), copyOf(foreign));
        inverseForeignKeys.add(foreignKey);
        return foreignKey;
    }

    protected <P extends Path<?>> P addMetadata(P path, ColumnMetadata metadata) {
        columnMetadata.put(path, metadata);
        return path;
    }

    @Override
    public FactoryExpression<T> getProjection() {
        if (projection == null) {
            projection = RelationalPathUtils.createProjection(this);
        }
        return projection;
    }

    public Path<?>[] all() {
        Path<?>[] all = new Path[columns.size()];
        columns.toArray(all);
        return all;
    }

    @Override
    protected <P extends Path<?>> P add(P path) {
        columns.add(path);
        return path;
    }

    @Override
    public List<Path<?>> getColumns() {
        return columns;
    }

    @Override
    public Collection<ForeignKey<?>> getForeignKeys() {
        return foreignKeys;
    }

    @Override
    public Collection<ForeignKey<?>> getInverseForeignKeys() {
        return inverseForeignKeys;
    }

    @Override
    public PrimaryKey<T> getPrimaryKey() {
        return primaryKey;
    }

    @Override
    public Pair<String, String> getSchemaAndTable() {
        return schemaAndTable;
    }

    @Override
    public String getSchemaName() {
        return schema;
    }

    @Override
    public String getTableName() {
        return table;
    }

    @Override
    public ColumnMetadata getMetadata(Path<?> column) {
        return columnMetadata.get(column);
    }

}
