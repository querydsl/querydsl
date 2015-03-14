package com.querydsl.jpa;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.support.ReplaceVisitor;
import com.querydsl.core.types.*;

class JPAMapAccessVisitor extends ReplaceVisitor<Void> {

    private final QueryMetadata metadata;

    private final Map<Path<?>, Path<?>> replacements = Maps.newHashMap();

    public JPAMapAccessVisitor(QueryMetadata metadata) {
        this.metadata = metadata;
    }

    @Override
    public Expression<?> visit(Operation<?> expr, @Nullable Void context) {
        if (expr.getOperator() == Ops.CONTAINS_KEY) {
            ParameterizedExpression map = (ParameterizedExpression<?>) expr.getArg(0);
            Expression key = expr.getArg(1);
            Path replacement = new PathImpl<Object>(map.getParameter(1),
                    ExpressionUtils.createRootVariable((Path<?>)map, Math.abs(expr.hashCode())));
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

    @Override
    public Expression<?> visit(Path<?> expr, @Nullable Void context) {
        expr = (Path<?>) super.visit(expr, null);
        PathMetadata pathMetadata = expr.getMetadata();
        if (pathMetadata.getPathType() == PathType.MAPVALUE
         || pathMetadata.getPathType() == PathType.MAPVALUE_CONSTANT) {
            Path<?> replacement = replacements.get(expr);
            if (replacement == null) {
                // join parent as path123 on key(path123) = ...
                Path parent = pathMetadata.getParent();
                ParameterizedExpression parExpr = (ParameterizedExpression) parent;
                replacement = new PathImpl(parExpr.getParameter(1),
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

}
