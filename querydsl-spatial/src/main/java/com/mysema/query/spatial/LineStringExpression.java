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
package com.mysema.query.spatial;

import javax.annotation.Nullable;

import org.geolatte.geom.LineString;
import org.geolatte.geom.Point;

import com.mysema.query.types.ConstantImpl;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class LineStringExpression<T extends LineString> extends CurveExpression<T> {

    private static final long serialVersionUID = -6572984614863252657L;

    @Nullable
    private volatile NumberExpression<Integer> numPoints;

    public LineStringExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Integer> numPoints() {
        if (numPoints == null) {
            numPoints = NumberOperation.create(Integer.class, SpatialOps.NUM_POINTS, mixin);
        }
        return numPoints;
    }

    public PointExpression<Point> pointN(int idx) {
        return PointOperation.create(Point.class, SpatialOps.POINTN, mixin, ConstantImpl.create(idx));
    }

}
