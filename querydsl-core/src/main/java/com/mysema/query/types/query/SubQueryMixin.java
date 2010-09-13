/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.query;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.MixinBase;

/**
 * Mixin implementation of the SubQuery interface
 *
 * @author tiwe
 *
 */
public class SubQueryMixin<T> extends MixinBase<T> implements SubQueryExpression<T>{

    private static final long serialVersionUID = 6775967804458163L;

    @Nullable
    private volatile BooleanExpression exists;

    private final QueryMetadata metadata;

    private final Expression<T> self;

    public SubQueryMixin(SubQueryExpression<T> self, QueryMetadata metadata){
        this.self = self;
        this.metadata = metadata;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o){
        if (o == this || o == self){
            return true;
        }else if (o instanceof SubQueryExpression){
            SubQueryExpression<T> s = (SubQueryExpression<T>)o;
            return s.getMetadata().equals(metadata);
        }else{
            return false;
        }
    }

    @Override
    public BooleanExpression exists() {
        if (exists == null){
            exists = BooleanOperation.create(Ops.EXISTS, self);
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
    public BooleanExpression notExists() {
        return exists().not();
    }

}
