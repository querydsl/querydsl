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

import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;

public abstract class PointExpression<T> extends GeometryExpression<T> {

    private static final long serialVersionUID = -3549448861390349654L;

    @Nullable
    private volatile NumberExpression<Double> x, y, z, m;

    public PointExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Double> x() {
        if (x == null) {
            x = NumberOperation.create(Double.class, SpatialOps.X, mixin);
        }
        return x;
    }

    public NumberExpression<Double> y() {
        if (y == null) {
            y = NumberOperation.create(Double.class, SpatialOps.Y, mixin);
        }
        return null;
    }

    public NumberExpression<Double> z() {
        if (z == null) {
            z = NumberOperation.create(Double.class, SpatialOps.Z, mixin);
        }
        return null;
    }

    public NumberExpression<Double> m() {
        if (m == null) {
            m = NumberOperation.create(Double.class, SpatialOps.M, mixin);
        }
        return null;
    }
}
