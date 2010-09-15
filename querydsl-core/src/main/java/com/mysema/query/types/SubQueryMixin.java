/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import javax.annotation.Nullable;

import com.mysema.query.QueryMetadata;

/**
 * Mixin implementation of the SubQuery interface
 *
 * @author tiwe
 *
 */
public class SubQueryMixin<T> extends MixinBase<T> implements SubQueryExpression<T>{

    private static final long serialVersionUID = 6775967804458163L;

    @Nullable
    private volatile Predicate exists;

    private final QueryMetadata metadata;

    public SubQueryMixin(Class<? extends T> type, QueryMetadata metadata){
        super(type);
        this.metadata = metadata;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o){
        if (o == this){
            return true;
        }else if (o instanceof SubQueryExpression){
            SubQueryExpression<T> s = (SubQueryExpression<T>)o;
            return s.getMetadata().equals(metadata);
        }else{
            return false;
        }
    }

    @Override
    public Predicate exists() {
        if (exists == null){
            exists = new PredicateOperation(Ops.EXISTS, this);
        }
        return exists;
    }

    @Override
    public QueryMetadata getMetadata() {
        return metadata;
    }

    public int hashCode(){
        return type.hashCode();
    }

    @Override
    public Predicate notExists() {
        return exists().not();
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}
