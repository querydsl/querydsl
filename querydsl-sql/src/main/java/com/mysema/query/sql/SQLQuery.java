/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;

import com.mysema.query.Projectable;
import com.mysema.query.Query;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * Query interface for SQL queries
 * 
 * @author tiwe
 *
 */
public interface SQLQuery extends Query<SQLQuery>, Projectable {
  
    /**
     * @param o
     * @return
     */
    SQLQuery from(PEntity<?>... o);

    /**
     * @param o
     * @return
     */
    SQLQuery fullJoin(PEntity<?> o);

    /**
     * @param o
     * @return
     */
    SQLQuery innerJoin(PEntity<?> o);

    /**
     * @param o
     * @return
     */
    SQLQuery join(PEntity<?> o);
    
    /**
     * @param o
     * @return
     */
    SQLQuery leftJoin(PEntity<?> o);
    
    /**
     * @param conditions
     * @return
     */
    SQLQuery on(EBoolean... conditions);
    
    /**
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(ListSubQuery<RT>... sq);
    
    /**
     * @param <RT>
     * @param sq
     * @return
     */
    <RT> Union<RT> union(ObjectSubQuery<RT>... sq);
    
    /**
     * @param conn
     * @return
     */
    SQLQuery clone(Connection conn);

}
