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
package com.mysema.query.sql.dml;

import javax.annotation.Nonnegative;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import com.google.common.collect.ImmutableList;
import com.mysema.commons.lang.Pair;
import com.mysema.query.*;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.dml.UpdateClause;
import com.mysema.query.sql.*;
import com.mysema.query.sql.types.Null;
import com.mysema.query.types.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLUpdateClause defines a UPDATE clause
 *
 * @author tiwe
 *
 */
public class SQLUpdateClause extends AbstractSQLClause<SQLUpdateClause> implements UpdateClause<SQLUpdateClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLInsertClause.class);

    private final Connection connection;

    private final RelationalPath<?> entity;

    private final List<SQLUpdateBatch> batches = new ArrayList<SQLUpdateBatch>();

    private List<Pair<Path<?>,Expression<?>>> updates = new ArrayList<Pair<Path<?>,Expression<?>>>();

    private QueryMetadata metadata = new DefaultQueryMetadata();

    private transient String queryString;

    private transient List<Object> constants;

    public SQLUpdateClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public SQLUpdateClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration);
        this.connection = connection;
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    /**
     * Add the given String literal at the given position as a query flag
     *
     * @param position
     * @param flag
     * @return
     */
    public SQLUpdateClause addFlag(Position position, String flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    /**
     * Add the given Expression at the given position as a query flag
     *
     * @param position
     * @param flag
     * @return
     */
    public SQLUpdateClause addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    /**
     * Add the current state of bindings as a batch item
     *
     * @return
     */
    public SQLUpdateClause addBatch() {
        assertNoTemplateExpressionsInBatch();
        batches.add(new SQLUpdateBatch(metadata, updates));
        updates = new ArrayList<Pair<Path<?>,Expression<?>>>();
        metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, entity);
        return this;
    }

    protected void assertNoTemplateExpressionsInBatch() {
        for (Pair<Path<?>, Expression<?>> pair : updates) {
            assertNoTemplateExpressionInBatch(pair.getSecond());
        }
        assertNoTemplateExpressionInBatch(metadata.getWhere());
    }

    private PreparedStatement createStatement() throws SQLException{
        PreparedStatement stmt;
        if (batches.isEmpty()) {
            SQLSerializer serializer = createSerializer();
            serializer.serializeUpdate(metadata, entity, updates);
            queryString = serializer.toString();
            constants = serializer.getConstants();
            logger.debug(queryString);
            stmt = connection.prepareStatement(queryString);
            setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
        } else {
            SQLSerializer serializer = createSerializer();
            serializer.serializeUpdate(batches.get(0).getMetadata(), entity, batches.get(0).getUpdates());
            queryString = serializer.toString();
            constants = serializer.getConstants();
            logger.debug(queryString);

            // add first batch
            stmt = connection.prepareStatement(queryString);
            setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
            stmt.addBatch();

            // add other batches
            for (int i = 1; i < batches.size(); i++) {
                serializer = createSerializer();
                serializer.serializeUpdate(batches.get(i).getMetadata(), entity, batches.get(i).getUpdates());
                setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
                stmt.addBatch();
            }
        }
        return stmt;
    }

    @Override
    public long execute() {
        PreparedStatement stmt = null;
        try {
            stmt = createStatement();
            if (batches.isEmpty()) {
                listeners.notifyUpdate(entity, metadata, updates);
                return stmt.executeUpdate();
            } else {
                listeners.notifyUpdates(entity, batches);
                return executeBatch(stmt);
            }
        } catch (SQLException e) {
            throw configuration.translate(queryString, constants, e);
        } finally {
            if (stmt != null) {
                close(stmt);
            }
        }
    }

    @Override
    public List<SQLBindings> getSQL() {
        if (batches.isEmpty()) {
            SQLSerializer serializer = createSerializer();
            serializer.serializeUpdate(metadata, entity, updates);
            return ImmutableList.of(createBindings(metadata, serializer));
        } else {
            ImmutableList.Builder<SQLBindings> builder = ImmutableList.builder();
            for (SQLUpdateBatch batch : batches) {
                SQLSerializer serializer = createSerializer();
                serializer.serializeUpdate(batch.getMetadata(), entity, batch.getUpdates());
                builder.add(createBindings(metadata, serializer));
            }
            return builder.build();
        }
    }

    @Override
    public <T> SQLUpdateClause set(Path<T> path, T value) {
        if (value instanceof Expression<?>) {
            updates.add(Pair.<Path<?>,Expression<?>>of(path, (Expression<?>)value));
        } else if (value != null) {
            updates.add(Pair.<Path<?>,Expression<?>>of(path, ConstantImpl.create(value)));
        } else {
            setNull(path);
        }
        return this;
    }

    @Override
    public <T> SQLUpdateClause set(Path<T> path, Expression<? extends T> expression) {
        if (expression != null) {
            updates.add(Pair.<Path<?>,Expression<?>>of(path, expression));
        } else {
            setNull(path);
        }
        return this;
    }

    @Override
    public <T> SQLUpdateClause setNull(Path<T> path) {
        updates.add(Pair.<Path<?>,Expression<?>>of(path, Null.CONSTANT));
        return this;
    }

    @Override
    public SQLUpdateClause set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++) {
            if (values.get(i) instanceof Expression) {
                updates.add(Pair.<Path<?>,Expression<?>>of(paths.get(i), (Expression<?>)values.get(i)));
            } else if (values.get(i) != null) {
                updates.add(Pair.<Path<?>,Expression<?>>of(paths.get(i), ConstantImpl.create(values.get(i))));
            } else {
                updates.add(Pair.<Path<?>,Expression<?>>of(paths.get(i), Null.CONSTANT));
            }
        }
        return this;
    }

    public SQLUpdateClause where(Predicate p) {
        metadata.addWhere(p);
        return this;
    }

    @Override
    public SQLUpdateClause where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);
        }
        return this;
    }

    public SQLUpdateClause limit(@Nonnegative long limit) {
        metadata.setModifiers(QueryModifiers.limit(limit));
        return this;
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer();
        serializer.serializeUpdate(metadata, entity, updates);
        return serializer.toString();
    }

    /**
     * Populate the UPDATE clause with the properties of the given bean.
     * The properties need to match the fields of the clause's entity instance.
     * Primary key columns are skipped in the population.
     *
     * @param bean
     * @return
     */
    @SuppressWarnings("unchecked")
    public SQLUpdateClause populate(Object bean) {
        return populate(bean, DefaultMapper.DEFAULT);
    }

    /**
     * Populate the UPDATE clause with the properties of the given bean using the given Mapper.
     *
     * @param obj
     * @param mapper
     * @return
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> SQLUpdateClause populate(T obj, Mapper<T> mapper) {
        Collection<? extends Path<?>> primaryKeyColumns = entity.getPrimaryKey() != null
                ? entity.getPrimaryKey().getLocalColumns()
                : Collections.<Path<?>>emptyList();
        Map<Path<?>, Object> values = mapper.createMap(entity, obj);
        for (Map.Entry<Path<?>, Object> entry : values.entrySet()) {
            if (!primaryKeyColumns.contains(entry.getKey())) {
                set((Path)entry.getKey(), entry.getValue());
            }
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return updates.isEmpty() && batches.isEmpty();
    }
}
