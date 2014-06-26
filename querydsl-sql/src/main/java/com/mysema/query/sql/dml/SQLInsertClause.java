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

import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinType;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.InsertClause;
import com.mysema.query.sql.*;
import com.mysema.query.sql.types.Null;
import com.mysema.query.types.*;
import com.mysema.util.ResultSetAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SQLInsertClause defines an INSERT INTO clause
 *
 * @author tiwe
 *
 */
public class SQLInsertClause extends AbstractSQLClause<SQLInsertClause> implements
        InsertClause<SQLInsertClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLInsertClause.class);

    private final Connection connection;

    private final RelationalPath<?> entity;

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    @Nullable
    private SubQueryExpression<?> subQuery;

    @Nullable
    private AbstractSQLSubQuery<?> subQueryBuilder;

    private final List<SQLInsertBatch> batches = new ArrayList<SQLInsertBatch>();

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final List<Expression<?>> values = new ArrayList<Expression<?>>();

    private transient String queryString;

    private transient List<Object> constants;

    public SQLInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public SQLInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity,
            AbstractSQLSubQuery<?> subQuery) {
        this(connection, new Configuration(templates), entity);
        this.subQueryBuilder = subQuery;
    }

    public SQLInsertClause(Connection connection, Configuration configuration,
            RelationalPath<?> entity, AbstractSQLSubQuery<?> subQuery) {
        this(connection, configuration, entity);
        this.subQueryBuilder = subQuery;
    }

    public SQLInsertClause(Connection connection, Configuration configuration,
            RelationalPath<?> entity) {
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
    public SQLInsertClause addFlag(Position position, String flag) {
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
    public SQLInsertClause addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    /**
     * Add the current state of bindings as a batch item
     *
     * @return
     */
    public SQLInsertClause addBatch() {
        assertNoTemplateExpressionsInBatch();
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.list(values.toArray(new Expression[values.size()]));
            values.clear();
        }
        batches.add(new SQLInsertBatch(columns, values, subQuery));
        columns.clear();
        values.clear();
        subQuery = null;
        return this;
    }

    @Override
    protected void assertNoTemplateExpressionsInBatch() {
        for (Expression<?> expr : values) {
            assertNoTemplateExpressionInBatch(expr);
        }
    }

    @Override
    public SQLInsertClause columns(Path<?>... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Execute the clause and return the generated key with the type of the
     * given path. If no rows were created, null is returned, otherwise the key
     * of the first row is returned.
     *
     * @param <T>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T executeWithKey(Path<T> path) {
        return executeWithKey((Class<T>) path.getType(), path);
    }

    /**
     * Execute the clause and return the generated key cast to the given type.
     * If no rows were created, null is returned, otherwise the key of the first
     * row is returned.
     *
     * @param <T>
     * @param type
     * @return
     */
    public <T> T executeWithKey(Class<T> type) {
        return executeWithKey(type, null);
    }

    private <T> T executeWithKey(Class<T> type, @Nullable Path<T> path) {
        ResultSet rs = executeWithKeys();
        try {
            if (rs.next()) {
                return configuration.get(rs, path, 1, type);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw configuration.translate(e);
        } finally {
            close(rs);
        }
    }

    /**
     * Execute the clause and return the generated key with the type of the
     * given path. If no rows were created, or the referenced column is not a
     * generated key, null is returned. Otherwise, the key of the first row is
     * returned.
     *
     * @param <T>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> executeWithKeys(Path<T> path) {
        return executeWithKeys((Class<T>) path.getType(), path);
    }

    public <T> List<T> executeWithKeys(Class<T> type) {
        return executeWithKeys(type, null);
    }

    private <T> List<T> executeWithKeys(Class<T> type, @Nullable Path<T> path) {
        ResultSet rs = executeWithKeys();
        try {
            List<T> rv = new ArrayList<T>();
            while (rs.next()) {
                rv.add(configuration.get(rs, path, 1, type));
            }
            return rv;
        } catch (SQLException e) {
            throw configuration.translate(e);
        } finally {
            close(rs);
        }
    }

    private PreparedStatement createStatement(boolean withKeys) throws SQLException {
        SQLSerializer serializer = createSerializer();
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.list(values.toArray(new Expression[values.size()]));
            values.clear();
        }
        PreparedStatement stmt = null;
        if (batches.isEmpty()) {
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
            stmt = prepareStatementAndSetParameters(serializer, withKeys);
        } else {
            serializer.serializeInsert(metadata, entity, batches.get(0).getColumns(), batches
                    .get(0).getValues(), batches.get(0).getSubQuery());
            stmt = prepareStatementAndSetParameters(serializer, withKeys);

            // add first batch
            stmt.addBatch();

            // add other batches
            for (int i = 1; i < batches.size(); i++) {
                SQLInsertBatch batch = batches.get(i);
                serializer = createSerializer();
                serializer.serializeInsert(metadata, entity, batch.getColumns(),
                        batch.getValues(), batch.getSubQuery());
                setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(),
                        metadata.getParams());
                stmt.addBatch();
            }
        }
        return stmt;
    }

    private PreparedStatement prepareStatementAndSetParameters(SQLSerializer serializer,
            boolean withKeys) throws SQLException {
        queryString = serializer.toString();
        constants = serializer.getConstants();
        logger.debug(queryString);
        PreparedStatement stmt;
        if (withKeys) {
            if (entity.getPrimaryKey() != null) {
                String[] target = new String[entity.getPrimaryKey().getLocalColumns().size()];
                for (int i = 0; i < target.length; i++) {
                    Path<?> path = entity.getPrimaryKey().getLocalColumns().get(i);
                    String column = ColumnMetadata.getName(path);
                    target[i] = column;
                }
                stmt = connection.prepareStatement(queryString, target);
            } else {
                stmt = connection.prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
            }
        } else {
            stmt = connection.prepareStatement(queryString);
        }
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(),
                metadata.getParams());
        return stmt;
    }

    /**
     * Execute the clause and return the generated keys as a ResultSet
     *
     * @return
     */
    public ResultSet executeWithKeys() {
        try {
            final PreparedStatement stmt = createStatement(true);
            if (batches.isEmpty()) {
                listeners.notifyInsert(entity, metadata, columns, values, subQuery);
                stmt.executeUpdate();
            } else {
                listeners.notifyInserts(entity, metadata, batches);
                stmt.executeBatch();
            }
            ResultSet rs = stmt.getGeneratedKeys();
            return new ResultSetAdapter(rs) {
                @Override
                public void close() throws SQLException {
                    try {
                        super.close();
                    } finally {
                        stmt.close();
                    }
                }
            };
        } catch (SQLException e) {
            throw configuration.translate(queryString, constants, e);
        }
    }

    @Override
    public long execute() {
        PreparedStatement stmt = null;
        try {
            stmt = createStatement(false);
            if (batches.isEmpty()) {
                listeners.notifyInsert(entity, metadata, columns, values, subQuery);
                return stmt.executeUpdate();
            } else {
                listeners.notifyInserts(entity, metadata, batches);
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
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
            return ImmutableList.of(createBindings(metadata, serializer));
        } else {
            ImmutableList.Builder<SQLBindings> builder = ImmutableList.builder();
            for (SQLInsertBatch batch : batches) {
                SQLSerializer serializer = createSerializer();
                serializer.serializeInsert(metadata, entity, batch.getColumns(), batch.getValues(), batch.getSubQuery());
                builder.add(createBindings(metadata, serializer));
            }
            return builder.build();
        }
    }

    @Override
    public SQLInsertClause select(SubQueryExpression<?> sq) {
        subQuery = sq;
        for (Map.Entry<ParamExpression<?>, Object> entry : sq.getMetadata().getParams().entrySet()) {
            metadata.setParam((ParamExpression) entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public <T> SQLInsertClause set(Path<T> path, T value) {
        columns.add(path);
        if (value instanceof Expression<?>) {
            values.add((Expression<?>) value);
        } else if (value != null) {
            values.add(ConstantImpl.create(value));
        } else {
            values.add(Null.CONSTANT);
        }
        return this;
    }

    @Override
    public <T> SQLInsertClause set(Path<T> path, Expression<? extends T> expression) {
        columns.add(path);
        values.add(expression);
        return this;
    }

    @Override
    public <T> SQLInsertClause setNull(Path<T> path) {
        columns.add(path);
        values.add(Null.CONSTANT);
        return this;
    }

    @Override
    public SQLInsertClause values(Object... v) {
        for (Object value : v) {
            if (value instanceof Expression<?>) {
                values.add((Expression<?>) value);
            } else if (value != null) {
                values.add(ConstantImpl.create(value));
            } else {
                values.add(Null.CONSTANT);
            }
        }
        return this;
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer();
        serializer.serializeInsert(metadata, entity, columns, values, subQuery);
        return serializer.toString();
    }

    /**
     * Populate the INSERT clause with the properties of the given bean. The
     * properties need to match the fields of the clause's entity instance.
     *
     * @param bean
     * @return
     */
    public SQLInsertClause populate(Object bean) {
        return populate(bean, DefaultMapper.DEFAULT);
    }

    /**
     * Populate the INSERT clause with the properties of the given bean using
     * the given Mapper.
     *
     * @param obj
     * @param mapper
     * @return
     */
    @SuppressWarnings("rawtypes")
    public <T> SQLInsertClause populate(T obj, Mapper<T> mapper) {
        Map<Path<?>, Object> values = mapper.createMap(entity, obj);
        for (Map.Entry<Path<?>, Object> entry : values.entrySet()) {
            set((Path) entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty() && batches.isEmpty();
    }

}
