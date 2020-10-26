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

import org.jetbrains.annotations.Nullable;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.spatial.SpatialOps;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;

/**
 * A Geometry collection is a geometric object that is a collection of some number of geometric objects.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class JTSGeometryCollectionExpression<T extends GeometryCollection> extends JTSGeometryExpression<T> {

    private static final long serialVersionUID = 8874174644259834690L;

    @Nullable
    private transient volatile NumberExpression<Integer> numGeometries;

    public JTSGeometryCollectionExpression(Expression<T> mixin) {
        super(mixin);
    }

    /**
     * Returns the number of geometries in this GeometryCollection.
     *
     * @return numbers of geometries
     */
    public NumberExpression<Integer> numGeometries() {
        if (numGeometries == null) {
            numGeometries = Expressions.numberOperation(Integer.class, SpatialOps.NUM_GEOMETRIES, mixin);
        }
        return numGeometries;
    }

    /**
     * Returns the Nth geometry in this GeometryCollection.
     *
     * @param n one based index
     * @return matching geometry
     */
    public JTSGeometryExpression<Geometry> geometryN(Integer n) {
        return JTSGeometryExpressions.geometryOperation(SpatialOps.GEOMETRYN, mixin, ConstantImpl.create(n));
    }

}
