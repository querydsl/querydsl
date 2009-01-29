/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;

import com.mysema.query.grammar.OracleSerializer;
import com.mysema.query.grammar.SqlOps;
import com.mysema.query.grammar.SqlSerializer;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * OracleQuery provides Oracle specific extensions 
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleQuery extends AbstractSqlQuery<OracleQuery>{

    private EBoolean connectBy, connectByPrior, connectByNocyclePrior;

    private Expr<?> orderSiblingsBy;
    
    private EBoolean startWith;
        
    public OracleQuery(Connection conn, SqlOps ops) {
        super(conn, ops);    
    }    
    
    public OracleQuery connectByPrior(EBoolean cond){
        connectByPrior = cond;
        return this;
    }
    
    public OracleQuery connectBy(EBoolean cond) {
        connectBy = cond;
        return this;
    }
    
    public OracleQuery connectByNocyclePrior(EBoolean cond){
        connectByNocyclePrior = cond;
        return this;
    }
    
    public <A> OracleQuery startWith(EBoolean cond){
        startWith = cond;
        return this;
    }
    
    public OracleQuery orderSiblingsBy(Expr<?> path){
        orderSiblingsBy = path;
        return this;
    }
    
    protected SqlSerializer createSerializer(){
        return new OracleSerializer(ops, 
            connectBy, connectByNocyclePrior, connectByPrior, 
            orderSiblingsBy, startWith);
    }
    
    // TODO : connect by root
    
    // TODO : connect by iscycle
    
    // TODO : connect by isleaf (pseudocolumn)
    
    // TODO : sys connect path
}
