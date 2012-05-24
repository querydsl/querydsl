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
import com.mysema.query.types.Ops;
import com.mysema.query.types.Visitor;

/**
 * NumberOperation represents numeric operations
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class NumberOperation<T extends Number & Comparable<?>>
        extends NumberExpression<T> implements Operation<T> {

    private static final long serialVersionUID = -3593040852095778453L;

    /**
     * Factory method
     *
     * @param <D>
     * @param type
     * @param op
     * @param args
     * @return
     */
    public static <D extends Number & Comparable<?>> NumberExpression<D> create(Class<? extends D> type, Operator<? super D> op, Expression<?>... args){
        return new NumberOperation<D>(type, op, args);
    }

    private final Operation<T> opMixin;

    protected NumberOperation(Class<? extends T> type, Operator<? super T> op, Expression<?>... args) {
        this(type, op, Arrays.asList(args));
    }

    protected NumberOperation(Class<? extends T> type, Operator<? super T> op, List<Expression<?>> args) {
        super(new OperationImpl<T>(type, op, args));
        this.opMixin = (Operation<T>)mixin;
    }

    @SuppressWarnings("unchecked")
    @Override
    public NumberExpression<T> negate(){
        if (opMixin.getOperator() == Ops.NEGATE) {
            return (NumberExpression<T>) opMixin.getArg(0);
        } else {
            return super.negate();
        }
    }
    
    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
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

}
