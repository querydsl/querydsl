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
package com.mysema.query.sql;

import com.mysema.query.DefaultQueryMetadata;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryFlag;
import com.mysema.query.QueryFlag.Position;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.DetachableQuery;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Path;
import com.mysema.query.types.Predicate;
import com.mysema.query.types.SubQueryExpression;
import com.mysema.query.types.TemplateExpressionImpl;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 */
public class AbstractSQLSubQuery<Q extends AbstractSQLSubQuery<Q>> extends DetachableQuery<Q> implements SQLCommonQuery<Q> {

    protected final QueryMixin<Q> queryMixin;

    public AbstractSQLSubQuery() {
        this(new DefaultQueryMetadata().noValidate());
    }

    @SuppressWarnings("unchecked")
    public AbstractSQLSubQuery(QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin = super.queryMixin;
        this.queryMixin.setSelf((Q)this);
    }

    /**
     * Add the given prefix and expression as a general query flag
     *
     * @param position position of the flag
     * @param prefix prefix for the flag
     * @param expr expression of the flag
     * @return
     */
    @Override
    public Q addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = TemplateExpressionImpl.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given String literal as a query flag
     *
     * @param position
     * @param flag
     * @return
     */
    @Override
    public Q addFlag(Position position, String flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given Expression as a query flag
     *
     * @param position
     * @param flag
     * @return
     */
    @Override
    public Q addFlag(Position position, Expression<?> flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given String literal as a join flag to the last added join with the
     * position BEFORE_TARGET
     *
     * @param flag
     * @return
     */
    @Override
    public Q addJoinFlag(String flag) {
        return addJoinFlag(flag, JoinFlag.Position.BEFORE_TARGET);
    }

    /**
     * Add the given String literal as a join flag to the last added join
     *
     * @param flag
     * @param position
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Q addJoinFlag(String flag, JoinFlag.Position position) {
        queryMixin.addJoinFlag(new JoinFlag(flag, position));
        return (Q)this;
    }

    public Q from(Expression<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    public Q from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression)subQuery, alias));
    }

    @Override
    public Q fullJoin(RelationalPath<?> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    public <E> Q fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    @Override
    public Q fullJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public Q innerJoin(RelationalPath<?> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <E> Q innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <E> Q innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    public Q innerJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public Q join(RelationalPath<?> target) {
        return queryMixin.join(target);
    }

    @Override
    public <E> Q join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <E> Q join(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    @Override
    public Q join(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public Q leftJoin(RelationalPath<?> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <E> Q leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <E> Q leftJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    @Override
    public Q leftJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    @Override
    public Q rightJoin(RelationalPath<?> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <E> Q rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public <E> Q rightJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    @Override
    public Q rightJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public Q withRecursive(Path<?> alias, SubQueryExpression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    public Q with(Path<?> alias, SubQueryExpression<?> target) {
        Expression<?> expr = OperationImpl.create(alias.getType(), SQLOps.WITH_ALIAS, alias, target);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    public String toString() {
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            SQLSerializer serializer = new SQLSerializer(Configuration.DEFAULT);
            serializer.setStrict(false);
            serializer.serialize(queryMixin.getMetadata(), false);
            return serializer.toString().trim();
        } else {
            return super.toString();
        }
    }

}
