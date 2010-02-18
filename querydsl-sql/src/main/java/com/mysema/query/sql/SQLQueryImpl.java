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
     * Create a new SQLQueryImpl instance
     * 
     * @param conn Connection to use
     * @param templates SQLTemplates to use
     */
    public SQLQueryImpl(Connection conn, SQLTemplates templates) {
        super(conn, templates, new DefaultQueryMetadata());
    }

    /**
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
        return new SQLQueryImpl(conn, templates, getMetadata().clone());   
    }
    
}
