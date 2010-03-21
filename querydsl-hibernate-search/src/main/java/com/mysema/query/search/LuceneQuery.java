/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.search;

import org.hibernate.Session;

import com.mysema.query.QueryModifiers;
import com.mysema.query.SimpleProjectable;
import com.mysema.query.SimpleQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.path.Path;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class LuceneQuery<T> implements SimpleQuery<LuceneQuery<T>>, SimpleProjectable<T>{
    
    private final Path<?> entityPath;
    
    private final QueryMixin<LuceneQuery<T>> queryMixin;
    
    private final Session session;
    
    public LuceneQuery(Session session, Path<?> entityPath) {
        this.session = session;
        this.entityPath = entityPath;
        this.queryMixin = new QueryMixin<LuceneQuery<T>>(this);
    }

    @Override
    public LuceneQuery<T> limit(long limit) {
        return queryMixin.limit(limit);
    }

    @Override
    public LuceneQuery<T> offset(long offset) {
        return queryMixin.offset(offset);
    }

    @Override
    public LuceneQuery<T> orderBy(OrderSpecifier<?>... o) {
        return queryMixin.orderBy(o);
    }

    @Override
    public LuceneQuery<T> restrict(QueryModifiers modifiers) {
        return queryMixin.restrict(modifiers);
    }
    
    @Override
    public LuceneQuery<T> where(EBoolean... e) {
        return queryMixin.where(e);
    }

    // TODO : implementations of Projectable methods
    
}
