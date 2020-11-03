/*
 * Copyright 2020, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.spatial2;

import com.querydsl.core.types.*;
import org.geolatte.geom.Polygon;

import java.lang.reflect.AnnotatedElement;

/**
 * {@code PolygonPath} extends {@link PolygonExpression} to implement the
 * {@link Path} interface
 *
 * @author tiwe
 * @author Nikita Kochkurov
 *
 * @param <T>
 */
public class PolygonPath<T extends Polygon> extends PolygonExpression<T> implements Path<T> {

    private static final long serialVersionUID = 7234935599677788283L;

    private final PathImpl<T> pathMixin;

    @SuppressWarnings("unchecked")
    public PolygonPath(Path<?> parent, String property) {
        this((Class<? extends T>) Polygon.class, parent, property);
    }

    public PolygonPath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    @SuppressWarnings("unchecked")
    public PolygonPath(PathMetadata metadata) {
        this((Class<? extends T>) Polygon.class, metadata);
    }

    public PolygonPath(Class<? extends T> type, PathMetadata metadata) {
        super(ExpressionUtils.path(type, metadata));
        this.pathMixin = (PathImpl<T>) mixin;
    }

    @SuppressWarnings("unchecked")
    public PolygonPath(String var) {
        this((Class<? extends T>) Polygon.class, PathMetadataFactory.forVariable(var));
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(pathMixin, context);
    }

    public PolygonPath(Class<? extends T> type, String var) {
        this(type, PathMetadataFactory.forVariable(var));
    }

    @Override
    public PathMetadata getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        return pathMixin.getAnnotatedElement();
    }

}
