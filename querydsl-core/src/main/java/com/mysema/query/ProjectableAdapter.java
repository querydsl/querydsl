/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Iterator;
import java.util.List;

import com.mysema.query.grammar.types.Expr;
import com.mysema.query.util.Assert;

/**
 * ProjectableAdapter is an adapter implementation for the Projectable 
 * interface
 *
 * @author tiwe
 * @version $Id$
 */
public abstract class ProjectableAdapter implements Projectable{
    
    private Projectable projectable;
    
    public ProjectableAdapter(){}
    
    public ProjectableAdapter(Projectable projectable){
        this.projectable = Assert.notNull(projectable);
    }

    public long count() {
        return projectable.count();
    }
    
    public long countDistinct() {
        return projectable.countDistinct();
    }

    public Iterator<Object[]> iterate(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.iterate(first, second, rest);
    }
    
    public Iterator<Object[]> iterateDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.iterateDistinct(first, second, rest);
    }

    public <RT> Iterator<RT> iterate(Expr<RT> projection) {
        return projectable.iterate(projection);
    }

    public <RT> Iterator<RT> iterateDistinct(Expr<RT> projection) {
        return projectable.iterateDistinct(projection);
    }
    
    public List<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.list(first, second, rest);
    }

    public List<Object[]> listDistinct(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return projectable.listDistinct(first, second, rest);
    }
    
    public <RT> List<RT> list(Expr<RT> projection) {
        return projectable.list(projection);
    }
    
    public <RT> List<RT> listDistinct(Expr<RT> projection) {
        return projectable.listDistinct(projection);
    }

    public <RT> RT uniqueResult(Expr<RT> expr) {
        return projectable.uniqueResult(expr);
    }

    public void setProjectable(Projectable projectable) {
        this.projectable = Assert.notNull(projectable);
    }
    
    public String toString(){
        return projectable.toString();
    }
    
}
