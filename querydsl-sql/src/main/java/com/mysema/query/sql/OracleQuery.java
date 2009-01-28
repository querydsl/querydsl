/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql;

import java.sql.Connection;

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

    private EBoolean connectByPrior, connectByNocyclePrior, startWith;
    
    private Expr<?> orderSiblingsBy;
    
    public OracleQuery(Connection conn, SqlOps ops) {
        super(conn, ops);    
    }    
    
    public OracleQuery connectByPrior(EBoolean cond){
        connectByPrior = cond;
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
        return new SqlSerializer(ops){
            @Override
            protected void beforeOrderBy() {
                if (startWith != null){
                    _append(ops.startWith()).handle(startWith);
                }
                if (connectByPrior != null){
                    _append(ops.connectByPrior()).handle(connectByPrior);
                }
                if (connectByNocyclePrior != null){
                    _append(ops.connectByNocyclePrior()).handle(connectByNocyclePrior);
                }                
                if (orderSiblingsBy != null){
                    _append(ops.orderSiblingsBy()).handle(orderSiblingsBy);
                }
            }
        };
    }
    
    // TODO : connect by root
    
    // TODO : connect by iscycle
    
    // TODO : connect by isleaf (pseudocolumn)
    
    // TODO : sys connect path
}
