/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.mysema.query.collections.IteratorSource;
import com.mysema.query.collections.eval.Evaluator;
import com.mysema.query.collections.utils.EvaluatorUtils;
import com.mysema.query.collections.utils.QueryIteratorUtils;
import com.mysema.query.grammar.JavaOps;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Expr.EBoolean;

/**
 * DefaultIndexSupport provides a local query specific result cache which creates
 * optimized children of itself for given query conditions
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultIndexSupport extends SimpleIndexSupport{
    
    private Map<Path<?>,Map<?,? extends Iterable<?>>> pathToKeyToValues;
    
    private Map<Path<?>,IndexedPath> rootToIndexedPath = new HashMap<Path<?>,IndexedPath>();
    
    /**
     * Create a new DefaultIndexSupport instance
     * 
     * @param iteratorSource
     * @param ops
     */
    public DefaultIndexSupport(IteratorSource iteratorSource, JavaOps ops, List<? extends Expr<?>> sources) {
        super(iteratorSource, ops, sources);
        this.pathToKeyToValues = new HashMap<Path<?>,Map<?,? extends Iterable<?>>>();        
    }        

    public DefaultIndexSupport addPath(Path<?> path, IndexedPath indexedPath){
        rootToIndexedPath.put(path, indexedPath);
        return this;
    }
    
    public DefaultIndexSupport getChildFor(EBoolean condition){  
        if (condition == null){
            return this;
        }        
        DefaultIndexSupport indexSupport = new DefaultIndexSupport(iteratorSource, ops, sources);
        indexSupport.pathToKeyToValues = this.pathToKeyToValues;
        
        // populate the "path eq path" index
        if (condition instanceof Operation){
            new DefaultIndexCreationTask(indexSupport, sources, ops, condition).run();
        }        
        return indexSupport;
    }
    
    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr) {
        if (rootToIndexedPath.containsKey(expr)){
            IndexedPath ie = rootToIndexedPath.get(expr);
            Map<?,? extends Iterable<?>> indexEntry = pathToKeyToValues.get(ie.getIndexedPath());
            // NOTE : this works only for static keys
            Object key = ie.getEvaluator().evaluate((Object[])null);
            if (indexEntry.containsKey(key)){
                return (Iterator<A>)indexEntry.get(key).iterator();    
            }else{
                return Collections.<A>emptyList().iterator();
            }            
        }else{
            return super.getIterator(expr);    
        }     
    }
    

    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        if (rootToIndexedPath.containsKey(expr)){
            IndexedPath ie = rootToIndexedPath.get(expr);
            Map<?,? extends Iterable<?>> indexEntry = pathToKeyToValues.get(ie.getIndexedPath());
            Object key = ie.getEvaluator().evaluate(bindings);
            if (indexEntry.containsKey(key)){
                return (Iterator<A>)indexEntry.get(key).iterator();    
            }else{
                return Collections.<A>emptyList().iterator();
            }            
        }else{
            return super.getIterator(expr,bindings);    
        }        
    }
    
    Map<Path<?>, Map<?, ? extends Iterable<?>>> getPathToKeyToValues() {
        return Collections.unmodifiableMap(pathToKeyToValues);
    }
    
    public DefaultIndexSupport indexToHash(Path<?> path){
        if (pathToKeyToValues.containsKey(path)){
            return this;
        }
        // create the index entry
        Evaluator ev = EvaluatorUtils.create(ops, Collections.<Expr<?>>singletonList((Expr<?>)path.getRoot()), (Expr<?>)path);
        Map<?,? extends Iterable<?>> map = QueryIteratorUtils.projectToMap(iteratorSource.getIterator((Expr<?>)path.getRoot()), ev);
        
        pathToKeyToValues.put(path, map);
        return this;
    }
    
    DefaultIndexSupport indexToTree(Path<?> path){
        // TODO
        return this;
    }
 
    public boolean isIndexed(Path<?> path){
        return rootToIndexedPath.containsKey(path);
    }
    
}
