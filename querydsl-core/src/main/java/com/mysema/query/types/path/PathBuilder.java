/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.util.HashMap;
import java.util.Map;

import com.mysema.query.types.Expr;
import com.mysema.query.types.Path;
import com.mysema.query.types.PathMetadata;

/**
 * PathBuilder is an extension to PEntity for dynamic path construction
 * 
 * @author tiwe
 *
 * @param <D>
 */
public final class PathBuilder<D> extends PEntity<D>{
    
    private static final long serialVersionUID = -1666357914232685088L;
    
    private final Map<String,PathBuilder<?>> properties = new HashMap<String,PathBuilder<?>>();

    /**
     * Creates a new PathBuilder instance
     * 
     * @param type
     * @param pathMetadata
     */
    public PathBuilder(Class<? extends D> type, PathMetadata<?> pathMetadata) {
        super(type, pathMetadata);
    }
    
    /**
     * Creates a new PathBuilder instance
     * 
     * @param type
     * @param variable
     */
    public PathBuilder(Class<? extends D> type, String variable) {
        super(type, PathMetadataFactory.forVariable(variable));
    }
    
    /**
     * Get a PathBuilder instance for the given property
     * 
     * @param property property name
     * @return
     */
    @SuppressWarnings("unchecked")
    public PathBuilder<Object> get(String property) {
        PathBuilder<Object> path = (PathBuilder) properties.get(property);
        if (path == null){
            path = new PathBuilder<Object>(Object.class, forProperty(property));
            properties.put(property, path);
        }        
        return path;
    }

    /**
     * Get a PathBuilder for the given property with the given type
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A> PathBuilder<A> get(String property, Class<A> type) {
        PathBuilder<A> path = (PathBuilder<A>) properties.get(property);
        if (path == null || !type.isAssignableFrom(path.getType())){
            path = new PathBuilder<A>(type, forProperty(property));
            properties.put(property, path);
        }        
        return path;
    }

    /**
     * Get a PArray instance for the given property and the given array type
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> PArray<A> getArray(String property, Class<A[]> type) {
        return super.createArray(property, type);
    }

    /**
     * @param path
     * @return
     */
    public PBoolean get(PBoolean path){
        return getBoolean(path.toString());
    }
    
    /**
     * Get a new Boolean typed path
     * 
     * @param propertyName property name
     * @return
     */
    public PBoolean getBoolean(String propertyName) {
        return super.createBoolean(propertyName);
    }

    /**
     * Get a new Collection typed path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> PCollection<A> getCollection(String property, Class<A> type) {
        return super.createCollection(property, type);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> PComparable<A> get(PComparable<A> path){
        return getComparable(path.toString(), (Class<A>)path.getType());
    }
    
    /**
     * Get a new Comparable typed path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> PComparable<A> getComparable(String property, Class<A> type) {
        return super.createComparable(property, type);
    }

    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> PDate<A> get(PDate<A> path){
        return getDate(path.toString(), (Class<A>)path.getType());
    }
    
    /**
     * Get a new Date path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> PDate<A> getDate(String property, Class<A> type) {
        return super.createDate(property, type);
    }
    
    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> PDateTime<A> get(PDateTime<A> path){
        return getDateTime(path.toString(), (Class<A>)path.getType());
    }
    
    /**
     * Get a new DateTime path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> PDateTime<A> getDateTime(String property, Class<A> type) {
        return super.createDateTime(property, type);
    }

    /**
     * Get a new List typed path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> PList<A, PathBuilder<A>> getList(String property, Class<A> type) {
        return super.createList(property, type, PathBuilder.class);
    }
    
    /**
     * Get a new List typed path
     * 
     * @param <A>
     * @param <E>
     * @param property property name
     * @param type
     * @param queryType
     * @return
     */
    public <A, E extends Expr<A>> PList<A, E> getList(String property, Class<A> type, Class<E> queryType) {
        return super.createList(property, type, queryType);
    }

    /**
     * Get a new Map typed path
     * 
     * @param <K>
     * @param <V>
     * @param property property name
     * @param key
     * @param value
     * @return
     */
    public <K, V> PMap<K, V, PathBuilder<V>> getMap(String property, Class<K> key, Class<V> value) {
        return super.<K,V,PathBuilder<V>>createMap(property, key, value, PathBuilder.class);
    }

    /**
     * Get a new Map typed path
     * 
     * @param <K>
     * @param <V>
     * @param <E>
     * @param property property name
     * @param key
     * @param value
     * @param queryType
     * @return
     */
    public <K, V, E extends Expr<V>> PMap<K, V, E> getMap(String property, Class<K> key, Class<V> value, Class<E> queryType) {
        return super.<K,V,E>createMap(property, key, value, queryType);
    }
    
    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A extends Number & Comparable<?>> PNumber<A> get(PNumber<A> path){
        return getNumber(path.toString(), (Class<A>)path.getType());
    }
    
    
    /**
     * Get a new Number typed path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Number & Comparable<?>> PNumber<A> getNumber(String property, Class<A> type) {
        return super.createNumber(property, type);
    }

    /**
     * Get a new Set typed path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> PSet<A> getSet(String property, Class<A> type) {
        return super.createSet(property, type);
    }
    
    /**
     * @param <A>
     * @param path
     * @return
     */
    @SuppressWarnings("unchecked")
    public <A> Path<A> get(Path<A> path){
        return getSimple(path.toString(), (Class<A>)path.getType());
    }
    
    /**
     * Get a new Simple path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A> PSimple<A> getSimple(String property, Class<A> type) {
        return super.createSimple(property, type);
    }

    /**
     * @param path
     * @return
     */
    public PString get(PString path){
        return getString(path.toString());
    }
    
    /**
     * Get a new String typed path
     * 
     * @param property property name
     * @return
     */
    public PString getString(String property) {
        return super.createString(property);
    }
    
    /**
     * @param <A>
     * @param path
     */
    @SuppressWarnings("unchecked")
    public <A extends Comparable<?>> PTime<A> get(PTime<A> path){
        return getTime(path.toString(), (Class<A>)path.getType());
    }

    /**
     * Get a new Time typed path
     * 
     * @param <A>
     * @param property property name
     * @param type
     * @return
     */
    public <A extends Comparable<?>> PTime<A> getTime(String property, Class<A> type) {
        return super.createTime(property, type);
    }

}
