/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.Query;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.support.FetchableSubQueryBase;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.Wildcard;

/**
 * {@code ProjectableSQLQuery} is the base type for SQL query implementations
 *
 * @param <T> result type
 * @param <Q> concrete subtype
 */
public abstract class ProjectableSQLQuery<T, Q extends ProjectableSQLQuery<T, Q> & Query<Q>> extends FetchableSubQueryBase<T, Q>
        implements SQLCommonQuery<Q>, FetchableQuery<T, Q> {

    private static final Path<?> defaultQueryAlias = ExpressionUtils.path(Object.class, "query");

    protected final Configuration configuration;

    @Nullable
    protected Expression<?> union;

    protected SubQueryExpression<?> firstUnionSubQuery;

    protected boolean unionAll;

    @SuppressWarnings("unchecked")
    public ProjectableSQLQuery(QueryMixin<Q> queryMixin, Configuration configuration) {
        super(queryMixin);
        this.queryMixin.setSelf((Q) this);
        this.configuration = configuration;
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, @Nullable C context) {
        if (union != null) {
            return union.accept(v, context);
        } else {
            return super.accept(v, context);
        }
    }

    /**
     * Add the given String literal as a join flag to the last added join with the position
     * BEFORE_TARGET
     *
     * @param flag join flag
     * @return the current object
     */
    @Override
    public Q addJoinFlag(String flag) {
        return addJoinFlag(flag, JoinFlag.Position.BEFORE_TARGET);
    }

    /**
     * Add the given String literal as a join flag to the last added join
     *
     * @param flag join flag
     * @param position position
     * @return the current object
     */
    @Override
    @SuppressWarnings("unchecked")
    public Q addJoinFlag(String flag, JoinFlag.Position position) {
        queryMixin.addJoinFlag(new JoinFlag(flag, position));
        return (Q) this;
    }

    /**
     * Add the given prefix and expression as a general query flag
     *
     * @param position position of the flag
     * @param prefix prefix for the flag
     * @param expr expression of the flag
     * @return the current object
     */
    @Override
    public Q addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = Expressions.template(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given query flag
     *
     * @param flag query flag
     * @return the current object
     */
    public Q addFlag(QueryFlag flag) {
        return queryMixin.addFlag(flag);
    }

    /**
     * Add the given String literal as query flag
     *
     * @param position position
     * @param flag query flag
     * @return the current object
     */
    @Override
    public Q addFlag(Position position, String flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given Expression as a query flag
     *
     * @param position position
     * @param flag query flag
     * @return the current object
     */
    @Override
    public Q addFlag(Position position, Expression<?> flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    @Override
    public long fetchCount() {
        queryMixin.setProjection(Wildcard.countAsInt);
        return ((Number) fetchOne()).longValue();
    }

    public Q from(Expression<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    public Q from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression) subQuery, alias));
    }

    @Override
    public Q fullJoin(EntityPath<?> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    public <E> Q fullJoin(EntityPath<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public <E> Q fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public Q fullJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    public <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    @Override
    public Q innerJoin(EntityPath<?> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    public <E> Q innerJoin(EntityPath<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <E> Q innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public Q innerJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    public <E> Q innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    public Q join(EntityPath<?> target) {
        return queryMixin.join(target);
    }

    @Override
    public <E> Q join(EntityPath<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <E> Q join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public Q join(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    public <E> Q join(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    @Override
    public Q leftJoin(EntityPath<?> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    public <E> Q leftJoin(EntityPath<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <E> Q leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public Q leftJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    public <E> Q leftJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    @Override
    public Q rightJoin(EntityPath<?> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    public <E> Q rightJoin(EntityPath<E> target, Path<E> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <E> Q rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public Q rightJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    public <E> Q rightJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    @SuppressWarnings("unchecked")
    private <RT> Union<RT> innerUnion(SubQueryExpression<?>... sq) {
        return innerUnion((List) ImmutableList.copyOf(sq));
    }

    @SuppressWarnings("unchecked")
    private <RT> Union<RT> innerUnion(List<SubQueryExpression<RT>> sq) {
        queryMixin.setProjection(sq.get(0).getMetadata().getProjection());
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = UnionUtils.union(sq, unionAll);
        this.firstUnionSubQuery = sq.get(0);
        return new UnionImpl(this);
    }

    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq subqueries
     * @return union
     */
    public <RT> Union<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq subqueries
     * @return union
     */
    public <RT> Union<RT> union(List<SubQueryExpression<RT>> sq) {
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param alias alias for union
     * @param sq subqueries
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    public <RT> Q union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(ImmutableList.copyOf(sq), (Path) alias, false));
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq subqueries
     * @return union
     */
    public <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq subqueries
     * @return union
     */
    public <RT> Union<RT> unionAll(List<SubQueryExpression<RT>> sq) {
        unionAll = true;
        return innerUnion(sq);
    }


    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param alias alias for union
     * @param sq subqueries
     * @return the current object
     */
    @SuppressWarnings("unchecked")
    public <RT> Q unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(ImmutableList.copyOf(sq), (Path) alias, true));
    }

    @Override
    public T fetchOne() {
        if (getMetadata().getModifiers().getLimit() == null
            && !queryMixin.getMetadata().getProjection().toString().contains("count(")) {
            limit(2);
        }
        CloseableIterator<T> iterator = iterate();
        return uniqueResult(iterator);
    }

    @Override
    public Q withRecursive(Path<?> alias, SubQueryExpression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    public Q withRecursive(Path<?> alias, Expression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    public WithBuilder<Q> withRecursive(Path<?> alias, Path<?>... columns) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, columns);
    }

    @Override
    public Q with(Path<?> alias, SubQueryExpression<?> query) {
        Expression<?> expr = ExpressionUtils.operation(alias.getType(), SQLOps.WITH_ALIAS, alias, query);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    public Q with(Path<?> alias, Expression<?> query) {
        Expression<?> expr = ExpressionUtils.operation(alias.getType(), SQLOps.WITH_ALIAS, alias, query);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    public WithBuilder<Q> with(Path<?> alias, Path<?>... columns) {
        Expression<?> columnsCombined = ExpressionUtils.list(Object.class, columns);
        Expression<?> aliasCombined = Expressions.operation(alias.getType(), SQLOps.WITH_COLUMNS, alias, columnsCombined);
        return new WithBuilder<Q>(queryMixin, aliasCombined);
    }

    protected void clone(Q query) {
        this.union = query.union;
        this.unionAll = query.unionAll;
        this.firstUnionSubQuery = query.firstUnionSubQuery;
    }

    @Override
    public abstract Q clone();

    protected abstract SQLSerializer createSerializer();

    private Set<Path<?>> getRootPaths(Collection<? extends Expression<?>> exprs) {
        Set<Path<?>> paths = Sets.newHashSet();
        for (Expression<?> e : exprs) {
            Path<?> path = e.accept(PathExtractor.DEFAULT, null);
            if (path != null && !path.getMetadata().isRoot()) {
                paths.add(path.getMetadata().getRootPath());
            }
        }
        return paths;
    }

    @SuppressWarnings("unchecked")
    private Collection<? extends Expression<?>> expandProjection(Expression<?> expr) {
        if (expr instanceof FactoryExpression) {
            return ((FactoryExpression) expr).getArgs();
        } else {
            return ImmutableList.of(expr);
        }
    }

    @SuppressWarnings("unchecked")
    protected SQLSerializer serialize(boolean forCountRow) {
        SQLSerializer serializer = createSerializer();
        if (union != null) {
            if (queryMixin.getMetadata().getProjection() == null ||
                expandProjection(queryMixin.getMetadata().getProjection())
                .equals(expandProjection(firstUnionSubQuery.getMetadata().getProjection()))) {
                serializer.serializeUnion(union, queryMixin.getMetadata(), unionAll);
            } else {
                QueryMixin<Q> mixin2 = new QueryMixin<Q>(queryMixin.getMetadata().clone());
                Set<Path<?>> paths = getRootPaths(expandProjection(mixin2.getMetadata().getProjection()));
                if (paths.isEmpty()) {
                    mixin2.from(ExpressionUtils.as((Expression) union, defaultQueryAlias));
                } else if (paths.size() == 1) {
                    mixin2.from(ExpressionUtils.as((Expression) union, paths.iterator().next()));
                } else {
                    throw new IllegalStateException("Unable to create serialize union");
                }
                serializer.serialize(mixin2.getMetadata(), forCountRow);
            }
        } else {
            serializer.serialize(queryMixin.getMetadata(), forCountRow);
        }
        return serializer;
    }

    /**
     * Get the query as an SQL query string and bindings
     *
     * @return SQL string and bindings
     */
    public SQLBindings getSQL() {
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
        SQLSerializer serializer = serialize(false);
        return serializer.toString().trim();
    }

}
