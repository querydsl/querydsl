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
package com.mysema.query.sql.dml;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.mysema.query.QueryException;
import com.mysema.query.QueryMetadata;
import com.mysema.query.dml.DMLClause;
import com.mysema.query.sql.Configuration;
import com.mysema.query.sql.SQLBindings;
import com.mysema.query.sql.SQLListener;
import com.mysema.query.sql.SQLListeners;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.types.ParamExpression;
import com.mysema.query.types.ParamNotSetException;
import com.mysema.query.types.Path;

/**
 * AbstractSQLClause is a superclass for SQL based DMLClause implementations
 *
 * @author tiwe
 *
 */
public abstract class AbstractSQLClause<C extends AbstractSQLClause<C>> implements DMLClause<C> {

    protected final Configuration configuration;

    protected final SQLListeners listeners;

    /**
     * @param configuration
     */
    public AbstractSQLClause(Configuration configuration) {
        this.configuration = configuration;
        this.listeners = new SQLListeners(configuration.getListeners());
    }

    /**
     * @param listener
     */
    public void addListener(SQLListener listener) {
        listeners.add(listener);
    }

    protected SQLBindings createBindings(QueryMetadata metadata, SQLSerializer serializer) {
        String queryString = serializer.toString();
        ImmutableList.Builder<Object> args = ImmutableList.builder();
        Map<ParamExpression<?>, Object> params = metadata.getParams();
        for (Object o : serializer.getConstants()) {
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = metadata.getParams().get(o);
            }
            args.add(o);
        }
        return new SQLBindings(queryString, args.build());
    }

    /**
     * Get the SQL string and bindings
     *
     * @return
     */
    public abstract List<SQLBindings> getSQL();

    /**
     * Set the parameters to the given PreparedStatement
     *
     * @param stmt preparedStatement to be populated
     * @param objects list of constants
     * @param constantPaths list of paths related to the constants
     * @param params map of param to value for param resolving
     */
    protected void setParameters(PreparedStatement stmt, List<?> objects,
            List<Path<?>> constantPaths, Map<ParamExpression<?>, ?> params) {
        if (objects.size() != constantPaths.size()) {
            throw new IllegalArgumentException("Expected " + objects.size() + " paths, " +
            		"but got " + constantPaths.size());
        }
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            try {
                if (o instanceof ParamExpression) {
                    if (!params.containsKey(o)) {
                        throw new ParamNotSetException((ParamExpression<?>) o);
                    }
                    o = params.get(o);
                }
                configuration.set(stmt, constantPaths.get(i), i+1, o);
            } catch (SQLException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    protected long executeBatch(PreparedStatement stmt) throws SQLException {
        if (configuration.getTemplates().isBatchCountViaGetUpdateCount()) {
            stmt.executeBatch();
            return stmt.getUpdateCount();
        } else {
            long rv = 0;
            for (int i : stmt.executeBatch()) {
                rv += i;
            }
            return rv;
        }
    }

    protected void close(Statement stmt) {
        try {
            stmt.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

    protected void close(ResultSet rs) {
        try {
            rs.close();
        } catch (SQLException e) {
            throw new QueryException(e);
        }
    }

}
