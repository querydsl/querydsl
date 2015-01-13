/*
 * Copyright 2014, Timo Westkämper
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

import com.querydsl.spatial.SpatialOps;
import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.expr.NumberExpression;
import com.querydsl.core.types.expr.NumberOperation;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * A GeometryCollection is a geometric object that is a collection of some number of geometric objects.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class JTSGeometryCollectionExpression<T extends GeometryCollection> extends JTSGeometryExpression<T> {

    private static final long serialVersionUID = 8874174644259834690L;

    @Nullable
    private volatile NumberExpression<Integer> numGeometries;

    public JTSGeometryCollectionExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Returns the number of geometries in this GeometryCollection.
     *
     * @return
     */
    public NumberExpression<Integer> numGeometries() {
        if (numGeometries == null) {
            numGeometries = NumberOperation.create(Integer.class, SpatialOps.NUM_GEOMETRIES, mixin);
        }
        return numGeometries;
    }

    /**
     * Returns the Nth geometry in this GeometryCollection.
     *
     * @param n
     * @return
     */
    public JTSGeometryExpression<Geometry> geometryN(Integer n) {
        return JTSGeometryOperation.create(Geometry.class, SpatialOps.GEOMETRYN, mixin, ConstantImpl.create(n));
    }

}
