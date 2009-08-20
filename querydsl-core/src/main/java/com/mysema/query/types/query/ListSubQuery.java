/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import java.util.List;

import net.jcip.annotations.Immutable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollectionBase;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;

/**
 * List result subquery
 * 
 * @author tiwe
 *
 * @param <JM>
 * @param <A>
 */
@Immutable
public class ListSubQuery<A> extends ECollectionBase<A> implements SubQuery{

    private final Class<A> elementType;
    
    private final QueryMetadata md;
    
    private EBoolean exists;
    
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
    
    @Override
    public EBoolean exists() {
        if (exists == null){
            exists = new OBoolean(Ops.EXISTS, this);
        }
        return exists;
    }

}
