/**
 * 
 */
package com.mysema.query.collections.perf;

import java.util.List;
import java.util.Map;

import com.mysema.query.collections.ColQueryImpl;
import com.mysema.query.collections.ColQueryPatterns;
import com.mysema.query.collections.QueryIndexSupport;
import com.mysema.query.collections.support.SimpleIndexSupport;
import com.mysema.query.collections.support.SimpleIteratorSource;
import com.mysema.query.types.expr.Expr;

class ColQueryWithoutIndexing extends ColQueryImpl {
    @Override
    protected QueryIndexSupport createIndexSupport(
            Map<Expr<?>, Iterable<?>> exprToIt, ColQueryPatterns ops,
            List<Expr<?>> sources) {
        return new SimpleIndexSupport(new SimpleIteratorSource(exprToIt),ops, sources);
    }
}