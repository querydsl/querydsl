/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;

/**
 * SqlQuery is a JDBC based implementation of the Querydsl Query interface
 * 
 * @author tiwe
 * @version $Id$
 */
public class SQLQueryImpl extends AbstractSQLQuery<SQLQueryImpl> implements SQLQuery{

    public SQLQueryImpl(Connection conn, SQLTemplates templates) {
        super(conn, templates);
    }

}
