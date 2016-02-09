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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnegative;
import javax.inject.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.querydsl.core.*;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.dml.DeleteClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.ValidatingVisitor;
import com.querydsl.sql.*;

/**
 * {@code SQLDeleteClause} defines a DELETE clause
 *
 * @author tiwe
 *
 */
public class SQLDeleteClause extends AbstractSQLClause<SQLDeleteClause> implements DeleteClause<SQLDeleteClause> {

    private static final Logger logger = LoggerFactory.getLogger(SQLDeleteClause.class);

    private static final ValidatingVisitor validatingVisitor = new ValidatingVisitor("Undeclared path '%s'. " +
            "A delete operation can only reference a single table. " +
            "Consider this alternative: DELETE ... WHERE EXISTS (subquery)");

    private final RelationalPath<?> entity;

    private final List<QueryMetadata> batches = new ArrayList<QueryMetadata>();

    private DefaultQueryMetadata metadata = new DefaultQueryMetadata();

    private transient String queryString;

    private transient List<Object> constants;

    public SQLDeleteClause(Connection connection, SQLTemplates templates, RelationalPath<?> entity) {
        this(connection, new Configuration(templates), entity);
    }

    public SQLDeleteClause(Connection connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
    }

    public SQLDeleteClause(Provider<Connection> connection, Configuration configuration, RelationalPath<?> entity) {
        super(configuration, connection);
        this.entity = entity;
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
    }

    /**
     * Add the given String literal at the given position as a query flag
     *
     * @param position position
     * @param flag query flag
     * @return the current object
     */
    public SQLDeleteClause addFlag(Position position, String flag) {
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
    public SQLDeleteClause addFlag(Position position, Expression<?> flag) {
        metadata.addFlag(new QueryFlag(position, flag));
        return this;
    }

    /**
     * Add current state of bindings as a batch item
     *
     * @return the current object
     */
    public SQLDeleteClause addBatch() {
        batches.add(metadata);
        metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
        return this;
    }

    @Override
    public void clear() {
        batches.clear();
        metadata = new DefaultQueryMetadata();
        metadata.addJoin(JoinType.DEFAULT, entity);
        metadata.setValidatingVisitor(validatingVisitor);
    }

    private PreparedStatement createStatement() throws SQLException {
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        serializer.serializeDelete(metadata, entity);
        queryString = serializer.toString();
        constants = serializer.getConstants();
        logQuery(logger, queryString, constants);
        context.addSQL(queryString);
        listeners.rendered(context);

        listeners.prePrepare(context);
        PreparedStatement stmt = connection().prepareStatement(queryString);
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());

        context.addPreparedStatement(stmt);
        listeners.prepared(context);

        return stmt;
    }

    private Collection<PreparedStatement> createStatements() throws SQLException {
        boolean addBatches = !configuration.getUseLiterals();
        listeners.preRender(context);
        SQLSerializer serializer = createSerializer();
        serializer.serializeDelete(batches.get(0), entity);
        queryString = serializer.toString();
        constants = serializer.getConstants();
        logQuery(logger, queryString, constants);
        context.addSQL(queryString);
        listeners.rendered(context);

        Map<String, PreparedStatement> stmts = Maps.newHashMap();

        // add first batch
        listeners.prePrepare(context);
        PreparedStatement stmt = connection().prepareStatement(queryString);
        setParameters(stmt, serializer.getConstants(), serializer.getConstantPaths(), metadata.getParams());
        if (addBatches) {
            stmt.addBatch();
        }
        stmts.put(queryString, stmt);
        context.addPreparedStatement(stmt);
        listeners.prepared(context);


        // add other batches
        for (int i = 1; i < batches.size(); i++) {
            listeners.preRender(context);
            serializer = createSerializer();
            serializer.serializeDelete(batches.get(i), entity);
            context.addSQL(serializer.toString());
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
                listeners.notifyDelete(entity, metadata);

                listeners.preExecute(context);
                int rc = stmt.executeUpdate();
                listeners.executed(context);
                return rc;
            } else {
                stmts = createStatements();
                listeners.notifyDeletes(entity, batches);

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
            serializer.serializeDelete(metadata, entity);
            return ImmutableList.of(createBindings(metadata, serializer));
        } else {
            ImmutableList.Builder<SQLBindings> builder = ImmutableList.builder();
            for (QueryMetadata metadata : batches) {
                SQLSerializer serializer = createSerializer();
                serializer.serializeDelete(metadata, entity);
                builder.add(createBindings(metadata, serializer));
            }
            return builder.build();
        }
    }

    public SQLDeleteClause where(Predicate p) {
        metadata.addWhere(p);
        return this;
    }

    @Override
    public SQLDeleteClause where(Predicate... o) {
        for (Predicate p : o) {
            metadata.addWhere(p);
        }
        return this;
    }

    public SQLDeleteClause limit(@Nonnegative long limit) {
        metadata.setModifiers(QueryModifiers.limit(limit));
        return this;
    }

    @Override
    public int getBatchCount() {
        return batches.size();
    }

    @Override
    public String toString() {
        SQLSerializer serializer = createSerializer();
        serializer.serializeDelete(metadata, entity);
        return serializer.toString();
    }

}
