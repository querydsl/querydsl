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
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.BooleanExpression;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.util.MathUtils;

/**
 * NumberConstant represents numeric constants
 *
 * @author tiwe
 *
 * @param <D>
 */
final class NumberConstant<D extends Number & Comparable<?>> extends NumberExpression<D> implements Constant<D>{

    private static final long serialVersionUID = 2958824808974260439L;

    /**
     * Factory method
     *
     * @param <T>
     * @param val
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number & Comparable<?>> NumberExpression<T> create(T val) {
        return new NumberConstant<T>((Class<T>)val.getClass(), val);
    }

    private final D constant;

    public NumberConstant(Class<? extends D> type, D constant) {
        super(ConstantImpl.create(constant));
        this.constant = constant;
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public BooleanExpression eq(D b) {
        return BooleanConstant.create(constant.equals(b));
    }

    @Override
    public D getConstant() {
        return constant;
    }

    @Override
    public BooleanExpression ne(D b) {
        return BooleanConstant.create(!constant.equals(b));
    }

    @Override
    public NumberExpression<D> add(Number right) {
        return NumberConstant.create(MathUtils.sum(constant, right));
    }

    @Override
    public <N extends Number & Comparable<?>> NumberExpression<D> add(Expression<N> right) {
        if (right instanceof Constant<?>) {
            return add(((Constant<N>)right).getConstant());
        } else {
            return super.add(right);
        }
    }

    @Override
    public NumberExpression<D> subtract(Number right) {
        return NumberConstant.create(MathUtils.difference(constant, right));
    }

    @Override
    public <N extends Number & Comparable<?>> NumberExpression<D> subtract(Expression<N> right) {
        if (right instanceof Constant<?>) {
            return subtract(((Constant<N>)right).getConstant());
        } else {
            return super.subtract(right);
        }
    }

    @Override
    public NumberExpression<Byte> byteValue() {
        return NumberConstant.create(constant.byteValue());
    }

    @Override
    public NumberExpression<Double> doubleValue() {
        return NumberConstant.create(constant.doubleValue());
    }

    @Override
    public NumberExpression<Float> floatValue() {
        return NumberConstant.create(constant.floatValue());
    }

    @Override
    public NumberExpression<Long> longValue() {
        return NumberConstant.create(constant.longValue());
    }

    @Override
    public NumberExpression<Short> shortValue() {
        return NumberConstant.create(constant.shortValue());
    }

}
