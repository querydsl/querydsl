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
import com.querydsl.core.*;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.dml.StoreClause;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.util.ResultSetAdapter;
import com.querydsl.sql.*;
import com.querydsl.sql.types.Null;

/**
 * {@code SQLMergeClause} defines an MERGE INTO clause
 *
 * @author tiwe
 *
 */
public class SQLMergeClause extends AbstractSQLClause<SQLMergeClause> implements StoreClause<SQLMergeClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLMergeClause.class);

    private final List<Path<?>> columns = new ArrayList<Path<?>>();

    private final RelationalPath<?> entity;

    private final QueryMetadata metadata = new DefaultQueryMetadata();

    private final List<Path<?>> keys = new ArrayList<Path<?>>();

    @Nullable
    private SubQueryExpression<?> subQuery;

    private final List<SQLMergeBatch> batches = new ArrayList<SQLMergeBatch>();

    private final List<Expression<?>> values = new ArrayList<Expression<?>>();

    private transient String queryString;

    private transient List<Object> constants;

    public SQLMergeClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public SQLMergeClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public SQLMergeClause(Provider<Connection> connection, Configuration configuration, RelationalPath<?> entity) {
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
    public SQLMergeClause addFlag(Position position, String flag) {
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
    public SQLMergeClause addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    private List<? extends Path<?>> getKeys() {
        if (!keys.isEmpty()) {
            return keys;
        } else if (entity.getPrimaryKey() != null) {
            return entity.getPrimaryKey().getLocalColumns();
        } else {
            throw new IllegalStateException("No keys were defined, invoke keys(..) to add keys");
        }
    }

    /**
     * Add the current state of bindings as a batch item
     *
     * @return the current object
     */
    public SQLMergeClause addBatch() {
        if (!configuration.getTemplates().isNativeMerge()) {
            throw new IllegalStateException("batch only supported for databases that support native merge");
        }

        batches.add(new SQLMergeBatch(keys, columns, values, subQuery));
        columns.clear();
        values.clear();
        keys.clear();
        subQuery = null;
        return this;
    }

    @Override
    public void clear() {
        batches.clear();
        columns.clear();
        values.clear();
        keys.clear();
        subQuery = null;
    }

    public SQLMergeClause columns(Path<?>... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return this;
    }

    /**
     * Execute the clause and return the generated key with the type of the given path.
     * If no rows were created, null is returned, otherwise the key of the first row is returned.
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
     * If no rows were created, null is returned, otherwise the key of the first row is returned.
     *
     * @param <T>
     * @param type type of key
     * @return generated key
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
     * Execute the clause and return the generated key with the type of the given path.
     * If no rows were created, or the referenced column is not a generated key, null is returned.
     * Otherwise, the key of the first row is returned.
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

    /**
     * Execute the clause and return the generated keys as a ResultSet
     *
     * @return result set with generated keys
     */
    public ResultSet executeWithKeys() {
        context = startContext(connection(), metadata, entity);
        try {
            if (configuration.getTemplates().isNativeMerge()) {
                PreparedStatement stmt = null;
                if (batches.isEmpty()) {
                    stmt = createStatement(true);
                    listeners.notifyMerge(entity, metadata, keys, columns, values, subQuery);

                    listeners.preExecute(context);
                    stmt.executeUpdate();
                    listeners.executed(context);
                } else {
                    Collection<PreparedStatement> stmts = createStatements(true);
                    if (stmts != null && stmts.size() > 1) {
                        throw new IllegalStateException("executeWithKeys called with batch statement and multiple SQL strings");
                    }
                    stmt = stmts.iterator().next();
                    listeners.notifyMerges(entity, metadata, batches);

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
            } else {
                if (hasRow()) {
                    // update
                    SQLUpdateClause update = new SQLUpdateClause(connection(), configuration, entity);
                    update.addListener(listeners);
                    populate(update);
                    addKeyConditions(update);
                    reset();
                    endContext(context);
                    return EmptyResultSet.DEFAULT;
                } else {
                    // insert
                    SQLInsertClause insert = new SQLInsertClause(connection(), configuration, entity);
                    insert.addListener(listeners);
                    populate(insert);
                    return insert.executeWithKeys();
                }
            }
        } catch (SQLException e) {
            onException(context,e);
            reset();
            endContext(context);
            throw configuration.translate(queryString, constants, e);
        }
    }

    @Override
    public long execute() {
        if (configuration.getTemplates().isNativeMerge()) {
            return executeNativeMerge();
        } else {
            return executeCompositeMerge();
        }
    }

    @Override
    public List<SQLBindings> getSQL() {
        if (batches.isEmpty()) {
            SQLSerializer serializer = createSerializer();
            serializer.serializeMerge(metadata, entity, keys, columns, values, subQuery);
            return ImmutableList.of(createBindings(metadata, serializer));
        } else {
            ImmutableList.Builder<SQLBindings> builder = ImmutableList.builder();
            for (SQLMergeBatch batch : batches) {
                SQLSerializer serializer = createSerializer();
                serializer.serializeMerge(metadata, entity, batch.getKeys(), batch.getColumns(), batch.getValues(), batch.getSubQuery());
                builder.add(createBindings(metadata, serializer));
            }
            return builder.build();
        }
    }

    private boolean hasRow() {
        SQLQuery<?> query = new SQLQuery<Void>(connection(), configuration).from(entity);
        for (SQLListener listener : listeners.getListeners()) {
            query.addListener(listener);
        }
        query.addListener(SQLNoCloseListener.DEFAULT);
        addKeyConditions(query);
        return query.select(Expressions.ONE).fetchFirst() != null;
    }

    @SuppressWarnings("unchecked")
    private void addKeyConditions(FilteredClause<?> query) {
        List<? extends Path<?>> keys = getKeys();
        for (int i = 0; i < columns.size(); i++) {
            if (keys.contains(columns.get(i))) {
                if (values.get(i) instanceof NullExpression) {
                    query.where(ExpressionUtils.isNull(columns.get(i)));
                } else {
                    query.where(ExpressionUtils.eq(columns.get(i),(Expression) values.get(i)));
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private long executeCompositeMerge() {
        if (hasRow()) {
            // update
            SQLUpdateClause update = new SQLUpdateClause(connection(), configuration, entity);
            populate(update);
            addListeners(update);
            addKeyConditions(update);
            return update.execute();
        } else {
            // insert
            SQLInsertClause insert = new SQLInsertClause(connection(), configuration, entity);
            addListeners(insert);
            populate(insert);
            return insert.execute();

        }
    }

    private void addListeners(AbstractSQLClause<?> clause) {
        for (SQLListener listener : listeners.getListeners()) {
            clause.addListener(listener);
        }
    }

    @SuppressWarnings("unchecked")
    private void populate(StoreClause<?> clause) {
        for (int i = 0; i < columns.size(); i++) {
            clause.set((Path) columns.get(i), (Object) values.get(i));
        }
    }

    private PreparedStatement createStatement(boolean withKeys) throws SQLException {
        boolean addBatches = !configuration.getUseLiterals();
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        PreparedStatement stmt = null;
        if (batches.isEmpty()) {
            serializer.serializeMerge(metadata, entity, keys, columns, values, subQuery);
            context.addSQL(serializer.toString());
            listeners.rendered(context);

            listeners.prePrepare(context);
            stmt = prepareStatementAndSetParameters(serializer, withKeys);
            context.addPreparedStatement(stmt);
            listeners.prepared(context);
        } else {
            serializer.serializeMerge(metadata, entity,
                    batches.get(0).getKeys(), batches.get(0).getColumns(),
                    batches.get(0).getValues(), batches.get(0).getSubQuery());
            context.addSQL(serializer.toString());
            listeners.rendered(context);

            stmt = prepareStatementAndSetParameters(serializer, withKeys);

            // add first batch
            if (addBatches) {
                stmt.addBatch();
            }

            // add other batches
            for (int i = 1; i < batches.size(); i++) {
                SQLMergeBatch batch = batches.get(i);
                listeners.preRender(context);
                serializer = createSerializer();
                serializer.serializeMerge(metadata, entity, batch.getKeys(), batch.getColumns(), batch.getValues(), batch.getSubQuery());
                context.addSQL(serializer.toString());
                listeners.rendered(context);

                setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
                if (addBatches) {
                    stmt.addBatch();
                }
            }
        }
        return stmt;
    }

    private Collection<PreparedStatement> createStatements(boolean withKeys) throws SQLException {
        boolean addBatches = !configuration.getUseLiterals();
        Map<String, PreparedStatement> stmts = Maps.newHashMap();

        // add first batch
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        serializer.serializeMerge(metadata, entity,
                batches.get(0).getKeys(), batches.get(0).getColumns(),
                batches.get(0).getValues(), batches.get(0).getSubQuery());
        context.addSQL(serializer.toString());
        listeners.rendered(context);

        PreparedStatement stmt = prepareStatementAndSetParameters(serializer, withKeys);
        stmts.put(serializer.toString(), stmt);
        if (addBatches) {
            stmt.addBatch();
        }

        // add other batches
        for (int i = 1; i < batches.size(); i++) {
            SQLMergeBatch batch = batches.get(i);
            serializer = createSerializer();
            serializer.serializeMerge(metadata, entity,
                    batch.getKeys(), batch.getColumns(), batch.getValues(), batch.getSubQuery());
            stmt = stmts.get(serializer.toString());
            if (stmt == null) {
                stmt = prepareStatementAndSetParameters(serializer, withKeys);
                stmts.put(serializer.toString(), stmt);
            } else {
                setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
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
            String[] target = new String[keys.size()];
            for (int i = 0; i < target.length; i++) {
                target[i] = ColumnMetadata.getName(getKeys().get(i));
            }
            stmt = connection().prepareStatement(queryString, target);
        } else {
            stmt = connection().prepareStatement(queryString);
        }
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
        context.addPreparedStatement(stmt);
        listeners.prepared(context);

        return stmt;
    }

    private long executeNativeMerge() {
        context = startContext(connection(), metadata, entity);
        PreparedStatement stmt = null;
        Collection<PreparedStatement> stmts = null;
        try {
            if (batches.isEmpty()) {
                stmt = createStatement(false);
                listeners.notifyMerge(entity, metadata, keys, columns, values, subQuery);

                listeners.preExecute(context);
                int rc = stmt.executeUpdate();
                listeners.executed(context);
                return rc;
            } else {
                stmts = createStatements(false);
                listeners.notifyMerges(entity, metadata, batches);

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

    /**
     * Set the keys to be used in the MERGE clause
     *
     * @param paths keys
     * @return the current object
     */
    public SQLMergeClause keys(Path<?>... paths) {
        keys.addAll(Arrays.asList(paths));
        return this;
    }

    public SQLMergeClause select(SubQueryExpression<?> subQuery) {
        this.subQuery = subQuery;
        return this;
    }

    @Override
    public <T> SQLMergeClause set(Path<T> path, @Nullable T value) {
        columns.add(path);
        if (value != null) {
            values.add(ConstantImpl.create(value));
        } else {
            values.add(Null.CONSTANT);
        }
        return this;
    }

    @Override
    public <T> SQLMergeClause set(Path<T> path, Expression<? extends T> expression) {
        columns.add(path);
        values.add(expression);
        return this;
    }

    @Override
    public <T> SQLMergeClause setNull(Path<T> path) {
        columns.add(path);
        values.add(Null.CONSTANT);
        return this;
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer();
        serializer.serializeMerge(metadata, entity, keys, columns, values, subQuery);
        return serializer.toString();
    }

    public SQLMergeClause values(Object... v) {
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
    public boolean isEmpty() {
        return values.isEmpty() && batches.isEmpty();
    }

    @Override
    public int getBatchCount() {
        return batches.size();
    }

}
