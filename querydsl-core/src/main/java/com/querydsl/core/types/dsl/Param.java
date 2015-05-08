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

import com.querydsl.core.types.ParamExpression;
import com.querydsl.core.types.ParamExpressionImpl;
import com.querydsl.core.types.Visitor;

/**
 * {@code Param} defines a parameter in a query with an optional name
 *
 * @author tiwe
 *
 * @param <T> expression type
 */
public class Param<T> extends SimpleExpression<T> implements ParamExpression<T> {

    private static final long serialVersionUID = -6872502615009012503L;

    private final ParamExpression<T> paramMixin;
    
    public Param(Class<? extends T> type, String name) {
        super(new ParamExpressionImpl<T>(type, name));
        this.paramMixin = (ParamExpression<T>)mixin;
    }

    public Param(Class<? extends T> type) {
        super(new ParamExpressionImpl<T>(type));
        this.paramMixin = (ParamExpression<T>)mixin;
    }
    
    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public String getName() {
        return paramMixin.getName();
    }

    @Override
    public boolean isAnon() {
        return paramMixin.isAnon();
    }

    @Override
    public String getNotSetMessage() {
        return paramMixin.getNotSetMessage();
    }
}
