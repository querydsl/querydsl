/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import com.mysema.query.types.Expr;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.query.*;

/**
 * DetachableAdapter is an apadater implementation for the Detachable interface
 * 
 * @author tiwe
 *
 */
public class DetachableAdapter implements Detachable{

    private final Detachable detachable;

    public DetachableAdapter(Detachable detachable){
        this.detachable = detachable;
    }

    public ObjectSubQuery<Long> count() {
        return detachable.count();
    }

    public EBoolean exists() {
        return detachable.exists();
    }

    public Detachable getDetachable(){
        return detachable;
    }

    public ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return detachable.list(first, second, rest);
    }

    public ListSubQuery<Object[]> list(Expr<?>[] args) {
        return detachable.list(args);
    }

    public <RT> ListSubQuery<RT> list(Expr<RT> projection) {
        return detachable.list(projection);
    }

    public EBoolean notExists() {
        return detachable.notExists();
    }

    public BooleanSubQuery unique(EBoolean projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> ComparableSubQuery<RT> unique(EComparable<RT> projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> DateSubQuery<RT> unique(EDate<RT> projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(EDateTime<RT> projection) {
        return detachable.unique(projection);
    }

    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(ENumber<RT> projection) {
        return detachable.unique(projection);
    }

    public StringSubQuery unique(EString projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> TimeSubQuery<RT> unique(ETime<RT> projection) {
        return detachable.unique(projection);
    }

    public ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return detachable.unique(first, second, rest);
    }

    public ObjectSubQuery<Object[]> unique(Expr<?>[] args) {
        return detachable.unique(args);
    }

    public <RT> ObjectSubQuery<RT> unique(Expr<RT> projection) {
        return detachable.unique(projection);
    }

}
