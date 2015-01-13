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
package com.querydsl.sql.mssql;

import java.sql.Connection;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.sql.AbstractSQLQuery;
import com.querydsl.sql.Configuration;
import com.querydsl.sql.SQLServerTemplates;
import com.querydsl.sql.SQLTemplates;

/**
 * SQLServerQuery provides SQL Server related extensions to SQLQuery
 *
 * @author tiwe
 *
 */
public class SQLServerQuery extends AbstractSQLQuery<SQLServerQuery> {

    public SQLServerQuery(Connection conn) {
        this(conn, new SQLServerTemplates(), new DefaultQueryMetadata());
    }

    public SQLServerQuery(Connection conn, SQLTemplates templates) {
        this(conn, templates, new DefaultQueryMetadata());
    }

    protected SQLServerQuery(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    public SQLServerQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    public SQLServerQuery(Connection conn, Configuration configuration) {
        super(conn, configuration);
    }

    /**
     * @param tableHints
     * @return
     */
    public SQLServerQuery tableHints(SQLServerTableHints... tableHints) {
        if (tableHints.length > 0) {
            String hints = SQLServerGrammar.tableHints(tableHints);
            addJoinFlag(hints, JoinFlag.Position.END);
        }
        return this;
    }
    
    @Override
    public SQLServerQuery clone(Connection conn) {
        SQLServerQuery q = new SQLServerQuery(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

}