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
package com.mysema.query.types.expr;

import java.util.Arrays;
import java.util.List;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Visitor;

/**
 * DslOperation represents a simple operation expression
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class DslOperation<T> extends DslExpression<T> implements Operation<T> {

    private static final long serialVersionUID = -285668548371034230L;

    /**
     * Factory method
     *
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <D> DslExpression<D> create(Class<D> type, Operator<? super D> op, Expression<?>... args) {
        return new DslOperation<D>(type, op, args);
    }

    private final Operation< T> opMixin;

    protected DslOperation(Class<T> type, Operator<? super T> op, Expression<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    protected DslOperation(Class<T> type, Operator<? super T> op, List<Expression<?>> args) {
        super(type);
        this.opMixin = new OperationImpl<T>(type, op, args);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
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

    @Override
    public boolean equals(Object o) {
        return opMixin.equals(o);
    }

    @Override
    public int hashCode() {
        return getType().hashCode();
    }

}
