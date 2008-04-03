/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.grammar.types;

import com.mysema.query.grammar.Ops;

/**
 * 
 * PathMetadata provides metadata for Path expressions
 * 
 * @author tiwe
 * @version $Id$
 * 
 */
public final class PathMetadata<T> {
    private final Expr<T> expression;

    private final Path<?> parent;

    private final PathType pathType;

    public PathMetadata(Path<?> parent, Expr<T> expression, PathType type) {
        this.parent = parent;
        this.expression = expression;
        this.pathType = type;
    }

    public static PathMetadata<Integer> forListAccess(Path.Collection<?> parent,
            Expr<Integer> index) {
        return new PathMetadata<Integer>(parent, index, PathType.LISTVALUE);
    }

    public static PathMetadata<Integer> forListAccess(Path.Collection<?> parent,
            int index) {
        return new PathMetadata<Integer>(parent, Factory.createConstant(index),
                PathType.LISTVALUE_CONSTANT);
    }

    public static PathMetadata<Integer> forSize(Path.Collection<?> parent) {
        return new PathMetadata<Integer>(parent, null, PathType.SIZE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(Path.Map<?, ?> parent,
            Expr<KT> key) {
        return new PathMetadata<KT>(parent, key, PathType.MAPVALUE);
    }

    public static <KT> PathMetadata<KT> forMapAccess(Path.Map<?, ?> parent,
            KT key) {
        return new PathMetadata<KT>(parent, Factory.createConstant(key), PathType.MAPVALUE_CONSTANT);
    }

    public static PathMetadata<String> forProperty(Path<?> parent,
            String property) {
        return new PathMetadata<String>(parent, Factory.createConstant(property), PathType.PROPERTY);
    }

    public static PathMetadata<String> forVariable(String variable) {
        return new PathMetadata<String>(null, Factory.createConstant(variable), PathType.VARIABLE);
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
    
    public static final class PathTypeImpl implements PathType{ }

    /**
     * Type provides
     *
     */
    public interface PathType extends Ops.Op<Path<?>> {
        PathType LISTVALUE = new PathTypeImpl(); 
        PathType LISTVALUE_CONSTANT = new PathTypeImpl();
        PathType MAPVALUE = new PathTypeImpl();
        PathType MAPVALUE_CONSTANT = new PathTypeImpl();
        PathType PROPERTY = new PathTypeImpl();
        PathType VARIABLE = new PathTypeImpl();
        PathType SIZE = new PathTypeImpl();
    }

}
