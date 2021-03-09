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
package com.querydsl.r2dbc.mssql;

import com.querydsl.core.JoinFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.r2dbc.AbstractR2DBCQuery;
import com.querydsl.r2dbc.Configuration;
import com.querydsl.r2dbc.R2DBCConnectionProvider;
import io.r2dbc.spi.Connection;

/**
 * {@code AbstractSQLServerQuery} provides SQL Server related extensions to SQLQuery
 *
 * @param <T> result type
 * @param <C> the concrete subtype
 * @author mc_fish
 */
public abstract class AbstractR2DBCSQLServerQuery<T, C extends AbstractR2DBCSQLServerQuery<T, C>> extends AbstractR2DBCQuery<T, C> {
    public AbstractR2DBCSQLServerQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public AbstractR2DBCSQLServerQuery(R2DBCConnectionProvider connProvider, Configuration configuration, QueryMetadata metadata) {
        super(connProvider, configuration, metadata);
    }

    /**
     * Set the table hints
     *
     * @param tableHints table hints
     * @return the current object
     */
    public C tableHints(SQLServerTableHints... tableHints) {
        if (tableHints.length > 0) {
            String hints = SQLServerGrammar.tableHints(tableHints);
            addJoinFlag(hints, JoinFlag.Position.BEFORE_CONDITION);
        }
        return (C) this;
    }
}
