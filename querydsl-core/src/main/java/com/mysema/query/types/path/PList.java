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
import java.util.Map;
import java.util.Set;

import com.mysema.query.types.expr.EList;
import com.mysema.query.types.expr.Expr;

/**
 * PEntityList represents entity list paths
 * 
 * @author tiwe
 * 
 * @param <E> component type
 */
@SuppressWarnings("serial")
public class PList<E, Q extends Expr<E>> extends PCollection<E> implements EList<E>{
    
    private static final Set<Class<?>> typedClasses = new HashSet<Class<?>>(Arrays.<Class<?>>asList(
            PComparable.class,
            PDate.class,
            PDateTime.class,
            PNumber.class,
            PSimple.class, 
            PTime.class            
            ));
    
    private final Class<Q> queryType;
    
    private Constructor<Q> queryTypeConstructor;
    
    private final Map<Integer,Q> cache = new HashMap<Integer,Q>();
    
    public PList(Class<? super E> elementType, Class<Q> queryType, PathMetadata<?> metadata) {
        super(elementType, elementType.getSimpleName(), metadata);
        this.queryType = queryType;
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
    
    private Q create(int index){
        PathMetadata<Integer> md = PathMetadata.forListAccess(this, index);
        try {
            return newInstance(md);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

}