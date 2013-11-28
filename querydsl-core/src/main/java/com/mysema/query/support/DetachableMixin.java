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

import com.mysema.query.Detachable;
import com.mysema.query.QueryMetadata;
import com.mysema.query.Tuple;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.NullExpression;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.ProjectionRole;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.ComparableExpression;
import com.mysema.query.types.expr.DateExpression;
import com.mysema.query.types.expr.DateTimeExpression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.StringExpression;
import com.mysema.query.types.expr.TimeExpression;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.query.types.query.BooleanSubQuery;
import com.mysema.query.types.query.ComparableSubQuery;
import com.mysema.query.types.query.DateSubQuery;
import com.mysema.query.types.query.DateTimeSubQuery;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.query.NumberSubQuery;
import com.mysema.query.types.query.SimpleSubQuery;
import com.mysema.query.types.query.StringSubQuery;
import com.mysema.query.types.query.TimeSubQuery;

/**
 * Mixin style implementation of the Detachable interface
 *
 * @author tiwe
 *
 */
public class DetachableMixin implements Detachable {

    private final QueryMixin<?> queryMixin;

    public DetachableMixin(QueryMixin<?> queryMixin) {
        this.queryMixin = queryMixin;
    }

    @Override
    public NumberSubQuery<Long> count() {
        return new NumberSubQuery<Long>(Long.class, projection(Wildcard.count));
    }

    @Override
    public BooleanExpression exists() {
        if (queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("No sources given");
        }
        return unique(queryMixin.getMetadata().getJoins().get(0).getTarget()).exists();
    }

    @Override
    public ListSubQuery<Tuple> list(Expression<?>... args) {
        return new ListSubQuery<Tuple>(Tuple.class, projection(args));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <RT> ListSubQuery<RT> list(Expression<RT> projection) {
        return new ListSubQuery<RT>((Class)projection.getType(), projection(projection));
    }

    public ListSubQuery<Tuple> list(Object arg) {
        return list((Expression)convert(arg));
    }

    @Override
    public ListSubQuery<Tuple> list(Object... args) {
        return list(convert(args));
    }

    @Override
    public SimpleSubQuery<Tuple> unique(Object... args) {
        return unique(convert(args));
    }

    private Expression<?> convert(Object arg) {
        if (arg instanceof Expression<?>) {
            return (Expression<?>)arg;
        } else if (arg instanceof ProjectionRole) {
            return ((ProjectionRole<?>)arg).getProjection();
        } else if (arg != null) {
            return ConstantImpl.create(arg);
        } else {
            return NullExpression.DEFAULT;
        }
    }

    private Expression<?>[] convert(Object... args) {
        final Expression<?>[] exprs = new Expression[args.length];
        for (int i = 0; i < exprs.length; i++) {
            exprs[i] = convert(args[i]);
        }
        return exprs;
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

    private QueryMetadata projection(Expression<?>... projection) {
        QueryMetadata metadata = queryMixin.getMetadata().clone();
        for (Expression<?> expr : projection) {
            expr = queryMixin.convert(expr);
            metadata.addProjection(nullAsTemplate(expr));
        }
        return metadata;
    }

    private Expression<?> nullAsTemplate(@Nullable Expression<?> expr) {
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
    public SimpleSubQuery<Tuple> unique(Expression<?>... args) {
        return new SimpleSubQuery<Tuple>(Tuple.class, uniqueProjection(args));
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

    private QueryMetadata uniqueProjection(Expression<?>... projection) {
        QueryMetadata metadata = projection(projection);
        metadata.setUnique(true);
        return metadata;
    }

}
