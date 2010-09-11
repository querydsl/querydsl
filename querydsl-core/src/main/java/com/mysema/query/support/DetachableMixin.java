/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EComparable;
import com.mysema.query.types.expr.EDate;
import com.mysema.query.types.expr.EDateTime;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.ETime;
import com.mysema.query.types.expr.ONumber;
import com.mysema.query.types.query.*;

/**
 * Mixin style implementation of the Detachable interface
 *
 * @author tiwe
 *
 */
public class DetachableMixin implements Detachable{

    private static final ENumber<Long> COUNT_ALL_AGG_EXPR = ONumber.create(Long.class, Ops.AggOps.COUNT_ALL_AGG);

    private final QueryMixin<?> queryMixin;

    public DetachableMixin(QueryMixin<?> queryMixin){
        this.queryMixin = Assert.notNull(queryMixin,"queryMixin");
    }

    @Override
    public ObjectSubQuery<Long> count() {
        return new ObjectSubQuery<Long>(Long.class, projection(COUNT_ALL_AGG_EXPR));
    }

    @Override
    public EBoolean exists(){
        if (queryMixin.getMetadata().getJoins().isEmpty()){
            throw new IllegalArgumentException("No sources given");
        }
        return unique(queryMixin.getMetadata().getJoins().get(0).getTarget()).exists();
    }

    @Override
    public ListSubQuery<Object[]> list(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return new ListSubQuery<Object[]>(Object[].class, projection(first, second, rest));
    }

    @Override
    public ListSubQuery<Object[]> list(Expr<?>[] args) {
        return new ListSubQuery<Object[]>(Object[].class, projection(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<RT> list(Expr<RT> projection) {
        return new ListSubQuery<RT>((Class)projection.getType(), projection(projection));
    }

    @Override
    public EBoolean notExists(){
        return exists().not();
    }

    @Override
    public BooleanSubQuery unique(EBoolean projection) {
        return new BooleanSubQuery(uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> ComparableSubQuery<RT> unique(EComparable<RT> projection) {
        return new ComparableSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateSubQuery<RT> unique(EDate<RT> projection) {
        return new DateSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> DateTimeSubQuery<RT> unique(EDateTime<RT> projection) {
        return new DateTimeSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(ENumber<RT> projection) {
        return new NumberSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @Override
    public StringSubQuery unique(EString projection) {
        return new StringSubQuery(uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Comparable<?>> TimeSubQuery<RT> unique(ETime<RT> projection) {
        return new TimeSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?> first, Expr<?> second, Expr<?>... rest) {
        return new ObjectSubQuery<Object[]>(Object[].class, uniqueProjection(first, second, rest));
    }


    @Override
    public ObjectSubQuery<Object[]> unique(Expr<?>[] args) {
        return new ObjectSubQuery<Object[]>(Object[].class, uniqueProjection(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ObjectSubQuery<RT> unique(Expr<RT> projection) {
        return new ObjectSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }
    
    private QueryMetadata projection(Expr<?>... projection){
        QueryMetadata metadata = queryMixin.getMetadata().clone();
        for (Expr<?> expr : projection){
            metadata.addProjection(expr);    
        }
        return metadata;        
    }
    
    private QueryMetadata projection(Expr<?> first, Expr<?> second, Expr<?>[] rest) {
        QueryMetadata metadata = queryMixin.getMetadata().clone();
        metadata.addProjection(first);    
        metadata.addProjection(second);    
        for (Expr<?> expr : rest){
            metadata.addProjection(expr);    
        }
        return metadata;   
    }
    
    private QueryMetadata uniqueProjection(Expr<?>... projection){
        QueryMetadata metadata = projection(projection);
        metadata.setUnique(true);
        return metadata;
    }
    

    private QueryMetadata uniqueProjection(Expr<?> first, Expr<?> second, Expr<?>[] rest) {
        QueryMetadata metadata = projection(first, second, rest);
        metadata.setUnique(true);
        return metadata;
    }
    
}
