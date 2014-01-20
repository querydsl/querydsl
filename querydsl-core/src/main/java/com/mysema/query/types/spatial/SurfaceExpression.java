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

public abstract class SurfaceExpression<T> extends GeometryExpression<T> {

    private static final long serialVersionUID = 3534197011234723698L;

    @Nullable
    private volatile PointExpression<?> centroid, pointOnSurface;

    public SurfaceExpression(Expression<T> mixin) {
        super(mixin);
    }

    public Object area() { // TODO Area type
        return null;
    }

    public PointExpression<?> centroid() {
        if (centroid == null) {
            // TODO
        }
        return centroid;
    }

    public PointExpression<?> pointOnSurface() {
        if (pointOnSurface == null) {
            // TODO
        }
        return pointOnSurface;
    }

//    @Override
//    public MultiCurveExpression<?> boundary() {
//        return null;
//    }
}
