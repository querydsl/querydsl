/*
 * Copyright 2011, Mysema Ltd
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
package com.querydsl.core.types;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;
import java.io.Serializable;

import com.google.common.base.Objects;

/**
 * PathMetadata provides metadata for {@link Path} expressions.
 *
 * @author tiwe
 */
@Immutable
public final class PathMetadata<T> implements Serializable{

    private static final long serialVersionUID = -1055994185028970065L;

    private final Object element;

    private final int hashCode;

    @Nullable
    private final Path<?> parent, root;

    private final PathType pathType;

    public PathMetadata(@Nullable Path<?> parent, Object element, PathType type) {
        this.parent = parent;
        this.element = element;
        this.pathType = type;
        this.root = parent != null ? parent.getRoot() : null;
        this.hashCode = 31 * element.hashCode() + pathType.name().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof PathMetadata<?>) {
            PathMetadata<?> p = (PathMetadata<?>) obj;
            return element.equals(p.element) &&
                    pathType == p.pathType &&
                    Objects.equal(parent, p.parent);
        } else {
            return false;
        }

    }

    public Object getElement() {
        return element;
    }

    public String getName() {
        if (pathType == PathType.VARIABLE || pathType == PathType.PROPERTY) {
            return (String)element;
        } else {
            throw new IllegalStateException("name property not available for path of type " + pathType +
                    ". Use getElement() to access the generic path element.");
        }
    }

    @Nullable
    public Path<?> getParent() {
        return parent;
    }

    public PathType getPathType() {
        return pathType;
    }

    @Nullable
    public Path<?> getRoot() {
        return root;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    public boolean isRoot() {
        return parent == null || (pathType == PathType.DELEGATE && parent.getMetadata().isRoot());
    }

}
