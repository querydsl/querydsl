/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;

import com.mysema.query.grammar.SqlOps;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Path;

/**
 * OracleQuery provides Oracle specific extensions 
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleQuery extends AbstractSqlQuery<OracleQuery>{

    private EBoolean connectByPrior;
    
    private Path<?> orderSiblingBy;
    
    private Path<?>[] startWith;
    
    public OracleQuery(Connection conn, SqlOps ops) {
        super(conn, ops);    
    }    
    
    public OracleQuery connectByPrior(EBoolean cond){
        connectByPrior = cond;
        return this;
    }
    
    public <A> OracleQuery startWith(Path<A> path1, Path<A> path2){
        startWith = new Path[]{path1,path2};
        return this;
    }
    
    public OracleQuery orderSiblingBy(Path<?> path){
        orderSiblingBy = path;
        return this;
    }
    
    // TODO : connect by root
    
    // TODO : connect by iscycle
    
    // TODO : connect by isleaf (pseudocolumn)
    
    // TODO : level (pseudocolumn)
    
    // TODO : sys connect path
}
