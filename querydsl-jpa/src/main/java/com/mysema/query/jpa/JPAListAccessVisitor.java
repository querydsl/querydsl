package com.mysema.query.jpa;

import javax.annotation.Nullable;
import java.util.Map;

import com.google.common.collect.Maps;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.support.Expressions;
import com.mysema.query.support.ReplaceVisitor;
import com.mysema.query.types.*;

class JPAListAccessVisitor extends ReplaceVisitor {

    private final QueryMetadata metadata;

    private final Map<Path<?>, Path<?>> replacements = Maps.newHashMap();

    public JPAListAccessVisitor(QueryMetadata metadata) {
        this.metadata = metadata;
    }

    public Expression<?> visit(Path<?> expr, @Nullable Void context) {
        expr = (Path<?>) super.visit(expr, null);
        PathMetadata pathMetadata = expr.getMetadata();
        if (pathMetadata.getPathType() == PathType.LISTVALUE
                || pathMetadata.getPathType() == PathType.LISTVALUE_CONSTANT) {
            Path<?> replacement = replacements.get(expr);
            if (replacement == null) {
                // join parent as path123 on index(path123) = ...
                Path parent = pathMetadata.getParent();
                replacement = new PathImpl(expr.getType(),
                        ExpressionUtils.createRootVariable(parent));
                metadata.addJoin(JoinType.LEFTJOIN, ExpressionUtils.as(parent, replacement));
                metadata.addJoinCondition(ExpressionUtils.eq(
                        (Expression)Expressions.operation(Integer.class, JPQLOps.INDEX, replacement),
                        ExpressionUtils.toExpression(pathMetadata.getElement())));
                replacements.put(expr, replacement);
            }
            return replacement;
        } else {
            return super.visit(expr, context);
        }
    }

}