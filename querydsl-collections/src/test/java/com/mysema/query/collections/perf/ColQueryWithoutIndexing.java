/**
 * 
 */
package com.mysema.query.collections.perf;

import java.util.List;
import java.util.Map;

import com.mysema.query.collections.eval.ColQueryPatterns;
import com.mysema.query.collections.impl.ColQueryImpl;
import com.mysema.query.collections.impl.QueryIndexSupport;
import com.mysema.query.collections.impl.SimpleIndexSupport;
import com.mysema.query.collections.impl.SimpleIteratorSource;
import com.mysema.query.types.expr.Expr;

class ColQueryWithoutIndexing extends ColQueryImpl {
    @Override
    protected QueryIndexSupport createIndexSupport(
            Map<Expr<?>, Iterable<?>> exprToIt, ColQueryPatterns ops,
            List<Expr<?>> sources) {
        return new SimpleIndexSupport(new SimpleIteratorSource(exprToIt),ops, sources);
    }
}