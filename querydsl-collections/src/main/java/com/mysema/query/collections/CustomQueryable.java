/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.impl.ColQueryImpl;
import com.mysema.query.collections.impl.EvaluatorFactory;
import com.mysema.query.support.ProjectableAdapter;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * CustomQueryable a ColQuery like interface for querying on custom
 * IteratorSource sources
 * 
 * @author tiwe
 * @version $Id$
 */
public abstract class CustomQueryable<SubType extends CustomQueryable<SubType>> extends ProjectableAdapter {

    private final ColQueryImpl query ;

    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType) this;

    public CustomQueryable(QueryMetadata metadata, EvaluatorFactory evaluatorFactory) {
        super(new ColQueryImpl(metadata, evaluatorFactory));
        query = (ColQueryImpl) projectable;
    }

    protected QueryMetadata getMetadata() {
        return query.getMetadata();
    }

    @SuppressWarnings("unchecked")
    public SubType from(Expr<?>... exprs) {
        for (Expr<?> expr : exprs){
            query.from(expr, (Iterable)getContent(expr));
        }
        return _this;
    }

    protected abstract <T> Iterable<T> getContent(Expr<T> expr);

    public SubType orderBy(OrderSpecifier<?>... o) {
        query.orderBy(o);
        return _this;
    }

    public SubType where(EBoolean... o) {
        query.where(o);
        return _this;
    }

}
