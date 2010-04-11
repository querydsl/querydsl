/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Collection;

import com.mysema.query.dml.DeleteClause;
import com.mysema.query.types.Path;
import com.mysema.query.types.expr.EBoolean;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class ColDeleteClause<T> implements DeleteClause<ColDeleteClause<T>>{

    private final Collection<? extends T> col;
    
    private final Path<T> expr;
    
    private final ColQuery query;
    
    public ColDeleteClause(ExprEvaluatorFactory ef, Path<T> expr, Collection<? extends T> col){
        this.query = new ColQueryImpl(ef).from(expr, col);
        this.expr = expr;
        this.col = col;
    }
    
    public ColDeleteClause(Path<T> expr, Collection<? extends T> col){
        this(ExprEvaluatorFactory.DEFAULT, expr, col);
    }
    
    @Override
    public long execute() {
        int rv = 0;
        for (T match : query.list(expr.asExpr())){
            col.remove(match);
            rv++;
        }
        return rv;
    }

    @Override
    public ColDeleteClause<T> where(EBoolean... o) {
        query.where(o);
        return this;
    }

}
