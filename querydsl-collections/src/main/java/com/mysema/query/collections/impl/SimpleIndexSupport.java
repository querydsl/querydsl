/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.impl;

import java.util.Iterator;
import java.util.List;

import com.mysema.commons.lang.Assert;
import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.eval.ColQueryTemplates;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;

/**
 * SimpleIndexSupport is a minimal QueryIndexSupport implementation
 * 
 * @see QueryIndexSupport
 * @author tiwe
 * @version $Id$
 */
public class SimpleIndexSupport implements QueryIndexSupport {

    protected final IteratorSource iteratorSource;

    protected final ColQueryTemplates patterns;

    protected final List<? extends Expr<?>> sources;

    public SimpleIndexSupport(IteratorSource iteratorSource, ColQueryTemplates patterns,
            List<? extends Expr<?>> sources) {
        this.iteratorSource = Assert.notNull(iteratorSource);
        this.patterns = Assert.notNull(patterns);
        this.sources = Assert.notNull(sources);
    }

    public IteratorSource getChildFor(EBoolean condition) {
        // do nothing
        return this;
    }

    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return iteratorSource.getIterator(expr);
    }

    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        return iteratorSource.getIterator(expr);
    }

}
