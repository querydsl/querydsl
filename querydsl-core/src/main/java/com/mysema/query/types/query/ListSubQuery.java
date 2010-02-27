/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import java.util.List;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Visitor;
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
@SuppressWarnings("serial")
public final class ListSubQuery<A> extends ECollectionBase<List<A>,A> implements SubQuery{

    private final Class<A> elementType;
    
    private final QueryMetadata md;
    
    @Nullable
    private volatile EBoolean exists;
    
    @SuppressWarnings("unchecked")
    public ListSubQuery(QueryMetadata md, Class<A> elementType) {
        super((Class)List.class);
        this.elementType = elementType;
        this.md = md;
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
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
            exists = OBoolean.create(Ops.EXISTS, this);
        }
        return exists;
    }
    
    @Override
    public EBoolean notExists() {
        return exists().not();
    }

    @Override
    public boolean equals(Object o) {
       if (o == this){
           return true;
       }else if (o instanceof SubQuery){
           SubQuery s = (SubQuery)o;
           return s.getMetadata().equals(md);
       }else{
           return false;
       }
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }

}
