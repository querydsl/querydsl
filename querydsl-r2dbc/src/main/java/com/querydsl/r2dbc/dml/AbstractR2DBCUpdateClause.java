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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.querydsl.core.*;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.dml.ReactiveUpdateClause;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.SQLSerializer;
import com.querydsl.r2dbc.types.Null;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLBindings;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnegative;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides a base class for dialect-specific UPDATE clauses.
 *
 * @param <C> The type extending this class.
 * @author tiwe
 */
public abstract class AbstractR2DBCUpdateClause<C extends AbstractR2DBCUpdateClause<C>> extends AbstractR2DBCClause<C> implements ReactiveUpdateClause<C> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractR2DBCUpdateClause.class);

    protected final RelationalPath<?> entity;

    protected final List<R2DBCUpdateBatch> batches = new ArrayList<R2DBCUpdateBatch>();

    protected Map<Path<?>, Expression<?>> updates = Maps.newLinkedHashMap();

    protected QueryMetadata metadata = new DefaultQueryMetadata();

    protected transient String queryString;

    protected transient List<Object> constants;

    public AbstractR2DBCUpdateClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public AbstractR2DBCUpdateClause(R2DBCConnectionProvider connection, Configuration configuration, RelationalPath<?> entity) {
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
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
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
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public C addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return (C) this;
    }

    /**
     * Add the current state of bindings as a batch item
     *
     * @return the current object
     */
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public C addBatch() {
        batches.add(new R2DBCUpdateBatch(metadata, updates));
        updates = Maps.newLinkedHashMap();
        metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, entity);
        return (C) this;
    }

    @Override
    public void clear() {
        batches.clear();
        updates = Maps.newLinkedHashMap();
        metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    protected Statement createStatement(Connection connection) {
        SQLSerializer serializer = createSerializerAndSerialize(null);
        return prepareStatementAndSetParameters(connection, serializer);
    }

    protected Collection<Statement> createStatements(Connection connection) {
        return batches
                .stream()
                .map(batch -> {
                    SQLSerializer serializer = createSerializerAndSerialize(batch);
                    Statement statement = prepareStatementAndSetParameters(connection, serializer);

                    if (useLiterals) {
                        return statement.add();
                    }

                    return statement;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Mono<Long> execute() {
        if (batches.isEmpty()) {
            return getConnection()
                    .map(this::createStatement)
                    .flatMap(this::executeStatement);
        } else {
            return getConnection()
                    .flatMapIterable(this::createStatements)
                    .flatMap(this::executeStatements)
                    .reduce(0L, Long::sum);
        }
    }


    private Mono<Long> executeStatement(Statement stmt) {
        return Mono.from(stmt.execute())
                .flatMap(result -> Mono.from(result.getRowsUpdated()))
                .map(Long::valueOf);
    }

    private Flux<Long> executeStatements(Statement stmt) {
        return Flux.from(stmt.execute())
                .flatMap(result -> Mono.from(result.getRowsUpdated()))
                .map(Long::valueOf);
    }


    @Override
    public List<SQLBindings> getSQL() {
        if (batches.isEmpty()) {
            SQLSerializer serializer = createSerializer(true);
            serializer.serializeUpdate(metadata, entity, updates);
            return ImmutableList.of(createBindings(metadata, serializer));
        } else {
            ImmutableList.Builder<SQLBindings> builder = ImmutableList.builder();
            for (R2DBCUpdateBatch batch : batches) {
                SQLSerializer serializer = createSerializer(true);
                serializer.serializeUpdate(batch.getMetadata(), entity, batch.getUpdates());
                builder.add(createBindings(metadata, serializer));
            }
            return builder.build();
        }
    }

    @Override
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public <T> C set(Path<T> path, T value) {
        if (value instanceof Expression<?>) {
            updates.put(path, (Expression<?>) value);
        } else if (value != null) {
            updates.put(path, ConstantImpl.create(value));
        } else {
            setNull(path);
        }
        return (C) this;
    }

    @Override
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public <T> C set(Path<T> path, Expression<? extends T> expression) {
        if (expression != null) {
            updates.put(path, expression);
        } else {
            setNull(path);
        }
        return (C) this;
    }

    @Override
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public <T> C setNull(Path<T> path) {
        updates.put(path, Null.CONSTANT);
        return (C) this;
    }

    @Override
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public C set(List<? extends Path<?>> paths, List<?> values) {
        for (int i = 0; i < paths.size(); i++) {
            if (values.get(i) instanceof Expression) {
                updates.put(paths.get(i), (Expression<?>) values.get(i));
            } else if (values.get(i) != null) {
                updates.put(paths.get(i), ConstantImpl.create(values.get(i)));
            } else {
                updates.put(paths.get(i), Null.CONSTANT);
            }
        }
        return (C) this;
    }

    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public C where(Predicate p) {
        metadata.addWhere(p);
        return (C) this;
    }

    @Override
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public C where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);
        }
        return (C) this;
    }

    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public C limit(@Nonnegative long limit) {
        metadata.setModifiers(QueryModifiers.limit(limit));
        return (C) this;
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer(true);
        serializer.serializeUpdate(metadata, entity, updates);
        return serializer.toString();
    }

    /**
     * Populate the UPDATE clause with the properties of the given bean.
     * The properties need to match the fields of the clause's entity instance.
     * Primary key columns are skipped in the population.
     *
     * @param bean bean to use for population
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public C populate(Object bean) {
        return populate(bean, DefaultMapper.DEFAULT);
    }

    /**
     * Populate the UPDATE clause with the properties of the given bean using the given Mapper.
     *
     * @param obj    object to use for population
     * @param mapper mapper to use
     * @return the current object
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    @WithBridgeMethods(value = R2DBCUpdateClause.class, castRequired = true)
    public <T> C populate(T obj, Mapper<T> mapper) {
        Collection<? extends Path<?>> primaryKeyColumns = entity.getPrimaryKey() != null
                ? entity.getPrimaryKey().getLocalColumns()
                : Collections.emptyList();
        Map<Path<?>, Object> values = mapper.createMap(entity, obj);
        for (Map.Entry<Path<?>, Object> entry : values.entrySet()) {
            if (!primaryKeyColumns.contains(entry.getKey())) {
                set((Path) entry.getKey(), entry.getValue());
            }
        }
        return (C) this;
    }

    @Override
    public boolean isEmpty() {
        return updates.isEmpty() && batches.isEmpty();
    }

    @Override
    public int getBatchCount() {
        return batches.size();
    }

    private SQLSerializer createSerializerAndSerialize(R2DBCUpdateBatch batch) {
        SQLSerializer serializer = createSerializer(true);
        if (!batches.isEmpty() && batch != null) {
            serializer.serializeUpdate(batch.getMetadata(), entity, batch.getUpdates());
        } else {
            serializer.serializeUpdate(metadata, entity, updates);
        }
        return serializer;
    }

    private Statement prepareStatementAndSetParameters(Connection connection, SQLSerializer serializer) {
        String queryString = serializer.toString();
        //queryString = R2dbcUtils.replaceBindingArguments(queryString);
        Statement stmt = connection.createStatement(queryString);
        if (batches.isEmpty()) {
            setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams(), 0);
        } else {
            int offset = 0;
            for (R2DBCUpdateBatch batch : batches) {
                setBatchParameters(stmt, batch, offset);
                if (useLiterals) {
                    connection.createBatch().add(stmt.toString());
                } else {
                    stmt.add();
                }
            }
        }
        return stmt;
    }

    private <T> void setBatchParameters(Statement stmt, R2DBCUpdateBatch batch, int offset) {
        SQLSerializer helperSerializer = createSerializer(true);
        helperSerializer.serializeUpdate(batch.getMetadata(), entity, batch.getUpdates());
        setParameters(stmt, helperSerializer.getConstants(), helperSerializer.getConstantPaths(), batch.getMetadata().getParams(), offset);
    }

}
