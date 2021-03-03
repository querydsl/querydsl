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

import java.util.Arrays;
import java.util.List;

import com.querydsl.core.types.*;

/**
 * {@code StringOperation} represents a String typed operation
 *
 * @author tiwe
 *
 */
public class StringOperation extends StringExpression implements Operation<String> {

    private static final long serialVersionUID = 6846556373847139549L;

    private final OperationImpl<String> opMixin;

    protected StringOperation(OperationImpl<String> mixin) {
        super(mixin);
        this.opMixin = mixin;
    }

    protected StringOperation(Operator op, Expression<?>... args) {
        this(op, Arrays.asList(args));
    }

    protected StringOperation(Operator op, List<Expression<?>> args) {
        super(ExpressionUtils.operation(String.class, op, args));
        this.opMixin = (OperationImpl<String>) mixin;
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
