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
package com.querydsl.core.types;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.ImmutableList;

class AppendingFactoryExpression<T> extends FactoryExpressionBase<T> {

    private static final long serialVersionUID = -1337452521648394353L;

    private final Expression<T> base;

    private final List<Expression<?>> args;

    protected AppendingFactoryExpression(Expression<T> base, Expression<?>... rest) {
        super(base.getType());
        this.base = base;
        ImmutableList.Builder<Expression<?>> builder = ImmutableList.builder();
        builder.add(base);
        builder.add(rest);
        this.args = builder.build();
    }

    @Override
    public List<Expression<?>> getArgs() {
        return args;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public T newInstance(Object... args) {
        return (T) args[0];
    }

    @Nullable
    @Override
    public <R, C> R accept(Visitor<R, C> v, @Nullable C context) {
        return base.accept(v, context);
    }
}
