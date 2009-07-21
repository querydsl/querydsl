/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.Expr;

/**
 * Single result subquery
 * 
 * @author tiwe
 *
 * @param <JM>
 * @param <A>
 */
public class ObjectSubQuery<A> extends Expr<A> implements SubQuery{

    private final QueryMetadata md;
    
    public ObjectSubQuery(QueryMetadata md, Class<A> type) {
        super(type);
        this.md = md;
    }

    @Override
    public QueryMetadata getMetadata() {
        return md;
    }

}
