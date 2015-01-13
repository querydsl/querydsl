/*
 * Copyright 2014, Mysema Ltd
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
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.*;
import com.querydsl.core.types.*;
import com.querydsl.core.types.template.BooleanTemplate;

/**
 * ReplaceVisitor is a deep visitor that can be customized to replace segments of
 * expression trees
 */
public class ReplaceVisitor implements Visitor<Expression<?>, Void> {

    @Override
    public Expression<?> visit(Constant<?> expr, @Nullable Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(FactoryExpression<?> expr, @Nullable Void context) {
        List<Expression<?>> args = visit(expr.getArgs());
        if (args.equals(expr.getArgs())) {
            return expr;
        } else {
            return FactoryExpressionUtils.wrap(expr, args);
        }
    }

    @Override
    public Expression<?> visit(Operation<?> expr, @Nullable Void context) {
        ImmutableList<Expression<?>> args = visit(expr.getArgs());
        if (args.equals(expr.getArgs())) {
            return expr;
        } else if (expr instanceof Predicate) {
            return new PredicateOperation((Operator)expr.getOperator(), args);
        } else {
            return new OperationImpl(expr.getType(), expr.getOperator(), args);
        }
    }

    @Override
    public Expression<?> visit(ParamExpression<?> expr, @Nullable Void context) {
        return expr;
    }

    @Override
    public Expression<?> visit(Path<?> expr, @Nullable Void context) {
        if (expr.getMetadata().isRoot()) {
            return expr;
        } else {
            PathMetadata metadata = expr.getMetadata();
            Path<?> parent = (Path)metadata.getParent().accept(this, null);
            Object element = metadata.getElement();
            if (element instanceof Expression<?>) {
                element = ((Expression) element).accept(this, null);
            }
            if (parent.equals(metadata.getParent()) && Objects.equals(element, metadata.getElement())) {
                return expr;
            } else {
                metadata = new PathMetadata(parent, element, metadata.getPathType());
                return new PathImpl(expr.getType(), metadata);
            }
        }
    }

    @Override
    public Expression<?> visit(SubQueryExpression<?> expr, @Nullable Void context) {
        QueryMetadata md = new DefaultQueryMetadata();
        md.setValidate(false);
        md.setDistinct(expr.getMetadata().isDistinct());
        md.setModifiers(expr.getMetadata().getModifiers());
        md.setUnique(expr.getMetadata().isUnique());
        for (QueryFlag flag : expr.getMetadata().getFlags()) {
            md.addFlag(new QueryFlag(flag.getPosition(), flag.getFlag().accept(this, null)));
        }
        for (Expression<?> e : expr.getMetadata().getGroupBy()) {
            md.addGroupBy(e.accept(this, null));
        }
        Predicate having = expr.getMetadata().getHaving();
        if (having != null) {
            md.addHaving((Predicate)having.accept(this, null));
        }
        for (JoinExpression je : expr.getMetadata().getJoins()) {
            md.addJoin(je.getType(), je.getTarget().accept(this, null));
            if (je.getCondition() != null) {
                md.addJoinCondition((Predicate)je.getCondition().accept(this, null));
            }
            for (JoinFlag jf : je.getFlags()) {
                md.addJoinFlag(new JoinFlag(jf.getFlag().accept(this, null), jf.getPosition()));
            }
        }
        for (OrderSpecifier<?> os : expr.getMetadata().getOrderBy()) {
            OrderSpecifier<?> os2 = new OrderSpecifier(os.getOrder(), os.getTarget().accept(this,
                    null));
            switch (os.getNullHandling()) {
                case NullsFirst: os2 = os2.nullsFirst(); break;
                case NullsLast: os2 = os2.nullsLast(); break;
            }
            md.addOrderBy(os2);
        }
        for (Map.Entry<ParamExpression<?>, Object> entry : expr.getMetadata().getParams()
                .entrySet()) {
            md.setParam((ParamExpression)entry.getKey().accept(this, null), entry.getValue());
        }
        for (Expression<?> e : expr.getMetadata().getProjection()) {
            md.addProjection(e.accept(this, null));
        }
        Predicate where = expr.getMetadata().getWhere();
        if (where != null) {
           md.addWhere((Predicate)where.accept(this, null));
        }
        if (expr.getMetadata().equals(md)) {
            return expr;
        } else {
            return new SubQueryExpressionImpl(expr.getType(), md);
        }
    }

    @Override
    public Expression<?> visit(TemplateExpression<?> expr, @Nullable Void context) {
        ImmutableList.Builder builder = ImmutableList.builder();
        for (Object arg : expr.getArgs()) {
            if (arg instanceof Expression) {
                builder.add(((Expression)arg).accept(this, null));
            } else {
                builder.add(arg);
            }
        }
        ImmutableList args = builder.build();
        if (args.equals(expr.getArgs())) {
            return expr;
        } else {
            if (expr instanceof Predicate) {
                return BooleanTemplate.create(expr.getTemplate(), args);
            } else {
                return new TemplateExpressionImpl(expr.getType(), expr.getTemplate(), args);
            }
        }
    }

    private ImmutableList<Expression<?>> visit(List<Expression<?>> args) {
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        for (Expression<?> arg : args) {
            builder.add(arg.accept(this, null));
        }
        return builder.build();
    }
}
