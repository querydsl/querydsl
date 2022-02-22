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
package com.querydsl.jpa;

import java.util.HashMap;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.ReplaceVisitor;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;

public class JPAListAccessVisitor extends ReplaceVisitor<Void> {

    private final QueryMetadata metadata;

    private final Map<Expression<?>, Path<?>> aliases;

    private final Map<Path<?>, Path<?>> replacements = new HashMap<>();

    public JPAListAccessVisitor(QueryMetadata metadata, Map<Expression<?>, Path<?>> aliases) {
        this.metadata = metadata;
        this.aliases = aliases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(Path<?> expr, @Nullable Void context) {
        expr = (Path<?>) super.visit(expr, null);
        PathMetadata pathMetadata = expr.getMetadata();
        if (pathMetadata.getPathType() == PathType.LISTVALUE
                || pathMetadata.getPathType() == PathType.LISTVALUE_CONSTANT) {
            Path<?> replacement = replacements.get(expr);
            if (replacement == null) {
                // join parent as path123 on index(path123) = ...
                Path parent = shorten(pathMetadata.getParent(), true);
                replacement = ExpressionUtils.path(expr.getType(),
                        ExpressionUtils.createRootVariable(parent, replacements.size()));
                metadata.addJoin(JoinType.LEFTJOIN, ExpressionUtils.as(parent, replacement));
                metadata.addJoinCondition(ExpressionUtils.eq(
                        (Expression) Expressions.operation(Integer.class, JPQLOps.INDEX, replacement),
                        ExpressionUtils.toExpression(pathMetadata.getElement())));
                replacements.put(expr, replacement);
            }
            return replacement;
        } else {
            return super.visit(expr, context);
        }
    }

    /**
     * Shorten the parent path to a length of max 2 elements
     */
    protected Path<?> shorten(Path<?> path, boolean outer) {
        if (aliases.containsKey(path)) {
            return aliases.get(path);
        } else if (path.getMetadata().isRoot()) {
            return path;
        } else if (path.getMetadata().getParent().getMetadata().isRoot() && outer) {
            return path;
        } else {
            Class<?> type = JPAQueryMixin.getElementTypeOrType(path);
            Path<?> parent = shorten(path.getMetadata().getParent(), false);
            Path oldPath = ExpressionUtils.path(path.getType(),
                    new PathMetadata(parent, path.getMetadata().getElement(), path.getMetadata().getPathType()));
            if (oldPath.getMetadata().getParent().getMetadata().isRoot() && outer) {
                return oldPath;
            } else {
                Path newPath = ExpressionUtils.path(type, ExpressionUtils.createRootVariable(oldPath));
                aliases.put(path, newPath);
                metadata.addJoin(JoinType.LEFTJOIN, ExpressionUtils.as(oldPath, newPath));
                return newPath;
            }
        }
    }

}
