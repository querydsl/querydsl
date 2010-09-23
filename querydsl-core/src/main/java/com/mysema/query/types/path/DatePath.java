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
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.DateExpression;

/**
 * DatePath presents Date paths
 * 
 * @author tiwe
 *
 * @param <D>
 */
@SuppressWarnings({"unchecked"})
public class DatePath<D extends Comparable> extends DateExpression<D> implements Path<D>{

    private static final long serialVersionUID = -6030609920388740246L;

    private final Path<D> pathMixin;

    public DatePath(Class<? extends D> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    public DatePath(Class<? extends D> type, PathMetadata<?> metadata) {
        super(type);
        this.pathMixin = new PathImpl<D>(type, metadata);
    }

    public DatePath(Class<? extends D> type, String var) {
        this(type, PathMetadataFactory.forVariable(var));
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
