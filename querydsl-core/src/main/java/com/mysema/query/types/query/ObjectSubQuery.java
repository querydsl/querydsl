/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;

/**
 * Single result subquery
 * 
 * @author tiwe
 *
 * @param <JM>
 * @param <A>
 */
@SuppressWarnings("serial")
public class ObjectSubQuery<A> extends Expr<A> implements SubQuery{

    private final QueryMetadata md;
    
    private EBoolean exists;
    
    public ObjectSubQuery(QueryMetadata md, Class<A> type) {
        super(type);
        this.md = md;
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public QueryMetadata getMetadata() {
        return md;
    }

    @Override
    public EBoolean exists() {
        if (exists == null){
            exists = OBoolean.create(Ops.EXISTS, this);
        }
        return exists;
    }
    
    @Override
    public EBoolean notExists() {
        return exists().not();
    }
}
