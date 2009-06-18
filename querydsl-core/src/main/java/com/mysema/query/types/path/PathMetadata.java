/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.mysema.query.types.expr.EConstant;
import com.mysema.query.types.expr.Expr;

/**
 * PathMetadata provides metadata for Path expressions.
 * 
 * @author tiwe
 * @version $Id$
 */
public final class PathMetadata<T> {

    /**
     * Create a new PathMetadata instance for indexed array access
     * 
     * @param parent parent path
     * @param index
     * @return
     */
    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.ARRAYVALUE);
    }

    /**
     * Create a new PathMetadata instance for indexed array access
     * 
     * @param parent parent path
     * @param index
     * @return
     */
    public static PathMetadata<Integer> forArrayAccess(PArray<?> parent, int index) {
        return new PathMetadata<Integer>(parent, EConstant.create(index), PathType.ARRAYVALUE_CONSTANT);
    }

    /**
     * Create a new PathMetadata instance for indexed List access
     * 
     * @param parent parent path
     * @param index
     * @return
     */
    public static PathMetadata<Integer> forListAccess(PList<?> parent, Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    /**
     * Create a new PathMetadata instance for indexed list access
     * 
     * @param parent parent path
     * @param index
     * @return
     */
    public static PathMetadata<Integer> forListAccess(PList<?> parent, int index) {
        return new PathMetadata<Integer>(parent, EConstant.create(index), PathType.LISTVALUE_CONSTANT);
    }

    /**
     * Create a new PathMetadata instance for Map value access
     * 
     * @param <KT> key type
     * @param parent parent path
     * @param key
     * @return
     */
    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent, Expr<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    /**
     * Create a new PathMetadata instance for Map value access
     * 
     * @param <KT> key type
     * @param parent parent path
     * @param key
     * @return
     */
    public static <KT> PathMetadata<KT> forMapAccess(PMap<?, ?> parent, KT key) {
        return new PathMetadata<KT>(parent,EConstant.create(key), PathType.MAPVALUE_CONSTANT);
    }

    /**
     * Create a new PathMetadata instance for a property based path
     * 
     * @param parent parent path
     * @param property
     * @return
     */
    public static PathMetadata<String> forProperty(Path<?> parent, String property) {
        return new PathMetadata<String>(parent, EConstant.create(property), PathType.PROPERTY);
    }

    /**
     * Create a new PathMetadata instance for a variable based path
     * 
     * @param variable
     * @return
     */
    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, EConstant.create(variable), PathType.VARIABLE);
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

    @Override
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
     * Get the expression related to this path
     * 
     * @return
     */
    public Expr<T> getExpression() {
        return expression;
    }

    /**
     * Get the parent path
     * 
     * @return
     */
    public Path<?> getParent() {
        return parent;
    }

    /**
     * Get the path type
     * 
     * @return
     */
    public PathType getPathType() {
        return pathType;
    }

    /**
     * Get the root path
     * 
     * @return
     */
    public Path<?> getRoot() {
        return root;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

}
