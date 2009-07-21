/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import java.util.List;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.ECollectionBase;

/**
 * List result subquery
 * 
 * @author tiwe
 *
 * @param <JM>
 * @param <A>
 */
public class ListSubQuery<A> extends ECollectionBase<A> implements SubQuery{

    private final Class<A> elementType;
    
    private final QueryMetadata md;
    
    @SuppressWarnings("unchecked")
    public ListSubQuery(QueryMetadata md, Class<A> elementType) {
        super((Class)List.class);
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
