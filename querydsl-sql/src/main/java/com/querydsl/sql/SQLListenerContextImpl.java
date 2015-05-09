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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.querydsl.core.QueryMetadata;

/**
 * A mutable implementation of SQL listener context.
 * <p>
 * INTERNAL USE ONLY - {@link com.querydsl.sql.SQLDetailedListener} implementations are not expected to use this
 * class directly
 */
public class SQLListenerContextImpl implements SQLListenerContext {
    private final Map<String, Object> contextMap;

    private final QueryMetadata md;

    private final List<String> sqlStatements;

    private final List<PreparedStatement> preparedStatements;

    private RelationalPath<?> entity;

    private Connection connection;

    private Exception exception;

    public SQLListenerContextImpl(final QueryMetadata metadata, final Connection connection, final RelationalPath<?> entity) {
        this.contextMap = Maps.newHashMap();
        this.preparedStatements = Lists.newArrayList();
        this.sqlStatements = Lists.newArrayList();
        this.md = metadata;
        this.connection = connection;
        this.entity = entity;
    }

    public SQLListenerContextImpl(final QueryMetadata metadata, final Connection connection) {
        this(metadata, connection, null);
    }

    public SQLListenerContextImpl(final QueryMetadata metadata) {
        this(metadata, null, null);
    }

    public void addSQL(final String sql) {
        this.sqlStatements.add(sql);
    }

    public void setEntity(final RelationalPath<?> entity) {
        this.entity = entity;
    }

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }

    public void setException(final Exception exception) {
        this.exception = exception;
    }

    public void addPreparedStatement(final PreparedStatement preparedStatement) {
        this.preparedStatements.add(preparedStatement);
    }

    @Override
    public QueryMetadata getMetadata() {
        return md;
    }

    @Override
    public RelationalPath<?> getEntity() {
        return entity;
    }

    @Override
    public String getSQL() {
        return sqlStatements.isEmpty() ? null : sqlStatements.get(0);
    }

    @Override
    public Collection<String> getSQLStatements() {
        return sqlStatements;
    }

    @Override
    public Exception getException() {
        return exception;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public Collection<PreparedStatement> getPreparedStatements() {
        return preparedStatements;
    }

    @Override
    public PreparedStatement getPreparedStatement() {
        return preparedStatements.isEmpty() ? null : preparedStatements.get(0);
    }

    @Override
    public Object getData(final String dataKey) {
        return contextMap.get(dataKey);
    }

    @Override
    public void setData(final String dataKey, final Object value) {
        contextMap.put(dataKey, value);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder()
                .append(" sql:").append(nicerSql(getSQL()))
                .append(" connection:").append(connection == null ? "not connected" : "connected")
                .append(" entity:").append(entity)
                .append(" exception:").append(exception);

        for (Map.Entry<String, Object> entry : contextMap.entrySet()) {
            sb.append(" [").append(entry.getKey()).append(":").append(entry.getValue()).append("]");
        }
        return sb.toString();
    }

    private String nicerSql(final String sql) {
        return "'" + (sql == null ? null : sql.replace('\n', ' ')) + "'";
    }
}
