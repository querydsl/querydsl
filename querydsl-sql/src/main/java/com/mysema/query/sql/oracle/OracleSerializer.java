/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.oracle;

import com.mysema.query.sql.SQLPatterns;
import com.mysema.query.sql.SQLSerializer;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * OracleSerializer extended the SqlSerializer to support Oracle specific
 * constructs
 * 
 * @author tiwe
 * @version $Id$
 */
public class OracleSerializer extends SQLSerializer {

    private EBoolean connectBy, connectByPrior, connectByNocyclePrior;

    private Expr<?> orderSiblingsBy;

    private EBoolean startWith;

    public OracleSerializer(SQLPatterns ops, EBoolean connectBy,
            EBoolean connectByNocyclePrior, EBoolean connectByPrior,
            Expr<?> orderSiblingsBy, EBoolean startWith) {
        super(ops);
        this.connectBy = connectBy;
        this.connectByNocyclePrior = connectByNocyclePrior;
        this.connectByPrior = connectByPrior;
        this.orderSiblingsBy = orderSiblingsBy;
        this.startWith = startWith;
    }

    @Override
    protected void beforeOrderBy() {
        if (startWith != null)
            append(ops.startWith()).handle(startWith);
        if (connectBy != null)
            append(ops.connectBy()).handle(connectBy);
        if (connectByPrior != null)
            append(ops.connectByPrior()).handle(connectByPrior);
        if (connectByNocyclePrior != null)
            append(ops.connectByNocyclePrior()).handle(connectByNocyclePrior);
        if (orderSiblingsBy != null)
            append(ops.orderSiblingsBy()).handle(orderSiblingsBy);
    }
}