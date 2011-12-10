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
package com.mysema.query.types;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * PathMetadata provides metadata for {@link Path} expressions.
 *
 * @author tiwe
 */
@Immutable
public final class PathMetadata<T> implements Serializable{

    private static final long serialVersionUID = -1055994185028970065L;

    private final Expression<T> expression;

    private final int hashCode;

    @Nullable
    private final Path<?> parent, root;

    private final PathType pathType;

    public PathMetadata(@Nullable Path<?> parent, Expression<T> expression, PathType type) {
        this.parent = parent;
        this.expression = expression;
        this.pathType = type;
        this.root = parent != null ? parent.getRoot() : null;
        this.hashCode = new HashCodeBuilder().append(expression).append(parent).append(pathType).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof PathMetadata<?>) { 
            PathMetadata<?> p = (PathMetadata<?>) obj;
            return new EqualsBuilder()
                .append(expression, p.expression)
                .append(parent, p.parent)
                .append(pathType, p.pathType).isEquals();
        } else {
            return false;
        }

    }

    public Expression<T> getExpression() {
        return expression;
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

    public boolean isRoot(){
        return parent == null;
    }

}
