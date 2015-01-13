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

import javax.annotation.Nullable;

import org.geolatte.geom.Point;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.NumberOperation;

/**
 * A Point is a 0-dimensional geometric object and represents a single location in coordinate space. A Point has an
 * x-coordinate value, a y-coordinate value. If called for by the associated Spatial Reference System, it may also
 * have coordinate values for z and m.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class PointExpression<T extends Point> extends GeometryExpression<T> {

    private static final long serialVersionUID = -3549448861390349654L;

    @Nullable
    private volatile NumberExpression<Double> x, y, z, m;

    public PointExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * The x-coordinate value for this Point.
     *
     * @return
     */
    public NumberExpression<Double> x() {
        if (x == null) {
            x = NumberOperation.create(Double.class, SpatialOps.X, mixin);
        }
        return x;
    }

    /**
     * The y-coordinate value for this Point.
     *
     * @return
     */
    public NumberExpression<Double> y() {
        if (y == null) {
            y = NumberOperation.create(Double.class, SpatialOps.Y, mixin);
        }
        return y;
    }

    /**
     * The z-coordinate value for this Point, if it has one. Returns NIL otherwise.
     *
     * @return
     */
    public NumberExpression<Double> z() {
        if (z == null) {
            z = NumberOperation.create(Double.class, SpatialOps.Z, mixin);
        }
        return z;
    }

    /**
     * The m-coordinate value for this Point, if it has one. Returns NIL otherwise.
     *
     * @return
     */
    public NumberExpression<Double> m() {
        if (m == null) {
            m = NumberOperation.create(Double.class, SpatialOps.M, mixin);
        }
        return m;
    }
}
