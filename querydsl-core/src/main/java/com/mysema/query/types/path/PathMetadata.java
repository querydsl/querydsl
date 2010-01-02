/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.io.Serializable;

import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mysema.query.types.expr.Expr;

/**
 * PathMetadata provides metadata for Path expressions.
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

    PathMetadata(@Nullable Path<?> parent, Expr<T> expression, PathType type) {
        this.parent = parent;
        this.expression = expression;
        this.pathType = type;
        this.root = parent != null ? parent.getRoot() : null;
        this.hashCode = new HashCodeBuilder().append(expression).append(parent).append(pathType).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        PathMetadata<?> p = (PathMetadata<?>) obj;
        return new EqualsBuilder()
            .append(expression, p.expression)
            .append(parent, p.parent)
            .append(pathType, p.pathType).isEquals();
    }

    public Expr<T> getExpression() {
        return expression;
    }

    public Path<?> getParent() {
        return parent;
    }
    
    public PathType getPathType() {
        return pathType;
    }

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
