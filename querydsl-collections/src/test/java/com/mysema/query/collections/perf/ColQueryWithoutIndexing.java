/**
 * 
 */
package com.mysema.query.collections.perf;

import java.util.List;
import java.util.Map;

import com.mysema.query.collections.eval.ColQueryTemplates;
import com.mysema.query.collections.impl.ColQueryImpl;
import com.mysema.query.collections.impl.QueryIndexSupport;
import com.mysema.query.collections.impl.SimpleIndexSupport;
import com.mysema.query.collections.impl.SimpleIteratorSource;
import com.mysema.query.types.expr.Expr;

class ColQueryWithoutIndexing extends ColQueryImpl {
    
    public ColQueryWithoutIndexing(ColQueryTemplates patterns) {
        super(patterns);
    }

    @Override
    protected QueryIndexSupport createIndexSupport(
            Map<Expr<?>, Iterable<?>> exprToIt, ColQueryTemplates ops,
            List<Expr<?>> sources) {
        return new SimpleIndexSupport(new SimpleIteratorSource(exprToIt),ops, sources);
    }
}