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
import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.querydsl.core.*;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.dml.ReactiveDeleteClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.ValidatingVisitor;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.R2dbcUtils;
import com.querydsl.r2dbc.SQLSerializer;
import com.querydsl.r2dbc.binding.BindMarkers;
import com.querydsl.r2dbc.binding.BindTarget;
import com.querydsl.r2dbc.binding.StatementWrapper;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLBindings;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Nonnegative;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides a base class for dialect-specific DELETE clauses.
 *
 * @param <C> The type extending this class.
 * @author mc_fish
 */
public abstract class AbstractR2DBCDeleteClause<C extends AbstractR2DBCDeleteClause<C>> extends AbstractR2DBCClause<C> implements ReactiveDeleteClause<C> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractR2DBCDeleteClause.class);

    protected static final ValidatingVisitor validatingVisitor = new ValidatingVisitor("Undeclared path '%s'. " +
            "A delete operation can only reference a single table. " +
            "Consider this alternative: DELETE ... WHERE EXISTS (subquery)");

    protected final RelationalPath<?> entity;

    protected final List<QueryMetadata> batches = new ArrayList<QueryMetadata>();

    protected DefaultQueryMetadata metadata = new DefaultQueryMetadata();

    protected transient String queryString;

    protected transient List<Object> constants;

    public AbstractR2DBCDeleteClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
    }

    public AbstractR2DBCDeleteClause(R2DBCConnectionProvider connectionProvider, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connectionProvider);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
    }

    /**
     * Add the given String literal at the given position as a query flag
     *
     * @param position position
     * @param flag     query flag
     * @return the current object
     */
    @WithBridgeMethods(value = R2DBCDeleteClause.class, castRequired = true)
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
    @WithBridgeMethods(value = R2DBCDeleteClause.class, castRequired = true)
    public C addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return (C) this;
    }

    /**
     * Add current state of bindings as a batch item
     *
     * @return the current object
     */
    @WithBridgeMethods(value = R2DBCDeleteClause.class, castRequired = true)
    public C addBatch() {
        batches.add(metadata);
        metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
        return (C) this;
    }

    @Override
    public void clear() {
        batches.clear();
        metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
    }

    private Statement prepareStatementAndSetParameters(Connection connection, SQLSerializer serializer) {
        List<Object> constants = serializer.getConstants();
        String originalSql = serializer.toString();
        queryString = R2dbcUtils.replaceBindingArguments(configuration.getBindMarkerFactory().create(), constants, originalSql);

        logQuery(logger, queryString, serializer.getConstants());
        Statement statement = connection.createStatement(queryString);
        BindTarget bindTarget = new StatementWrapper(statement);

        if (batches.isEmpty()) {
            setParameters(bindTarget, configuration.getBindMarkerFactory().create(), serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
        } else {
            for (QueryMetadata batch : batches) {
                if (useLiterals) {
                    throw new UnsupportedOperationException("Batch deletes are not supported with literals");
                }
                setBatchParameters(bindTarget, configuration.getBindMarkerFactory().create(), batch);
            }
        }
        return statement;
    }

    private <T> void setBatchParameters(BindTarget bindTarget, BindMarkers bindMarkers, QueryMetadata batch) {
        SQLSerializer helperSerializer = createSerializerAndSerialize(batch);
        helperSerializer.serializeDelete(batch, entity);
        setParameters(bindTarget, bindMarkers, helperSerializer.getConstants(), helperSerializer.getConstantPaths(), metadata.getParams());
    }

    private SQLSerializer createSerializerAndSerialize(QueryMetadata batch) {
        SQLSerializer serializer = createSerializer(true);
        if (!batches.isEmpty() && batch != null) {
            serializer.serializeDelete(batch, entity);
        } else {
            serializer.serializeDelete(metadata, entity);
        }
        return serializer;
    }

    private Mono<Statement> createStatement() {
        return getConnection()
                .map(connection -> {
                    SQLSerializer serializer = createSerializerAndSerialize(null);
                    return prepareStatementAndSetParameters(connection, serializer);
                });
    }

    protected Flux<Statement> createStatements() {
        boolean addBatches = !configuration.getUseLiterals();

        return Flux
                .fromIterable(batches)
                .flatMap(batch -> getConnection()
                        .map(connection -> {
                            SQLSerializer serializer = createSerializerAndSerialize(batch);
                            Statement statement = prepareStatementAndSetParameters(connection, serializer);

                            if (addBatches) {
                                return statement.add();
                            }

                            return statement;
                        })
                );
    }

    @Override
    public Mono<Long> execute() {
        return getConnection()
                .flatMap(connection -> {
                    if (batches.isEmpty()) {
                        return createStatement()
                                .flatMap(statement -> Mono.from(statement.execute()))
                                .flatMap(result -> Mono.from(result.getRowsUpdated()))
                                .map(Long::valueOf)
                                .doOnError(e -> Mono.error(configuration.translate(queryString, constants, e)));
                    }

                    return createStatements()
                            .flatMap(statement -> Flux.from(statement.execute()))
                            .flatMap(result -> Mono.from(result.getRowsUpdated()))
                            .map(Long::valueOf)
                            .reduce(0L, Long::sum)
                            .doOnError(e -> Mono.error(configuration.translate(queryString, constants, e)));
                });
    }

    @Override
    public List<SQLBindings> getSQL() {
        if (batches.isEmpty()) {
            SQLSerializer serializer = createSerializer(true);
            serializer.serializeDelete(metadata, entity);
            return ImmutableList.of(createBindings(metadata, serializer));
        }

        ImmutableList.Builder<SQLBindings> builder = ImmutableList.builder();
        for (QueryMetadata metadata : batches) {
            SQLSerializer serializer = createSerializer(true);
            serializer.serializeDelete(metadata, entity);
            builder.add(createBindings(metadata, serializer));
        }
        return builder.build();
    }

    @WithBridgeMethods(value = R2DBCDeleteClause.class, castRequired = true)
    public C where(Predicate p) {
        metadata.addWhere(p);
        return (C) this;
    }

    @Override
    @WithBridgeMethods(value = R2DBCDeleteClause.class, castRequired = true)
    public C where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);
        }
        return (C) this;
    }

    @WithBridgeMethods(value = R2DBCDeleteClause.class, castRequired = true)
    public C limit(@Nonnegative long limit) {
        metadata.setModifiers(QueryModifiers.limit(limit));
        return (C) this;
    }

    @Override
    public int getBatchCount() {
        return batches.size();
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer(true);
        serializer.serializeDelete(metadata, entity);
        return serializer.toString();
    }

}
