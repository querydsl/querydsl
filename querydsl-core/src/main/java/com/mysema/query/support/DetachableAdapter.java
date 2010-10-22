/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.TimeExpression;
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

    public NumberSubQuery<Long> count() {
        return detachable.count();
    }

    public BooleanExpression exists() {
        return detachable.exists();
    }

    public Detachable getDetachable(){
        return detachable;
    }

    public ListSubQuery<Object[]> list(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return detachable.list(first, second, rest);
    }

    public ListSubQuery<Object[]> list(Expression<?>[] args) {
        return detachable.list(args);
    }

    public <RT> ListSubQuery<RT> list(Expression<RT> projection) {
        return detachable.list(projection);
    }

    public BooleanExpression notExists() {
        return detachable.notExists();
    }

    public BooleanSubQuery unique(Predicate projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> ComparableSubQuery<RT> unique(ComparableExpression<RT> projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> DateSubQuery<RT> unique(DateExpression<RT> projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(DateTimeExpression<RT> projection) {
        return detachable.unique(projection);
    }

    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(NumberExpression<RT> projection) {
        return detachable.unique(projection);
    }

    public StringSubQuery unique(StringExpression projection) {
        return detachable.unique(projection);
    }

    public <RT extends Comparable<?>> TimeSubQuery<RT> unique(TimeExpression<RT> projection) {
        return detachable.unique(projection);
    }

    public SimpleSubQuery<Object[]> unique(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return detachable.unique(first, second, rest);
    }

    public SimpleSubQuery<Object[]> unique(Expression<?>[] args) {
        return detachable.unique(args);
    }

    public <RT> SimpleSubQuery<RT> unique(Expression<RT> projection) {
        return detachable.unique(projection);
    }

}
