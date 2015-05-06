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

import java.lang.reflect.AnnotatedElement;

import org.geolatte.geom.PolyHedralSurface;

import com.querydsl.core.types.*;

/**
 * {@code PolyhedralSurfacePath} extends {@link PolyhedralSurfaceExpression} to implement the
 * {@link Path} interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public class PolyhedralSurfacePath<T extends PolyHedralSurface> extends PolyhedralSurfaceExpression<T> implements Path<T> {

    private static final long serialVersionUID = 312776751843333543L;

    private final PathImpl<T> pathMixin;

    public PolyhedralSurfacePath(Path<?> parent, String property) {
        this((Class<? extends T>) PolyHedralSurface.class, parent, property);
    }

    public PolyhedralSurfacePath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    public PolyhedralSurfacePath(PathMetadata metadata) {
        this((Class<? extends T>) PolyHedralSurface.class, metadata);
    }

    public PolyhedralSurfacePath(Class<? extends T> type, PathMetadata metadata) {
        super(ExpressionUtils.path(type, metadata));
        this.pathMixin = (PathImpl<T>)mixin;
    }

    public PolyhedralSurfacePath(String var) {
        this((Class<? extends T>) PolyHedralSurface.class, PathMetadataFactory.forVariable(var));
    }

    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(pathMixin, context);
    }

    public PolyhedralSurfacePath(Class<? extends T> type, String var) {
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
