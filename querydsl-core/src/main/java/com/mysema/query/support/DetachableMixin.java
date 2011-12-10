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
package com.mysema.query.support;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.NullExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.TimeExpression;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.query.types.query.*;

/**
 * Mixin style implementation of the Detachable interface
 *
 * @author tiwe
 *
 */
public class DetachableMixin implements Detachable{

    private final QueryMixin<?> queryMixin;

    public DetachableMixin(QueryMixin<?> queryMixin){
        this.queryMixin = Assert.notNull(queryMixin,"queryMixin");
    }

    @Override
    public NumberSubQuery<Long> count() {
        return new NumberSubQuery<Long>(Long.class, projection(Wildcard.count));
    }

    @Override
    public BooleanExpression exists(){
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
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
    public ListSubQuery<Object[]> list(Object... args) {
        return list(convert(args));
    }
    
    @Override
    public SimpleSubQuery<Object[]> unique(Object... args) {
        return unique(convert(args));
    }

    private Expression<?>[] convert(Object... args) {
        Expression<?>[] exprs = new Expression[args.length];
        for (int i = 0; i < exprs.length; i++){
            if (args[i] instanceof Expression<?>) {
                exprs[i] = (Expression<?>)args[i];
            } else if (args[i] != null) {
                exprs[i] = new ConstantImpl<Object>(args[i]);
            } else {
                exprs[i] = NullExpression.DEFAULT;
            }
        }
        return exprs;
    }

    @Override
    public BooleanExpression notExists(){
        return exists().not();
    }

    private QueryMetadata projection(Expression<?>... projection){
        QueryMetadata metadata = queryMixin.getMetadata().clone();
        for (Expression<?> expr : projection) {
            metadata.addProjection(nullAsTemplate(expr));             
        }
        return metadata;        
    }

    private QueryMetadata projection(Expression<?> first, Expression<?> second, Expression<?>[] rest) {
        QueryMetadata metadata = queryMixin.getMetadata().clone();
        metadata.addProjection(nullAsTemplate(first));    
        metadata.addProjection(nullAsTemplate(second));    
        for (Expression<?> expr : rest) {
            metadata.addProjection(nullAsTemplate(expr));    
        }
        return metadata;   
    }
    
    private Expression<?> nullAsTemplate(@Nullable Expression<?> expr){
        return expr != null ? expr : NullExpression.DEFAULT;
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

    @Override
    public SimpleSubQuery<Object[]> unique(Expression<?> first, Expression<?> second, Expression<?>... rest) {
        return new SimpleSubQuery<Object[]>(Object[].class, uniqueProjection(first, second, rest));
    }

    @Override
    public SimpleSubQuery<Object[]> unique(Expression<?>[] args) {
        return new SimpleSubQuery<Object[]>(Object[].class, uniqueProjection(args));
    }


    @SuppressWarnings("unchecked")
    @Override
    public <RT> SimpleSubQuery<RT> unique(Expression<RT> projection) {
        return new SimpleSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT extends Number & Comparable<?>> NumberSubQuery<RT> unique(NumberExpression<RT> projection) {
        return new NumberSubQuery<RT>((Class)projection.getType(), uniqueProjection(projection));
    }
    
    @Override
    public BooleanSubQuery unique(Predicate projection) {
        return new BooleanSubQuery(uniqueProjection(projection));
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
