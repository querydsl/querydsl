/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.Expr;

/**
 * @author tiwe
 *
 * @param <JM>
 * @param <A>
 */
public class ObjectSubQuery<JM, A> extends Expr<A> implements SubQuery<JM>{

    private QueryMetadata<JM> md;
    
    public ObjectSubQuery(QueryMetadata<JM> md, Class<A> type) {
        super(type);
        this.md = md;
    }

    @Override
    public QueryMetadata<JM> getMetadata() {
        return md;
    }

}
