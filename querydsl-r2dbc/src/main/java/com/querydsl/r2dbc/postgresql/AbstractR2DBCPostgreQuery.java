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
package com.querydsl.r2dbc.postgresql;

import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.r2dbc.AbstractR2DBCQuery;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import com.querydsl.r2dbc.R2DBCQuery;
import com.querydsl.sql.RelationalPath;
import io.r2dbc.spi.Connection;

/**
 * {@code PostgreSQLQuery} provides PostgreSQL related extensions to SQLQuery
 *
 * @param <T> result type
 * @param <C> the concrete subtype
 * @author tiwe
 * @see R2DBCQuery
 */
public abstract class AbstractR2DBCPostgreQuery<T, C extends AbstractR2DBCPostgreQuery<T, C>> extends AbstractR2DBCQuery<T, C> {
    public AbstractR2DBCPostgreQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public AbstractR2DBCPostgreQuery(R2DBCConnectionProvider connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    /**
     * FOR SHARE causes the rows retrieved by the SELECT statement to be locked as though for update.
     *
     * @return the current object
     */
    @WithBridgeMethods(value = R2DBCPostgreQuery.class, castRequired = true)
    public C forShare() {
        // global forShare support was added later, delegating to super implementation
        return super.forShare();
    }

    /**
     * With NOWAIT, the statement reports an error, rather than waiting, if a selected row cannot
     * be locked immediately.
     *
     * @return the current object
     */
    @WithBridgeMethods(value = R2DBCPostgreQuery.class, castRequired = true)
    public C noWait() {
        QueryFlag noWaitFlag = configuration.getTemplates().getNoWaitFlag();
        return addFlag(noWaitFlag);
    }

    /**
     * FOR UPDATE / FOR SHARE OF tables
     *
     * @param paths tables
     * @return the current object
     */
    @WithBridgeMethods(value = R2DBCPostgreQuery.class, castRequired = true)
    public C of(RelationalPath<?>... paths) {
        StringBuilder builder = new StringBuilder(" of ");
        for (RelationalPath<?> path : paths) {
            if (builder.length() > 4) {
                builder.append(", ");
            }
            builder.append(getConfiguration().getTemplates().quoteIdentifier(path.getTableName()));
        }
        return addFlag(Position.END, builder.toString());
    }

    /**
     * adds a DISTINCT ON clause
     *
     * @param exprs
     * @return
     */
    @WithBridgeMethods(value = R2DBCPostgreQuery.class, castRequired = true)
    public C distinctOn(Expression<?>... exprs) {
        return addFlag(Position.AFTER_SELECT,
                Expressions.template(Object.class, "distinct on({0}) ",
                        ExpressionUtils.list(Object.class, exprs)));
    }

}
