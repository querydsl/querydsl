/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.ECollectionBase;

/**
 * @author tiwe
 *
 * @param <JM>
 * @param <A>
 */
public class ListSubQuery<A> extends ECollectionBase<A> implements SubQuery{

    private Class<A> elementType;
    
    private QueryMetadata md;
    
    public ListSubQuery(QueryMetadata md, Class<A> elementType) {
        super(null);
        this.elementType = elementType;
        this.md = md;
    }
    
    public Class<A> getElementType() {
        return elementType;
    }
    
    @Override
    public QueryMetadata getMetadata() {
        return md;
    }

}
