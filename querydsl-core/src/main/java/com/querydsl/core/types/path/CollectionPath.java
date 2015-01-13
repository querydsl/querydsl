/*
 * Copyright 2011, Mysema Ltd
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.core.types.path;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

import javax.annotation.Nullable;

import com.querydsl.core.types.Path;
import com.querydsl.core.types.PathImpl;
import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.PathMetadataFactory;
import com.querydsl.core.types.Visitor;
import com.querydsl.core.types.expr.SimpleExpression;

/**
 * CollectionPath represents collection paths
 *
 * @author tiwe
 *
 * @param <E> component type
 * @param <Q> component querydsl type
 */
public class CollectionPath<E, Q extends SimpleExpression<? super E>> extends CollectionPathBase<Collection<E>, E, Q> {    

    private static final long serialVersionUID = -4982311799113762600L;

    private final Class<E> elementType;

    private final PathImpl<Collection<E>> pathMixin;

    @Nullable
    private transient Q any;
    
    private final Class<Q> queryType;
    
    public CollectionPath(Class<? super E> type, Class<Q> queryType, String variable) {
        this(type, queryType, PathMetadataFactory.forVariable(variable));
    }
    
    public CollectionPath(Class<? super E> type, Class<Q> queryType, Path<?> parent, String property) {
        this(type, queryType, PathMetadataFactory.forProperty(parent, property));
    }
    
    public CollectionPath(Class<? super E> type, Class<Q> queryType, PathMetadata<?> metadata) {
        this(type, queryType, metadata, PathInits.DIRECT);
    }
    
    @SuppressWarnings("unchecked")
    public CollectionPath(Class<? super E> type, Class<Q> queryType, PathMetadata<?> metadata, PathInits inits) {
        super(new PathImpl<Collection<E>>((Class)Collection.class, metadata), inits);
        this.elementType = (Class<E>)type;
        this.queryType = queryType;
        this.pathMixin = (PathImpl<Collection<E>>)mixin;
    }
    
    @Override
    public final <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(pathMixin, context);
    }
    
    @Override
    public Q any() {
        if (any == null) {
            any = newInstance(queryType, PathMetadataFactory.forCollectionAny(pathMixin));
        }
        return any;
    }

    public Class<E> getElementType() {
        return elementType;
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
    public AnnotatedElement getAnnotatedElement() {
        return pathMixin.getAnnotatedElement();
    }

    @Override
    public Class<?> getParameter(int index) {
        if (index == 0) {
            return elementType;
        } else {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }
    
}
