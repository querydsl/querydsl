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
package com.querydsl.apt;

import javax.lang.model.type.IntersectionType;
import javax.lang.model.util.SimpleTypeVisitor8;

/**
 * Converts Java 8 {@link javax.lang.model.type.IntersectionType IntersectionType} instances into their first bound when visiting
 *
 * @param <R>
 * @param <P>
 */
class SimpleTypeVisitorAdapter<R, P> extends SimpleTypeVisitor8<R, P> {

    @Override
    public R visitIntersection(IntersectionType t, P p) {
        return t.getBounds().get(0).accept(this, p);
    }
}
