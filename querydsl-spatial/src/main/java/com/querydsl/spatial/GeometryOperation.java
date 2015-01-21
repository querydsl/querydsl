/*
 * Copyright 2014, Mysema Ltd
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
package com.querydsl.spatial;

import java.util.List;

import org.geolatte.geom.Geometry;

import com.google.common.collect.ImmutableList;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.OperationImpl;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Visitor;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class GeometryOperation<T extends Geometry> extends GeometryExpression<T> implements Operation<T> {

    private static final long serialVersionUID = 3433471874808633698L;

    public static <D extends Geometry> GeometryOperation<D> create(Class<D> type, Operator<? super D> op, Expression<?> one) {
        return new GeometryOperation<D>(type, op, ImmutableList.<Expression<?>>of(one));
    }

    public static <D extends Geometry> GeometryOperation<D> create(Class<D> type, Operator<? super D> op, Expression<?> one, Expression<?> two) {
        return new GeometryOperation<D>(type, op, ImmutableList.of(one, two));
    }

    public static <D extends Geometry> GeometryOperation<D> create(Class<D> type, Operator<? super D> op, Expression<?>... args) {
        return new GeometryOperation<D>(type, op, args);
    }

    private final OperationImpl< T> opMixin;

    protected GeometryOperation(Class<T> type, Operator<? super T> op, Expression<?>... args) {
        this(type, op, ImmutableList.copyOf(args));
    }

    protected GeometryOperation(Class<T> type, Operator<? super T> op, ImmutableList<Expression<?>> args) {
        super(new OperationImpl<T>(type, op, args));
        this.opMixin = (OperationImpl<T>)mixin;
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
    public Operator<? super T> getOperator() {
        return opMixin.getOperator();
    }

}
