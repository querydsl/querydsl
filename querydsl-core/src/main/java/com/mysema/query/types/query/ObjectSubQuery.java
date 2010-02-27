/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import javax.annotation.Nullable;

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
public final class ObjectSubQuery<A> extends Expr<A> implements SubQuery{

    private static final long serialVersionUID = -64156984110154969L;

    private final QueryMetadata md;
    
    @Nullable
    private volatile EBoolean exists;
    
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
    
    @Override
    public boolean equals(Object o) {
       if (o == this){
           return true;
       }else if (o instanceof SubQuery){
           return ((SubQuery)o).getMetadata().equals(md);
       }else{
           return false;
       }
    }
    
    @Override
    public int hashCode(){
        return getType().hashCode();
    }
}
