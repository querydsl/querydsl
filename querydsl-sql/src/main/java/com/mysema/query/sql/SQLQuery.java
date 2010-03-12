/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.SubQuery;

/**
 * Query interface for SQL queries
 * 
 * @author tiwe
 *
 */
public interface SQLQuery extends Query<SQLQuery>, Projectable {
  
    /**
     * Defines the sources of the query
     * 
     * @param o
     * @return
     */
    SQLQuery from(Expr<?>... o);

    /**
     * Adds a full join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery fullJoin(PEntity<?> o);

    /**
     * Adds an inner join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery innerJoin(PEntity<?> o);

    /**
     * Adds a join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery join(PEntity<?> o);
    
    /**
     * Adds a left join to the given target
     * 
     * @param o
     * @return
     */
    SQLQuery leftJoin(PEntity<?> o);
    
    /**
     * Defines a filter to the last added join
     * 
     * @param conditions
     * @return
     */
    SQLQuery on(EBoolean... conditions);
    
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
    <RT> Union<RT> union(SubQuery<RT>... sq);
    
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
    ResultSet getResults(Expr<?>... args);
}
