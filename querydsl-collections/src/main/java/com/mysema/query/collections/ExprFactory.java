/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import org.apache.commons.lang.StringUtils;

import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.ColTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * ExprFactory is a factory implementation for the creation of Path instances
 *
 * @author tiwe
 * @version $Id$
 */
// TODO : consider moving this later to querydsl-core
class ExprFactory {
    
    final ExtString strExt = new ExtString(PathMetadata.forVariable("str"));
        
    private final Map<Object,PSimple<?>> simToPath = new PathFactory<Object,PSimple<?>>(new Transformer<Object,PSimple<?>>(){
        @SuppressWarnings("unchecked")
        public PSimple<?> transform(Object arg) {
            return new PSimple(arg.getClass(), md());
        }    
    });
    
    private final Map<Object,PComparable<?>> comToPath = new PathFactory<Object,PComparable<?>>(new Transformer<Object,PComparable<?>>(){
        @SuppressWarnings("unchecked")
        public PComparable<?> transform(Object arg) {
            return new PComparable(arg.getClass(), md());
        }    
    });
    
    private final Map<String,ExtString> strToExtPath = new PathFactory<String,ExtString>(new Transformer<String,ExtString>(){
        public ExtString transform(String str) {
            return new ExtString(md());
        }        
    });
    
    private long counter = 0;
        
    public PBoolean create(Boolean arg){
        // NOTE : we can't really cache Booleans, since there are only two values,
        // but possibly more variables to be tracked
        return new PBoolean(md());
    }
    
    @SuppressWarnings("unchecked")
    public <D extends Comparable<D>> PComparable<D> create(D arg){
        return (PComparable<D>) comToPath.get(arg);
    }
    
    public ExtString create(String arg){
        return StringUtils.isEmpty(arg) ? strExt : strToExtPath.get(arg);
    }
    
    @SuppressWarnings("unchecked")
    public <D> PSimple<D> create(D arg){
        return (PSimple<D>) simToPath.get(arg);
    }
    
    public PBooleanArray create(Boolean[] args){
        // TODO : cache
        return new PBooleanArray(md());
    }
    
    @SuppressWarnings("unchecked")
    public <D extends Comparable<D>> PComparableArray<D> create(D[] args){
        // TODO : cache
        return new PComparableArray(args[0].getClass(),md());
    }
    
    public PStringArray create(String[] args){
        // TODO : cache
        return new PStringArray(md());
    }
    
    private PathMetadata<String> md(){
        return PathMetadata.forVariable("var"+String.valueOf(++counter));
    }
    
    private static class PathFactory<K,V> extends LazyMap<K,V>{

        private static final long serialVersionUID = -2443097467085594792L;
        
        protected PathFactory(Transformer<K,V> transformer) {
            super(new WeakHashMap<K,V>(), transformer);
        }
                
    }
   
}
