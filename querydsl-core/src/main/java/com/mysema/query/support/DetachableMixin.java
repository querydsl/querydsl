/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.TimeExpression;
import com.mysema.query.types.query.*;

/**
 * Mixin style implementation of the Detachable interface
 *
 * @author tiwe
 *
 */
public class DetachableMixin implements Detachable{

    private static final NumberExpression<Long> COUNT_ALL_AGG_EXPR = NumberOperation.create(Long.class, Ops.AggOps.COUNT_ALL_AGG);

    private final QueryMixin<?> queryMixin;

    public DetachableMixin(QueryMixin<?> queryMixin){
        this.queryMixin = Assert.notNull(queryMixin,"queryMixin");
    }

    @Override
    public ObjectSubQuery<Long> count() {
        return new ObjectSubQuery<Long>(Long.class, projection(COUNT_ALL_AGG_EXPR));
    }

    @Override
    public BooleanExpression exists(){
        if (queryMixin.getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No sources given");
        }
        return unique(queryMixin.getMetadata().getJoins().get(0).getTarget()).exists();
    }

    @Override
    public ListSubQuery<Object[]> list(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return new ListSubQuery<Object[]>(Object[].class, projection(first, second, rest));
    }

    @Override
    public ListSubQuery<Object[]> list(Expression<?>[] args) {
        return new ListSubQuery<Object[]>(Object[].class, projection(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<RT> list(Expression<RT> projection) {
        return new ListSubQuery<RT>((Class)projection.getType(), projection(projection));
    }

    @Override
    public BooleanExpression notExists(){
        return exists().not();
    }

    @Override
    public BooleanSubQuery unique(Predicate projection) {
        return new BooleanSubQuery(uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> ComparableSubQuery<RT> unique(ComparableExpression<RT> projection) {
        return new ComparableSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateSubQuery<RT> unique(DateExpression<RT> projection) {
        return new DateSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(DateTimeExpression<RT> projection) {
        return new DateTimeSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(NumberExpression<RT> projection) {
        return new NumberSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @Override
    public StringSubQuery unique(StringExpression projection) {
        return new StringSubQuery(uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> TimeSubQuery<RT> unique(TimeExpression<RT> projection) {
        return new TimeSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return new ObjectSubQuery<Object[]>(Object[].class, uniqueProjection(first, second, rest));
    }


    @Override
    public ObjectSubQuery<Object[]> unique(Expression<?>[] args) {
        return new ObjectSubQuery<Object[]>(Object[].class, uniqueProjection(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ObjectSubQuery<RT> unique(Expression<RT> projection) {
        return new ObjectSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }
    
    private QueryMetadata projection(Expression<?>... projection){
        QueryMetadata metadata = queryMixin.getMetadata().clone();
        for (Expression<?> expr : projection){
            metadata.addProjection(expr);    
        }
        return metadata;        
    }
    
    private QueryMetadata projection(Expression<?> first, Expression<?> second, Expression<?>[] rest) {
        QueryMetadata metadata = queryMixin.getMetadata().clone();
        metadata.addProjection(first);    
        metadata.addProjection(second);    
        for (Expression<?> expr : rest){
            metadata.addProjection(expr);    
        }
        return metadata;   
    }
    
    private QueryMetadata uniqueProjection(Expression<?>... projection){
        QueryMetadata metadata = projection(projection);
        metadata.setUnique(true);
        return metadata;
    }
    

    private QueryMetadata uniqueProjection(Expression<?> first, Expression<?> second, Expression<?>[] rest) {
        QueryMetadata metadata = projection(first, second, rest);
        metadata.setUnique(true);
        return metadata;
    }
    
}
