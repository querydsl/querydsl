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
import java.sql.ResultSet;

import com.mysema.query.Projectable;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
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
     * If you use forUpdate() with a backend that uses page or row locks, rows examined by the 
     * query are write-locked until the end of the current transaction
     * 
     * @return
     */
    SQLQuery forUpdate();
    
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
    <RT> SQLQuery union(Path<?> alias, ListSubQuery<RT>... sq);

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(SubQueryExpression<RT>... sq);
    
    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> SQLQuery union(Path<?> alias, SubQueryExpression<RT>... sq);
    
    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> unionAll(ListSubQuery<RT>... sq);
    
    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> SQLQuery unionAll(Path<?> alias, ListSubQuery<RT>... sq);

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq);


    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> SQLQuery unionAll(Path<?> alias, SubQueryExpression<RT>... sq);
        
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
