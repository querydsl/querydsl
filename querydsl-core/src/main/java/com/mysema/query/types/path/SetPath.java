/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;
import java.util.Set;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMixin;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.CollectionExpressionBase;

/**
 * SetPath represents set paths
 *
 * @author tiwe
 *
 * @param <E> component type
 */
public class SetPath<E> extends CollectionExpressionBase<Set<E>,E> implements Path<Set<E>> {

    private static final long serialVersionUID = 4145848445507037373L;

    private final Class<E> elementType;

    private final String entityName;

    private final Path<Set<E>> pathMixin;

    @SuppressWarnings("unchecked")
    public SetPath(Class<? super E> type, String entityName, PathMetadata<?> metadata) {
        super((Class)Set.class);
        this.elementType = (Class<E>) Assert.notNull(type,"type");
        this.entityName = Assert.notNull(entityName,"entityName");
        this.pathMixin = new PathMixin<Set<E>>(this, metadata);
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
    public Class<E> getElementType() {
        return elementType;
    }

    /**
     * Get the entity name for this Entity collection path
     *
     * @return
     */
    public String getEntityName() {
        return entityName;
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
