/*
 * Copyright (c) 2009 Mysema Ltd.
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
 * @version $Id$
 */
public class SQLQueryImpl extends AbstractSQLQuery<SQLQueryImpl> implements SQLQuery{
    
    /**
     * Create a detached SQLQueryImpl instance
     * The query can be attached via the clone method
     * 
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     */
    public SQLQueryImpl(SQLTemplates templates) {
        super(null, templates, new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQueryImpl instance
     * 
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     */
    public SQLQueryImpl(Connection conn, SQLTemplates templates) {
        super(conn, templates, new DefaultQueryMetadata());
    }

    /**
     * Create a new SQLQueryImpl instance
     * 
     * @param conn
     * @param templates
     * @param metadata
     */
    protected SQLQueryImpl(Connection conn, SQLTemplates templates, QueryMetadata metadata) {
        super(conn, templates, metadata);
    }

    /**
     * Clone the state of this query to a new SQLQueryImpl instance with the given Connection
     * 
     * @param conn
     * @return
     */
    public SQLQueryImpl clone(Connection conn){
        return new SQLQueryImpl(conn, getTemplates(), getMetadata().clone());   
    }
    
}
