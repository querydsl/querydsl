/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.OBoolean;

/**
 * Mixin implementation of the SubQuery interface
 *
 * @author tiwe
 *
 */
public class SubQueryMixin<T> implements SubQuery<T>{

    @Nullable
    private volatile EBoolean exists;

    private final QueryMetadata metadata;

    private final Expr<T> self;

    public SubQueryMixin(SubQuery<T> self, QueryMetadata metadata){
        this.self = self.asExpr();
        this.metadata = metadata;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o){
        if (o == this || o == self){
            return true;
        }else if (o instanceof SubQuery){
            SubQuery<T> s = (SubQuery<T>)o;
            return s.getMetadata().equals(metadata);
        }else{
            return false;
        }
    }

    @Override
    public EBoolean exists() {
        if (exists == null){
            exists = OBoolean.create(Ops.EXISTS, self);
        }
        return exists;
    }

    @Override
    public QueryMetadata getMetadata() {
        return metadata;
    }

    public int hashCode(){
        return self.getType().hashCode();
    }

    @Override
    public EBoolean notExists() {
        return exists().not();
    }

    @Override
    public Expr<T> asExpr() {
        return self;
    }

}
