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
package com.querydsl.r2dbc.dml;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.dml.ReactiveInsertClause;
import com.querydsl.core.types.*;
import com.querydsl.core.util.CollectionUtils;
import com.querydsl.r2dbc.*;
import com.querydsl.r2dbc.binding.BindTarget;
import com.querydsl.r2dbc.binding.StatementWrapper;
import com.querydsl.r2dbc.types.Null;
import com.querydsl.sql.ColumnMetadata;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.dml.DefaultMapper;
import com.querydsl.sql.dml.Mapper;
import io.r2dbc.spi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Provides a base class for dialect-specific INSERT clauses.
 *
 * @param <C> The type extending this class.
 * @author mc_fish
 */
public abstract class AbstractR2DBCInsertClause<C extends AbstractR2DBCInsertClause<C>> extends AbstractR2DBCClause<C> implements ReactiveInsertClause<C> {

    protected static final Logger logger = Logger.getLogger(AbstractR2DBCInsertClause.class.getName());

    protected final RelationalPath<?> entity;

    protected final QueryMetadata metadata = new DefaultQueryMetadata();

    @Nullable
    protected SubQueryExpression<?> subQuery;

    @Nullable
    protected R2DBCQuery<?> subQueryBuilder;

    protected final List<R2DBCInsertBatch> batches = new ArrayList<R2DBCInsertBatch>();

    protected final List<Path<?>> columns = new ArrayList<Path<?>>();

    protected final List<Expression<?>> values = new ArrayList<Expression<?>>();

    protected transient String queryString;

    protected transient List<Object> constants;

    protected transient boolean batchToBulk;

    public AbstractR2DBCInsertClause(Connection connection, Configuration configuration, RelationalPath<?> entity, R2DBCQuery<?> subQuery) {
        this(connection, configuration, entity);
        this.subQueryBuilder = subQuery;
    }

    public AbstractR2DBCInsertClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public AbstractR2DBCInsertClause(R2DBCConnectionProvider connection, Configuration configuration, RelationalPath<?> entity, R2DBCQuery<?> subQuery) {
        this(connection, configuration, entity);
        this.subQueryBuilder = subQuery;
    }

    public AbstractR2DBCInsertClause(R2DBCConnectionProvider connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    /**
     * Add the given String literal at the given position as a query flag
     *
     * @param position position
     * @param flag     query flag
     * @return the current object
     */
    public C addFlag(Position position, String flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return (C) this;
    }

    /**
     * Add the given Expression at the given position as a query flag
     *
     * @param position position
     * @param flag     query flag
     * @return the current object
     */
    public C addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return (C) this;
    }

