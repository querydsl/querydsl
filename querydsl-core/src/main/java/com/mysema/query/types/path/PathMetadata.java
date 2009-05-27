/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mysema.query.types.ExprFactory;
import com.mysema.query.types.SimpleExprFactory;
import com.mysema.query.types.expr.Expr;

/**
 * PathMetadata provides metadata for Path expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class PathMetadata<T> {

    private static ExprFactory factory = SimpleExprFactory.getInstance();

    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.ARRAYVALUE);
    }

    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, int index) {
        return new PathMetadata<Integer>(parent, factory.createConstant(index), PathType.ARRAYVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forListAccess(PCollection<?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(PCollection<?> parent, int index) {
        return new PathMetadata<Integer>(parent, factory.createConstant(index), PathType.LISTVALUE_CONSTANT);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent, Expr<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent, KT key) {
        return new PathMetadata<KT>(parent, factory.createConstant(key), PathType.MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent, String property) {
        return new PathMetadata<String>(parent, factory.createConstant(property), PathType.PROPERTY);
    }

    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, factory.createConstant(variable), PathType.VARIABLE);
    }

    private final Expr<T> expression;

    private final Path<?> parent, root;

    private final PathType pathType;

    private final int hashCode;

    public PathMetadata(Path<?> parent, Expr<T> expression, PathType type) {
        this.parent = parent;
        this.expression = expression;
        this.pathType = type;
        this.root = parent != null ? parent.getRoot() : null;
        this.hashCode = new HashCodeBuilder().append(expression).append(parent).append(pathType).hashCode();
    }

    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj == this)
            return true;
        if (obj.getClass() != getClass())
            return false;
        PathMetadata<?> p = (PathMetadata<?>) obj;
        return new EqualsBuilder().append(expression, p.expression).append(
                parent, p.parent).append(pathType, p.pathType).isEquals();
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

    public int hashCode() {
        return hashCode;
    }

}
