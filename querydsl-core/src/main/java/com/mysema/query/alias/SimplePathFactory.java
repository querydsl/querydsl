/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.alias;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import com.mysema.query.types.expr.Expr;
import com.mysema.query.types.path.PBoolean;
import com.mysema.query.types.path.PCollection;
import com.mysema.query.types.path.PComparable;
import com.mysema.query.types.path.PDate;
import com.mysema.query.types.path.PDateTime;
import com.mysema.query.types.path.PEntity;
import com.mysema.query.types.path.PList;
import com.mysema.query.types.path.PMap;
import com.mysema.query.types.path.PNumber;
import com.mysema.query.types.path.PSet;
import com.mysema.query.types.path.PString;
import com.mysema.query.types.path.PTime;
import com.mysema.query.types.path.Path;
import com.mysema.query.types.path.PathMetadata;

/**
 * SimplePathFactory is a PathFactory implementation for the creation of Path
 * instances
 * 
 * @author tiwe
 * @version $Id$
 */
class SimplePathFactory implements PathFactory {

    private final Map<Object,Path<?>> cache = new WeakHashMap<Object,Path<?>>();
    
    private final PBoolean btrue = new PBoolean(createMetadata());
    
    private final PBoolean bfalse = new PBoolean(createMetadata());

    private long counter = 0;

    public <D> Expr<D> createAny(D arg) {
        throw new UnsupportedOperationException();
    }

    public PBoolean createBoolean(Boolean arg) {
        return arg.booleanValue() ? btrue : bfalse;
    }

    @SuppressWarnings("unchecked")
    public <D> PCollection<D> createCollection(Collection<D> arg) {
        if (cache.containsKey(arg)){
            return (PCollection<D>) cache.get(arg);
        }
        
        PCollection<D> rv;
        if (!arg.isEmpty()) {
            Class<?> cl = ((Collection) arg).iterator().next().getClass();
            rv = new PCollection(cl, cl.getSimpleName(), createMetadata());
        } else {
            rv = new PCollection(Object.class, "Object", createMetadata());
        }    
        cache.put(arg, rv);
        return rv;
    }
    
    private <T, P extends Path<T>> P cache(T key, P value){
        cache.put(key, value);
        return value;
    }

    @SuppressWarnings("unchecked")
    public <D extends Comparable<?>> PComparable<D> createComparable(D arg) {
        if (cache.containsKey(arg)){
            return (PComparable<D>) cache.get(arg);
        }else{
            return (PComparable<D>) cache(arg, new PComparable(arg.getClass(), createMetadata()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDate<D> createDate(D arg) {
        if (cache.containsKey(arg)){
            return (PDate<D>) cache.get(arg);
        }else{
            return (PDate<D>) cache(arg, new PDate(arg.getClass(), createMetadata()));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PDateTime<D> createDateTime(D arg) {
        if (cache.containsKey(arg)){
            return (PDateTime<D>) cache.get(arg);
        }else{
            return (PDateTime<D>) cache(arg, new PDateTime(arg.getClass(), createMetadata()));
        }
    }

    @SuppressWarnings("unchecked")
    public <D> PEntity<D> createEntity(D arg) {
        if (cache.containsKey(arg)){
            return (PEntity<D>) cache.get(arg);
        }else{
            return (PEntity<D>) cache(arg, 
                new PEntity(arg.getClass(), arg.getClass().getSimpleName(), createMetadata()));    
        }        
    }
    
    @SuppressWarnings({ "unchecked", "serial" })
    @Override
    public <D> PList<D,?> createList(List<D> arg) {
        if (cache.containsKey(arg)){
            return (PList<D, ?>) cache.get(arg);
        }        
        final Class<?> cl = arg.isEmpty() ?  Object.class : arg.get(0).getClass();
        return (PList<D, ?>) cache(arg, new PList<D,PEntity<D>>(Object.class, Object.class.getSimpleName(), null, createMetadata()){                        
            @Override
            public PEntity get(Expr<Integer> index) {
                return new PEntity(cl, cl.getSimpleName(), PathMetadata.forListAccess(this, index));
            }
            @Override
            public PEntity get(int index) {
                return new PEntity(cl, cl.getSimpleName(), PathMetadata.forListAccess(this, index));
            }
        });
        
    }

    @SuppressWarnings({ "unchecked", "serial" })
    public <K, V> PMap<K, V, ?> createMap(Map<K, V> arg) {
        if (cache.containsKey(arg)){
            return (PMap<K, V, ?>) cache.get(arg);
        }
        
        if (!arg.isEmpty()) {
            Map.Entry entry = arg.entrySet().iterator().next();
            final Class<Object> keyType = (Class)entry.getKey().getClass();
            final Class<Object> valueType = (Class)entry.getValue().getClass();
            return cache(arg, new PMap<K,V,PEntity<V>>(keyType, valueType, null, createMetadata()){
                @Override
                public PEntity get(Expr<K> key) {
                    return new PEntity(valueType, valueType.getSimpleName(), 
                            PathMetadata.forMapAccess(this, key));
                }
                @Override
                public PEntity get(K key) {
                    return new PEntity(valueType, valueType.getSimpleName(), 
                            PathMetadata.forMapAccess(this, key));
                }
            });
        } else {
            return cache(arg, new PMap<K,V,PEntity<V>>(Object.class, Object.class, null, createMetadata()){
                @Override
                public PEntity get(Expr<K> key) {
                    return new PEntity(Object.class, Object.class.getSimpleName(), 
                            PathMetadata.forMapAccess(this, key));
                }
                @Override
                public PEntity get(K key) {
                    return new PEntity(Object.class, Object.class.getSimpleName(), 
                            PathMetadata.forMapAccess(this, key));
                }
            });
        }
        
    }

    @SuppressWarnings("unchecked")
    public <D extends Number & Comparable<?>> PNumber<D> createNumber(D arg) {
        if (cache.containsKey(arg)){
            return (PNumber<D>) cache.get(arg);
        }else{
            return (PNumber<D>) cache(arg, new PNumber(arg.getClass(), createMetadata()));    
        }
        
    }

    @SuppressWarnings("unchecked")
    public <D> PSet<D> createSet(Set<D> arg) {
        if (cache.containsKey(arg)){
            return (PSet<D>) cache.get(arg);
        }else{
            Class<?> cl = Object.class;
            if (!arg.isEmpty()) {
                cl = ((Set) arg).iterator().next().getClass();                
            }   
            return (PSet<D>) cache(arg, new PSet(cl, cl.getSimpleName(), createMetadata()));
        }        
    }

    public PString createString(String arg) {
        if (cache.containsKey(arg)){
            return (PString) cache.get(arg);
        }else{
            return cache(arg, new PString(createMetadata()));    
        }
        
    }

    @SuppressWarnings("unchecked")
    @Override
    public <D extends Comparable> PTime<D> createTime(D arg) {
        if (cache.containsKey(arg)){
            return (PTime<D>) cache.get(arg);
        }else{
            return (PTime<D>) cache(arg, new PTime(arg.getClass(), createMetadata()));    
        }
        
    }

    private PathMetadata<String> createMetadata() {
        return PathMetadata.forVariable("v" + String.valueOf(++counter));
    }

}
