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

import com.vividsolutions.jts.geom.LineString;

import com.querydsl.core.types.Expression;

/**
 * A LinearRing is a LineString that is both closed and simple.
 *
 * @author tiwe
 *
 * @param <T>
 */
public abstract class JTSLinearRingExpression<T extends LineString> extends JTSLineStringExpression<T> {

    private static final long serialVersionUID = -759466658721392938L;

    public JTSLinearRingExpression(Expression<T> mixin) {
        super(mixin);
    }

}
