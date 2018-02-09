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

import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;

/**
 * {@code ReplaceVisitor} is a deep visitor that can be customized to replace segments of
 * expression trees
 *
 * @param <C> context type
 */
public class ReplaceVisitor<C> implements Visitor<Expression<?>, C> {

    @Override
    public Expression<?> visit(Constant<?> expr, C context) {
        return expr;
    }

    @Override
    public Expression<?> visit(FactoryExpression<?> expr, C context) {
        List<Expression<?>> args = visit(expr.getArgs(), context);
        if (args.equals(expr.getArgs())) {
            return expr;
        } else {
            return FactoryExpressionUtils.wrap(expr, args);
        }
    }

    @Override
    public Expression<?> visit(Operation<?> expr, C context) {
        ImmutableList<Expression<?>> args = visit(expr.getArgs(), context);
        if (args.equals(expr.getArgs())) {
            return expr;
        } else if (expr instanceof Predicate) {
            return ExpressionUtils.predicate(expr.getOperator(), args);
        } else {
            return ExpressionUtils.operation(expr.getType(), expr.getOperator(), args);
        }
    }

    @Override
    public Expression<?> visit(ParamExpression<?> expr, C context) {
        return expr;
    }

    @Override
    public Expression<?> visit(Path<?> expr, C context) {
        if (expr.getMetadata().isRoot()) {
            return expr;
        } else {
            PathMetadata metadata = expr.getMetadata();
            Path<?> parent = (Path) metadata.getParent().accept(this, context);
            Object element = metadata.getElement();
            if (element instanceof Expression<?>) {
                element = ((Expression<?>) element).accept(this, context);
            }
            if (parent.equals(metadata.getParent()) && Objects.equal(element, metadata.getElement())) {
                return expr;
            } else {
                metadata = new PathMetadata(parent, element, metadata.getPathType());
                return ExpressionUtils.path(expr.getType(), metadata);
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(SubQueryExpression<?> expr, C context) {
        QueryMetadata md = new DefaultQueryMetadata();
        md.setValidate(false);
        md.setDistinct(expr.getMetadata().isDistinct());
        md.setModifiers(expr.getMetadata().getModifiers());
        md.setUnique(expr.getMetadata().isUnique());
        for (QueryFlag flag : expr.getMetadata().getFlags()) {
            md.addFlag(new QueryFlag(flag.getPosition(), flag.getFlag().accept(this, context)));
        }
        for (Expression<?> e : expr.getMetadata().getGroupBy()) {
            md.addGroupBy(e.accept(this, context));
        }
        Predicate having = expr.getMetadata().getHaving();
        if (having != null) {
            md.addHaving((Predicate) having.accept(this, context));
        }
        for (JoinExpression je : expr.getMetadata().getJoins()) {
            md.addJoin(je.getType(), je.getTarget().accept(this, context));
            if (je.getCondition() != null) {
                md.addJoinCondition((Predicate) je.getCondition().accept(this, context));
            }
            for (JoinFlag jf : je.getFlags()) {
                md.addJoinFlag(new JoinFlag(jf.getFlag().accept(this, context), jf.getPosition()));
            }
        }
        for (OrderSpecifier<?> os : expr.getMetadata().getOrderBy()) {
            OrderSpecifier<?> os2 = new OrderSpecifier(os.getOrder(), os.getTarget().accept(this,
                    context), os.getNullHandling());
            md.addOrderBy(os2);
        }
        for (Map.Entry<ParamExpression<?>, Object> entry : expr.getMetadata().getParams()
                .entrySet()) {
            md.setParam((ParamExpression) entry.getKey().accept(this, context), entry.getValue());
        }
        if (expr.getMetadata().getProjection() != null) {
            md.setProjection(expr.getMetadata().getProjection().accept(this, context));
        }
        Predicate where = expr.getMetadata().getWhere();
        if (where != null) {
            md.addWhere((Predicate) where.accept(this, context));
        }
        if (expr.getMetadata().equals(md)) {
            return expr;
        } else {
            return new SubQueryExpressionImpl(expr.getType(), md);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Expression<?> visit(TemplateExpression<?> expr, C context) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (Object arg : expr.getArgs()) {
            if (arg instanceof Expression) {
                builder.add(((Expression<?>) arg).accept(this, context));
            } else {
                builder.add(arg);
            }
        }
        ImmutableList args = builder.build();
        if (args.equals(expr.getArgs())) {
            return expr;
        } else {
            if (expr instanceof Predicate) {
                return Expressions.booleanTemplate(expr.getTemplate(), args);
            } else {
                return ExpressionUtils.template(expr.getType(), expr.getTemplate(), args);
            }
        }
    }

    private ImmutableList<Expression<?>> visit(List<Expression<?>> args, C context) {
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (Expression<?> arg : args) {
            builder.add(arg.accept(this, context));
        }
        return builder.build();
    }
}
