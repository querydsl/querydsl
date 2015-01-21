/*
 * Copyright 2011, Mysema Ltd
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

import java.sql.Connection;

import javax.inject.Provider;

import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.querydsl.sql.dml.SQLDeleteClause;
import com.querydsl.sql.dml.SQLInsertClause;
import com.querydsl.sql.dml.SQLMergeClause;
import com.querydsl.sql.dml.SQLUpdateClause;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.SubQueryExpression;

/**
 * AbstractSQLQueryFactory is the base class for {@link SQLCommonQueryFactory} implementations
 *
 * @author tiwe
 *
 */
public abstract class AbstractSQLQueryFactory<Q extends SQLCommonQuery<Q>, SQ extends AbstractSQLSubQuery<SQ>> implements SQLCommonQueryFactory<Q, SQ,
    SQLDeleteClause, SQLUpdateClause, SQLInsertClause, SQLMergeClause> {

    protected final Configuration configuration;

    protected final Provider<Connection> connection;

    public AbstractSQLQueryFactory(Configuration configuration, Provider<Connection> connection) {
        this.configuration = configuration;
        this.connection = connection;
    }

    @Override
    public final SQLDeleteClause delete(RelationalPath<?> path) {
        return new SQLDeleteClause(connection.get(), configuration, path);
    }

    @Override
    public final Q from(Expression<?> from) {
        return (Q) query().from(from);
    }

    public final Q from(Expression<?>... args) {
        return (Q) query().from(args);
    }

    public final Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return (Q) query().from(subQuery, alias);
    }

    @Override
    public final SQLInsertClause insert(RelationalPath<?> path) {
        return new SQLInsertClause(connection.get(), configuration, path);
    }

    @Override
    public final SQLMergeClause merge(RelationalPath<?> path) {
        return new SQLMergeClause(connection.get(), configuration, path);
    }

    @Override
    public final SQLUpdateClause update(RelationalPath<?> path) {
        return new SQLUpdateClause(connection.get(), configuration, path);
    }

    @SuppressWarnings("unchecked")
    @Override
    @WithBridgeMethods(value=SQLSubQuery.class, castRequired=true)
    public SQ subQuery() {
        return (SQ) new SQLSubQuery();
    }

    @Override
    @WithBridgeMethods(value=SQLSubQuery.class, castRequired=true)
    public final SQ subQuery(Expression<?> from) {
        return subQuery().from(from);
    }

    public final Configuration getConfiguration() {
        return configuration;
    }

    public final Connection getConnection() {
        return connection.get();
    }

}
