/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types;

import java.lang.reflect.AnnotatedElement;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.util.ReflectionUtils;

/**
 * PathImpl defines a default implementation of the Path interface
 *
 * @author tiwe
 *
 * @param <T>
 */
public class PathImpl<T> extends ExpressionBase<T> implements Path<T> {

    private static final long serialVersionUID = -2498447742798348162L;

    private final PathMetadata<?> metadata;

    private final Path<?> root;

    @Nullable
    private AnnotatedElement annotatedElement;

    public PathImpl(Class<? extends T> type, String variable){
        this(type, PathMetadataFactory.forVariable(variable));
    }

    public PathImpl(Class<? extends T> type, PathMetadata<?> metadata){
        super(type);
        this.metadata = Assert.notNull(metadata,"metadata");
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    public PathImpl(Class<? extends T> type, Path<?> parent, String property) {
        this(type, PathMetadataFactory.forProperty(parent, property));
    }

    @Override
    public boolean equals(Object o) {
        if (o == this){
            return true;
        }else if (o instanceof Path){
            return ((Path<?>) o).getMetadata().equals(metadata);
        }else{
            return false;
        }
    }

    @Override
    public PathMetadata<?> getMetadata() {
        return metadata;
    }

    @Override
    public Path<?> getRoot() {
        return root;
    }

    @Override
    public int hashCode() {
        return metadata.hashCode();
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        if (annotatedElement == null){
            if (metadata.getPathType() == PathType.PROPERTY){
                Class<?> beanClass = metadata.getParent().getType();
                String propertyName = metadata.getExpression().toString();
                annotatedElement = ReflectionUtils.getAnnotatedElement(beanClass, propertyName, type);

            }else{
                annotatedElement = type;
            }
        }
        return annotatedElement;
    }

    @Override
    public <R, C> R accept(Visitor<R, C> v, C context) {
        return v.visit(this, context);
    }

}
