/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.sql.oracle;

import com.mysema.query.sql.SQLTemplates;
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

    public OracleSerializer(SQLTemplates patterns, EBoolean connectBy,
            EBoolean connectByNocyclePrior, EBoolean connectByPrior,
            Expr<?> orderSiblingsBy, EBoolean startWith) {
        super(patterns);
        this.connectBy = connectBy;
        this.connectByNocyclePrior = connectByNocyclePrior;
        this.connectByPrior = connectByPrior;
        this.orderSiblingsBy = orderSiblingsBy;
        this.startWith = startWith;
    }

    @Override
    protected void beforeOrderBy() {
        if (startWith != null){
            append(templates.startWith()).handle(startWith);
        }            
        if (connectBy != null){
            append(templates.connectBy()).handle(connectBy);
        }            
        if (connectByPrior != null){
            append(templates.connectByPrior()).handle(connectByPrior);
        }            
        if (connectByNocyclePrior != null){
            append(templates.connectByNocyclePrior()).handle(connectByNocyclePrior);
        }            
        if (orderSiblingsBy != null){
            append(templates.orderSiblingsBy()).handle(orderSiblingsBy);
        }            
    }
}