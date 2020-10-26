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

import java.util.ArrayList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.Expressions;

/**
 * {@code ConstantHidingExpression} removes constants from the argument list and injects them back into the result chain
 * @param <T>
 */
public class ConstantHidingExpression<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = -7834053123363933721L;

    private final FactoryExpression<T> expr;

    private final List<Expression<?>> args;

    private final Object[] template;

    public ConstantHidingExpression(FactoryExpression<T> expr) {
        super(expr.getType());
        this.expr = expr;
        this.args = new ArrayList<>();
        this.template = new Object[expr.getArgs().size()];
        for (int i = 0; i < template.length; i++) {
            Expression<?> arg = expr.getArgs().get(i);
            Expression<?> unwrapped = unwrap(arg);
            if (unwrapped instanceof Constant) {
                template[i] = ((Constant<?>) unwrapped).getConstant();
            } else if (unwrapped.equals(Expressions.TRUE)) {
                template[i] = Boolean.TRUE;
            } else if (unwrapped.equals(Expressions.FALSE)) {
                template[i] = Boolean.FALSE;
            } else {
                args.add(arg);
            }
        }
    }

    private static Expression<?> unwrap(Expression<?> expr) {
        expr = ExpressionUtils.extract(expr);
        if (expr instanceof Operation && ((Operation<?>) expr).getOperator() == Ops.ALIAS) {
            return ((Operation<?>) expr).getArg(0);
        }
        return expr;
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Nullable
    @Override
    public T newInstance(Object... args) {
        Object[] expanded = new Object[template.length];
        System.arraycopy(template, 0, expanded, 0, template.length);
        int j = 0;
        for (int i = 0; i < expanded.length; i++) {
            if (expanded[i] == null) {
                expanded[i] = args[j++];
            }
        }
        return expr.newInstance(expanded);
    }

}
