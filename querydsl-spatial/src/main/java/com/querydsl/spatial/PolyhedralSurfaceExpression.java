/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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

import org.jetbrains.annotations.Nullable;

import org.geolatte.geom.PolyHedralSurface;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;

/**
 * A PolyhedralSurface is a contiguous collection of polygons, which share common boundary segments.
 * For each pair of polygons that “touch”, the common boundary shall be expressible as a finite
 * collection of LineStrings. Each such LineString shall be part of the boundary of at most 2 Polygon
 * patches.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class PolyhedralSurfaceExpression<T extends PolyHedralSurface> extends SurfaceExpression<T> {

    private static final long serialVersionUID = -6732418858467327780L;

    @Nullable
    private transient volatile NumberExpression<Integer> numPatches;

    @Nullable
    private transient volatile BooleanExpression closed;

    public PolyhedralSurfaceExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Returns the number of including polygons
     *
     * @return number of polygons
     */
    public NumberExpression<Integer> numPatches() {
        if (numPatches == null) {
            numPatches = Expressions.numberOperation(Integer.class, SpatialOps.NUM_SURFACES, mixin);
        }
        return numPatches;
    }

    /**
     * Returns a polygon in this surface, the order is arbitrary.
     *
     * @param n one based index
     * @return polygon at index
     */
    public PolygonExpression<?> patchN(int n) {
        return GeometryExpressions.polygonOperation(SpatialOps.SURFACE, mixin, ConstantImpl.create(n));
    }

    /**
     * Returns 1 (True) if the polygon closes on itself, and thus has no boundary and
     * encloses a solid
     *
     * @return closed
     */
    public BooleanExpression isClosed() {
        if (closed == null) {
            closed = Expressions.booleanOperation(SpatialOps.IS_CLOSED, mixin);
        }
        return closed;
    }
}
