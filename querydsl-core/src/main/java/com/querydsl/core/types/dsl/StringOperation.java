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
package com.querydsl.core.types.dsl;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.*;

/**
 * StringOperation represents a String typed operation
 *
 * @author tiwe
 *
 */
public class StringOperation extends StringExpression implements Operation<String> {

    private static final long serialVersionUID = 6846556373847139549L;

    private final OperationImpl<String> opMixin;

    protected StringOperation(Operator op, Expression<?>... args) {
        this(op, ImmutableList.copyOf(args));
    }

    protected StringOperation(Operator op, ImmutableList<Expression<?>> args) {
        super(ExpressionUtils.operation(String.class, op, args));
        this.opMixin = (OperationImpl<String>)mixin;
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

}
