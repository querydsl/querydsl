/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.sql.Connection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;

/**
 * SQLQueryImpl is a JDBC based implementation of the Querydsl SQLQuery interface
 *
 * @author tiwe
 */
public class SQLQueryImpl extends AbstractSQLQuery<SQLQueryImpl> implements SQLQuery{

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
    public SQLQueryImpl clone(Connection conn){
        return new SQLQueryImpl(conn, getConfiguration(), getMetadata().clone());
    }

}
