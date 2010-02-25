/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nullable;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EMapBase;
import com.mysema.query.types.expr.Expr;

/**
 * PMap represents map paths
 * 
 * @author tiwe
 * 
 * @param <K> key type
 * @param <V> value type
 */
@SuppressWarnings("serial")
public class PMap<K, V, E extends Expr<V>> extends EMapBase<K, V> implements Path<Map<K, V>> {
    
    private static final Set<Class<?>> typedClasses = new HashSet<Class<?>>(Arrays.<Class<?>>asList(
            PathBuilder.class,
            PComparable.class,
            PDate.class,
            PDateTime.class,
            PNumber.class,
            PSimple.class, 
            PTime.class            
            ));
        
    private final Class<K> keyType;
    
    private final Path<Map<K,V>> pathMixin;
    
    private final Class<E> queryType;
    
    @Nullable 
    private transient Constructor<E> constructor; 
    
    private final Class<V> valueType;
    
    @SuppressWarnings("unchecked")
    public PMap(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, PathMetadata<?> metadata) {
        super((Class)Map.class);
        this.keyType = (Class<K>) keyType;
        this.valueType = (Class<V>) valueType;
        this.queryType = queryType;
        this.pathMixin = new PathMixin<Map<K,V>>(this, metadata);
    }
    
    protected PathMetadata<K> forMapAccess(K key){
        return PathMetadataFactory.forMapAccess(this, key);
    }
    
    protected PathMetadata<K> forMapAccess(Expr<K> key){
        return PathMetadataFactory.forMapAccess(this, key);
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @Override
    public PMap<K,V,E> asExpr() {
        return this;
    }

    @Override
    public boolean equals(Object o) {
        return pathMixin.equals(o);
    }

    @Override
    public E get(Expr<K> key) {
        PathMetadata<K> md =  forMapAccess(key);
        try {
            return newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public E get(K key) {
        PathMetadata<K> md =  forMapAccess(key);
        try {
            return newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
    
    @Override
    public Class<K> getKeyType() {
        return keyType;
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
    public Class<V> getValueType() {
        return valueType;
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
    
    @Override
    public AnnotatedElement getAnnotatedElement(){
        return pathMixin.getAnnotatedElement();
    }
    
    private E newInstance(PathMetadata<?> pm) throws Exception{
        if (constructor == null){
            try {
                if (typedClasses.contains(queryType)){
                    constructor = queryType.getConstructor(Class.class, PathMetadata.class);   
                }else{
                    constructor = queryType.getConstructor(PathMetadata.class);    
                }                
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }    
        }
        if (typedClasses.contains(queryType)){
            return constructor.newInstance(getValueType(), pm);
        }else{
            return constructor.newInstance(pm);
        }
    }
    
}