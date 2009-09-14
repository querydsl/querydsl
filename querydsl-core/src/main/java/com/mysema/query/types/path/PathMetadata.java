/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.io.Serializable;

import javax.annotation.Nonnegative;
import javax.annotation.Nullable;

import net.jcip.annotations.Immutable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.expr.ENumber;
import com.mysema.query.types.expr.EString;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.expr.ExprConst;
import com.mysema.query.util.NotEmpty;

/**
 * PathMetadata provides metadata for Path expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
@Immutable
public final class PathMetadata<T> implements Serializable{

    private static final long serialVersionUID = -1055994185028970065L;

    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.ARRAYVALUE);
    }

    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, ENumber.create(index), PathType.ARRAYVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forListAccess(PList<?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(PList<?> parent, @Nonnegative int index) {
        return new PathMetadata<Integer>(parent, ENumber.create(index), PathType.LISTVALUE_CONSTANT);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent, Expr<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent, KT key) {
        return new PathMetadata<KT>(parent,ExprConst.create(key), PathType.MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent, @NotEmpty String property) {
        return new PathMetadata<String>(parent, EString.create(Assert.hasLength(property)), PathType.PROPERTY);
    }

    public static PathMetadata<String> forVariable(@NotEmpty String variable) {
        return new PathMetadata<String>(null, EString.create(Assert.hasLength(variable)), PathType.VARIABLE);
    }

    private final Expr<T> expression;

    @Nullable
    private final Path<?> parent, root;

    private final PathType pathType;

    private final int hashCode;

    private PathMetadata(@Nullable Path<?> parent, Expr<T> expression, PathType type) {
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

}
