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

import com.querydsl.core.*;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.util.ResultSetAdapter;
import com.querydsl.sql.*;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

/**
 * {@code SQLMergeUsingClause} defines a MERGE INTO clause with USING
 *
 * @author borgiiri
 */
public class SQLMergeUsingClause extends AbstractSQLClause<SQLMergeUsingClause> {

    protected static final Logger logger = Logger.getLogger(SQLMergeUsingClause.class.getName());

    protected final RelationalPath<?> entity;

    protected final QueryMetadata metadata = new DefaultQueryMetadata();

    protected SimpleExpression<?> usingExpression;

    protected BooleanBuilder usingOn = new BooleanBuilder();
    protected List<SQLMergeUsingCase> whens = new ArrayList<SQLMergeUsingCase>();
    protected transient String queryString;

    protected transient List<Object> constants;

    public SQLMergeUsingClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public SQLMergeUsingClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public SQLMergeUsingClause(Supplier<Connection> connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public SQLMergeUsingClause(Connection connection, Configuration configuration, RelationalPath<?> entity, SimpleExpression<?> usingExpression) {
        super(configuration, connection);
        this.entity = entity;
        this.usingExpression = usingExpression;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public SQLMergeUsingClause(Supplier<Connection> connection, Configuration configuration, RelationalPath<?> entity, SimpleExpression<?> usingExpression) {
        super(configuration, connection);
        this.entity = entity;
        this.usingExpression = usingExpression;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    /**
     * Add the given String literal at the given position as a query flag
     *
     * @param position position
     * @param flag     query flag
     * @return the current object
     */
    public SQLMergeUsingClause addFlag(Position position, String flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    /**
     * Add the given Expression at the given position as a query flag
     *
     * @param position position
     * @param flag     query flag
     * @return the current object
     */
    public SQLMergeUsingClause addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    @Override
    public void clear() {
        usingExpression = null;
        whens.clear();
        usingOn = new BooleanBuilder();
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

    protected <T> T executeWithKey(Class<T> type, @Nullable Path<T> path) {
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

    protected <T> List<T> executeWithKeys(Class<T> type, @Nullable Path<T> path) {
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
            PreparedStatement stmt = null;
            stmt = createStatement(true);
            listeners.notifyMergeUsing(entity, metadata, usingExpression, usingOn.getValue(), whens);

            listeners.preExecute(context);
            stmt.executeUpdate();
            listeners.executed(context);

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
        return executeNativeMerge();
    }

    @Override
    public List<SQLBindings> getSQL() {
        SQLSerializer serializer = createSerializer();
        serializer.serializeMergeUsing(metadata, entity, usingExpression, usingOn.getValue(), whens);
        return Collections.singletonList(createBindings(metadata, serializer));
    }

    public int getBatchCount() {
        return 0;
    }

    protected void addListeners(AbstractSQLClause<?> clause) {
        for (SQLListener listener : listeners.getListeners()) {
            clause.addListener(listener);
        }
    }

    protected PreparedStatement createStatement(boolean withKeys) throws SQLException {
        boolean addBatches = !configuration.getUseLiterals();
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        PreparedStatement stmt = null;

        serializer.serializeMergeUsing(metadata, entity, usingExpression, usingOn.getValue(), whens);
        context.addSQL(createBindings(metadata, serializer));
        listeners.rendered(context);

        listeners.prePrepare(context);
        stmt = prepareStatementAndSetParameters(serializer, withKeys);
        context.addPreparedStatement(stmt);
        listeners.prepared(context);
        return stmt;
    }

    protected PreparedStatement prepareStatementAndSetParameters(
            SQLSerializer serializer,
            boolean withKeys
    ) throws SQLException {
        listeners.prePrepare(context);

        queryString = serializer.toString();
        constants = serializer.getConstants();
        logQuery(logger, queryString, constants);
        PreparedStatement stmt;
        stmt = connection().prepareStatement(queryString);
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
        context.addPreparedStatement(stmt);
        listeners.prepared(context);

        return stmt;
    }

    protected long executeNativeMerge() {
        context = startContext(connection(), metadata, entity);
        PreparedStatement stmt = null;
        try {
            stmt = createStatement(false);
            listeners.notifyMergeUsing(entity, metadata, usingExpression, usingOn.getValue(), whens);

            listeners.preExecute(context);
            int rc = stmt.executeUpdate();
            listeners.executed(context);
            return rc;
        } catch (SQLException e) {
            onException(context, e);
            throw configuration.translate(queryString, constants, e);
        } finally {
            if (stmt != null) {
                close(stmt);
            }
            reset();
            endContext(context);
        }
    }

    public SQLMergeUsingClause on(Predicate... conditions) {
        for (Predicate condition : conditions) {
            on(condition);
        }
        return this;
    }

    public SQLMergeUsingClause on(Predicate condition) {
        usingOn.and(condition);
        return this;
    }

    public SQLMergeUsingClause using(SimpleExpression<?> usingExpression) {
        this.usingExpression = usingExpression;
        return this;
    }

    public SQLMergeUsingClause addWhen(SQLMergeUsingCase mergeCase) {
        whens.add(mergeCase);
        return this;
    }

    public SQLMergeUsingCase whenMatched() {
        return new SQLMergeUsingCase(this, true);
    }

    public SQLMergeUsingCase whenNotMatched() {
        return new SQLMergeUsingCase(this, false);
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer();
        serializer.serializeMergeUsing(metadata, entity, usingExpression, usingOn.getValue(), whens);
        return serializer.toString();
    }

}
