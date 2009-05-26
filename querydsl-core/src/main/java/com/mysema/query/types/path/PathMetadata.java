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
import com.mysema.query.types.operation.Ops;

/**
 * PathMetadata provides metadata for Path expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class PathMetadata<T> {

    private static ExprFactory factory = SimpleExprFactory.getInstance();

    // TODO : refactor to operation
    public static final PathType ARRAY_SIZE = new PathType("array size");

    public static final PathType ARRAYVALUE = new PathType("array value");

    public static final PathType ARRAYVALUE_CONSTANT = new PathType(
            "array value constant");

    public static final PathType LISTVALUE = new PathType("list value");

    public static final PathType LISTVALUE_CONSTANT = new PathType(
            "list value constant");

    public static final PathType MAPVALUE = new PathType("map value");

    public static final PathType MAPVALUE_CONSTANT = new PathType(
            "map value constant");

    public static final PathType PROPERTY = new PathType("propery");

    // TODO : refactor to operation
    public static final PathType SIZE = new PathType("size");

    public static final PathType VARIABLE = new PathType("variable");

    private final Expr<T> expression;

    private final Path<?> parent, root;

    private final PathType pathType;

    private final int hashCode;

    public PathMetadata(Path<?> parent, Expr<T> expression, PathType type) {
        this.parent = parent;
        this.expression = expression;
        this.pathType = type;
        this.root = parent != null ? parent.getRoot() : null;
        this.hashCode = new HashCodeBuilder().append(expression).append(parent)
                .append(pathType).hashCode();
    }

    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent,
            Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, ARRAYVALUE);
    }

    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent,
            int index) {
        return new PathMetadata<Integer>(parent, factory.createConstant(index),
                ARRAYVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forListAccess(PCollection<?> parent,
            Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(PCollection<?> parent,
            int index) {
        return new PathMetadata<Integer>(parent, factory.createConstant(index),
                LISTVALUE_CONSTANT);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent,
            Expr<KT> key) {
        return new PathMetadata<KT>(parent, key, MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent, KT key) {
        return new PathMetadata<KT>(parent, factory.createConstant(key),
                MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent,
            String property) {
        return new PathMetadata<String>(parent, factory
                .createConstant(property), PROPERTY);
    }

    public static PathMetadata<Integer> forSize(PArray<?> parent) {
        return new PathMetadata<Integer>(parent, null, ARRAY_SIZE);
    }

    public static PathMetadata<Integer> forSize(PCollection<?> parent) {
        return new PathMetadata<Integer>(parent, null, SIZE);
    }

    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, factory.createConstant(variable),
                VARIABLE);
    }

    public Expr<T> getExpression() {
        return expression;
    }

    public Path<?> getParent() {
        return parent;
    }

    public Path<?> getRoot() {
        return root;
    }

    public PathType getPathType() {
        return pathType;
    }

    public int hashCode() {
        return hashCode;
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

    /**
     * The Class PathType.
     */
    public static class PathType extends Ops.Op<Path<?>> {
        private final String symbol;

        public PathType(String symbol) {
            super();
            this.symbol = symbol;
        }

        public String toString() {
            return symbol;
        }
    }

}
