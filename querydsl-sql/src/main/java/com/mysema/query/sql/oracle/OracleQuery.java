/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.oracle;

import java.sql.Connection;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.AbstractSQLQuery;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * OracleQuery provides Oracle specific extensions to the base SQL query type
 * 
 * @author tiwe
 * @version $Id$
 */
public final class OracleQuery extends AbstractSQLQuery<OracleQuery> {

    private EBoolean connectBy, connectByPrior, connectByNocyclePrior;

    private Expr<?> orderSiblingsBy;

    private EBoolean startWith;

    public OracleQuery(Connection conn, SQLTemplates patterns) {
        super(conn, patterns, new DefaultQueryMetadata());
    }
    
    protected OracleQuery(Connection conn, SQLTemplates patterns, QueryMetadata metadata) {
        super(conn, patterns, metadata);
    }

    public OracleQuery connectByPrior(EBoolean cond) {
        connectByPrior = cond;
        return this;
    }

    public OracleQuery connectBy(EBoolean cond) {
        connectBy = cond;
        return this;
    }

    public OracleQuery connectByNocyclePrior(EBoolean cond) {
        connectByNocyclePrior = cond;
        return this;
    }

    public <A> OracleQuery startWith(EBoolean cond) {
        startWith = cond;
        return this;
    }

    public OracleQuery orderSiblingsBy(Expr<?> path) {
        orderSiblingsBy = path;
        return this;
    }

    protected SQLSerializer createSerializer() {
        return new OracleSerializer(getTemplates(), connectBy, connectByNocyclePrior,
                connectByPrior, orderSiblingsBy, startWith);
    }
    
    /**
     * Clone the state of this query to a new SQLQueryImpl instance with the given Connection
     * 
     * @param conn
     * @return
     */
    public OracleQuery clone(Connection conn){
        return new OracleQuery(conn, getTemplates(), getMetadata().clone());   
    }

    // TODO : connect by root

    // TODO : connect by iscycle

    // TODO : connect by isleaf (pseudocolumn)

    // TODO : sys connect path
}
