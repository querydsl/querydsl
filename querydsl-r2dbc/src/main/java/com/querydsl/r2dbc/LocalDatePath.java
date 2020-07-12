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
package com.querydsl.r2dbc;

import com.querydsl.core.types.*;

import java.lang.reflect.AnnotatedElement;

/**
 * {@code LocalDatePath} presents LocalDate paths
 *
 * @param <T> expression type
 * @author mc_fish
 */
@SuppressWarnings({"unchecked"})
public class LocalDatePath<T extends Comparable> extends LocalDateExpression<T> implements Path<T> {

    private static final long serialVersionUID = -6030609920388740246L;

    private final PathImpl<T> pathMixin;

    protected LocalDatePath(PathImpl<T> mixin) {
        super(mixin);
        this.pathMixin = mixin;
    }

    protected LocalDatePath(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    protected LocalDatePath(Class<? extends T> type, PathMetadata metadata) {
        super(ExpressionUtils.path(type, metadata));
        this.pathMixin = (PathImpl<T>) mixin;
    }

    protected LocalDatePath(Class<? extends T> type, String var) {
        this(type, PathMetadataFactory.forVariable(var));
    }

    @Override
    public final <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(pathMixin, context);
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
