/*
 * Copyright 2020, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.spatial2;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import org.geolatte.geom.AbstractGeometryCollection;

import javax.annotation.Nullable;

/**
 * A MultiCurve is a 1-dimensional GeometryCollection whose elements are Curves.
 *
 * @author tiwe
 * @author Nikita Kochkurov
 *
 * @param <T>
 */
public abstract class MultiCurveExpression<T extends AbstractGeometryCollection> extends GeometryCollectionExpression<T> {

    private static final long serialVersionUID = -7341509317498328267L;

    @Nullable
    private transient volatile BooleanExpression closed;

    @Nullable
    private transient volatile NumberExpression<Double> length;

    public MultiCurveExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Returns 1 (TRUE) if this MultiCurve is closed [StartPoint ( ) = EndPoint ( ) for each
     * Curve in this MultiCurve].
     *
     * @return closed
     */
    public BooleanExpression isClosed() {
        if (closed == null) {
            closed = Expressions.booleanOperation(SpatialOps.IS_CLOSED, mixin);
        }
        return closed;
    }

    /**
     * The Length of this MultiCurve which is equal to the sum of the lengths of the element
     * Curves.
     *
     * @return length
     */
    public NumberExpression<Double> length() {
        if (length == null) {
            length = Expressions.numberOperation(Double.class, SpatialOps.LENGTH, mixin);
        }
        return length;
    }

}
