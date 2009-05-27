/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.oracle;

import java.sql.Connection;

import com.mysema.query.sql.AbstractSQLQuery;
import com.mysema.query.sql.SQLPatterns;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * OracleQuery provides Oracle specific extensions to the base SQL query type
 * 
 * @author tiwe
 * @version $Id$
 */
public class OracleQuery extends AbstractSQLQuery<OracleQuery> {

    private EBoolean connectBy, connectByPrior, connectByNocyclePrior;

    private Expr<?> orderSiblingsBy;

    private EBoolean startWith;

    public OracleQuery(Connection conn, SQLPatterns ops) {
        super(conn, ops);
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
        return new OracleSerializer(ops, connectBy, connectByNocyclePrior,
                connectByPrior, orderSiblingsBy, startWith);
    }

    // TODO : connect by root

    // TODO : connect by iscycle

    // TODO : connect by isleaf (pseudocolumn)

    // TODO : sys connect path
}
