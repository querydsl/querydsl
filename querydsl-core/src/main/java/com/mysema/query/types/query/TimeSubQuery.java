/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.query;

import com.mysema.query.QueryMetadata;
import com.mysema.query.types.EBoolean;
import com.mysema.query.types.ETime;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.SubQuery;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.operation.OTime;

/**
 * Single result subquery
 * 
 * @author tiwe
 *
 * @param <A>
 */
public final class TimeSubQuery<A extends Comparable<?>> extends ETime<A> implements SubQuery<A>{

    private static final long serialVersionUID = -64156984110154969L;

    private final SubQueryMixin<A> subQueryMixin;
    
    public TimeSubQuery(QueryMetadata md, Class<A> type) {
        super(type);
        subQueryMixin = new SubQueryMixin<A>(this,md);
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }

    @Override
    public boolean equals(Object o) {
       return subQueryMixin.equals(o);
    }
    
    @Override
    public EBoolean exists() {
        return subQueryMixin.exists();
    }
    
    @Override
    public QueryMetadata getMetadata() {
        return subQueryMixin.getMetadata();
    }

    @Override
    public int hashCode(){
        return subQueryMixin.hashCode();
    }
    
    @Override
    public EBoolean notExists() {
        return subQueryMixin.notExists();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ETime<A> as(Path<A> alias) {
        return OTime.create(getType(),(Operator)Ops.ALIAS, this, alias.asExpr());
    }

}
