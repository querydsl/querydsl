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
package com.mysema.query.jpa;

import javax.annotation.Nullable;

import com.mysema.query.JoinFlag;
import com.mysema.query.Query;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.sql.ForeignKey;
import com.mysema.query.sql.RelationalFunctionCall;
import com.mysema.query.sql.RelationalPath;
import com.mysema.query.sql.SQLCommonQuery;
import com.mysema.query.sql.SQLTemplates;
import com.mysema.query.sql.Union;
import com.mysema.query.sql.UnionImpl;
import com.mysema.query.sql.UnionUtils;
import com.mysema.query.support.ProjectableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.expr.Wildcard;
import com.mysema.query.types.query.ListSubQuery;
import com.mysema.query.types.template.NumberTemplate;
import com.mysema.query.types.template.SimpleTemplate;

/**
 * Abstract super class for SQLQuery implementation for JPA and Hibernate
 *
 * @author tiwe
 *
 * @param <T> concrete subtype
 */
public abstract class AbstractSQLQuery<T extends AbstractSQLQuery<T> & Query<T>> extends ProjectableQuery<T> implements SQLCommonQuery<T> {

    private static final class NativeQueryMixin<T> extends QueryMixin<T> {
        private NativeQueryMixin(QueryMetadata metadata) {
            super(metadata, false);
        }

        @Override
        public <RT> Expression<RT> convert(Expression<RT> expr) {
            return super.convert(Conversions.convertForNativeQuery(expr));
        }
    }

    protected final QueryMixin<T> queryMixin;

    @Nullable
    protected Expression<?> union;

    protected boolean unionAll;

    @SuppressWarnings("unchecked")
    public AbstractSQLQuery(QueryMetadata metadata) {
        super(new NativeQueryMixin<T>(metadata));
        this.queryMixin = super.queryMixin;
        this.queryMixin.setSelf((T)this);
    }

    @Override
    public long count() {
        Number number = uniqueResult(Wildcard.countAsInt);
        return number.longValue();
    }

    @Override
    public boolean exists() {
        return limit(1).uniqueResult(NumberTemplate.ONE) != null;
    }

    public T from(Expression<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    public T from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression)subQuery, alias));
    }

    @Override
    public T fullJoin(RelationalPath<?> o) {
        return queryMixin.fullJoin(o);
    }

    @Override
    public <E> T fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public <E> T fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    @Override
    public T fullJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.fullJoin(o, alias);
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    @Override
    public T innerJoin(RelationalPath<?> o) {
        return queryMixin.innerJoin(o);
    }

    @Override
    public <E> T innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <E> T innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    public T innerJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.innerJoin(o, alias);
    }

    @Override
    public T join(RelationalPath<?> o) {
        return queryMixin.join(o);
    }

    @Override
    public <E> T join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <E> T join(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    @Override
    public T join(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.join(o, alias);
    }

    @Override
    public T leftJoin(RelationalPath<?> o) {
        return queryMixin.leftJoin(o);
    }

    @Override
    public <E> T leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <E> T leftJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    public T leftJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.leftJoin(o, alias);
    }

    public T on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
    public T on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    @Override
    public T rightJoin(RelationalPath<?> o) {
        return queryMixin.rightJoin(o);
    }

    @Override
    public <E> T rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <E> T rightJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    public T rightJoin(SubQueryExpression<?> o, Path<?> alias) {
        return queryMixin.rightJoin(o, alias);
    }

    public <RT> Union<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    public <RT> Union<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }

    public <RT> Union<RT> unionAll(ListSubQuery<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    public <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    public <RT> T union(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }

    public <RT> T union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }

    public <RT> T unionAll(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }

    public <RT> T unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }

    @Override
    public T withRecursive(Path<?> alias, SubQueryExpression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    public T with(Path<?> alias, SubQueryExpression<?> query) {
        Expression<?> expr = OperationImpl.create(alias.getType(), Ops.ALIAS, alias, query);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @SuppressWarnings("unchecked")
    protected <RT> Union<RT> innerUnion(SubQueryExpression<?>... sq) {
        queryMixin.getMetadata().setValidate(false);
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = UnionUtils.union(sq, unionAll);
        return new UnionImpl<T, RT>((T)this, sq[0].getMetadata().getProjection());
    }

    @Override
    public T addJoinFlag(String flag) {
        return addJoinFlag(flag, JoinFlag.Position.BEFORE_TARGET);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T addJoinFlag(String flag, JoinFlag.Position position) {
        queryMixin.addJoinFlag(new JoinFlag(flag, position));
        return (T)this;
    }

    @Override
    public T addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = SimpleTemplate.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    @Override
    public T addFlag(Position position, String flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    @Override
    public T addFlag(Position position, Expression<?> flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

}
