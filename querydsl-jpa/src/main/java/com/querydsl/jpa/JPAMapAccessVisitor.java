package com.querydsl.jpa;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.support.Expressions;
import com.querydsl.core.support.ReplaceVisitor;
import com.querydsl.core.types.*;

class JPAMapAccessVisitor extends ReplaceVisitor {

    private final QueryMetadata metadata;

    private final Map<Path<?>, Path<?>> replacements = Maps.newHashMap();

    public JPAMapAccessVisitor(QueryMetadata metadata) {
        this.metadata = metadata;
    }

    public Expression<?> visit(Path<?> expr, @Nullable Void context) {
        expr = (Path<?>) super.visit(expr, null);
        PathMetadata pathMetadata = expr.getMetadata();
        if (pathMetadata.getPathType() == PathType.MAPVALUE
         || pathMetadata.getPathType() == PathType.MAPVALUE_CONSTANT) {
            Path<?> replacement = replacements.get(expr);
            if (replacement == null) {
                // join parent as path123 on key(path123) = ...
                Path parent = pathMetadata.getParent();
                ParametrizedExpression parExpr = (ParametrizedExpression) parent;
                replacement = new PathImpl(parExpr.getParameter(1),
                        ExpressionUtils.createRootVariable(parent));
                metadata.addJoin(JoinType.JOIN, ExpressionUtils.as(parent, replacement));
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

}
