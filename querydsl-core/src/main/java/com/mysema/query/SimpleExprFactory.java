/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;
import org.apache.commons.lang.StringUtils;

import com.mysema.query.grammar.types.PathMetadata;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EComparable;
import com.mysema.query.grammar.types.ExtTypes.ExtString;
import com.mysema.query.grammar.types.Path.*;

/**
 * SimpleExprFactory is a factory implementation for the creation of Path instances
 *
 * @author tiwe
 * @version $Id$
 */
// TODO : consider moving this later to querydsl-core
public class SimpleExprFactory implements ExprFactory {
    
    private final ExtString strExt = new ExtString(PathMetadata.forVariable("str"));
    
    private final PBoolean btrue = new PBoolean(md()), bfalse = new PBoolean(md());
    
    private long counter = 0;
    
    private final Map<Object,PBooleanArray> baToPath = new PathFactory<Object,PBooleanArray>(new Transformer<Object,PBooleanArray>(){
        @SuppressWarnings("unchecked")
        public PBooleanArray transform(Object arg) {
            return new PBooleanArray(md());
        }    
    });
    
    private final Map<Object,PComparableArray<?>> caToPath = new PathFactory<Object,PComparableArray<?>>(new Transformer<Object,PComparableArray<?>>(){
        @SuppressWarnings("unchecked")
        public PComparableArray<?> transform(Object arg) {
            return new PComparableArray(((List)arg).get(0).getClass(), md());
        }    
    });
    
    private final Map<Collection<?>,PEntityCollection<?>> ecToPath = new PathFactory<Collection<?>,PEntityCollection<?>>(new Transformer<Collection<?>,PEntityCollection<?>>(){
        @SuppressWarnings("unchecked")
        public PEntityCollection<?> transform(Collection<?> arg) {
            if (!arg.isEmpty()){
                Class<?> cl = ((Collection)arg).iterator().next().getClass();
                return new PEntityCollection(cl, cl.getSimpleName(), md());    
            }else{
                return new PEntityCollection(null, null, md());
            }            
        }    
    });
        
    private final Map<List<?>,PEntityList<?>> elToPath = new PathFactory<List<?>,PEntityList<?>>(new Transformer<List<?>,PEntityList<?>>(){
        @SuppressWarnings("unchecked")
        public PEntityList<?> transform(List<?> arg) {
            if (!arg.isEmpty()){
                Class<?> cl = arg.get(0).getClass();
                return new PEntityList(cl, cl.getSimpleName(), md());    
            }else{
                return new PEntityList(null, null, md());
            }            
        }    
    });
    
    private final Map<Object,PComparable<?>> comToPath = new PathFactory<Object,PComparable<?>>(new Transformer<Object,PComparable<?>>(){
        @SuppressWarnings("unchecked")
        public PComparable<?> transform(Object arg) {
            return new PComparable(arg.getClass(), md());
        }    
    });
        
    private final Map<Object,PStringArray> saToPath = new PathFactory<Object,PStringArray>(new Transformer<Object,PStringArray>(){
        public PStringArray transform(Object arg) {
            return new PStringArray(md());
        }    
    });
        
    private final Map<Object,PEntity<?>> entityToPath = new PathFactory<Object,PEntity<?>>(new Transformer<Object,PEntity<?>>(){
        @SuppressWarnings("unchecked")
        public PEntity<?> transform(Object arg) {
            return new PEntity(arg.getClass(), arg.getClass().getSimpleName(), md());
        }    
    });
    
    private final Map<String,ExtString> strToExtPath = new PathFactory<String,ExtString>(new Transformer<String,ExtString>(){
        public ExtString transform(String str) {
            return new ExtString(md());
        }        
    });
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.Boolean)
     */
    public EBoolean create(Boolean arg){
        return arg.booleanValue() ? btrue : bfalse;
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.Boolean[])
     */
    public PBooleanArray create(Boolean[] args){
        return baToPath.get(Arrays.asList(args));
    }
    
    @SuppressWarnings("unchecked")
    public <D> PEntityCollection<D> create(Collection<D> arg) {
        return (PEntityCollection<D>) ecToPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(D)
     */
    @SuppressWarnings("unchecked")
    public <D extends Comparable<D>> EComparable<D> create(D arg){
        return (EComparable<D>) comToPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(D)
     */
    @SuppressWarnings("unchecked")
    public <D> PEntity<D> create(D arg){
        return (PEntity<D>) entityToPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(D[])
     */
    @SuppressWarnings("unchecked")
    public <D extends Comparable<D>> PComparableArray<D> create(D[] args){
        return (PComparableArray<D>) caToPath.get(Arrays.asList(args));
    }

    @SuppressWarnings("unchecked")
    public <D> PEntityList<D> create(List<D> arg) {
        return (PEntityList<D>) elToPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.String)
     */
    public ExtString create(String arg){
        return StringUtils.isEmpty(arg) ? strExt : strToExtPath.get(arg);
    }
    
    /* (non-Javadoc)
     * @see com.mysema.query.collections.ExprFactory#create(java.lang.String[])
     */
    public PStringArray create(String[] args){
        return saToPath.get(Arrays.asList(args));
    }
    
    private PathMetadata<String> md(){
        return PathMetadata.forVariable("v"+String.valueOf(++counter));
    }
    
    private static class PathFactory<K,V> extends LazyMap<K,V>{

        private static final long serialVersionUID = -2443097467085594792L;
        
        protected PathFactory(Transformer<K,V> transformer) {
            super(new WeakHashMap<K,V>(), transformer);
        }
                
    }
   
}
