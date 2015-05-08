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
package com.querydsl.core.types.dsl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;

/**
 * {@code BooleanOperation} represents boolean operations
 *
 * @author tiwe
 *
 */
public class BooleanOperation extends BooleanExpression implements Operation<Boolean> {

    private static final long serialVersionUID = 7432281499861357581L;

    private final PredicateOperation opMixin;

    protected BooleanOperation(Operator op, Expression<?>... args) {
        this(op, ImmutableList.copyOf(args));
    }
    
    protected BooleanOperation(Operator op, ImmutableList<Expression<?>> args) {
        super(ExpressionUtils.predicate(op, args));
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
    public Operator getOperator() {
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
