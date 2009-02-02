/**
 * 
 */
package com.mysema.query.grammar;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * OracleSerializer extended the SqlSerializer to support Oracle specific constructs
 *
 * @author tiwe
 * @version $Id$
 */
public class OracleSerializer extends SqlSerializer {
    
    private EBoolean connectBy, connectByPrior, connectByNocyclePrior;

    private Expr<?> orderSiblingsBy;
    
    private EBoolean startWith;
    
    public OracleSerializer(SqlOps ops, EBoolean connectBy, EBoolean connectByNocyclePrior,
            EBoolean connectByPrior, Expr<?> orderSiblingsBy,
            EBoolean startWith) {
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