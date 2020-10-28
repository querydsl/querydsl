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

import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Maps;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.ReplaceVisitor;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;

class JPAMapAccessVisitor extends ReplaceVisitor<Void> {

    private final QueryMetadata metadata;

    private final Map<Expression<?>, Path<?>> aliases;

    private final Map<Path<?>, Path<?>> replacements = Maps.newHashMap();

    public JPAMapAccessVisitor(QueryMetadata metadata, Map<Expression<?>, Path<?>> aliases) {
        this.metadata = metadata;
        this.aliases = aliases;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(Operation<?> expr, @Nullable Void context) {
        if (expr.getOperator() == Ops.CONTAINS_KEY) {
            ParameterizedExpression map = (ParameterizedExpression<?>) expr.getArg(0);
            Expression key = expr.getArg(1);
            Path replacement = ExpressionUtils.path(map.getParameter(1),
                    ExpressionUtils.createRootVariable((Path<?>) map, Math.abs(expr.hashCode())));
            metadata.addJoin(JoinType.LEFTJOIN, ExpressionUtils.as(map, replacement));
            metadata.addJoinCondition(ExpressionUtils.eq(
                    Expressions.operation(map.getParameter(0), JPQLOps.KEY, replacement),
                    key));
            return ExpressionUtils.isNotNull(replacement);
        } else if (expr.getOperator() == Ops.CONTAINS_VALUE) {
            ParameterizedExpression<?> map = (ParameterizedExpression<?>) expr.getArg(0);
            Expression<?> value = expr.getArg(1);
            return Expressions.predicate(JPQLOps.MEMBER_OF, value, map);
        } else {
            return super.visit(expr, context);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(Path<?> expr, @Nullable Void context) {
        expr = (Path<?>) super.visit(expr, null);
        PathMetadata pathMetadata = expr.getMetadata();
        if (pathMetadata.getPathType() == PathType.MAPVALUE
         || pathMetadata.getPathType() == PathType.MAPVALUE_CONSTANT) {
            Path<?> replacement = replacements.get(expr);
            if (replacement == null) {
                // join parent as path123 on key(path123) = ...
                Path parent = shorten(pathMetadata.getParent(), true);
                ParameterizedExpression parExpr = (ParameterizedExpression) pathMetadata.getParent();
                replacement = ExpressionUtils.path(parExpr.getParameter(1),
                        ExpressionUtils.createRootVariable(parent, replacements.size()));
                metadata.addJoin(JoinType.LEFTJOIN, ExpressionUtils.as(parent, replacement));
                metadata.addJoinCondition(ExpressionUtils.eq(
                        Expressions.operation(parExpr.getParameter(0), JPQLOps.KEY, replacement),
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
    private Path<?> shorten(Path<?> path, boolean outer) {
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
