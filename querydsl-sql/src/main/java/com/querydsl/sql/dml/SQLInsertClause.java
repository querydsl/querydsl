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
package com.querydsl.sql.dml;

import java.sql.*;
import java.util.*;

import javax.annotation.Nullable;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.dml.InsertClause;
import com.querydsl.core.types.*;
import com.querydsl.core.util.ResultSetAdapter;
import com.querydsl.sql.*;
import com.querydsl.sql.types.Null;

/**
 * SQLInsertClause defines an INSERT INTO clause
 *
 * @author tiwe
 *
 */
public class SQLInsertClause extends AbstractSQLClause<SQLInsertClause> implements
        InsertClause<SQLInsertClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLInsertClause.class);

    private final RelationalPath<?> entity;

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    @Nullable
    private SubQueryExpression<?> subQuery;

    @Nullable
    private SQLQuery<?> subQueryBuilder;

    private final List<SQLInsertBatch> batches = new ArrayList<SQLInsertBatch>();

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final List<Expression<?>> values = new ArrayList<Expression<?>>();

    private transient String queryString;

    private transient List<Object> constants;

    private transient boolean batchToBulk;

    public SQLInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public SQLInsertClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity,
            SQLQuery<?> subQuery) {
        this(connection, new Configuration(templates), entity);
        this.subQueryBuilder = subQuery;
    }

    public SQLInsertClause(Connection connection, Configuration configuration,
            RelationalPath<?> entity, SQLQuery<?> subQuery) {
        this(connection, configuration, entity);
        this.subQueryBuilder = subQuery;
    }

    public SQLInsertClause(Connection connection, Configuration configuration,
            RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public SQLInsertClause(Provider<Connection> connection, Configuration configuration,
                           RelationalPath<?> entity, SQLQuery<?> subQuery) {
        this(connection, configuration, entity);
        this.subQueryBuilder = subQuery;
    }

    public SQLInsertClause(Provider<Connection> connection, Configuration configuration,
                           RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    /**
     * Add the given String literal at the given position as a query flag
     *
     * @param position position
     * @param flag query flag
     * @return the current object
     */
    public SQLInsertClause addFlag(Position position, String flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    /**
     * Add the given Expression at the given position as a query flag
     *
     * @param position position
     * @param flag query flag
     * @return the current object
     */
    public SQLInsertClause addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    /**
     * Add the current state of bindings as a batch item
     *
     * @return the current object
     */
    public SQLInsertClause addBatch() {
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.select(values.toArray(new Expression[values.size()])).clone();
            values.clear();
        }
        batches.add(new SQLInsertBatch(columns, values, subQuery));
        columns.clear();
        values.clear();
        subQuery = null;
        return this;
    }

    /**
     * Set whether batches should be optimized into a single bulk operation.
     * Will revert to batches, if bulk is not supported
     */
    public void setBatchToBulk(boolean b) {
        this.batchToBulk = b && configuration.getTemplates().isBatchToBulkSupported();
    }

    @Override
    public void clear() {
        batches.clear();
        columns.clear();
        values.clear();
        subQuery = null;
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
     * @param path path for key
     * @return generated key
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
     * @param type type of key
     * @return generated key
     */
    public <T> T executeWithKey(Class<T> type) {
        return executeWithKey(type, null);
    }

    private <T> T executeWithKey(Class<T> type, @Nullable Path<T> path) {
        ResultSet rs = null;
        try {
            rs = executeWithKeys();
            if (rs.next()) {
                return configuration.get(rs, path, 1, type);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw configuration.translate(e);
        } finally {
            if (rs != null) {
                close(rs);
            }
            reset();
        }
    }

    /**
     * Execute the clause and return the generated key with the type of the
     * given path. If no rows were created, or the referenced column is not a
     * generated key, null is returned. Otherwise, the key of the first row is
     * returned.
     *
     * @param <T>
     * @param path path for key
     * @return generated keys
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> executeWithKeys(Path<T> path) {
        return executeWithKeys((Class<T>) path.getType(), path);
    }

    public <T> List<T> executeWithKeys(Class<T> type) {
        return executeWithKeys(type, null);
    }

    private <T> List<T> executeWithKeys(Class<T> type, @Nullable Path<T> path) {
        ResultSet rs = null;
        try {
            rs = executeWithKeys();
            List<T> rv = new ArrayList<T>();
            while (rs.next()) {
                rv.add(configuration.get(rs, path, 1, type));
            }
            return rv;
        } catch (SQLException e) {
            throw configuration.translate(e);
        } finally {
            if (rs != null) {
                close(rs);
            }
            reset();
        }
    }

    private PreparedStatement createStatement(boolean withKeys) throws SQLException {
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.select(values.toArray(new Expression[values.size()])).clone();
            values.clear();
        }

        if (!batches.isEmpty() && batchToBulk) {
            serializer.serializeInsert(metadata, entity, batches);
        } else {
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
        }
        context.addSQL(serializer.toString());
        listeners.rendered(context);
        return prepareStatementAndSetParameters(serializer, withKeys);
    }

    private Collection<PreparedStatement> createStatements(boolean withKeys) throws SQLException {
        boolean addBatches = !configuration.getUseLiterals();
        listeners.preRender(context);

        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.select(values.toArray(new Expression[values.size()])).clone();
            values.clear();
        }

        Map<String, PreparedStatement> stmts = Maps.newHashMap();

        // add first batch
        SQLSerializer serializer = createSerializer();
        serializer.serializeInsert(metadata, entity, batches.get(0).getColumns(), batches
                .get(0).getValues(), batches.get(0).getSubQuery());
        PreparedStatement stmt = prepareStatementAndSetParameters(serializer, withKeys);
        if (addBatches) {
            stmt.addBatch();
        }
        stmts.put(serializer.toString(), stmt);
        context.addSQL(serializer.toString());
        listeners.rendered(context);

        // add other batches
        for (int i = 1; i < batches.size(); i++) {
            SQLInsertBatch batch = batches.get(i);

            listeners.preRender(context);
            serializer = createSerializer();
            serializer.serializeInsert(metadata, entity, batch.getColumns(),
                    batch.getValues(), batch.getSubQuery());
            context.addSQL(serializer.toString());
            listeners.rendered(context);

            stmt = stmts.get(serializer.toString());
            if (stmt == null) {
                stmt = prepareStatementAndSetParameters(serializer, withKeys);
                stmts.put(serializer.toString(), stmt);
            } else {
                setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(),
                        metadata.getParams());
            }
            if (addBatches) {
                stmt.addBatch();
            }
        }

        return stmts.values();
    }

    private PreparedStatement prepareStatementAndSetParameters(SQLSerializer serializer,
            boolean withKeys) throws SQLException {
        listeners.prePrepare(context);

        queryString = serializer.toString();
        constants = serializer.getConstants();
        logQuery(logger, queryString, constants);
        PreparedStatement stmt;
        if (withKeys) {
            if (entity.getPrimaryKey() != null) {
                String[] target = new String[entity.getPrimaryKey().getLocalColumns().size()];
                for (int i = 0; i < target.length; i++) {
                    Path<?> path = entity.getPrimaryKey().getLocalColumns().get(i);
                    String column = ColumnMetadata.getName(path);
                    target[i] = column;
                }
                stmt = connection().prepareStatement(queryString, target);
            } else {
                stmt = connection().prepareStatement(queryString, Statement.RETURN_GENERATED_KEYS);
            }
        } else {
            stmt = connection().prepareStatement(queryString);
        }
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(),
                metadata.getParams());

        context.addPreparedStatement(stmt);
        listeners.prepared(context);
        return stmt;
    }

    /**
     * Execute the clause and return the generated keys as a ResultSet
     *
     * @return result set with generated keys
     */
    public ResultSet executeWithKeys() {
        context = startContext(connection(), metadata, entity);
        try {
            PreparedStatement stmt = null;
            if (batches.isEmpty()) {
                stmt = createStatement(true);
                listeners.notifyInsert(entity, metadata, columns, values, subQuery);

                listeners.preExecute(context);
                stmt.executeUpdate();
                listeners.executed(context);
            } else if (batchToBulk) {
                stmt = createStatement(true);
                listeners.notifyInserts(entity, metadata, batches);

                listeners.preExecute(context);
                stmt.executeUpdate();
                listeners.executed(context);
            } else {
                Collection<PreparedStatement> stmts = createStatements(true);
                if (stmts != null && stmts.size() > 1) {
                    throw new IllegalStateException("executeWithKeys called with batch statement and multiple SQL strings");
                }
                stmt = stmts.iterator().next();
                listeners.notifyInserts(entity, metadata, batches);

                listeners.preExecute(context);
                stmt.executeBatch();
                listeners.executed(context);
            }

            final Statement stmt2 = stmt;
            ResultSet rs = stmt.getGeneratedKeys();
            return new ResultSetAdapter(rs) {
                @Override
                public void close() throws SQLException {
                    try {
                        super.close();
                    } finally {
                        stmt2.close();
                        reset();
                        endContext(context);
                    }
                }
            };
        } catch (SQLException e) {
            onException(context, e);
            reset();
            endContext(context);
            throw configuration.translate(queryString, constants, e);
        }
    }

    @Override
    public long execute() {
        context = startContext(connection(), metadata,entity);
        PreparedStatement stmt = null;
        Collection<PreparedStatement> stmts = null;
        try {
            if (batches.isEmpty()) {
                stmt = createStatement(false);
                listeners.notifyInsert(entity, metadata, columns, values, subQuery);

                listeners.preExecute(context);
                int rc = stmt.executeUpdate();
                listeners.executed(context);
                return rc;
            } else if (batchToBulk) {
                stmt = createStatement(false);
                listeners.notifyInserts(entity, metadata, batches);

                listeners.preExecute(context);
                int rc = stmt.executeUpdate();
                listeners.executed(context);
                return rc;
            } else {
                stmts = createStatements(false);
                listeners.notifyInserts(entity, metadata, batches);

                listeners.preExecute(context);
                long rc = executeBatch(stmts);
                listeners.executed(context);
                return rc;
            }
        } catch (SQLException e) {
            onException(context,e);
            throw configuration.translate(queryString, constants, e);
        } finally {
            if (stmt != null) {
                close(stmt);
            }
            if (stmts != null) {
                close(stmts);
            }
            reset();
            endContext(context);
        }
    }

    @Override
    public List<SQLBindings> getSQL() {
        if (batches.isEmpty()) {
            SQLSerializer serializer = createSerializer();
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
            return ImmutableList.of(createBindings(metadata, serializer));
        } else if (batchToBulk) {
            SQLSerializer serializer = createSerializer();
            serializer.serializeInsert(metadata, entity, batches);
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
        if (!batches.isEmpty() && batchToBulk) {
            serializer.serializeInsert(metadata, entity, batches);
        } else {
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
        }
        return serializer.toString();
    }

    /**
     * Populate the INSERT clause with the properties of the given bean. The
     * properties need to match the fields of the clause's entity instance.
     *
     * @param bean bean to use for population
     * @return the current object
     */
    public SQLInsertClause populate(Object bean) {
        return populate(bean, DefaultMapper.DEFAULT);
    }

    /**
     * Populate the INSERT clause with the properties of the given bean using
     * the given Mapper.
     *
     * @param obj object to use for population
     * @param mapper mapper to use
     * @return the current object
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

    @Override
    public int getBatchCount() {
        return batches.size();
    }

}
