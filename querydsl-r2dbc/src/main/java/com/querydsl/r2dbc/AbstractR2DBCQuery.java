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
import com.querydsl.core.types.dsl.Wildcard;
import com.querydsl.r2dbc.binding.BindMarkers;
import com.querydsl.r2dbc.binding.BindTarget;
import com.querydsl.r2dbc.binding.StatementWrapper;
import com.querydsl.sql.StatementOptions;
import io.r2dbc.spi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@code AbstractSQLQuery} is the base type for SQL query implementations
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 * @author mc_fish
 */
public abstract class AbstractR2DBCQuery<T, Q extends AbstractR2DBCQuery<T, Q>> extends ProjectableR2DBCQuery<T, Q> {

    protected static final String PARENT_CONTEXT = AbstractR2DBCQuery.class.getName() + "#PARENT_CONTEXT";

    private static final Logger logger = Logger.getLogger(AbstractR2DBCQuery.class.getName());

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
    private <U> U get(Row row, Expression<?> expr, int i, Class<U> type) {
        return configuration.get(row, expr instanceof Path ? (Path<?>) expr : null, i, type);
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
        return getConnection().flatMapMany(conn -> {
            Expression<T> expr = (Expression<T>) queryMixin.getMetadata().getProjection();
            SQLSerializer serializer = serialize(false);
            Mapper<T> mapper = createMapper(expr);

            List<Object> constants = serializer.getConstants();
            String originalSql = serializer.toString();
            String sql = R2dbcUtils.replaceBindingArguments(configuration.getBindMarkerFactory().create(), constants, originalSql);

            Statement statement = conn.createStatement(sql);
            BindTarget bindTarget = new StatementWrapper(statement);

            setParameters(bindTarget, configuration.getBindMarkerFactory().create(), constants, serializer.getConstantPaths(), getMetadata().getParams());

            return Flux.from(statement.execute()).flatMap(result -> result.map(mapper::map));
        });
    }

    private Mapper<T> createMapper(Expression<T> expr) {
        if (expr instanceof FactoryExpression) {
            FactoryExpression<T> fe = (FactoryExpression<T>) expr;
            return (row, meta) -> newInstance(fe, row, 0);
        } else if (expr.equals(Wildcard.all)) {
            return this::toWildcardObjectArray;
        } else {
            return (row, meta) -> asRequired(row, expr);
        }
    }

    private T newInstance(FactoryExpression<T> c, Row rs, int offset) {
        Object[] args = new Object[c.getArgs().size()];
        for (int i = 0; i < args.length; i++) {
            args[i] = get(rs, c.getArgs().get(i), offset + i, c.getArgs().get(i).getType());
        }
        return Objects.requireNonNull(c.newInstance(args), "Null result");
    }

    private T toWildcardObjectArray(Row row, RowMetadata meta) {
        ArrayList<ColumnMetadata> metaList = new ArrayList<>();
        meta.getColumnMetadatas().forEach(metaList::add);

        Object[] args = new Object[metaList.size()];
        for (int i = 0; i < args.length; i++) {
            ColumnMetadata columnMetadata = metaList.get(i);
            args[i] = row.get(i, Objects.requireNonNull(columnMetadata.getJavaType(), "Unknown Java type"));
        }

        @SuppressWarnings("unchecked")
        T result = (T) args;
        return result;
    }

    private T asRequired(Row row, Expression<T> expr) {
        return Objects.requireNonNull(get(row, expr, 0, expr.getType()), "Null result");
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

    protected void setParameters(BindTarget bindTarget, BindMarkers bindMarkers, List<?> objects, List<Path<?>> constantPaths,
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

            configuration.set(bindMarkers.next(), bindTarget, constantPaths.get(i), o);
        }
    }

    private Mono<Long> unsafeCount() {
        SQLSerializer serializer = serialize(true);

        List<Object> constants = serializer.getConstants();
        String originalSql = serializer.toString();
        String sql = R2dbcUtils.replaceBindingArguments(configuration.getBindMarkerFactory().create(), constants, originalSql);

        logQuery(sql, constants);

        return getConnection()
                .flatMap(connection -> {
                    Statement statement = getStatement(connection, sql);
                    BindTarget bindTarget = new StatementWrapper(statement);

                    setParameters(bindTarget, configuration.getBindMarkerFactory().create(), constants, serializer.getConstantPaths(), getMetadata().getParams());

                    return Mono
                            .from(statement.execute())
                            .flatMap(result -> Mono.from(result.map((row, rowMetadata) -> row.get(0))))
                            .map(o -> {
                                if (Integer.class.isAssignableFrom(o.getClass())) {
                                    return ((Integer) o).longValue();
                                }

                                return (Long) o;
                            })
                            .defaultIfEmpty(0L)
                            .doOnError(e -> Mono.error(configuration.translate(sql, constants, e)));
                });
    }

    protected void logQuery(String queryString, Collection<Object> parameters) {
        if (logger.isLoggable(Level.FINE)) {
            String normalizedQuery = queryString.replace('\n', ' ');
            logger.fine(normalizedQuery);
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

    @FunctionalInterface
    private interface Mapper<T> {
        @NotNull
        T map(Row row, RowMetadata metadata);
    }

}
