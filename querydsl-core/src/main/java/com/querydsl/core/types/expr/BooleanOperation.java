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
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.PredicateOperation;
import com.querydsl.core.types.Visitor;

/**
 * BooleanOperation represents boolean operations
 *
 * @author tiwe
 *
 */
public class BooleanOperation extends BooleanExpression implements Operation<Boolean> {

    private static final long serialVersionUID = 7432281499861357581L;

    public static BooleanExpression create(Operator<? super Boolean> op, Expression<?> one) {
        return new BooleanOperation(op, ImmutableList.<Expression<?>>of(one));
    }
    
    public static BooleanExpression create(Operator<? super Boolean> op, Expression<?> one, Expression<?> two) {
        return new BooleanOperation(op, ImmutableList.of(one, two));
    }
    
    public static BooleanExpression create(Operator<? super Boolean> op, Expression<?>... args) {
        return new BooleanOperation(op, args);
    }
    
    private final PredicateOperation opMixin;

    protected BooleanOperation(Operator<? super Boolean> op, Expression<?>... args) {
        this(op, ImmutableList.copyOf(args));
    }
    
    protected BooleanOperation(Operator<? super Boolean> op, ImmutableList<Expression<?>> args) {
        super(new PredicateOperation((Operator)op, args));
        opMixin = (PredicateOperation)mixin;
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

}
