/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;

import javax.annotation.Nullable;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Ops;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathType;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ESimple;
import com.mysema.query.types.expr.OBoolean;
import com.mysema.util.ReflectionUtils;

/**
 * PathMixin defines a mixin version of the Path interface which can be used
 * as a component and target in actual Path implementations
 *
 * @author tiwe
 *
 * @param <T>
 */
public final class PathMixin<T> implements Path<T>, Serializable {

    private static final long serialVersionUID = -2498447742798348162L;

    @Nullable
    private volatile EBoolean isnull, isnotnull;

    private final PathMetadata<?> metadata;

    private final Path<?> root;

    private final Expr<T> self;

    @Nullable
    private AnnotatedElement annotatedElement;

    public PathMixin(Path<T> self, PathMetadata<?> metadata){
        this.self = self.asExpr();
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : self;
    }

    @Override
    public Expr<T> asExpr() {
        return self;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (o == this || o == self){
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
    public Class<? extends T> getType() {
        return self.getType();
    }

    @Override
    public int hashCode() {
        return metadata.hashCode();
    }

    @Override
    public EBoolean isNotNull() {
        if (isnotnull == null) {
            isnotnull = OBoolean.create(Ops.IS_NOT_NULL, self);
        }
        return isnotnull;
    }

    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = OBoolean.create(Ops.IS_NULL, self);
        }
        return isnull;
    }

    @Override
    public AnnotatedElement getAnnotatedElement() {
        if (annotatedElement == null){
            if (metadata.getPathType() == PathType.PROPERTY){
                Class<?> beanClass = metadata.getParent().getType();
                String propertyName = metadata.getExpression().toString();
                annotatedElement = ReflectionUtils.getAnnotatedElement(beanClass, propertyName, self.getType());

            }else{
                annotatedElement = self.getType();
            }
        }
        return annotatedElement;
    }

}
