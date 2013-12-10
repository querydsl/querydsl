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

import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.query.JoinFlag;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.Context;
import com.mysema.query.support.ListAccessVisitor;
import com.mysema.query.support.QueryMixin;
import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionUtils;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathType;
import com.mysema.query.types.Predicate;

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

    public static final JoinFlag FETCH = new JoinFlag("fetch ");

    public static final JoinFlag FETCH_ALL_PROPERTIES = new JoinFlag(" fetch all properties");

    public JPAQueryMixin() {}

    public JPAQueryMixin(QueryMetadata metadata) {
        super(metadata);
    }

    public JPAQueryMixin(T self, QueryMetadata metadata) {
        super(self, metadata);
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

    private <T> Path<T> shorten(Path<T> path) {
        PathMetadata<?> metadata = path.getMetadata();
        if (metadata.getPathType() != PathType.PROPERTY) {
            return path;
        } else if (aliases.containsKey(path)) {
            return (Path<T>) aliases.get(path);
        } else if (metadata.getParent().getMetadata().isRoot()) {
            Path<T> newPath = new PathImpl<T>(path.getType(), path.toString().replace('.', '_'));
            leftJoin(path, newPath);
            return newPath;
        } else {
            Path<?> parent = shorten(metadata.getParent());
            Path<T> oldPath = new PathImpl<T>(path.getType(),
                    new PathMetadata(parent, metadata.getElement(), metadata.getPathType()));
            Path<T> newPath = new PathImpl<T>(path.getType(), oldPath.toString().replace('.', '_'));
            leftJoin(oldPath, newPath);
            return newPath;
        }
    }

    @Override
    public <RT> Expression<RT> convert(Expression<RT> expr, boolean forOrder) {
        if (forOrder && expr instanceof Path) {
            Path<?> path = (Path<?>)expr;
            PathMetadata<?> metadata = path.getMetadata();
            // at least three levels
            if (metadata.getParent() != null && !metadata.getParent().getMetadata().isRoot()) {
                Path<?> shortened = shorten(metadata.getParent());
                expr = new PathImpl<RT>((Class)shortened.getType(),
                    new PathMetadata(shortened, metadata.getElement(), metadata.getPathType()));
            }
        }
        return super.convert(Conversions.convert(expr), forOrder);
    }

    @Override
    protected Predicate normalize(Predicate predicate, boolean where) {
        if (predicate != null) {
            predicate = (Predicate) ExpressionUtils.extract(predicate);
        }
        if (predicate != null) {
            // transform any usage
            predicate = (Predicate) predicate.accept(JPACollectionAnyVisitor.DEFAULT, new Context());

            // transform list access
            Context context = new Context();
            predicate = (Predicate) predicate.accept(ListAccessVisitor.DEFAULT, context);
            for (int i = 0; i < context.paths.size(); i++) {
                Path<?> path = context.paths.get(i);
                if (!paths.contains(path)) {
                    addCondition(context, i, path, where);
                }
            }
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
