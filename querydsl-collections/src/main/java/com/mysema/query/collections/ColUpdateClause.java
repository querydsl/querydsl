/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import com.mysema.query.dml.UpdateClause;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class ColUpdateClause<T> implements UpdateClause<ColUpdateClause<T>>{

    private final ColQuery query;
    
    private final Path<T> expr;
    
    public ColUpdateClause(Path<T> expr, Iterable<? extends T> col){
        this(EvaluatorFactory.DEFAULT, expr, col);
    }
    
    public ColUpdateClause(EvaluatorFactory ef, Path<T> expr, Iterable<? extends T> col){
        this.query = new ColQueryImpl(ef).from(expr, col);
        this.expr = expr;
    }
    
    @Override
    public long execute() {
        throw new UnsupportedOperationException("Not yet implemented");
//        int rv = 0;
//        for (T match : query.list(expr.asExpr())){
//            // TODO : update
//            rv++;
//        }
//        return rv;
    }

    @Override
    public <U> ColUpdateClause<T> set(Path<U> path, U value) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public ColUpdateClause<T> where(EBoolean... o) {
        query.where(o);
        return this;
    }

}
