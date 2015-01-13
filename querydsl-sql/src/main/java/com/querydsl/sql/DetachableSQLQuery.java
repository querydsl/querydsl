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
package com.querydsl.sql;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.DetachableQuery;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamNotSetException;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.TemplateExpressionImpl;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.CollectionExpressionBase;
import com.querydsl.core.types.expr.CollectionOperation;
import com.querydsl.core.types.query.ListSubQuery;
import com.querydsl.core.types.template.NumberTemplate;

/**
 * Abstract superclass for SubQuery implementations
 *
 * @author tiwe
 *
 */
public abstract class DetachableSQLQuery<Q extends DetachableSQLQuery<Q>> extends DetachableQuery<Q> implements SQLCommonQuery<Q> {

    protected final Configuration configuration;
    
    public DetachableSQLQuery() {
        this(new DefaultQueryMetadata().noValidate());
    }
    
    public DetachableSQLQuery(QueryMetadata metadata) {
        this(Configuration.DEFAULT, metadata);
    }

    @SuppressWarnings("unchecked")
    public DetachableSQLQuery(Configuration configuration, QueryMetadata metadata) {
        super(new QueryMixin<Q>(metadata));
        this.queryMixin.setSelf((Q)this);
        this.configuration = configuration;
    }

    /**
     * Add the given prefix and expression as a general querydsl flag
     *
     * @param position position of the flag
     * @param prefix prefix for the flag
     * @param expr expression of the flag
     * @return
     */
    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = TemplateExpressionImpl.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given String literal as a querydsl flag
     *
     * @param position
     * @param flag
     * @return
     */
    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q addFlag(Position position, String flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given Expression as a querydsl flag
     *
     * @param position
     * @param flag
     * @return
     */
    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
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
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
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
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    @SuppressWarnings("unchecked")
    public Q addJoinFlag(String flag, JoinFlag.Position position) {
        queryMixin.addJoinFlag(new JoinFlag(flag, position));
        return (Q)this;
    }

    @Override
    public BooleanExpression exists() {
        return unique(NumberTemplate.ONE).exists();
    }

    @Override
    public BooleanExpression notExists() {
        return exists().not();
    }

    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q from(Expression<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression)subQuery, alias));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q fullJoin(EntityPath<?> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q fullJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q innerJoin(EntityPath<?> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q innerJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q join(EntityPath<?> target) {
        return queryMixin.join(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q join(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q join(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q leftJoin(EntityPath<?> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q leftJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q leftJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q rightJoin(EntityPath<?> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public <E> Q rightJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q rightJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private <T> CollectionExpressionBase<?,T> union(Operator<Object> op, List<? extends SubQueryExpression<?>> sq) {
        Expression<?> rv = sq.get(0);
        if (sq.size() == 1 && !CollectionExpression.class.isInstance(rv)) {
            return new ListSubQuery(rv.getType(), sq.get(0).getMetadata());
        } else {
            Class<?> elementType = sq.get(0).getType();
            if (rv instanceof CollectionExpression) {
                elementType = ((CollectionExpression)rv).getParameter(0);
            }
            for (int i = 1; i < sq.size(); i++) {
                rv = CollectionOperation.create(op, (Class)elementType, rv, sq.get(i));
            }
            return (CollectionExpressionBase<?,T>)rv;
        }
    }

    public <T> CollectionExpressionBase<?,T> union(List<? extends SubQueryExpression<T>> sq) {
        return union(SQLOps.UNION, sq);
    }

    public <T> CollectionExpressionBase<?,T> union(ListSubQuery<T>... sq) {
        return union(SQLOps.UNION, Arrays.asList(sq));
    }

    public <T> CollectionExpressionBase<?,T> union(SubQueryExpression<T>... sq) {
        return union(SQLOps.UNION, Arrays.asList(sq));
    }

    public <T> CollectionExpressionBase<?,T> unionAll(List<? extends SubQueryExpression<T>> sq) {
        return union(SQLOps.UNION_ALL, sq);
    }

    public <T> CollectionExpressionBase<?,T> unionAll(ListSubQuery<T>... sq) {
        return union(SQLOps.UNION_ALL, Arrays.asList(sq));
    }

    public <T> CollectionExpressionBase<?,T> unionAll(SubQueryExpression<T>... sq) {
        return union(SQLOps.UNION_ALL, Arrays.asList(sq));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q withRecursive(Path<?> alias, SubQueryExpression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q withRecursive(Path<?> alias, Expression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public WithBuilder<Q> withRecursive(Path<?> alias, Path<?>... columns) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, columns);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q with(Path<?> alias, SubQueryExpression<?> target) {
        Expression<?> expr = OperationImpl.create(alias.getType(), SQLOps.WITH_ALIAS, alias, target);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLSubQuery.class, castRequired=true)
    public Q with(Path<?> alias, Expression<?> query) {
        Expression<?> expr = OperationImpl.create(alias.getType(), SQLOps.WITH_ALIAS, alias, query);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    public WithBuilder<Q> with(Path<?> alias, Path<?>... columns) {
        Expression<?> columnsCombined = ExpressionUtils.list(Object.class, columns);
        Expression<?> aliasCombined = Expressions.operation(alias.getType(), SQLOps.WITH_COLUMNS, alias, columnsCombined);
        return new WithBuilder<Q>(queryMixin, aliasCombined);
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }
    

    @Override
    public abstract Q clone();
    
    protected abstract SQLSerializer createSerializer();
    
    protected SQLSerializer serialize(boolean forCountRow) {
        SQLSerializer serializer = createSerializer();
        serializer.setStrict(false);
        serializer.serialize(queryMixin.getMetadata(), forCountRow);
        return serializer;
    }
    
    /**
     * Get the querydsl as an SQL querydsl string and bindings
     *
     * @param exprs
     * @return
     */
    public SQLBindings getSQL(Expression<?>... exprs) {
        queryMixin.addProjection(exprs);
        SQLSerializer serializer = serialize(false);
        ImmutableList.Builder<Object> args = ImmutableList.builder();
        Map<ParamExpression<?>, Object> params = getMetadata().getParams();
        for (Object o : serializer.getConstants()) {
            if (o instanceof ParamExpression) {
                if (!params.containsKey(o)) {
                    throw new ParamNotSetException((ParamExpression<?>) o);
                }
                o = queryMixin.getMetadata().getParams().get(o);
            }
            args.add(o);
        }
        return new SQLBindings(serializer.toString(), args.build());
    }

    @Override
    public String toString() {
        if (!getMetadata().getJoins().isEmpty()) {
            SQLSerializer serializer = serialize(false);
            return serializer.toString().trim();
        } else {
            return super.toString();
        }
    }

}