    /**
     * Add the current state of bindings as a batch item
     *
     * @return the current object
     */
    public C addBatch() {
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.select(values.toArray(new Expression[values.size()])).clone();
            values.clear();
        }
        batches.add(new R2DBCInsertBatch(columns, values, subQuery));
        columns.clear();
        values.clear();
        subQuery = null;
        return (C) this;
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
    public C columns(Path<?>... columns) {
        this.columns.addAll(Arrays.asList(columns));
        return (C) this;
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
    public <T> Mono<T> executeWithKey(Path<T> path) {
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
    public <T> Mono<T> executeWithKey(Class<T> type) {
        return executeWithKey(type, null);
    }

    protected <T> Mono<T> executeWithKey(Class<T> type, @Nullable Path<T> path) {
        return getConnection()
                .map(connection -> createStatement(connection, true))
                .flatMap(connection -> executeStatementWithKey(connection, type, path));
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
    public <T> Flux<T> executeWithKeys(Path<T> path) {
        return executeWithKeys((Class<T>) path.getType(), path);
    }

    public <T> Flux<T> executeWithKeys(Class<T> type) {
        return executeWithKeys(type, null);
    }

    public <T> Flux<T> executeWithKeys(Class<T> type, @Nullable Path<T> path) {
        return getConnection()
                .map(connection -> createStatement(connection, true))
                .flatMapMany(connection -> executeStatementWithKeys(connection, type, path));
    }

    protected Statement createStatement(Connection connection, boolean withKeys) {
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.select(values.toArray(new Expression[values.size()])).clone();
            values.clear();
        }

        SQLSerializer serializer = createSerializerAndSerialize(null);
        return prepareStatementAndSetParameters(connection, serializer, withKeys);
    }

    protected Collection<Statement> createStatements(Connection connection, boolean withKeys) {
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.select(values.toArray(new Expression[values.size()])).clone();
            values.clear();
        }

        return batches
                .stream()
                .map(batch -> {
                    SQLSerializer serializer = createSerializerAndSerialize(batch);
                    Statement statement = prepareStatementAndSetParameters(connection, serializer, withKeys);

                    return statement.add();
                })
                .collect(Collectors.toList());
    }

    private Batch createBatches(Connection connection, boolean withKeys) {
        if (subQueryBuilder != null) {
            subQuery = subQueryBuilder.select(values.toArray(new Expression[values.size()])).clone();
            values.clear();
        }

        Batch r2dbcBatch = connection.createBatch();
        batches.forEach(batch -> {
            SQLSerializer serializer = createSerializerAndSerialize(batch);
            Statement statement = prepareStatementAndSetParameters(connection, serializer, withKeys);

//            r2dbcBatch.add(statement.toString());
            r2dbcBatch.add(serializer.toString());
        });

        return r2dbcBatch;
    }

    protected Statement prepareStatementAndSetParameters(Connection connection, SQLSerializer serializer, boolean withKeys) {
        List<Object> constants = serializer.getConstants();
        String originalSql = serializer.toString();
        queryString = R2dbcUtils.replaceBindingArguments(configuration.getBindMarkerFactory().create(), constants, originalSql);

        logQuery(logger, queryString, constants);

        Statement statement = connection.createStatement(queryString);
        BindTarget bindTarget = new StatementWrapper(statement);

        if (withKeys) {
            if (entity.getPrimaryKey() != null) {
                String[] target = new String[entity.getPrimaryKey().getLocalColumns().size()];
                for (int i = 0; i < target.length; i++) {
                    Path<?> path = entity.getPrimaryKey().getLocalColumns().get(i);
                    String column = ColumnMetadata.getName(path);
                    target[i] = column;
                }
                statement.returnGeneratedValues(target);
            }
        }

        setParameters(bindTarget, configuration.getBindMarkerFactory().create(), constants, serializer.getConstantPaths(), metadata.getParams());

        return statement;
    }

    /**
     * Execute the clause and return the generated keys as a Result
     *
     * @return result set with generated keys
     */
    protected <T> Mono<T> executeStatementWithKey(Statement stmt, Class<T> type, @Nullable Path<T> path) {
        RowMapper<T> mapper = (row, metadata) -> Objects.requireNonNull(row.get(0, type), "Null key result");
        return Mono
                .from(stmt.execute())
                .flatMap(result -> Mono.from(result.map(mapper::map)));
    }

    protected <T> Flux<T> executeStatementWithKeys(Statement stmt, Class<T> type, @Nullable Path<T> path) {
        RowMapper<T> mapper = (row, metadata) -> Objects.requireNonNull(row.get(0, type), "Null key result");
        return Flux
                .from(stmt.execute())
                .flatMap(result -> result.map(mapper::map));
    }

    private Mono<Long> executeStatement(Statement stmt) {
        return Mono
                .from(stmt.execute())
                .flatMap(result -> Mono.from(result.getRowsUpdated()))
                .map(Long::valueOf);
    }

    private Mono<Long> executeStatements(Statement stmt) {
        return Flux
                .from(stmt.execute())
                .flatMap(result -> Mono.from(result.getRowsUpdated()))
                .reduce(0L, Long::sum);
    }

    private Mono<Long> executeBatches(Batch batch) {
        return Flux
                .from(batch.execute())
                .flatMap(result -> Mono.from(result.getRowsUpdated()))
                .reduce(0L, Long::sum);
    }

    @Override
    public Mono<Long> execute() {
        {
            if (batchToBulk || batches.isEmpty()) {
                return getConnection()
                        .map(connection -> createStatement(connection, false))
                        .flatMap(this::executeStatement);
            }

            if (useLiterals) {
                return getConnection()
                        .map(connection -> createBatches(connection, false))
                        .flatMap(this::executeBatches);
            }

            return getConnection()
                    .flatMapIterable(connection -> createStatements(connection, false))
                    .flatMap(this::executeStatements)
                    .reduce(0L, Long::sum);
        }
    }

    @Override
    public List<SQLBindings> getSQL() {
        if (batches.isEmpty()) {
            SQLSerializer serializer = createSerializer(true);
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
            return Collections.singletonList(createBindings(metadata, serializer));
        } else if (batchToBulk) {
            SQLSerializer serializer = createSerializer(true);
            serializer.serializeInsert(metadata, entity, batches);
            return Collections.singletonList(createBindings(metadata, serializer));
        } else {
            List<SQLBindings> builder = new ArrayList<>();
            for (R2DBCInsertBatch batch : batches) {
                SQLSerializer serializer = createSerializer(true);
                serializer.serializeInsert(metadata, entity, batch.getColumns(), batch.getValues(), batch.getSubQuery());
                builder.add(createBindings(metadata, serializer));
            }
            return CollectionUtils.unmodifiableList(builder);
        }
    }

    @Override
    public C select(SubQueryExpression<?> sq) {
        subQuery = sq;
        for (Map.Entry<ParamExpression<?>, Object> entry : sq.getMetadata().getParams().entrySet()) {
            metadata.setParam((ParamExpression) entry.getKey(), entry.getValue());
        }
        return (C) this;
    }

    @Override
    public <T> C set(Path<T> path, T value) {
        columns.add(path);
        if (value instanceof Expression<?>) {
            values.add((Expression<?>) value);
        } else if (value != null) {
            values.add(ConstantImpl.create(value));
        } else {
            values.add(Null.CONSTANT);
        }
        return (C) this;
    }

    @Override
    public <T> C set(Path<T> path, Expression<? extends T> expression) {
        columns.add(path);
        values.add(expression);
        return (C) this;
    }

    @Override
    public <T> C setNull(Path<T> path) {
        columns.add(path);
        values.add(Null.CONSTANT);
        return (C) this;
    }

    @Override
    public C values(Object... v) {
        for (Object value : v) {
            if (value instanceof Expression<?>) {
                values.add((Expression<?>) value);
            } else if (value != null) {
                values.add(ConstantImpl.create(value));
            } else {
                values.add(Null.CONSTANT);
            }
        }
        return (C) this;
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer(true);
        if (!batches.isEmpty() && batchToBulk) {
            serializer.serializeInsert(metadata, entity, batches);
        } else {
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
        }
        return serializer.toString();
    }

    private SQLSerializer createSerializerAndSerialize(R2DBCInsertBatch batch) {
        SQLSerializer serializer = createSerializer(true);
        if (!batches.isEmpty() && batchToBulk) {
            serializer.serializeInsert(metadata, entity, batches);
        } else if (!batches.isEmpty() && batch != null) {
            serializer.serializeInsert(metadata, entity, batch.getColumns(), batch.getValues(), batch.getSubQuery());
        } else {
            serializer.serializeInsert(metadata, entity, columns, values, subQuery);
        }
        return serializer;
    }

    /**
     * Populate the INSERT clause with the properties of the given bean. The
     * properties need to match the fields of the clause's entity instance.
     *
     * @param bean bean to use for population
     * @return the current object
     */
    public C populate(Object bean) {
        return populate(bean, DefaultMapper.DEFAULT);
    }

    /**
     * Populate the INSERT clause with the properties of the given bean using
     * the given Mapper.
     *
     * @param obj    object to use for population
     * @param mapper mapper to use
     * @return the current object
     */
    @SuppressWarnings("rawtypes")
    public <T> C populate(T obj, Mapper<T> mapper) {
        Map<Path<?>, Object> values = mapper.createMap(entity, obj);
        for (Map.Entry<Path<?>, Object> entry : values.entrySet()) {
            set((Path) entry.getKey(), entry.getValue());
        }
        return (C) this;
    }

    @Override
    public boolean isEmpty() {
        return values.isEmpty() && batches.isEmpty();
    }

    @Override
    public int getBatchCount() {
        return batches.size();
    }

//    private <T> void setBatchParameters(Statement stmt, R2DBCInsertBatch batch, int offset) {
//        List<Object> constants = batch
//                .getValues()
//                .stream()
//                .map(c -> ((Constant<T>) c).getConstant()) // TODO: support expressions
//                .collect(Collectors.toList());
//        setParameters(stmt, constants, batch.getColumns(), metadata.getParams());
//    }

    @FunctionalInterface
    private interface RowMapper<T> {
        @NotNull
        T map(Row row, RowMetadata metadata);
    }

}
