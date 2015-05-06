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
package com.mysema.query.types.expr;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.OperationImpl;
import com.mysema.query.types.Operator;
import com.mysema.query.types.Visitor;

/**
 * StringOperation represents a String typed operation
 *
 * @author tiwe
 *
 */
public class StringOperation extends StringExpression implements Operation<String> {

    private static final long serialVersionUID = 6846556373847139549L;

    public static StringExpression create(Operator<? super String> op, Expression<?> one) {
        return new StringOperation(op, ImmutableList.<Expression<?>>of(one));
    }
    
    public static StringExpression create(Operator<? super String> op, Expression<?> one, Expression<?> two) {
        return new StringOperation(op, ImmutableList.of(one, two));
    }
    
    public static StringExpression create(Operator<? super String> op, Expression<?>... args) {
        return new StringOperation(op, args);
    }

    private final OperationImpl<String> opMixin;

    protected StringOperation(Operator<? super String> op, Expression<?>... args) {
        this(op, ImmutableList.copyOf(args));
    }

    protected StringOperation(Operator<? super String> op, ImmutableList<Expression<?>> args) {
        super(new OperationImpl<String>(String.class, op, args));
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
    public Operator<? super String> getOperator() {
        return opMixin.getOperator();
    }

}
