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
package com.mysema.query.sql;

import java.sql.Connection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;

/**
 * SQLQueryImpl is a JDBC based implementation of the {@link SQLQuery} interface
 *
 * @author tiwe
 */
public class SQLQueryImpl extends AbstractSQLQuery<SQLQueryImpl> implements SQLQuery {

    /**
     * Create a detached SQLQueryImpl instance
     * The query can be attached via the clone method
     *
     * @param connection Connection to use
     * @param templates SQLTemplates to use
     */
    public SQLQueryImpl(SQLTemplates templates) {
        super(null, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQueryImpl instance
     *
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     */
    public SQLQueryImpl(Connection conn, SQLTemplates templates) {
        super(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQueryImpl instance
     *
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     * @param metadata
     */
    public SQLQueryImpl(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    /**
     * Create a new SQLQueryImpl instance
     *
     * @param conn Connection to use
     * @param configuration
     */
    public SQLQueryImpl(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQueryImpl instance
     *
     * @param conn
     * @param templates
     * @param metadata
     */
    public SQLQueryImpl(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    /**
     * Clone the state of this query to a new SQLQueryImpl instance with the given Connection
     *
     * @param conn
     * @return
     */
    public SQLQueryImpl clone(Connection conn) {
        SQLQueryImpl q = new SQLQueryImpl(conn, getConfiguration(), getMetadata().clone());
        q.union = union;
        q.unionAll = unionAll;
        return q;
    }

}
