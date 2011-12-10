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
 * BooleanOperation represents boolean operations
 *
 * @author tiwe
 *
 */
public class BooleanOperation extends BooleanExpression implements Operation<Boolean> {

    private static final long serialVersionUID = 7432281499861357581L;

    public static BooleanExpression create(Operator<? super Boolean> op, Expression<?>... args){
        return new BooleanOperation(op, args);
    }

    private final Operation<Boolean> opMixin;

    protected BooleanOperation(Operator<? super Boolean> op, Expression<?>... args) {
        this(op, Arrays.asList(args));
    }

    protected BooleanOperation(Operator<? super Boolean> op, List<Expression<?>> args) {
        opMixin = new OperationImpl<Boolean>(Boolean.class, op, args);
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
    public Operator<? super Boolean> getOperator() {
        return opMixin.getOperator();
    }

    @Override
    public BooleanExpression not() {
        if (opMixin.getOperator() == Ops.NOT && opMixin.getArg(0) instanceof BooleanExpression) {
            return (BooleanExpression) opMixin.getArg(0);
        } else {
            return super.not();
        }
    }

    @Override
    public boolean equals(Object o){
        return opMixin.equals(o);
    }

    @Override
    public int hashCode(){
        return getType().hashCode();
    }

}
