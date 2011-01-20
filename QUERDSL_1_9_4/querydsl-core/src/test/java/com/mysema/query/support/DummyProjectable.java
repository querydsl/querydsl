/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.EmptyCloseableIterator;
import com.mysema.query.SearchResults;
import com.mysema.query.types.Expr;

public class DummyProjectable extends ProjectableQuery<DummyProjectable>{

    public DummyProjectable() {
        super(new QueryMixin<DummyProjectable>());
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public CloseableIterator<Object[]> iterate(Expr<?>[] args) {
        return new EmptyCloseableIterator<Object[]>();
    }

    @Override
    public <RT> CloseableIterator<RT> iterate(Expr<RT> projection) {
        return new EmptyCloseableIterator<RT>();
    }

    @Override
    public <RT> SearchResults<RT> listResults(Expr<RT> projection) {
        return SearchResults.emptyResults();
    }

}
