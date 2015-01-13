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
package com.querydsl.jpa;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;
import javax.persistence.Entity;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.querydsl.core.JoinFlag;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.Context;
import com.querydsl.core.support.PathsExtractor;
import com.querydsl.core.support.QueryMixin;
import com.querydsl.core.support.ReplaceVisitor;
import com.querydsl.core.types.*;
import com.querydsl.core.types.path.CollectionPathBase;

/**
 * JPAQueryMixin extends {@link QueryMixin} to support JPQL join construction
 *
 * @author tiwe
 *
 * @param <T>
 */
public class JPAQueryMixin<T> extends QueryMixin<T> {

    private final Set<Path<?>> paths = Sets.newHashSet();

    private final Map<Expression<?>, Path<?>> aliases = Maps.newHashMap();

    private final JPAMapAccessVisitor mapAccessVisitor;

    private final JPAListAccessVisitor listAccessVisitor;

    private ReplaceVisitor replaceVisitor;

    public static final JoinFlag FETCH = new JoinFlag("fetch ");

    public static final JoinFlag FETCH_ALL_PROPERTIES = new JoinFlag(" fetch all properties");

    public JPAQueryMixin() {
        mapAccessVisitor = new JPAMapAccessVisitor(getMetadata());
        listAccessVisitor = new JPAListAccessVisitor(getMetadata());
    }

    public JPAQueryMixin(QueryMetadata metadata) {
        super(metadata);
        mapAccessVisitor = new JPAMapAccessVisitor(metadata);
        listAccessVisitor = new JPAListAccessVisitor(metadata);
    }

    public JPAQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
        mapAccessVisitor = new JPAMapAccessVisitor(metadata);
        listAccessVisitor = new JPAListAccessVisitor(metadata);
    }

    public T fetch() {
        addJoinFlag(FETCH);
        return getSelf();
    }

    public T fetchAll() {
        addJoinFlag(FETCH_ALL_PROPERTIES);
        return getSelf();
    }

    @Override
    protected <D> Expression<D> createAlias(Expression<?> expr, Path<?> alias) {
        aliases.put(expr, alias);
        return super.createAlias(expr, alias);
    }

    private boolean isEntityPath(Path<?> path) {
        if (path instanceof CollectionPathBase) {
            return isEntityPath((Path<?>) ((CollectionPathBase)path).any());
        } else {
            return path instanceof EntityPath
                || path.getType().isAnnotationPresent(Entity.class);
        }
    }

    private <T> Class<T> getElementTypeOrType(Path<T> path) {
        if (path instanceof CollectionExpression) {
            return ((CollectionExpression)path).getParameter(0);
        } else {
            return (Class<T>) path.getType();
        }
    }

    private <T> Path<T> shorten(Path<T> path, List<Path<?>> paths) {
        PathMetadata<?> metadata = path.getMetadata();
        if (metadata.isRoot() || paths.contains(path)) {
            return path;
        } else if (aliases.containsKey(path)) {
            return (Path<T>) aliases.get(path);
        } else if (metadata.getPathType() == PathType.COLLECTION_ANY) {
            return (Path<T>) shorten(metadata.getParent(), paths);
        } else if (!isEntityPath(path)) {
            Path<?> parent = shorten(metadata.getParent(), paths);
            if (parent.equals(metadata.getParent())) {
                return path;
            } else {
                return new PathImpl<T>(path.getType(),
                        new PathMetadata(parent, metadata.getElement(), metadata.getPathType()));
            }
        } else if (metadata.getParent().getMetadata().isRoot()) {
            Class<T> type = getElementTypeOrType(path);
            Path<T> newPath = new PathImpl<T>(type, path.toString().replace('.', '_'));
            leftJoin(path, newPath);
            return newPath;
        } else {
            Class<T> type = getElementTypeOrType(path);
            Path<?> parent = shorten(metadata.getParent(), paths);
            Path<T> oldPath = new PathImpl<T>(path.getType(),
                    new PathMetadata(parent, metadata.getElement(), metadata.getPathType()));
            Path<T> newPath = new PathImpl<T>(type, oldPath.toString().replace('.', '_'));
            leftJoin(oldPath, newPath);
            return newPath;
        }
    }

    private <T> Path<T> convertPathForOrder(Path<T> path) {
        PathMetadata<?> metadata = path.getMetadata();
        // at least three levels
        if (metadata.getParent() != null && !metadata.getParent().getMetadata().isRoot()) {
            Set<Expression<?>> exprs = Sets.newHashSet();
            QueryMetadata md = getMetadata();
            exprs.addAll(md.getGroupBy());
            if (md.getWhere() != null) exprs.add(md.getWhere());
            if (md.getHaving() != null) exprs.add(md.getHaving());
            List<Path<?>> paths = Lists.newArrayList();
            // extract paths
            PathsExtractor.DEFAULT.visit(exprs, paths);

            if (!paths.contains(path) && !paths.contains(metadata.getParent())) {
                Path<?> shortened = shorten(metadata.getParent(), paths);
                return new PathImpl<T>(path.getType(),
                        new PathMetadata(shortened, metadata.getElement(), metadata.getPathType()));
            } else {
                return path;
            }
        } else {
            return path;
        }
    }

    @Override
    public <RT> Expression<RT> convert(Expression<RT> expr, boolean forOrder) {
        expr = (Expression<RT>) expr.accept(mapAccessVisitor, null);
        expr = (Expression<RT>) expr.accept(listAccessVisitor, null);
        if (forOrder) {
            if (expr instanceof Path) {
                expr = convertPathForOrder((Path)expr);
            } else {
                if (replaceVisitor == null) {
                    replaceVisitor = new ReplaceVisitor() {
                        public Expression<?> visit(Path<?> expr, Void context) {
                            return convertPathForOrder(expr);
                        }
                        public Expression<?> visit(SubQueryExpression<?> expr, @Nullable Void context) {
                            // don't shorten paths inside subquery expressions
                            return expr;
                        }
                    };
                }
                expr = (Expression<RT>)expr.accept(replaceVisitor, null);
            }
        }
        return Conversions.convert(super.convert(expr, forOrder));
    }

    @Override
    protected Predicate normalize(Predicate predicate, boolean where) {
        if (predicate != null) {
            predicate = (Predicate) ExpressionUtils.extract(predicate);
        }
        if (predicate != null) {
            predicate = (Predicate) predicate.accept(mapAccessVisitor, null);
            predicate = (Predicate) predicate.accept(listAccessVisitor, null);
        }
        if (predicate != null) {
            // transform any usage
            predicate = (Predicate) predicate.accept(JPACollectionAnyVisitor.DEFAULT, new Context());
            return predicate;
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void addCondition(Context context, int i, Path<?> path, boolean where) {
        paths.add(path);
        EntityPath<?> alias = context.replacements.get(i);
        leftJoin((Expression)path.getMetadata().getParent(), context.replacements.get(i));
        Expression index = OperationImpl.create(Integer.class, JPQLOps.INDEX, alias);
        Object element = path.getMetadata().getElement();
        if (!(element instanceof Expression)) {
            element = ConstantImpl.create(element);
        }
        Predicate condition = ExpressionUtils.eq(index, (Expression)element);
        if (where) {
            super.where(condition);
        } else {
            super.having(condition);
        }
    }

}
