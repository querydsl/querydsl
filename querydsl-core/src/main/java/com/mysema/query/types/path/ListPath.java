/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathImpl;
import com.mysema.query.types.PathMetadata;
import com.mysema.query.types.PathMetadataFactory;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.ListExpression;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * ListPath represents list paths
 *
 * @author tiwe
 *
 * @param <E> component type
 * @param <Q> component query type
 */
public class ListPath<E, Q extends SimpleExpression<? super E>> extends CollectionPathBase<List<E>, E, Q> implements ListExpression<E, Q> {

    private static final long serialVersionUID = 3302301599074388860L;

    private final Map<Integer,Q> cache = new HashMap<Integer,Q>();

    private final Class<E> elementType;

    private final Path<List<E>> pathMixin;

    private final Class<Q> queryType;

    @Nullable
    private transient Q any;    

    public ListPath(Class<? super E> elementType, Class<Q> queryType, String variable) {
        this(elementType, queryType, PathMetadataFactory.forVariable(variable));
    }
    
    public ListPath(Class<? super E> elementType, Class<Q> queryType, Path<?> parent, String property) {
        this(elementType, queryType, PathMetadataFactory.forProperty(parent, property));   
    }
    
    @SuppressWarnings("unchecked")
    public ListPath(Class<? super E> elementType, Class<Q> queryType, PathMetadata<?> metadata) {
        super((Class)List.class);
        this.elementType = (Class<E>) Assert.notNull(elementType,"type");
        this.queryType = queryType;
        this.pathMixin = new PathImpl<List<E>>((Class)List.class, metadata);
    }

    @Override
    public <R,C> R accept(Visitor<R,C> v, C context) {
        return v.visit(this, context);
    }
    
    @Override
    public Q any(){
        if (any == null){
            any = newInstance(queryType, PathMetadataFactory.forCollectionAny(this));
        }
        return any;
    }

    protected PathMetadata<Integer> forListAccess(int index){
        return PathMetadataFactory.forListAccess(this, index);
    }

    protected PathMetadata<Integer> forListAccess(Expression<Integer> index){
        return PathMetadataFactory.forListAccess(this, index);
    }

    private Q create(int index){
        PathMetadata<Integer> md = forListAccess(index);
        return newInstance(queryType, md);
    }

    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    @Override
    public Q get(Expression<Integer> index) {
        PathMetadata<Integer> md = forListAccess(index);
        return newInstance(queryType, md);
    }


    @Override
    public Q get(int index) {
        if (cache.containsKey(index)){
            return cache.get(index);
        }else{
            Q rv = create(index);
            cache.put(index, rv);
            return rv;
        }
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
    public int hashCode() {
        return pathMixin.hashCode();
    }

    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }

    @Override
    public Class<?> getParameter(int index) {
        if (index == 0){
            return elementType;
        }else{
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
    }

}
