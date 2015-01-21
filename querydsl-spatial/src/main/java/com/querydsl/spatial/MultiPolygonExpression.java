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
package com.querydsl.spatial;

import org.geolatte.geom.MultiPolygon;

import com.querydsl.core.types.Expression;

/**
 * A MultiPolygon is a MultiSurface whose elements are Polygons.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class MultiPolygonExpression<T extends MultiPolygon> extends MultiSurfaceExpression<T> {

    private static final long serialVersionUID = -2285946852207189655L;

    public MultiPolygonExpression(Expression<T> mixin) {
        super(mixin);
    }

}
