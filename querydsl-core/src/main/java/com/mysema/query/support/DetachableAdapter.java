/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.support;

import com.mysema.query.Detachable;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.ObjectSubQuery;

/**
 * @author tiwe
 *
 */
public class DetachableAdapter implements Detachable{

    private final Detachable detachable;
    
    public DetachableAdapter(Detachable detachable) {
        this.detachable = detachable;
    }
    
    @Override
    public ObjectSubQuery<Long> count(){
        return detachable.count();
    }
    
    @Override
    public EBoolean exists(){
        return detachable.exists();
    }
    
    @Override
    public ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return detachable.list(first, second, rest);
    }
    
    @Override
    public ListSubQuery<Object[]> list(Expr<?>[] args) {
        return detachable.list(args);
    }

    @Override
    public <RT> ListSubQuery<RT> list(Expr<RT> projection) {
        return detachable.list(projection);
    }

    @Override
    public EBoolean notExists(){
        return detachable.notExists();
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return detachable.unique(first, second, rest);
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?>[] args) {
        return detachable.unique(args);
    }

    @Override
    public <RT> ObjectSubQuery<RT> unique(Expr<RT> projection) {
        return detachable.unique(projection);
    }
}
