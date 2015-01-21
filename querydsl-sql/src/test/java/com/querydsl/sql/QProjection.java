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
package com.querydsl.sql;

import java.util.Arrays;
import java.util.List;

import com.querydsl.core.SimpleConstant;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionBase;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.Visitor;

public class QProjection extends ExpressionBase<Projection> implements FactoryExpression<Projection>{

    private static final long serialVersionUID = -7330905848558102164L;

    private final List<Expression<?>> args;
    
    public QProjection(Expression<?>... args) {
        super(Projection.class);
        this.args = Arrays.asList(args);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Projection newInstance(final Object... args) {
        return new Projection() {

            @Override
            public <T> T get(int index, Class<T> type) {
                return (T) args[index];
            }

            @Override
            public <T> T get(Expression<T> expr) {
                int index = getArgs().indexOf(expr);
                return index != -1 ? (T) args[index] : null;
            }

            @Override
            public <T> Expression<T> getExpr(Expression<T> expr) {
                T val = get(expr);
                return val != null ? SimpleConstant.create(val) : null;
            }

            @Override
            public <T> Expression<T> getExpr(int index, Class<T> type) {
                T val = (T)args[index];
                return val != null ? SimpleConstant.create(val) : null;
            }

            @Override
            public Object[] toArray() {
                return args;
            }

        };
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}
