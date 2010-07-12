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
 * ColDeleteClause is an implementation of the DeleteClause interface for the Querydsl Collections module
 *
 * @author tiwe
 *
 * @param <T>
 */
public class ColDeleteClause<T> implements DeleteClause<ColDeleteClause<T>>{

    private final Collection<? extends T> col;

    private final Path<T> expr;

    private final ColQuery query;

    public ColDeleteClause(QueryEngine qe, Path<T> expr, Collection<? extends T> col){
        this.query = new ColQueryImpl(qe).from(expr, col);
        this.expr = expr;
        this.col = col;
    }

    public ColDeleteClause(Path<T> expr, Collection<? extends T> col){
        this(QueryEngine.DEFAULT, expr, col);
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

    @Override
    public String toString(){
        return "delete " + query.toString();
    }
}
