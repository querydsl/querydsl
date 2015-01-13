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

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.QueryMetadata;
import java.sql.Connection;

/**
 * SQLQuery is a JDBC based implementation of the {@link SQLCommonQuery}
 * interface
 *
 * @author tiwe
 */
public class SQLQuery extends AbstractSQLQuery<SQLQuery> {

    /**
     * Create a detached SQLQuery instance The querydsl can be attached via the
     * clone method
     *
     * @param templates SQLTemplates to use
     */
    public SQLQuery(SQLTemplates templates) {
        super(null, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     */
    public SQLQuery(Connection conn, SQLTemplates templates) {
        super(conn, new Configuration(templates), new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     * @param metadata
     */
    public SQLQuery(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, new Configuration(templates), metadata);
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param configuration
     */
    public SQLQuery(Configuration configuration) {
        this(null, configuration);
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn Connection to use
     * @param configuration
     */
    public SQLQuery(Connection conn, Configuration configuration) {
        super(conn, configuration, new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQuery instance
     *
     * @param conn
     * @param configuration
     * @param metadata
     */
    public SQLQuery(Connection conn, Configuration configuration, QueryMetadata metadata) {
        super(conn, configuration, metadata);
    }

    @Override
    public SQLQuery clone(Connection conn) {
        SQLQuery q = new SQLQuery(conn, getConfiguration(), getMetadata().clone());
        q.clone(this);
        return q;
    }

}
