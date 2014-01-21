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
package com.mysema.query.types.spatial;

import javax.annotation.Nullable;

import com.mysema.query.types.Expression;
import com.mysema.query.types.Ops;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;

public abstract class SurfaceExpression<T> extends GeometryExpression<T> {

    private static final long serialVersionUID = 3534197011234723698L;

    @Nullable
    private volatile PointExpression<?> centroid, pointOnSurface;

    @Nullable
    private volatile NumberExpression<Double> area;

    public SurfaceExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Double> area() {
        if (area == null) {
            area = NumberOperation.create(Double.class, Ops.SpatialOps.AREA, mixin);
        }
        return area;
    }

    public PointExpression<?> centroid() {
        if (centroid == null) {
            centroid = PointOperation.create(null, Ops.SpatialOps.CENTROID, mixin);
        }
        return centroid;
    }

    public PointExpression<?> pointOnSurface() {
        if (pointOnSurface == null) {
            pointOnSurface = PointOperation.create(null, Ops.SpatialOps.POINT_ON_SURFACE, mixin);
        }
        return pointOnSurface;
    }

    @Override
    public MultiCurveExpression<T> boundary() {
        // TODO
        return null;
    }
}
