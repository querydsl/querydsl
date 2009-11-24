/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.types.path;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mysema.query.types.Visitor;
import com.mysema.query.types.expr.EBoolean;
import com.mysema.query.types.expr.EMapBase;
import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.operation.OBoolean;
import com.mysema.query.types.operation.Ops;

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
            PComparable.class,
            PDate.class,
            PDateTime.class,
            PNumber.class,
            PSimple.class, 
            PTime.class            
            ));
    
    
    private volatile EBoolean isnull, isnotnull;
    
    private final Class<K> keyType;
    
    private final Class<V> valueType;
    
    private final Class<E> queryType;
    
    private Constructor<E> queryTypeConstructor; 
    
    private final PathMetadata<?> metadata;
    
    private final Path<?> root;

    @SuppressWarnings("unchecked")
    public PMap(Class<? super K> keyType, Class<? super V> valueType, Class<E> queryType, PathMetadata<?> metadata) {
        super((Class)Map.class);
        this.keyType = (Class<K>) keyType;
        this.valueType = (Class<V>) valueType;
        this.queryType = queryType;
        this.metadata = metadata;
        this.root = metadata.getRoot() != null ? metadata.getRoot() : this;
    }

    @Override
    public void accept(Visitor v) {
        v.visit(this);        
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        return o instanceof Path ? ((Path<?>) o).getMetadata().equals(metadata) : false;
    }
    
    private E newInstance(PathMetadata<?> pm) throws Exception{
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
            return queryTypeConstructor.newInstance(getValueType(), pm);
        }else{
            return queryTypeConstructor.newInstance(pm);
        }
    }

    @Override
    public E get(Expr<K> key) {
        PathMetadata<K> md =  PathMetadata.forMapAccess(this, key);
        try {
            return newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public E get(K key) {
        PathMetadata<K> md =  PathMetadata.forMapAccess(this, key);
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
        return metadata;
    }

    @Override
    public Path<?> getRoot() {
        return root;
    }

    @Override
    public Class<V> getValueType() {
        return valueType;
    }
    
    @Override
    public int hashCode() {
        return metadata.hashCode();
    }
    
    @Override
    public EBoolean isNotNull() {
        if (isnotnull == null) {
            isnotnull = OBoolean.create(Ops.IS_NOT_NULL, this);
        }
        return isnotnull;
    }
    
    @Override
    public EBoolean isNull() {
        if (isnull == null) {
            isnull = OBoolean.create(Ops.IS_NULL, this);
        }
        return isnull;
    }
    
    @Override
    public EMapBase<K,V> asExpr() {
        return this;
    }
    
}