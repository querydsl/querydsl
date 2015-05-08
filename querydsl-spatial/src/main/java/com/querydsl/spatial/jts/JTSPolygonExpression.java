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
package com.querydsl.spatial.jts;

import javax.annotation.Nullable;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.spatial.SpatialOps;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Polygon;

/**
 * A Polygon is a planar Surface defined by 1 exterior boundary and 0 or more interior boundaries. Each interior
 * boundary defines a hole in the Polygon. A Triangle is a polygon with 3 distinct, non-collinear vertices and no
 * interior boundary.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class JTSPolygonExpression<T extends Polygon> extends JTSSurfaceExpression<T> {

    private static final long serialVersionUID = 7544382956232485312L;

    @Nullable
    private transient volatile NumberExpression<Integer> numInteriorRing;

    @Nullable
    private transient volatile JTSLineStringExpression<LineString> exteriorRing;

    public JTSPolygonExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Returns the exterior ring of this Polygon.
     *
     * @return exterior ring
     */
    public JTSLineStringExpression<?> exteriorRing() {
        if (exteriorRing == null) {
            exteriorRing = JTSGeometryExpressions.lineStringOperation(SpatialOps.EXTERIOR_RING, mixin);
        }
        return exteriorRing;
    }

    /**
     * Returns the number of interior rings in this Polygon.
     *
     * @return number of interior rings
     */
    public NumberExpression<Integer> numInteriorRing() {
        if (numInteriorRing == null) {
            numInteriorRing = Expressions.numberOperation(Integer.class, SpatialOps.NUM_INTERIOR_RING, mixin);
        }
        return numInteriorRing;
    }

    /**
     * Returns the N th interior ring for this Polygon as a LineString.
     *
     * @param idx one based index
     * @return interior ring at index
     */
    public JTSLineStringExpression<LineString> interiorRingN(int idx) {
        return JTSGeometryExpressions.lineStringOperation(SpatialOps.INTERIOR_RINGN, mixin, ConstantImpl.create(idx));
    }
}
