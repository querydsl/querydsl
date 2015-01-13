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
package com.querydsl.core;

import com.querydsl.core.types.Constant;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.SimpleExpression;

/**
 * SimpleConstant represents general constant expressions
 *
 * @author tiwe
 *
 * @param <D> Java type of constant
 */
public final class SimpleConstant<D> extends SimpleExpression<D> implements Constant<D> {

    private static final long serialVersionUID = -3211963259241932307L;

    /**
     * Factory method for constants
     *
     * @param <D>
     * @param val
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> SimpleExpression<T> create(T val) {
        if (val instanceof Boolean) {
            return (SimpleExpression<T>)BooleanConstant.create((Boolean)val);
        } else {
            return new SimpleConstant<T>(val);
        }
    }

    private final D constant;

    @SuppressWarnings("unchecked")
    SimpleConstant(D constant) {
        super(ConstantImpl.create(constant));
        this.constant = constant;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public BooleanExpression eq(D s) {
        return BooleanConstant.create(constant.equals(s));
    }

    /**
     * Get the embedded constant
     *
     * @return
     */
    @Override
    public D getConstant() {
        return constant;
    }

    @Override
    public BooleanExpression ne(D s) {
        return BooleanConstant.create(!constant.equals(s));
    }

}
