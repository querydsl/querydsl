/*
 * Copyright 2012, Mysema Ltd
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

import java.util.Collections;
import java.util.List;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpressionBase;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.util.MathUtils;

/**
 * NumberConversion ensures that the results of a numeric projection confirm to the type of the
 * projection expression
 *
 * @author tiwe
 *
 * @param <T>
 */
public class NumberConversion<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = 7840412008633901748L;

    private final List<Expression<?>> exprs;

    public NumberConversion(Expression<T> expr) {
        super(expr.getType());
        exprs = Collections.<Expression<?>>singletonList(expr);
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return exprs;
    }

    @Override
    public T newInstance(Object... args) {
        if (args[0] != null) {
            if (getType().equals(Boolean.class)) {
                return (T)Boolean.valueOf(((Number)args[0]).intValue() > 0);
            } else {
                return (T)MathUtils.cast((Number)args[0], (Class)getType());
            }
        } else {
            return null;
        }
    }

}
