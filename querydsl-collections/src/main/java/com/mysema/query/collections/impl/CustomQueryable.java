/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.util.List;
import java.util.Map;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.QueryMetadata;
import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.eval.ColQueryPatterns;
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
// TODO : find a better name for this
public class CustomQueryable<SubType extends CustomQueryable<SubType>> extends
        ProjectableAdapter {

    private final ColQueryImpl innerQuery;

    @SuppressWarnings("unchecked")
    private final SubType _this = (SubType) this;

    public CustomQueryable(IteratorSource iteratorSource) {
        this(iteratorSource, new DefaultQueryMetadata());
    }

    public CustomQueryable(final IteratorSource iteratorSource, QueryMetadata metadata) {
        super(new ColQueryImpl(metadata) {
            @Override
            protected QueryIndexSupport createIndexSupport(
                    Map<Expr<?>, Iterable<?>> exprToIt, ColQueryPatterns patterns,
                    List<Expr<?>> sources) {
                return new DefaultIndexSupport(iteratorSource, patterns, sources);
            }
        });
        innerQuery = (ColQueryImpl) projectable;
    }

    protected QueryMetadata getMetadata() {
        return innerQuery.getMetadata();
    }

    public SubType from(Expr<?>... o) {
        innerQuery.getMetadata().addFrom(o);
        return _this;
    }

    public SubType orderBy(OrderSpecifier<?>... o) {
        innerQuery.orderBy(o);
        return _this;
    }

    public SubType where(EBoolean... o) {
        innerQuery.where(o);
        return _this;
    }

}
