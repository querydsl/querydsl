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
package com.mysema.query.support;

import java.util.Collections;
import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpressionBase;
import com.mysema.query.types.Visitor;

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

    private final Enum<?>[] values;

    public EnumConversion(Expression<T> expr) {
        super(expr.getType());
        exprs = Collections.<Expression<?>>singletonList(expr);
        try {
            values = (Enum<?>[]) expr.getType().getMethod("values").invoke(null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
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
                return (T)Enum.valueOf((Class)getType(), (String)args[0]);
            } else if (args[0] instanceof Number) {
                return (T)values[((Number)args[0]).intValue()];
            } else {
                return (T)args[0];
            }
        } else {
            return null;
        }
    }

}
