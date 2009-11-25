/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.commons.lang.Assert;
import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.ECollectionBase;
import com.mysema.query.types.expr.EList;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.util.NotEmpty;

/**
 * PList represents list paths
 * 
 * @author tiwe
 * 
 * @param <E> component type
 */
@SuppressWarnings("serial")
public class PList<E, Q extends Expr<E>> extends ECollectionBase<List<E>,E> implements EList<E>, Path<List<E>>{
    
    private static final Set<Class<?>> typedClasses = new HashSet<Class<?>>(Arrays.<Class<?>>asList(
            PComparable.class,
            PDate.class,
            PDateTime.class,
            PNumber.class,
            PSimple.class, 
            PTime.class            
            ));
    
    private final Map<Integer,Q> cache = new HashMap<Integer,Q>();
    
    protected final Class<E> elementType;
    
    protected final String entityName;
    
    private final Path<List<E>> pathMixin;
    
    private final Class<Q> queryType;    
    
    private Constructor<Q> queryTypeConstructor;    
    
    @SuppressWarnings("unchecked")
    public PList(Class<? super E> elementType, @NotEmpty String entityName, Class<Q> queryType, PathMetadata<?> metadata) {
        super((Class)List.class);
        this.elementType = (Class<E>) Assert.notNull(elementType,"type is null");
        this.entityName = Assert.notNull(entityName,"entityName is null");
        this.queryType = queryType;
        this.pathMixin = new PathMixin<List<E>>(this, metadata);
    }
    
    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public Expr<List<E>> asExpr() {
        return this;
    }

    private Q create(int index){
        PathMetadata<Integer> md = PathMetadata.forListAccess(this, index);
        try {
            return newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    @Override
    public Q get(Expr<Integer> index) {
        PathMetadata<Integer> md = PathMetadata.forListAccess(this, index);        
        try {
            return newInstance(md);        
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }            
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

    @Override
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
    public EBoolean isNotNull() {
        return pathMixin.isNotNull();
    }

    @Override
    public EBoolean isNull() {
        return pathMixin.isNull();
    }
    
    private Q newInstance(PathMetadata<?> pm) throws Exception{
        if (queryTypeConstructor == null){
            try {
                if (typedClasses.contains(queryType)){
                    queryTypeConstructor = queryType.getConstructor(Class.class, PathMetadata.class);   
                }else{
                    queryTypeConstructor = queryType.getConstructor(PathMetadata.class);    
                }                
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }    
        }
        if (typedClasses.contains(queryType)){
            return queryTypeConstructor.newInstance(getElementType(), pm);
        }else{
            return queryTypeConstructor.newInstance(pm);
        }
    }

}