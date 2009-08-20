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
    
    @SuppressWarnings("unchecked")
    protected final SubType _this = (SubType) this;

    private final QueryMetadata metadata;

    protected String toString;

    public QueryBase(QueryMetadata metadata) {
        this.metadata = metadata;
    }

    protected SubType addToProjection(Expr<?>... o) {
        metadata.addProjection(o);
        return _this;
    }

    public SubType from(Expr<?>... o) {
        metadata.addFrom(o);
        return _this;
    }

    public QueryMetadata getMetadata() {
        return metadata;
    }

    public SubType groupBy(Expr<?>... o) {
        metadata.addGroupBy(o);
        return _this;
    }

    public SubType having(EBoolean... o) {
        metadata.addHaving(o);
        return _this;
    }

    public SubType orderBy(OrderSpecifier<?>... o) {
        metadata.addOrderBy(o);
        return _this;
    }

    public SubType where(EBoolean... o) {
        metadata.addWhere(o);
        return _this;
    }

    public String toString() {
        return metadata.toString();
    }
    
    public SubType limit(long limit) {
        metadata.setLimit(limit);
        return _this;
    }

    public SubType offset(long offset) {
        metadata.setOffset(offset);
        return _this;
    }

    public SubType restrict(QueryModifiers modifiers) {
        metadata.setModifiers(modifiers);
        return _this;
    }

}
