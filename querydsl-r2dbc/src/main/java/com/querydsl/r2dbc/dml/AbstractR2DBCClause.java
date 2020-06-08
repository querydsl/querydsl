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

import com.querydsl.core.QueryMetadata;
import com.querydsl.core.dml.ReactiveDMLClause;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.Path;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.SQLSerializer;
import com.querydsl.sql.SQLBindings;
import com.querydsl.sql.types.Null;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.slf4j.Logger;
import reactor.core.publisher.Mono;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;

/**
 * {@code AbstractSQLClause} is a superclass for SQL based DMLClause implementations
 *
 * @param <C> concrete subtype
 * @author tiwe
 */
public abstract class AbstractR2DBCClause<C extends AbstractR2DBCClause<C>> implements ReactiveDMLClause<C> {

    protected final Configuration configuration;

    protected boolean useLiterals;

    @Nullable
    private R2DBCConnectionProvider connProvider;

    @Nullable
    private Connection conn;

    public AbstractR2DBCClause(Configuration configuration) {
        this.configuration = configuration;
        this.useLiterals = configuration.getUseLiterals();
    }

    public AbstractR2DBCClause(Configuration configuration, R2DBCConnectionProvider connProvider) {
        this(configuration);
        this.connProvider = connProvider;
    }

    public AbstractR2DBCClause(Configuration configuration, Connection conn) {
        this(configuration);
        this.conn = conn;
    }

    /**
     * Clear the internal state of the clause
     */
    public abstract void clear();

    protected SQLBindings createBindings(QueryMetadata metadata, SQLSerializer serializer) {
        String queryString = serializer.toString();
        List<Object> args = newArrayList();
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
        return new SQLBindings(queryString, args);
    }

    protected SQLSerializer createSerializer(boolean dml) {
        SQLSerializer serializer = new SQLSerializer(configuration, dml);
        serializer.setUseLiterals(useLiterals);
        return serializer;
    }

    /**
     * Get the SQL string and bindings
     *
     * @return SQL and bindings
     */
    public abstract List<SQLBindings> getSQL();

    protected final Mono<Connection> getConnection() {
        if (connProvider != null) {
            return connProvider.getConnection();
        }

        if (conn != null) {
            return Mono.just(conn);
        }

        return Mono.error(new IllegalStateException("No connection provided"));
    }

    /**
     * Set the parameters to the given Statement
     *
     * @param stmt          Statement to be populated
     * @param objects       list of constants
     * @param constantPaths list of paths related to the constants
     * @param params        map of param to value for param resolving
     * @param offset        offset in the batch
     */
    protected void setParameters(Statement stmt, List<?> objects, List<Path<?>> constantPaths,
                                 Map<ParamExpression<?>, ?> params, int offset) {
        if (objects.size() != constantPaths.size()) {
            throw new IllegalArgumentException("Expected " + objects.size() + " paths, " +
                    "but got " + constantPaths.size());
        }
        for (int i = 0; i < objects.size(); i++) {
            Object o = objects.get(i);
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = params.get(o);
            }
            bind(stmt, constantPaths.get(i), (offset * objects.size()) + i, o);
        }
    }

    private <T> void bind(Statement stmt, Path<?> path, int i, T value) {
        if (value == null || value instanceof Null) {
            if (path != null) {
                stmt.bindNull(i, path.getType());
            }
        } else {
            stmt.bind(i, value);
        }
    }

    protected void logQuery(Logger logger, String queryString, Collection<Object> parameters) {
        if (logger.isDebugEnabled()) {
            String normalizedQuery = queryString.replace('\n', ' ');
            logger.debug(normalizedQuery);
        }
    }

    public void setUseLiterals(boolean useLiterals) {
        this.useLiterals = useLiterals;
    }

    public abstract int getBatchCount();

}
