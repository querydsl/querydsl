/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.mysema.query.Projectable;
import com.mysema.query.types.Expression;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.query.ListSubQuery;

/**
 * Query interface for SQL queries
 *
 * @author tiwe
 *
 */
public interface SQLQuery extends SQLCommonQuery<SQLQuery>, Projectable {

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(ListSubQuery<RT>... sq);

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(SubQueryExpression<RT>... sq);

    /**
     * Clone the state of the Query for the given Connection
     *
     * @param conn
     * @return
     */
    SQLQuery clone(Connection conn);

    /**
     * Get the results as an JDBC result set
     *
     * @param args
     * @return
     */
    ResultSet getResults(Expression<?>... args);
}
