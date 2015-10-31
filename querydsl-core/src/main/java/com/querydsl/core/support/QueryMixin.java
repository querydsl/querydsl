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
package com.querydsl.core.support;

import javax.annotation.Nullable;

import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.core.types.FactoryExpressionUtils.FactoryExpressionAdapter;

/**
 * Mixin style Query implementation
 *
 * @param <T> type of wrapped query
 *
 * @author tiwe
 */
public class QueryMixin<T> {

    /**
     * Role of expression in conversion
     */
    public enum Role { SELECT, FROM, WHERE, GROUP_BY, HAVING, ORDER_BY }

    private final QueryMetadata metadata;

    private final boolean expandAnyPaths;

    private final ReplaceVisitor<Void> replaceVisitor = new ReplaceVisitor<Void>() {
        @Override
        public Expression<?> visit(Path<?> expr, @Nullable Void context) {
            return normalizePath(expr);
        }
    };

    protected final CollectionAnyVisitor collectionAnyVisitor = new CollectionAnyVisitor();

    private T self;

    public QueryMixin() {
        this(null, new DefaultQueryMetadata(), true);
    }

    public QueryMixin(QueryMetadata metadata) {
        this(null, metadata, true);
    }

    public QueryMixin(QueryMetadata metadata, boolean expandAnyPaths) {
        this(null, metadata, expandAnyPaths);
    }

    public QueryMixin(T self) {
        this(self, new DefaultQueryMetadata(), true);
    }

    public QueryMixin(T self, QueryMetadata metadata) {
        this(self, metadata, true);
    }

    public QueryMixin(T self, QueryMetadata metadata, boolean expandAnyPaths) {
        this.self = self;
        this.metadata = metadata;
        this.expandAnyPaths = expandAnyPaths;
    }

    public T addJoin(JoinType joinType, Expression<?> target) {
        metadata.addJoin(joinType, target);
        return self;
    }

    public T addFlag(QueryFlag queryFlag) {
        metadata.addFlag(queryFlag);
        return self;
    }

    public T addJoinFlag(JoinFlag flag) {
        metadata.addJoinFlag(flag);
        return self;
    }

    public T removeFlag(QueryFlag queryFlag) {
        metadata.removeFlag(queryFlag);
        return self;
    }

    public <E> Expression<E> setProjection(Expression<E> e) {
        e = convert(e, Role.SELECT);
        metadata.setProjection(e);
        return e;
    }

    public Expression<?> setProjection(Expression<?>... o) {
        return setProjection(Projections.tuple(o));
    }

    private <P extends Path<?>> P assertRoot(P p) {
        if (!p.getRoot().equals(p)) {
            throw new IllegalArgumentException(p + " is not a root path");
        }
        return p;
    }

