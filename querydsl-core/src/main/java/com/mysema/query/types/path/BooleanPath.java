/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMixin;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * BooleanPath represents boolean path expressions
 *
 * @author tiwe
 * @see java.lang.Boolean
 *
 */
public class BooleanPath extends BooleanExpression implements Path<Boolean> {

    private static final long serialVersionUID = 6590516706769430565L;

    private final Path<Boolean> pathMixin;

    public BooleanPath(Path<?> parent, String property) {
        this(PathMetadataFactory.forProperty(parent, property));
    }

    public BooleanPath(PathMetadata<?> metadata) {
        this.pathMixin = new PathMixin<Boolean>(this, metadata);
    }

    public BooleanPath(String var) {
        this(PathMetadataFactory.forVariable(var));
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }

    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    @Override
    public PathMetadata<?> getMetadata() {
        return pathMixin.getMetadata();
    }

    @Override
    public Path<?> getRoot() {
        return pathMixin.getRoot();
    }

    @Override
    public int hashCode() {
        return pathMixin.hashCode();
    }

    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }

}
