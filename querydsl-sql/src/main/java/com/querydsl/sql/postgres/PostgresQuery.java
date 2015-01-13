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
package com.querydsl.sql.postgres;

import java.sql.Connection;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.PostgresTemplates;
import com.querydsl.sql.RelationalPath;
import com.querydsl.sql.SQLOps;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.SQLTemplates;

/**
 * PostgresQuery provides Postgres related extensions to SQLQuery
 *
 * @author tiwe
 * @see SQLQuery
 *
 */
public class PostgresQuery extends AbstractSQLQuery<PostgresQuery> {

    public PostgresQuery(Connection conn) {
        this(conn, new Configuration(new PostgresTemplates()), new DefaultQueryMetadata());
    }

    public PostgresQuery(Connection conn, SQLTemplates templates) {
        this(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    public PostgresQuery(Connection conn, Configuration configuration) {
        this(conn, configuration, new DefaultQueryMetadata());
    }

    public PostgresQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    /**
     * @return
     */
    public PostgresQuery forShare() {
        return addFlag(SQLOps.FOR_SHARE_FLAG);
    }

    /**
     * @return
     */
    public PostgresQuery noWait() {
        return addFlag(SQLOps.NO_WAIT_FLAG);
    }

    /**
     * @param paths
     * @return
     */
    public PostgresQuery of(RelationalPath<?>... paths) {
        StringBuilder builder = new StringBuilder(" of ");
        for (RelationalPath<?> path : paths) {
            if (builder.length() > 4) {
                builder.append(", ");
            }
            builder.append(getConfiguration().getTemplates().quoteIdentifier(path.getTableName()));
        }
        return addFlag(Position.END, builder.toString());
    }
 
    @Override
    public PostgresQuery clone(Connection conn) {
        PostgresQuery q = new PostgresQuery(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

}
