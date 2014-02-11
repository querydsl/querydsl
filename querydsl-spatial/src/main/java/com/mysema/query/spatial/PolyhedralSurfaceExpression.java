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

import org.geolatte.geom.PolyHedralSurface;

import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.BooleanExpression;
import com.mysema.query.types.expr.BooleanOperation;
import com.mysema.query.types.expr.NumberExpression;
import com.mysema.query.types.expr.NumberOperation;

/**
 * @author tiwe
 *
 * @param <T>
 */
public abstract class PolyhedralSurfaceExpression<T extends PolyHedralSurface> extends SurfaceExpression<T> {

    private static final long serialVersionUID = -6732418858467327780L;

    @Nullable
    private volatile NumberExpression<Integer> numPatches;

    @Nullable
    private volatile BooleanExpression closed;

    public PolyhedralSurfaceExpression(Expression<T> mixin) {
        super(mixin);
    }

    public NumberExpression<Integer> numPatches() {
        if (numPatches == null) {
            numPatches = NumberOperation.create(Integer.class, SpatialOps.NUM_SURFACES, mixin);
        }
        return numPatches;
    }

    public PolygonExpression<?> patchN(int n) {
        // TODO
        return null;
    }

    public MultiPolygonExpression<?> boundingPolygons(Object polygon) {
        // TODO
        return null;
    }

    public BooleanExpression isClosed() {
        if (closed == null) {
            closed = BooleanOperation.create(SpatialOps.IS_CLOSED, mixin);
        }
        return closed;
    }
}
