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
package com.querydsl.core.support;

import javax.annotation.Nullable;

import com.querydsl.core.DefaultQueryMetadata;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.CollectionExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.FactoryExpressionUtils.FactoryExpressionAdapter;
import com.querydsl.core.types.MapExpression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.ProjectionRole;
import com.querydsl.core.types.QTuple;
import com.querydsl.core.types.SubQueryExpression;

/**
 * Mixin style Query implementation
 *
 * @author tiwe
 *
 * @param <T> type of wrapped querydsl
 */
public class QueryMixin<T> {

    private final QueryMetadata metadata;

    private final boolean expandAnyPaths;

    private ReplaceVisitor replaceVisitor;

    private T self;

    public QueryMixin() {
        this.metadata = new DefaultQueryMetadata();
        this.expandAnyPaths = true;
    }

    public QueryMixin(QueryMetadata metadata) {
        this.metadata = metadata;
        this.expandAnyPaths = true;
    }

    public QueryMixin(QueryMetadata metadata, boolean expandAnyPaths) {
        this.metadata = metadata;
        this.expandAnyPaths = expandAnyPaths;
    }

    public QueryMixin(T self) {
        this(self, new DefaultQueryMetadata());
    }

    public QueryMixin(T self, QueryMetadata metadata) {
        this.self = self;
        this.metadata = metadata;
        this.expandAnyPaths = true;
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

    public <E> Expression<E> addProjection(Expression<E> e) {
        e = convert(e, false);
        metadata.addProjection(e);
        return e;
    }

    public T addProjection(Expression<?>... o) {
        for (Expression<?> e : o) {
            metadata.addProjection(convert(e, false));
        }
        return self;
    }

    private <P extends Path<?>> P assertRoot(P p) {
        if (!p.getRoot().equals(p)) {
            throw new IllegalArgumentException(p + " is not a root path");
        }
        return p;
    }

    private Path<?> normalizePath(Path<?> expr) {
        Context context = new Context();
        Path<?> replaced = (Path<?>)expr.accept(CollectionAnyVisitor.DEFAULT, context);
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

    @SuppressWarnings("rawtypes")
    public <RT> Expression<RT> convert(Expression<RT> expr, boolean forOrder) {
        if (expandAnyPaths) {
            if (expr instanceof Path) {
                expr = (Expression)normalizePath((Path)expr);
            } else if (expr != null) {
                if (replaceVisitor == null) {
                    replaceVisitor = new ReplaceVisitor() {
                        public Expression<?> visit(Path<?> expr, @Nullable Void context) {
                            return normalizePath(expr);
                        }
                    };
                }
                expr = (Expression)expr.accept(replaceVisitor, null);
            }
        }
        if (expr instanceof ProjectionRole<?>) {
            return convert(((ProjectionRole) expr).getProjection(), forOrder);
        } else if (expr instanceof FactoryExpression<?> && !(expr instanceof FactoryExpressionAdapter<?>)) {
            return FactoryExpressionUtils.wrap((FactoryExpression<RT>)expr);
        } else {
            return expr;
        }
    }

    public Expression<Tuple> createProjection(Expression<?>[] args) {
        return new QTuple(args);
    }

    protected <D> Expression<D> createAlias(Expression<?> expr, Path<?> alias) {
        assertRoot(alias);
        return ExpressionUtils.as((Expression)expr, alias);
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
        metadata.addHaving(normalize(e, false));
        return self;
    }

    public final T having(Predicate... o) {
        for (Predicate e : o) {
            metadata.addHaving(normalize(e, false));
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
        metadata.addJoinCondition(normalize(condition, false));
        return self;
    }

    public final T on(Predicate... conditions) {
        for (Predicate condition : conditions) {
            metadata.addJoinCondition(normalize(condition, false));
        }
        return self;
    }

    public final T orderBy(OrderSpecifier<?> spec) {
        Expression<?> e = convert(spec.getTarget(), true);
        if (!spec.getTarget().equals(e)) {
            metadata.addOrderBy(new OrderSpecifier(spec.getOrder(), e));
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
        metadata.addWhere(normalize(e, true));
        return self;
    }

    public final T where(Predicate... o) {
        for (Predicate e : o) {
            metadata.addWhere(normalize(e, true));
        }
        return self;
    }

    protected Predicate normalize(Predicate condition, boolean where) {
        return condition;
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o instanceof QueryMixin) {
            QueryMixin q = (QueryMixin)o;
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
