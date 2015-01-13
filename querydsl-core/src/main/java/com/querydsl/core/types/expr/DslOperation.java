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
package com.querydsl.core.types.expr;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Visitor;

/**
 * DslOperation represents a simple operation expression
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class DslOperation<T> extends DslExpression<T> implements Operation<T> {

    private static final long serialVersionUID = -285668548371034230L;

    public static <D> DslExpression<D> create(Class<D> type, Operator<? super D> op, Expression<?> one) {
        return new DslOperation<D>(type, op, ImmutableList.<Expression<?>>of(one));
    }
    
    public static <D> DslExpression<D> create(Class<D> type, Operator<? super D> op, Expression<?> one, Expression<?> two) {
        return new DslOperation<D>(type, op, ImmutableList.of(one, two));
    }
    
    public static <D> DslExpression<D> create(Class<D> type, Operator<? super D> op, Expression<?>... args) {
        return new DslOperation<D>(type, op, args);
    }

    private final OperationImpl<T> opMixin;

    protected DslOperation(Class<T> type, Operator<? super T> op, Expression<?>... args) {
        this(type, op, ImmutableList.copyOf(args));
    }

    protected DslOperation(Class<T> type, Operator<? super T> op, ImmutableList<Expression<?>> args) {
        super(new OperationImpl<T>(type, op, args));
        this.opMixin = (OperationImpl<T>)mixin;
    }
    
    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(opMixin, context);
    }

    @Override
    public Expression<?> getArg(int index) {
        return opMixin.getArg(index);
    }

    @Override
    public List<Expression<?>> getArgs() {
        return opMixin.getArgs();
    }

    @Override
    public Operator<? super T> getOperator() {
        return opMixin.getOperator();
    }

}
