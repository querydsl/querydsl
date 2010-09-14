/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;

import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.PathMixin;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.StringExpression;

/**
 * StringPath represents String typed paths
 *
 * @author tiwe
 *
 */
public class StringPath extends StringExpression implements Path<String> {

    private static final long serialVersionUID = 7983490925756833429L;

    private final Path<String> pathMixin;

    public StringPath(Path<?> parent, String property) {
        this(PathMetadataFactory.forProperty(parent, property));
    }

    public StringPath(PathMetadata<?> metadata) {
        this.pathMixin = new PathMixin<String>(String.class, metadata);
    }

    public StringPath(String var) {
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
