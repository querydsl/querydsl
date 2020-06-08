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
package com.querydsl.r2dbc;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryException;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.sql.StatementOptions;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Result;
import io.r2dbc.spi.Statement;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * {@code AbstractSQLQuery} is the base type for SQL query implementations
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 * @author tiwe
 */
public abstract class AbstractR2DBCQuery<T, Q extends AbstractR2DBCQuery<T, Q>> extends ProjectableR2DBCQuery<T, Q> {

    protected static final String PARENT_CONTEXT = AbstractR2DBCQuery.class.getName() + "#PARENT_CONTEXT";

    private static final Logger logger = LoggerFactory.getLogger(AbstractR2DBCQuery.class);

    private static final QueryFlag rowCountFlag = new QueryFlag(QueryFlag.Position.AFTER_PROJECTION, ", count(*) over() ");

    @Nullable
    private R2DBCConnectionProvider connProvider;

    @Nullable
    private Connection conn;

    protected boolean useLiterals;

    private boolean getLastCell;

    private Object lastCell;

    private StatementOptions statementOptions = StatementOptions.DEFAULT;

    public AbstractR2DBCQuery(@Nullable Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public AbstractR2DBCQuery(@Nullable Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata, false), configuration);
        this.conn = conn;
        this.useLiterals = configuration.getUseLiterals();
    }

    public AbstractR2DBCQuery(R2DBCConnectionProvider connProvider, Configuration configuration) {
        this(connProvider, configuration, new DefaultQueryMetadata());
    }

    public AbstractR2DBCQuery(R2DBCConnectionProvider connProvider, Configuration configuration, QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata, false), configuration);
        this.connProvider = connProvider;
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

    @Override
    public Mono<Long> fetchCount() {
        return unsafeCount();
    }

    /**
     * If you use forUpdate() with a backend that uses page or row locks, rows examined by the
     * query are write-locked until the end of the current transaction.
     * <p>
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
     * <p>
     * Supported by MySQL, PostgreSQL, SQLServer.
     *
     * @return the current object
     * @throws QueryException if the FOR SHARE is not supported.
     */
    public Q forShare() {
        return forShare(false);
    }

    /**
     * FOR SHARE causes the rows retrieved by the SELECT statement to be locked as though for update.
     * <p>
     * Supported by MySQL, PostgreSQL, SQLServer.
     *
     * @param fallbackToForUpdate if the FOR SHARE is not supported and this parameter is <code>true</code>, the
     *                            {@link #forUpdate()} functionality will be used.
     * @return the current object
     * @throws QueryException if the FOR SHARE is not supported and <i>fallbackToForUpdate</i> is set to
     *                        <code>false</code>.
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
    private <U> Publisher<U> get(Result rs, Expression<?> expr, int i, Class<U> type) {
        return configuration.get(rs, expr instanceof Path ? (Path<?>) expr : null, i, type);
    }

    private void set(Statement stmt, Path<?> path, int i, Object value) {
        configuration.set(stmt, path, i, value);
    }

    private Statement getStatement(Connection connection, String queryString) {
        Statement statement = connection.createStatement(queryString);
        if (statementOptions.getFetchSize() != null) {
            statement.fetchSize(statementOptions.getFetchSize());
        }
        if (statementOptions.getMaxFieldSize() != null) {
            statement.fetchSize(statementOptions.getMaxFieldSize());
        }
//        if (statementOptions.getQueryTimeout() != null) {
//            connection.setQueryTimeout(statementOptions.getQueryTimeout());
//        }
        if (statementOptions.getMaxRows() != null) {
            statement.fetchSize(statementOptions.getMaxRows());
        }
        return statement;
    }

    protected Configuration getConfiguration() {
        return configuration;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Flux<T> fetch() {
        return getConnection()
                .flatMapMany(conn -> {
                    Expression<T> expr = (Expression<T>) queryMixin.getMetadata().getProjection();
                    SQLSerializer serializer = serialize(false);
                    String originalSql = serializer.toString();
                    //String sql = R2dbcUtils.replaceBindingArguments(originalSql);
                    Statement statement = conn.createStatement(originalSql);
                    setParameters(statement, serializer.getConstants(), serializer.getConstantPaths(), getMetadata().getParams());

                    return Flux
                            .from(statement.execute())
                            .flatMap(result -> Mono.from(configuration.get(result, expr instanceof Path ? (Path<?>) expr : null, 1, expr.getType())));
                });
    }

    private Mono<Connection> getConnection() {
        if (connProvider != null) {
            return connProvider.getConnection();
        }
        if (conn != null) {
            return Mono.just(conn);
        }
        return Mono.error(new IllegalStateException("No connection provided"));
    }

    private <RT> RT newInstance(FactoryExpression<RT> c, Result rs, int offset) {
        Object[] args = new Object[c.getArgs().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = get(rs, c.getArgs().get(i), offset + i + 1, c.getArgs().get(i).getType());
        }
        return c.newInstance(args);
    }

    protected void setParameters(Statement stmt, List<?> objects, List<Path<?>> constantPaths,
                                 Map<ParamExpression<?>, ?> params) {
        if (objects.size() != constantPaths.size()) {
            throw new IllegalArgumentException("Expected " + objects.size() +
                    " paths, but got " + constantPaths.size());
        }
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = params.get(o);
            }
            set(stmt, constantPaths.get(i), i + 1, o);
        }
    }

    private Mono<Long> unsafeCount() {
        SQLSerializer serializer = serialize(true);
        String queryString = serializer.toString();
        List<Object> constants = serializer.getConstants();
        logQuery(queryString, constants);

        return getConnection()
                .flatMap(connection -> {
                    Statement stmt = getStatement(connection, queryString);
                    setParameters(stmt, constants, serializer.getConstantPaths(), getMetadata().getParams());

                    return Mono
                            .from(stmt.execute())
                            .flatMap(result -> Mono.from(result.map((row, rowMetadata) -> row.get(1))))
                            .cast(Long.class)
                            .defaultIfEmpty(0L)
                            .doOnError(e -> Mono.error(configuration.translate(queryString, constants, e)));
                });
    }

    protected void logQuery(String queryString, Collection<Object> parameters) {
        if (logger.isDebugEnabled()) {
            String normalizedQuery = queryString.replace('\n', ' ');
            logger.debug(normalizedQuery);
        }
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
