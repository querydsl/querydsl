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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.dml.UpdateClause;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLSerializer;
import com.querydsl.sql.types.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnegative;
import javax.inject.Provider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Provides a base class for dialect-specific UPDATE clauses.
 *
 * @author tiwe
 * @param <C> The type extending this class.
 *
 */
public abstract class AbstractSQLUpdateClause<C extends AbstractSQLClause<C> & UpdateClause<C>> extends AbstractSQLClause<C> implements UpdateClause<C> {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractSQLUpdateClause.class);

    protected final RelationalPath<?> entity;

    protected final List<SQLUpdateBatch> batches = new ArrayList<SQLUpdateBatch>();

    protected Map<Path<?>, Expression<?>> updates = Maps.newLinkedHashMap();

    protected QueryMetadata metadata = new DefaultQueryMetadata();

    protected transient String queryString;

    protected transient List<Object> constants;

    public AbstractSQLUpdateClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
    }

    public AbstractSQLUpdateClause(Provider<Connection> connection, Configuration configuration, RelationalPath<?> entity) {
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
    public C addFlag(Position position, String flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return (C) this;
    }

    /**
     * Add the given Expression at the given position as a query flag
     *
     * @param position position
     * @param flag query flag
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
        batches.add(new SQLUpdateBatch(metadata, updates));
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

    protected PreparedStatement createStatement() throws SQLException {
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        serializer.serializeUpdate(metadata, entity, updates);
        queryString = serializer.toString();
        constants = serializer.getConstants();
        logQuery(logger, queryString, constants);
        context.addSQL(createBindings(metadata, serializer));
        listeners.prepared(context);

        listeners.prePrepare(context);
        PreparedStatement stmt = connection().prepareStatement(queryString);
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
        context.addPreparedStatement(stmt);
        listeners.prepared(context);

        return stmt;
    }

    protected Collection<PreparedStatement> createStatements() throws SQLException {
        boolean addBatches = !configuration.getUseLiterals();
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        serializer.serializeUpdate(batches.get(0).getMetadata(), entity, batches.get(0).getUpdates());
        queryString = serializer.toString();
        constants = serializer.getConstants();
        logQuery(logger, queryString, constants);
        context.addSQL(createBindings(metadata, serializer));
        listeners.rendered(context);

        Map<String, PreparedStatement> stmts = Maps.newHashMap();

        // add first batch
        listeners.prePrepare(context);
        PreparedStatement stmt = connection().prepareStatement(queryString);
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
        if (addBatches) {
            stmt.addBatch();
        }
        stmts.put(serializer.toString(), stmt);
        context.addPreparedStatement(stmt);
        listeners.prepared(context);


        // add other batches
        for (int i = 1; i < batches.size(); i++) {
            listeners.preRender(context);
            serializer = createSerializer();
            serializer.serializeUpdate(batches.get(i).getMetadata(), entity, batches.get(i).getUpdates());
            context.addSQL(createBindings(metadata, serializer));
            listeners.rendered(context);

            stmt = stmts.get(serializer.toString());
            if (stmt == null) {
                listeners.prePrepare(context);
                stmt = connection().prepareStatement(serializer.toString());
                stmts.put(serializer.toString(), stmt);
                context.addPreparedStatement(stmt);
                listeners.prepared(context);
            }
            setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
            if (addBatches) {
                stmt.addBatch();
            }
        }

        return stmts.values();
    }

    @Override
    public long execute() {
        context = startContext(connection(), metadata, entity);

        PreparedStatement stmt = null;
        Collection<PreparedStatement> stmts = null;
        try {
            if (batches.isEmpty()) {
                stmt = createStatement();
                listeners.notifyUpdate(entity, metadata, updates);

                listeners.preExecute(context);
                int rc = stmt.executeUpdate();
                listeners.executed(context);
                return rc;
            } else {
                stmts = createStatements();
                listeners.notifyUpdates(entity, batches);

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
            serializer.serializeUpdate(metadata, entity, updates);
            return ImmutableList.of(createBindings(metadata, serializer));
        } else {
            ImmutableList.Builder<SQLBindings> builder = ImmutableList.builder();
            for (SQLUpdateBatch batch : batches) {
                SQLSerializer serializer = createSerializer();
                serializer.serializeUpdate(batch.getMetadata(), entity, batch.getUpdates());
                builder.add(createBindings(metadata, serializer));
            }
            return builder.build();
        }
    }

    @Override
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
    public <T> C set(Path<T> path, Expression<? extends T> expression) {
        if (expression != null) {
            updates.put(path, expression);
        } else {
            setNull(path);
        }
        return (C) this;
    }

    @Override
    public <T> C setNull(Path<T> path) {
        updates.put(path, Null.CONSTANT);
        return (C) this;
    }

    @Override
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

    public C where(Predicate p) {
        metadata.addWhere(p);
        return (C) this;
    }

    @Override
    public C where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);
        }
        return (C) this;
    }

    public C limit(@Nonnegative long limit) {
        metadata.setModifiers(QueryModifiers.limit(limit));
        return (C) this;
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer();
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
    public C populate(Object bean) {
        return populate(bean, DefaultMapper.DEFAULT);
    }

    /**
     * Populate the UPDATE clause with the properties of the given bean using the given Mapper.
     *
     * @param obj object to use for population
     * @param mapper mapper to use
     * @return the current object
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public <T> C populate(T obj, Mapper<T> mapper) {
        Collection<? extends Path<?>> primaryKeyColumns = entity.getPrimaryKey() != null
                ? entity.getPrimaryKey().getLocalColumns()
                : Collections.<Path<?>>emptyList();
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
}
