/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * PathMetadata provides metadata for {@link Path} expressions.
 *
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class PathMetadata<T> implements Serializable{

    private static final long serialVersionUID = -1055994185028970065L;

    private final Expr<T> expression;

    private final int hashCode;

    @Nullable
    private final Path<?> parent, root;

    private final PathType pathType;

    public PathMetadata(@Nullable Path<?> parent, Expr<T> expression, PathType type) {
        this.parent = parent;
        this.expression = expression;
        this.pathType = type;
        this.root = parent != null ? parent.getRoot() : null;
        this.hashCode = new HashCodeBuilder().append(expression).append(parent).append(pathType).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }else if (obj instanceof PathMetadata<?>){
            PathMetadata<?> p = (PathMetadata<?>) obj;
            return new EqualsBuilder()
                .append(expression, p.expression)
                .append(parent, p.parent)
                .append(pathType, p.pathType).isEquals();
        }else{
            return false;
        }

    }

    public Expr<T> getExpression() {
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
