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
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;

public abstract class CurveExpression<T> extends GeometryExpression<T> {

    private static final long serialVersionUID = 6139188586728676033L;

    @Nullable
    private volatile NumberExpression<Double> length;

    @Nullable
    private volatile PointExpression<?> startPoint, endPoint;

    @Nullable
    private volatile BooleanExpression closed, ring;

    public CurveExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Double> length() {
        if (length == null) {
            length = NumberOperation.create(null, Ops.SpatialOps.LENGTH, mixin);
        }
        return length;
    }

    public PointExpression<?> startPoint() {
        if (startPoint == null) {
            startPoint = PointOperation.create(null, Ops.SpatialOps.START_POINT, mixin);
        }
        return startPoint;
    }

    public PointExpression<?> endPoint() {
        if (endPoint == null) {
            endPoint = PointOperation.create(null, Ops.SpatialOps.END_POINT, mixin);
        }
        return endPoint;
    }

    public BooleanExpression isClosed() {
        if (closed == null) {
            closed = BooleanOperation.create(Ops.SpatialOps.IS_CLOSED, mixin);
        }
        return closed;
    }

    public BooleanExpression isRing() {
        if (ring == null) {
            ring = BooleanOperation.create(Ops.SpatialOps.IS_RING, mixin);
        }
        return ring;
    }

}
