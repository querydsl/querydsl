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
package com.querydsl.core.types;

import static com.querydsl.core.util.CollectionUtils.add;

import java.io.Serializable;
import java.util.Set;

import com.querydsl.core.JoinExpression;
import com.querydsl.core.QueryMetadata;

/**
 * ValidatingVisitor visits expressions and ensures that only known path instances are used
 *
 * @author tiwe
 *
 */
public final class ValidatingVisitor implements Visitor<Set<Expression<?>>, Set<Expression<?>>>, Serializable {

    private static final long serialVersionUID = 691350069621050872L;

    public static final ValidatingVisitor DEFAULT = new ValidatingVisitor();

    private final String errorTemplate;

    public ValidatingVisitor() {
        this.errorTemplate = "Undeclared path '%s'. Add this path as a source to the querydsl to be able to reference it.";
    }

    public ValidatingVisitor(String errorTemplate) {
        this.errorTemplate = errorTemplate;
    }

    @Override
    public Set<Expression<?>> visit(Constant<?> expr, Set<Expression<?>> known) {
        return known;
    }

    @Override
    public Set<Expression<?>> visit(FactoryExpression<?> expr, Set<Expression<?>> known) {
        for (Expression<?> arg : expr.getArgs()) {
            known = arg.accept(this, known);
        }
        return known;
    }

    @Override
    public Set<Expression<?>> visit(Operation<?> expr, Set<Expression<?>> known) {
        if (expr.getOperator() == Ops.ALIAS) {
            known = add(known, expr.getArg(1));
        }
        for (Expression<?> arg : expr.getArgs()) {
            known = arg.accept(this, known);
        }
        return known;
    }

    @Override
    public Set<Expression<?>> visit(ParamExpression<?> expr, Set<Expression<?>> known) {
        return known;
    }

    @Override
    public Set<Expression<?>> visit(Path<?> expr, Set<Expression<?>> known) {
        if (!known.contains(expr.getRoot())) {
            throw new IllegalArgumentException(String.format(errorTemplate,  expr.getRoot()));
        }
        return known;
    }

    @Override
    public Set<Expression<?>> visit(SubQueryExpression<?> expr, Set<Expression<?>> known) {
        Set<Expression<?>> old = known;
        final QueryMetadata md = expr.getMetadata();
        known = visitJoins(md.getJoins(), known);
        for (Expression<?> p : md.getProjection()) {
            known = p.accept(this, known);
        }
        for (OrderSpecifier<?> o : md.getOrderBy()) {
            known = o.getTarget().accept(this, known);
        }
        for (Expression<?> g : md.getGroupBy()) {
            known = g.accept(this, known);
        }
        if (md.getHaving() != null) {
            known = md.getHaving().accept(this, known);
        }
        if (md.getWhere() != null) {
            known = md.getWhere().accept(this, known);
        }
        return old;
    }


    @Override
    public Set<Expression<?>> visit(TemplateExpression<?> expr, Set<Expression<?>> known) {
        for (Object arg : expr.getArgs()) {
            if (arg instanceof Expression<?>) {
                known = ((Expression<?>)arg).accept(this, known);
            }
        }
        return known;
    }

    private Set<Expression<?>> visitJoins(Iterable<JoinExpression> joins, Set<Expression<?>> known) {
        for (JoinExpression j : joins) {
            final Expression<?> expr = j.getTarget();
            if (expr instanceof Path && ((Path)expr).getMetadata().isRoot()) {
                known = add(known, expr);
            } else {
                known = expr.accept(this, known);
            }
            if (j.getCondition() != null) {
                known = j.getCondition().accept(this, known);
            }
        }
        return known;
    }

}
