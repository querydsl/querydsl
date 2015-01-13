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

import com.google.common.base.Preconditions;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpressionBase;
import com.querydsl.core.types.Visitor;

/**
 * EnumConversion ensures that the results of an enum projection confirm to the type of the
 * projection expression
 *
 * @author tiwe
 *
 * @param <T>
 */
public class EnumConversion<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = 7840412008633901748L;

    private final List<Expression<?>> exprs;

    private final T[] values;

    public EnumConversion(Expression<T> expr) {
        super(expr.getType());
        Class<? extends T> type = getType();
        Preconditions.checkArgument(type.isEnum(), "%s is not an enum", type);
        exprs = Collections.<Expression<?>>singletonList(expr);
        values = type.getEnumConstants();
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
            if (args[0] instanceof String) {
                @SuppressWarnings("unchecked") //The expression type is an enum
                T rv = (T) Enum.valueOf(getType().asSubclass(Enum.class), (String)args[0]);
                return rv;
            } else if (args[0] instanceof Number) {
                return values[((Number)args[0]).intValue()];
            } else {
                @SuppressWarnings("unchecked")
                T rv = (T)args[0];
                return rv;
            }
        } else {
            return null;
        }
    }

}
