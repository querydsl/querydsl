/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;

import com.mysema.query.grammar.SqlOps;

/**
 * SqlQuery is a JDBC based implementation of the Querydsl Query interface
 *
 * @author tiwe
 * @version $Id$
 */
public class SqlQuery extends AbstractSqlQuery<SqlQuery>{

    public SqlQuery(Connection conn, SqlOps ops) {
        super(conn, ops);
    }

}
