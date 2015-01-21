/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.support;

import com.querydsl.core.Detachable;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.ComparableExpression;
import com.querydsl.core.types.expr.DateExpression;
import com.querydsl.core.types.expr.DateTimeExpression;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.StringExpression;
import com.querydsl.core.types.expr.TimeExpression;
import com.querydsl.core.types.query.BooleanSubQuery;
import com.querydsl.core.types.query.ComparableSubQuery;
import com.querydsl.core.types.query.DateSubQuery;
import com.querydsl.core.types.query.DateTimeSubQuery;
import com.querydsl.core.types.query.ListSubQuery;
import com.querydsl.core.types.query.NumberSubQuery;
import com.querydsl.core.types.query.SimpleSubQuery;
import com.querydsl.core.types.query.StringSubQuery;
import com.querydsl.core.types.query.TimeSubQuery;

/**
 * DetachableQuery is a base class for queries which implement the Query and Detachable interfaces
 * 
 * @author tiwe
 *
 * @param <Q> concrete subtype
 */
public class DetachableQuery <Q extends DetachableQuery<Q>> extends QueryBase<Q> implements Detachable {

    private final DetachableMixin detachableMixin;

    public DetachableQuery(QueryMixin<Q> queryMixin) {
        super(queryMixin);
        this.detachableMixin = new DetachableMixin(queryMixin);
    }

    @Override
    public NumberSubQuery<Long> count() {
        return detachableMixin.count();
    }

    @Override
    public BooleanExpression exists() {
        return detachableMixin.exists();
    }

    @Override
    public ListSubQuery<Tuple> list(Expression<?>... args) {
        return detachableMixin.list(args);
    }

    @Override
    public <RT> ListSubQuery<RT> list(Expression<RT> projection) {
        return detachableMixin.list(projection);
    }

    public ListSubQuery<Tuple> list(Object arg) {
        return detachableMixin.list(arg);
    }
    
    @Override
    public ListSubQuery<Tuple> list(Object... args) {
        return detachableMixin.list(args);
    }

    @Override
    public BooleanExpression notExists() {
        return detachableMixin.notExists();
    }

    @Override
    public <RT extends Comparable<?>> ComparableSubQuery<RT> unique(ComparableExpression<RT> projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public <RT extends Comparable<?>> DateSubQuery<RT> unique(DateExpression<RT> projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(DateTimeExpression<RT> projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public SimpleSubQuery<Tuple> unique(Expression<?>... args) {
        return detachableMixin.unique(args);
    }

    @Override
    public <RT> SimpleSubQuery<RT> unique(Expression<RT> projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(NumberExpression<RT> projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public BooleanSubQuery unique(Predicate projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public StringSubQuery unique(StringExpression projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public <RT extends Comparable<?>> TimeSubQuery<RT> unique(TimeExpression<RT> projection) {
        return detachableMixin.unique(projection);
    }

    @Override
    public SimpleSubQuery<Tuple> unique(Object... args) {
        return detachableMixin.unique(args);
    }

}
