/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * QueryBase provides a stub for Query implementations
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class QueryBase<SubType extends QueryBase<SubType>> {

    protected final QueryMixin<SubType> queryMixin;
    
    public QueryBase(QueryMixin<SubType> queryMixin) {
        this.queryMixin = queryMixin;
    }

    public SubType groupBy(Expr<?>... o) {
        return queryMixin.groupBy(o);
    }

    public SubType having(EBoolean... o) {
        return queryMixin.having(o);
    }

    public SubType orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    public SubType where(EBoolean... o) {
        return queryMixin.where(o);
    }

    public String toString() {
        return queryMixin.toString();
    }
    
    public SubType limit(long limit) {
        return queryMixin.limit(limit);
    }

    public SubType offset(long offset) {
        return queryMixin.offset(offset);
    }

    public SubType restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }

}
