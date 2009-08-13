/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.commons.lang.Assert;
import com.mysema.query.Query;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * QueryAdapter is an adapter implementation for the Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
public class QueryAdapter<SubType extends QueryAdapter<SubType>> implements Query<SubType> {

    private Query<?> query;

    @SuppressWarnings("unchecked")
    private SubType _this = (SubType) this;

    public QueryAdapter() {
    }

    public QueryAdapter(Query<?> query) {
        this.query = Assert.notNull(query,"query is null");
    }

    @Override
    public SubType from(Expr<?>... o) {
        query.from(o);
        return _this;
    }

    @Override
    public SubType fullJoin(Expr<?> o) {
        query.fullJoin(o);
        return _this;
    }

    @Override
    public SubType groupBy(Expr<?>... o) {
        query.groupBy(o);
        return _this;
    }

    @Override
    public SubType having(EBoolean... o) {
        query.having(o);
        return _this;
    }

    @Override
    public SubType innerJoin(Expr<?> o) {
        query.innerJoin(o);
        return _this;
    }

    @Override
    public SubType join(Expr<?> o) {
        query.join(o);
        return _this;
    }

    @Override
    public SubType leftJoin(Expr<?> o) {
        query.leftJoin(o);
        return _this;
    }

    @Override
    public SubType on(EBoolean o) {
        query.on(o);
        return _this;
    }

    @Override
    public SubType orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return _this;
    }

    public void setQuery(Query<?> query) {
        this.query = query;
    }

    @Override
    public String toString() {
        return query.toString();
    }

    @Override
    public SubType where(EBoolean... o) {
        query.where(o);
        return _this;
    }

}
