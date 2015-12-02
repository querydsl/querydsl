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

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.common.collect.ImmutableList;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.core.util.ResultSetAdapter;

/**
 * {@code AbstractSQLQuery} is the base type for SQL query implementations
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 *
 * @author tiwe
 */
public abstract class AbstractSQLQuery<T, Q extends AbstractSQLQuery<T, Q>> extends ProjectableSQLQuery<T, Q> {

    protected static final String PARENT_CONTEXT = AbstractSQLQuery.class.getName() + "#PARENT_CONTEXT";

    private static final Logger logger = LoggerFactory.getLogger(AbstractSQLQuery.class);

    private static final QueryFlag rowCountFlag = new QueryFlag(QueryFlag.Position.AFTER_PROJECTION, ", count(*) over() ");

    @Nullable
    private Provider<Connection> connProvider;

    @Nullable
    private Connection conn;

    protected SQLListeners listeners;

    protected boolean useLiterals;

    private boolean getLastCell;

    private Object lastCell;

    private SQLListenerContext parentContext;

    private StatementOptions statementOptions = StatementOptions.DEFAULT;

    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public AbstractSQLQuery(@Nullable Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata, false), configuration);
        this.conn = conn;
        this.listeners = new SQLListeners(configuration.getListeners());
        this.useLiterals = configuration.getUseLiterals();
    }

    public AbstractSQLQuery(Provider<Connection> connProvider, Configuration configuration) {
        this(connProvider, configuration, new DefaultQueryMetadata());
    }

    public AbstractSQLQuery(Provider<Connection> connProvider, Configuration configuration, QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata, false), configuration);
        this.connProvider = connProvider;
        this.listeners = new SQLListeners(configuration.getListeners());
        this.useLiterals = configuration.getUseLiterals();
    }

    /**
     * Create an alias for the expression
     *
     * @param alias alias
     * @return this as alias
     */
    public SimpleExpression<T> as(String alias) {
        return Expressions.as(this, alias);
    }

    /**
     * Create an alias for the expression
     *
     * @param alias alias
     * @return this as alias
     */
    @SuppressWarnings("unchecked")
    public SimpleExpression<T> as(Path<?> alias) {
        return Expressions.as(this, (Path) alias);
    }

    /**
     * Add a listener
     *
     * @param listener listener to add
     */
    public void addListener(SQLListener listener) {
        listeners.add(listener);
    }

    @Override
    public long fetchCount() {
        try {
            return unsafeCount();
        } catch (SQLException e) {
            String error = "Caught " + e.getClass().getName();
            logger.error(error, e);
            throw configuration.translate(e);
        }
    }

    /**
     * If you use forUpdate() with a backend that uses page or row locks, rows examined by the
     * query are write-locked until the end of the current transaction.
     *
     * Not supported for SQLite and CUBRID
     *
     * @return the current object
     */
    public Q forUpdate() {
        QueryFlag forUpdateFlag = configuration.getTemplates().getForUpdateFlag();
        return addFlag(forUpdateFlag);
    }

    /**
     * FOR SHARE causes the rows retrieved by the SELECT statement to be locked as though for update.
     *
     * Supported by MySQL, PostgreSQL, SQLServer.
     *
     * @return the current object
     *
     * @throws QueryException
     *          if the FOR SHARE is not supported.
     */
    public Q forShare() {
        return forShare(false);
    }

    /**
     * FOR SHARE causes the rows retrieved by the SELECT statement to be locked as though for update.
     *
     * Supported by MySQL, PostgreSQL, SQLServer.
     *
     * @param fallbackToForUpdate
     *          if the FOR SHARE is not supported and this parameter is <code>true</code>, the
     *          {@link #forUpdate()} functionality will be used.
     *
     * @return the current object
     *
     * @throws QueryException
     *          if the FOR SHARE is not supported and <i>fallbackToForUpdate</i> is set to
     *          <code>false</code>.
     */
    public Q forShare(boolean fallbackToForUpdate) {
        SQLTemplates sqlTemplates = configuration.getTemplates();

        if (sqlTemplates.isForShareSupported()) {
            QueryFlag forShareFlag = sqlTemplates.getForShareFlag();
            return addFlag(forShareFlag);
        }

        if (fallbackToForUpdate) {
            return forUpdate();
        }

        throw new QueryException("Using forShare() is not supported");
    }

    @Override
    protected SQLSerializer createSerializer() {
        SQLSerializer serializer = new SQLSerializer(configuration);
        serializer.setUseLiterals(useLiterals);
        return serializer;
    }

    @Nullable
    private <U> U get(ResultSet rs, Expression<?> expr, int i, Class<U> type) throws SQLException {
        return configuration.get(rs, expr instanceof Path ? (Path<?>) expr : null, i, type);
    }

    private void set(PreparedStatement stmt, Path<?> path, int i, Object value) throws SQLException {
        configuration.set(stmt, path, i, value);
    }

    /**
     * Called to create and start a new SQL Listener context
     *
     * @param connection the database connection
     * @param metadata   the meta data for that context
     * @return the newly started context
     */
    protected SQLListenerContextImpl startContext(Connection connection, QueryMetadata metadata) {
        SQLListenerContextImpl context = new SQLListenerContextImpl(metadata, connection);
        if (parentContext != null) {
            context.setData(PARENT_CONTEXT, parentContext);
        }
        listeners.start(context);
        return context;
    }

    /**
     * Called to make the call back to listeners when an exception happens
     *
     * @param context the current context in play
     * @param e       the exception
     */
    protected void onException(SQLListenerContextImpl context, Exception e) {
        context.setException(e);
        listeners.exception(context);
    }

    /**
     * Called to end a SQL listener context
     *
     * @param context the listener context to end
     */
    protected void endContext(SQLListenerContext context) {
        listeners.end(context);
    }

    /**
     * Get the results as a JDBC ResultSet
     *
     * @param exprs the expression arguments to retrieve
     * @return results as ResultSet
     * @deprecated Use @{code select(..)} to define the projection and {@code getResults()} to obtain
     *             the result set
     */
    @Deprecated
    public ResultSet getResults(Expression<?>... exprs) {
        if (exprs.length > 0) {
            queryMixin.setProjection(exprs);
        }
        return getResults();
    }

    /**
     * Get the results as a JDBC ResultSet
     *
     * @return results as ResultSet
     */
    public ResultSet getResults() {
        final SQLListenerContextImpl context = startContext(connection(), queryMixin.getMetadata());
        String queryString = null;
        List<Object> constants = ImmutableList.of();

        try {
            listeners.preRender(context);
            SQLSerializer serializer = serialize(false);
            queryString = serializer.toString();
            logQuery(queryString, serializer.getConstants());
            context.addSQL(queryString);
            listeners.rendered(context);

            listeners.notifyQuery(queryMixin.getMetadata());

            constants = serializer.getConstants();

            listeners.prePrepare(context);
            final PreparedStatement stmt = getPreparedStatement(queryString);
            setParameters(stmt, constants, serializer.getConstantPaths(), getMetadata().getParams());
            context.addPreparedStatement(stmt);
            listeners.prepared(context);

            listeners.preExecute(context);
            final ResultSet rs = stmt.executeQuery();
            listeners.executed(context);

            return new ResultSetAdapter(rs) {
                @Override
                public void close() throws SQLException {
                    try {
                        super.close();
                    } finally {
                        stmt.close();
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

    private PreparedStatement getPreparedStatement(String queryString) throws SQLException {
        PreparedStatement statement = connection().prepareStatement(queryString);
        if (statementOptions.getFetchSize() != null) {
            statement.setFetchSize(statementOptions.getFetchSize());
        }
        if (statementOptions.getMaxFieldSize() != null) {
            statement.setMaxFieldSize(statementOptions.getMaxFieldSize());
        }
        if (statementOptions.getQueryTimeout() != null) {
            statement.setQueryTimeout(statementOptions.getQueryTimeout());
        }
        if (statementOptions.getMaxRows() != null) {
            statement.setMaxRows(statementOptions.getMaxRows());
        }
        return statement;
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CloseableIterator<T> iterate() {
        Expression<T> expr = (Expression<T>) queryMixin.getMetadata().getProjection();
        return iterateSingle(queryMixin.getMetadata(), expr);
    }

    @SuppressWarnings("unchecked")
    private CloseableIterator<T> iterateSingle(QueryMetadata metadata, @Nullable final Expression<T> expr) {
        SQLListenerContextImpl context = startContext(connection(), queryMixin.getMetadata());
        String queryString = null;
        List<Object> constants = ImmutableList.of();

        try {
            listeners.preRender(context);
            SQLSerializer serializer = serialize(false);
            queryString = serializer.toString();
            logQuery(queryString, serializer.getConstants());
            context.addSQL(queryString);
            listeners.rendered(context);


            listeners.notifyQuery(queryMixin.getMetadata());
            constants = serializer.getConstants();

            listeners.prePrepare(context);
            final PreparedStatement stmt = getPreparedStatement(queryString);
            setParameters(stmt, constants, serializer.getConstantPaths(), metadata.getParams());
            context.addPreparedStatement(stmt);
            listeners.prepared(context);

            listeners.preExecute(context);
            final ResultSet rs = stmt.executeQuery();
            listeners.executed(context);

            if (expr == null) {
                return new SQLResultIterator<T>(configuration, stmt, rs, listeners, context) {
                    @Override
                    public T produceNext(ResultSet rs) throws Exception {
                        return (T) rs.getObject(1);
                    }
                };
            } else if (expr instanceof FactoryExpression) {
                return new SQLResultIterator<T>(configuration, stmt, rs, listeners, context) {
                    @Override
                    public T produceNext(ResultSet rs) throws Exception {
                        return newInstance((FactoryExpression<T>) expr, rs, 0);
                    }
                };
            } else if (expr.equals(Wildcard.all)) {
                return new SQLResultIterator<T>(configuration, stmt, rs, listeners, context) {
                    @Override
                    public T produceNext(ResultSet rs) throws Exception {
                        Object[] rv = new Object[rs.getMetaData().getColumnCount()];
                        for (int i = 0; i < rv.length; i++) {
                            rv[i] = rs.getObject(i + 1);
                        }
                        return (T) rv;
                    }
                };
            } else {
                return new SQLResultIterator<T>(configuration, stmt, rs, listeners, context) {
                    @Override
                    public T produceNext(ResultSet rs) throws Exception {
                        return get(rs, expr, 1, expr.getType());
                    }
                };
            }

        } catch (SQLException e) {
            onException(context, e);
            endContext(context);
            throw configuration.translate(queryString, constants, e);
        } catch (RuntimeException e) {
            logger.error("Caught " + e.getClass().getName() + " for " + queryString);
            throw e;
        } finally {
            reset();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> fetch() {
        Expression<T> expr = (Expression<T>) queryMixin.getMetadata().getProjection();
        SQLListenerContextImpl context = startContext(connection(), queryMixin.getMetadata());
        String queryString = null;
        List<Object> constants = ImmutableList.of();

        try {
            listeners.preRender(context);
            SQLSerializer serializer = serialize(false);
            queryString = serializer.toString();
            logQuery(queryString, serializer.getConstants());
            context.addSQL(queryString);
            listeners.rendered(context);

            listeners.notifyQuery(queryMixin.getMetadata());
            constants = serializer.getConstants();

            listeners.prePrepare(context);
            final PreparedStatement stmt = getPreparedStatement(queryString);
            try {
                setParameters(stmt, constants, serializer.getConstantPaths(), queryMixin.getMetadata().getParams());
                context.addPreparedStatement(stmt);
                listeners.prepared(context);

                listeners.preExecute(context);
                final ResultSet rs = stmt.executeQuery();
                listeners.executed(context);
                try {
                    lastCell = null;
                    final List<T> rv = new ArrayList<T>();
                    if (expr instanceof FactoryExpression) {
                        FactoryExpression<T> fe = (FactoryExpression<T>) expr;
                        while (rs.next()) {
                            if (getLastCell) {
                                lastCell = rs.getObject(fe.getArgs().size() + 1);
                                getLastCell = false;
                            }
                            rv.add(newInstance(fe, rs, 0));
                        }
                    } else if (expr.equals(Wildcard.all)) {
                        while (rs.next()) {
                            Object[] row = new Object[rs.getMetaData().getColumnCount()];
                            if (getLastCell) {
                                lastCell = rs.getObject(row.length);
                                getLastCell = false;
                            }
                            for (int i = 0; i < row.length; i++) {
                                row[i] = rs.getObject(i + 1);
                            }
                            rv.add((T) row);
                        }
                    } else {
                        while (rs.next()) {
                            if (getLastCell) {
                                lastCell = rs.getObject(2);
                                getLastCell = false;
                            }
                            rv.add(get(rs, expr, 1, expr.getType()));
                        }
                    }
                    return rv;
                } catch (IllegalAccessException e) {
                    onException(context, e);
                    throw new QueryException(e);
                } catch (InvocationTargetException e) {
                    onException(context,e);
                    throw new QueryException(e);
                } catch (InstantiationException e) {
                    onException(context,e);
                    throw new QueryException(e);
                } catch (SQLException e) {
                    onException(context,e);
                    throw configuration.translate(queryString, constants, e);
                } finally {
                    rs.close();
                }
            } finally {
                stmt.close();
            }
        } catch (SQLException e) {
            onException(context, e);
            throw configuration.translate(queryString, constants, e);
        } finally {
            endContext(context);
            reset();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryResults<T> fetchResults() {
        parentContext = startContext(connection(), queryMixin.getMetadata());
        Expression<T> expr = (Expression<T>) queryMixin.getMetadata().getProjection();
        QueryModifiers originalModifiers = queryMixin.getMetadata().getModifiers();
        try {
            if (configuration.getTemplates().isCountViaAnalytics()
                && queryMixin.getMetadata().getGroupBy().isEmpty()) {
                List<T> results;
                try {
                    queryMixin.addFlag(rowCountFlag);
                    getLastCell = true;
                    results = fetch();
                } finally {
                    queryMixin.removeFlag(rowCountFlag);
                }
                long total;
                if (!results.isEmpty()) {
                    if (lastCell instanceof Number) {
                        total = ((Number) lastCell).longValue();
                    } else {
                        throw new IllegalStateException("Unsupported lastCell instance " + lastCell);
                    }
                } else {
                    total = fetchCount();
                }
                return new QueryResults<T>(results, originalModifiers, total);

            } else {
                queryMixin.setProjection(expr);
                long total = fetchCount();
                if (total > 0) {
                    return new QueryResults<T>(fetch(), originalModifiers, total);
                } else {
                    return QueryResults.emptyResults();
                }
            }

        } finally {
            endContext(parentContext);
            reset();
            getLastCell = false;
            parentContext = null;
        }
    }

    private <RT> RT newInstance(FactoryExpression<RT> c, ResultSet rs, int offset)
        throws InstantiationException, IllegalAccessException, InvocationTargetException, SQLException {
        Object[] args = new Object[c.getArgs().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = get(rs, c.getArgs().get(i), offset + i + 1, c.getArgs().get(i).getType());
        }
        return c.newInstance(args);
    }

    private void reset() {
        cleanupMDC();
    }

    protected void setParameters(PreparedStatement stmt, List<?> objects, List<Path<?>> constantPaths,
            Map<ParamExpression<?>, ?> params) {
        if (objects.size() != constantPaths.size()) {
            throw new IllegalArgumentException("Expected " + objects.size() +
                    " paths, but got " + constantPaths.size());
        }
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            try {
                if (o instanceof ParamExpression) {
                    if (!params.containsKey(o)) {
                        throw new ParamNotSetException((ParamExpression<?>) o);
                    }
                    o = params.get(o);
                }
                set(stmt, constantPaths.get(i), i + 1, o);
            } catch (SQLException e) {
                throw configuration.translate(e);
            }
        }
    }

    private long unsafeCount() throws SQLException {
        SQLListenerContextImpl context = startContext(connection(), getMetadata());
        String queryString = null;
        List<Object> constants = ImmutableList.of();
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            listeners.preRender(context);
            SQLSerializer serializer = serialize(true);
            queryString = serializer.toString();
            logQuery(queryString, serializer.getConstants());
            context.addSQL(queryString);
            listeners.rendered(context);

            constants = serializer.getConstants();
            listeners.prePrepare(context);

            stmt = getPreparedStatement(queryString);
            setParameters(stmt, constants, serializer.getConstantPaths(), getMetadata().getParams());

            context.addPreparedStatement(stmt);
            listeners.prepared(context);

            listeners.preExecute(context);
            rs = stmt.executeQuery();
            boolean hasResult = rs.next();
            listeners.executed(context);

            if (hasResult) {
                return rs.getLong(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            onException(context, e);
            throw configuration.translate(queryString, constants, e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } finally {
                if (stmt != null) {
                    stmt.close();
                }
            }
            endContext(context);
            cleanupMDC();
        }
    }

    protected void logQuery(String queryString, Collection<Object> parameters) {
        if (logger.isDebugEnabled()) {
            String normalizedQuery = queryString.replace('\n', ' ');
            MDC.put(MDC_QUERY, normalizedQuery);
            MDC.put(MDC_PARAMETERS, String.valueOf(parameters));
            logger.debug(normalizedQuery);
        }
    }

    protected void cleanupMDC() {
        MDC.remove(MDC_QUERY);
        MDC.remove(MDC_PARAMETERS);
    }

    private Connection connection() {
        if (conn == null) {
            if (connProvider != null) {
                conn = connProvider.get();
            } else {
                throw new IllegalStateException("No connection provided");
            }
        }
        return conn;
    }

    /**
     * Set whether literals are used in SQL strings instead of parameter bindings (default: false)
     *
     * <p>Warning: When literals are used, prepared statement won't have any parameter bindings
     * and also batch statements will only be simulated, but not executed as actual batch statements.</p>
     *
     * @param useLiterals true for literals and false for bindings
     */
    public void setUseLiterals(boolean useLiterals) {
        this.useLiterals = useLiterals;
    }

    @Override
    protected void clone(Q query) {
        super.clone(query);
        this.useLiterals = query.useLiterals;
        this.listeners = new SQLListeners(query.listeners);
    }

    @Override
    public Q clone() {
        return this.clone(this.conn);
    }

    public abstract Q clone(Connection connection);

    /**
     * Set the options to be applied to the JDBC statements of this query
     *
     * @param statementOptions options to be applied to statements
     */
    public void setStatementOptions(StatementOptions statementOptions) {
        this.statementOptions = statementOptions;
    }
}