    private Path<?> normalizePath(Path<?> expr) {
        Context context = new Context();
        Path<?> replaced = (Path<?>) expr.accept(collectionAnyVisitor, context);
        if (!replaced.equals(expr)) {
            for (int i = 0; i < context.paths.size(); i++) {
                Path path = context.paths.get(i).getMetadata().getParent();
                Path replacement = context.replacements.get(i);
                this.innerJoin(path, replacement);
            }
            return replaced;
        } else {
            return expr;
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <RT> Expression<RT> convert(Expression<RT> expr, Role role) {
        if (expandAnyPaths) {
            if (expr instanceof Path) {
                expr = (Expression) normalizePath((Path) expr);
            } else if (expr != null) {
                expr = (Expression) expr.accept(replaceVisitor, null);
            }
        }
        if (expr instanceof ProjectionRole<?>) {
            return convert(((ProjectionRole) expr).getProjection(), role);
        } else if (expr instanceof FactoryExpression<?> && !(expr instanceof FactoryExpressionAdapter<?>)) {
            return FactoryExpressionUtils.wrap((FactoryExpression<RT>) expr);
        } else {
            return expr;
        }
    }

    protected Predicate convert(Predicate condition, Role role) {
        return condition;
    }

    @SuppressWarnings("unchecked")
    protected <D> Expression<D> createAlias(Expression<?> expr, Path<D> alias) {
        assertRoot(alias);
        return ExpressionUtils.as((Expression) expr, alias);
    }

    public final T distinct() {
        metadata.setDistinct(true);
        return self;
    }

    public final T from(Expression<?> arg) {
        metadata.addJoin(JoinType.DEFAULT, arg);
        return self;
    }

    public final T from(Expression<?>... args) {
        for (Expression<?> arg : args) {
            metadata.addJoin(JoinType.DEFAULT, arg);
        }
        return self;
    }

    public final T fullJoin(Expression<?> target) {
        metadata.addJoin(JoinType.FULLJOIN, target);
        return self;
    }

    public final <P> T fullJoin(Expression<P> target, Path<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }
    public final <P> T fullJoin(CollectionExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }

    public final <P> T fullJoin(MapExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }

    public final <P> T fullJoin(SubQueryExpression<P> target, Path<?> alias) {
        metadata.addJoin(JoinType.FULLJOIN, createAlias(target, alias));
        return self;
    }

    public final QueryMetadata getMetadata() {
        return metadata;
    }

    public final T getSelf() {
        return self;
    }

    public final T groupBy(Expression<?> e) {
        metadata.addGroupBy(e);
        return self;
    }

    public final T groupBy(Expression<?>... o) {
        for (Expression<?> e : o) {
            metadata.addGroupBy(e);
        }
        return self;
    }

    public final T having(Predicate e) {
        metadata.addHaving(convert(e, Role.HAVING));
        return self;
    }

    public final T having(Predicate... o) {
        for (Predicate e : o) {
            metadata.addHaving(convert(e, Role.HAVING));
        }
        return self;
    }

    public final <P> T innerJoin(Expression<P> target) {
        metadata.addJoin(JoinType.INNERJOIN, target);
        return self;
    }

    public final <P> T innerJoin(Expression<P> target, Path<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    public final <P> T innerJoin(CollectionExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    public final <P> T innerJoin(MapExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    public final <P> T innerJoin(SubQueryExpression<P> target, Path<?> alias) {
        metadata.addJoin(JoinType.INNERJOIN, createAlias(target, alias));
        return self;
    }

    public final boolean isDistinct() {
        return metadata.isDistinct();
    }

    public final boolean isUnique() {
        return metadata.isUnique();
    }

    public final <P> T join(Expression<P> target) {
        metadata.addJoin(JoinType.JOIN, target);
        return self;
    }

    public final <P> T join(Expression<P> target, Path<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T join(CollectionExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T join(MapExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T join(SubQueryExpression<P> target, Path<?> alias) {
        metadata.addJoin(JoinType.JOIN, createAlias(target, alias));
        return self;
    }

    public final <P> T leftJoin(Expression<P> target) {
        metadata.addJoin(JoinType.LEFTJOIN, target);
        return self;
    }

    public final <P> T leftJoin(Expression<P> target, Path<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T leftJoin(CollectionExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T leftJoin(MapExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T leftJoin(SubQueryExpression<P> target, Path<?> alias) {
        metadata.addJoin(JoinType.LEFTJOIN, createAlias(target, alias));
        return self;
    }

    public final T limit(long limit) {
        metadata.setLimit(limit);
        return self;
    }

    public final T offset(long offset) {
        metadata.setOffset(offset);
        return self;
    }

    public final T on(Predicate condition) {
        metadata.addJoinCondition(convert(condition, Role.FROM));
        return self;
    }

    public final T on(Predicate... conditions) {
        for (Predicate condition : conditions) {
            metadata.addJoinCondition(convert(condition, Role.FROM));
        }
        return self;
    }

    @SuppressWarnings("unchecked")
    public final T orderBy(OrderSpecifier<?> spec) {
        Expression<?> e = convert(spec.getTarget(), Role.ORDER_BY);
        if (!spec.getTarget().equals(e)) {
            metadata.addOrderBy(new OrderSpecifier(spec.getOrder(), e, spec.getNullHandling()));
        } else {
            metadata.addOrderBy(spec);
        }
        return self;
    }

    public final T orderBy(OrderSpecifier<?>... o) {
        for (OrderSpecifier<?> spec : o) {
            orderBy(spec);
        }
        return self;
    }

    public final T restrict(QueryModifiers modifiers) {
        metadata.setModifiers(modifiers);
        return self;
    }

    public final <P> T rightJoin(Expression<P> target) {
        metadata.addJoin(JoinType.RIGHTJOIN, target);
        return self;
    }

    public final <P> T rightJoin(Expression<P> target, Path<P> alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T rightJoin(CollectionExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T rightJoin(MapExpression<?,P> target, Path<P> alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return getSelf();
    }

    public final <P> T rightJoin(SubQueryExpression<P> target, Path<?> alias) {
        metadata.addJoin(JoinType.RIGHTJOIN, createAlias(target, alias));
        return self;
    }

    public final <P> T set(ParamExpression<P> param, P value) {
        metadata.setParam(param, value);
        return self;
    }

    public final void setDistinct(boolean distinct) {
        metadata.setDistinct(distinct);
    }

    public final void setSelf(T self) {
        this.self = self;
    }

    public final void setUnique(boolean unique) {
        metadata.setUnique(unique);
    }

    public final T where(Predicate e) {
        metadata.addWhere(convert(e, Role.WHERE));
        return self;
    }

    public final T where(Predicate... o) {
        for (Predicate e : o) {
            metadata.addWhere(convert(e, Role.WHERE));
        }
        return self;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof QueryMixin<?>) {
            QueryMixin<?> q = (QueryMixin<?>) o;
            return q.metadata.equals(metadata);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return metadata.hashCode();
    }

    @Override
    public String toString() {
        return metadata.toString();
    }

}
