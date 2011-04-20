/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import com.mysema.query.QueryMetadata;

/**
 * SubQueryExpressionImpl is the default implementation of the SubQueryExpression interface
 *
 * @author tiwe
 *
 */
public class SubQueryExpressionImpl<T> extends ExpressionBase<T> implements SubQueryExpression<T>{

    private static final long serialVersionUID = 6775967804458163L;

    private final QueryMetadata metadata;

    public SubQueryExpressionImpl(Class<? extends T> type, QueryMetadata metadata){
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
    public QueryMetadata getMetadata() {
        return metadata;
    }

    public int hashCode(){
        return type.hashCode();
    }
    
    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }
    
}
