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

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.infradna.tool.bridge_method_injector.WithBridgeMethods;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.*;
import com.querydsl.core.QueryFlag.Position;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.support.ProjectableQuery;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.types.*;
import com.querydsl.core.types.expr.Wildcard;
import com.querydsl.core.types.query.ListSubQuery;
import com.querydsl.core.types.template.NumberTemplate;
import com.querydsl.core.types.template.SimpleTemplate;

/**
 * ProjectableSQLQuery is the base type for SQL querydsl implementations
 *
 * @param <Q> concrete subtype
 */
public abstract class ProjectableSQLQuery<Q extends ProjectableSQLQuery<Q> & Query<Q>> extends ProjectableQuery<Q> implements SQLCommonQuery<Q> {

    private static final Path<?> defaultQueryAlias = new PathImpl(Object.class, "querydsl");

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

    /**
     * Add the given String literal as a join flag to the last added join with the position
     * BEFORE_TARGET
     *
     * @param flag
     * @return
     */
    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
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
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    @SuppressWarnings("unchecked")
    public Q addJoinFlag(String flag, JoinFlag.Position position) {
        queryMixin.addJoinFlag(new JoinFlag(flag, position));
        return (Q)this;
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
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q addFlag(Position position, String prefix, Expression<?> expr) {
        Expression<?> flag = SimpleTemplate.create(expr.getType(), prefix + "{0}", expr);
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    /**
     * Add the given querydsl flag
     *
     * @param flag
     * @return
     */
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q addFlag(QueryFlag flag) {
        return queryMixin.addFlag(flag);
    }

    /**
     * Add the given String literal as querydsl flag
     *
     * @param position
     * @param flag
     * @return
     */
    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
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
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q addFlag(Position position, Expression<?> flag) {
        return queryMixin.addFlag(new QueryFlag(position, flag));
    }

    @Override
    public long count() {
        Number number = uniqueResult(Wildcard.countAsInt);
        return number.longValue();
    }

    @Override
    public boolean exists() {
        return singleResult(NumberTemplate.ONE) != null;
    }

    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q from(Expression<?> arg) {
        return queryMixin.from(arg);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q from(Expression<?>... args) {
        return queryMixin.from(args);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Q from(SubQueryExpression<?> subQuery, Path<?> alias) {
        return queryMixin.from(ExpressionUtils.as((Expression) subQuery, alias));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q fullJoin(EntityPath<?> target) {
        return queryMixin.fullJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q fullJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q fullJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.fullJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q fullJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.fullJoin(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q innerJoin(EntityPath<?> target) {
        return queryMixin.innerJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q innerJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q innerJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.innerJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q innerJoin(ForeignKey<E> key, RelationalPath<E> entity) {
        return queryMixin.innerJoin(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q join(EntityPath<?> target) {
        return queryMixin.join(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q join(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q join(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.join(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q join(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.join(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q leftJoin(EntityPath<?> target) {
        return queryMixin.leftJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q leftJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q leftJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.leftJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q leftJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.leftJoin(entity).on(key.on(entity));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q rightJoin(EntityPath<?> target) {
        return queryMixin.rightJoin(target);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q rightJoin(RelationalFunctionCall<E> target, Path<E> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q rightJoin(SubQueryExpression<?> target, Path<?> alias) {
        return queryMixin.rightJoin(target, alias);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <E> Q rightJoin(ForeignKey<E> key, RelationalPath<E>  entity) {
        return queryMixin.rightJoin(entity).on(key.on(entity));
    }

    public QueryMetadata getMetadata() {
        return queryMixin.getMetadata();
    }

    @SuppressWarnings("unchecked")
    private <RT> Union<RT> innerUnion(SubQueryExpression<?>... sq) {
        queryMixin.getMetadata().setValidate(false);
        if (!queryMixin.getMetadata().getJoins().isEmpty()) {
            throw new IllegalArgumentException("Don't mix union and from");
        }
        this.union = UnionUtils.union(sq, unionAll);
        this.firstUnionSubQuery = sq[0];
        return new UnionImpl<Q ,RT>((Q)this, sq[0].getMetadata().getProjection());
    }

    @Override
    public CloseableIterator<Tuple> iterate(Expression<?>... args) {
        return iterate(queryMixin.createProjection(args));
    }

    @Override
    public List<Tuple> list(Expression<?>... args) {
        return list(queryMixin.createProjection(args));
    }

    @Override
    public SearchResults<Tuple> listResults(Expression<?>... args) {
        return listResults(queryMixin.createProjection(args));
    }

    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q on(Predicate condition) {
        return queryMixin.on(condition);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q on(Predicate... conditions) {
        return queryMixin.on(conditions);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> union(ListSubQuery<RT>... sq) {
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <RT> Q union(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> union(SubQueryExpression<RT>... sq) {
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <RT> Q union(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, false));
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> unionAll(ListSubQuery<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <RT> Q unionAll(Path<?> alias, ListSubQuery<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    public <RT> Union<RT> unionAll(SubQueryExpression<RT>... sq) {
        unionAll = true;
        return innerUnion(sq);
    }

    /**
     * Creates an union expression for the given subqueries
     *
     * @param <RT>
     * @param sq
     * @return
     */
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public <RT> Q unionAll(Path<?> alias, SubQueryExpression<RT>... sq) {
        return from(UnionUtils.union(sq, alias, true));
    }

    @Override
    public Tuple uniqueResult(Expression<?>... args) {
        return uniqueResult(queryMixin.createProjection(args));
    }

    @Override
    public <RT> RT uniqueResult(Expression<RT> expr) {
        if (getMetadata().getModifiers().getLimit() == null
           && !expr.toString().contains("count(")) {
            limit(2);
        }
        CloseableIterator<RT> iterator = iterate(expr);
        return uniqueResult(iterator);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q withRecursive(Path<?> alias, SubQueryExpression<?> query) {
        queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, SQLTemplates.RECURSIVE));
        return with(alias, query);
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
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
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
    public Q with(Path<?> alias, SubQueryExpression<?> query) {
        Expression<?> expr = OperationImpl.create(alias.getType(), SQLOps.WITH_ALIAS, alias, query);
        return queryMixin.addFlag(new QueryFlag(QueryFlag.Position.WITH, expr));
    }

    @Override
    @WithBridgeMethods(value=AbstractSQLQuery.class, castRequired=true)
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
    
    protected void clone(Q query) {
        this.union = query.union;
        this.unionAll = query.unionAll;
        this.firstUnionSubQuery = query.firstUnionSubQuery;
    }

    @Override
    public abstract Q clone();
    
    protected abstract SQLSerializer createSerializer();

    private Set<Path<?>> getRootPaths(Collection<Expression<?>> exprs) {
        Set<Path<?>> paths = Sets.newHashSet();
        for (Expression<?> e : exprs) {
            Path<?> path = e.accept(PathExtractor.DEFAULT, null);
            if (path != null && !path.getMetadata().isRoot()) {
                paths.add(path.getMetadata().getRoot());
            }
        }
        return paths;
    }

    private Collection<Expression<?>> expandProjection(Collection<Expression<?>> exprs) {
        if (exprs.size() == 1 && exprs.iterator().next() instanceof FactoryExpression) {
            return ((FactoryExpression) exprs.iterator().next()).getArgs();
        } else {
            return exprs;
        }
    }

    protected SQLSerializer serialize(boolean forCountRow) {
        SQLSerializer serializer = createSerializer();
        if (union != null) {
            if (queryMixin.getMetadata().getProjection().isEmpty() ||
                expandProjection(queryMixin.getMetadata().getProjection()).equals(
                expandProjection(firstUnionSubQuery.getMetadata().getProjection()))) {
                serializer.serializeUnion(union, queryMixin.getMetadata(), unionAll);
            } else {
                QueryMixin mixin2 = new QueryMixin(queryMixin.getMetadata().clone());
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
    
    public String toString() {
        SQLSerializer serializer = serialize(false);
        return serializer.toString().trim();
    }

}
