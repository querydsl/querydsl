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
package com.querydsl.sql.teradata;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.inject.Provider;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.SQLTemplates;
import com.querydsl.sql.dml.AbstractSQLClause;

/**
 * {@code SetQueryBandClause} provides support for Teradata specific set query_band executions.
 *
 * @author tiwe
 *
 */
public class SetQueryBandClause extends AbstractSQLClause<SetQueryBandClause> {

    private boolean forSession = true;

    private final Map<String, String> values = Maps.newLinkedHashMap();

    private transient String queryString;

    private transient String parameter;

    public SetQueryBandClause(Connection connection, SQLTemplates templates) {
        this(connection, new Configuration(templates));
    }

    public SetQueryBandClause(Connection connection, Configuration configuration) {
        super(configuration, connection);
    }

    public SetQueryBandClause(Provider<Connection> connection, Configuration configuration) {
        super(configuration, connection);
    }

    public SetQueryBandClause forSession() {
        queryString = null;
        forSession = true;
        return this;
    }

    public SetQueryBandClause forTransaction() {
        queryString = null;
        forSession = false;
        return this;
    }

    public SetQueryBandClause set(String key, String value) {
        queryString = null;
        values.put(key, value);
        return this;
    }

    public SetQueryBandClause set(Map<String, String> values) {
        queryString = null;
        this.values.putAll(values);
        return this;
    }

    @Override
    public void clear() {
        values.clear();
    }

    @Override
    public long execute() {
        PreparedStatement stmt = null;
        try {
            stmt = connection().prepareStatement(toString());
            if (parameter != null) {
                stmt.setString(1, parameter);
            }
            return 1;
        } catch (SQLException e) {
            List<Object> bindings = parameter != null ? ImmutableList.<Object>of(parameter) : ImmutableList.of();
            throw configuration.translate(queryString, bindings, e);
        } finally {
            if (stmt != null) {
                close(stmt);
            }
        }
    }

    @Override
    public List<SQLBindings> getSQL() {
        SQLBindings bindings;
        if (configuration.getUseLiterals() || forSession) {
            bindings = new SQLBindings(toString(), ImmutableList.of());
        } else {
            bindings = new SQLBindings(toString(), ImmutableList.<Object>of(parameter));
        }
        return ImmutableList.of(bindings);
    }

    @Override
    public String toString() {
        if (queryString == null) {
            StringBuilder builder = new StringBuilder();
            for (Map.Entry<String, String> entry : values.entrySet()) {
                builder.append(entry.getKey()).append("=").append(entry.getValue());
                builder.append(";");
            }
            if (configuration.getUseLiterals() || forSession) {
                queryString = "set query_band='"
                        + configuration.getTemplates().escapeLiteral(builder.toString())
                        + (forSession ? "' for session" : "' for transaction");
                parameter = null;
            } else {
                queryString = "set query_band=? for transaction";
                parameter = builder.toString();
            }

        }
        return queryString;
    }

    @Override
    public int getBatchCount() {
        return 0;
    }
}
