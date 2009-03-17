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
import com.mysema.query.grammar.Ops;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Operation;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Expr.EBoolean;
import com.mysema.query.grammar.types.Expr.EConstant;

/**
 * DefaultIndexSupport provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultIndexSupport extends SimpleIndexSupport{
    
    private Map<Path<?>,Map<?,? extends Iterable<?>>> pathToKeyToValues;
    
    private Map<Path<?>,IndexedPath> rootToIndexedPath;
    
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

    public IteratorSource getChildFor(EBoolean condition){  
        if (condition == null){
            rootToIndexedPath = new HashMap<Path<?>,IndexedPath>();
            return this;
        }        
        DefaultIndexSupport indexSupport = new DefaultIndexSupport(iteratorSource, ops, sources);
        indexSupport.pathToKeyToValues = this.pathToKeyToValues;
        indexSupport.rootToIndexedPath = new HashMap<Path<?>,IndexedPath>();
        
        // populate the "path eq path" index
        if (condition instanceof Operation){
            indexSupport.visitOperation((Operation<?,?>) condition);
        }        
        return indexSupport;
    }
    
    protected void visitOperation(Operation<?,?> op) {
        if (op.getOperator() == Ops.EQ_OBJECT  || op.getOperator() == Ops.EQ_PRIMITIVE){
            Expr<?> e1 = op.getArgs()[0];
            Expr<?> e2 = op.getArgs()[1];
            if (e1 instanceof Path && e2 instanceof Path){
                Path<?> p1 = (Path<?>)e1, p2 = (Path<?>)e2;
                int i1 = sources.indexOf(p1.getRoot());
                int i2 = sources.indexOf(p2.getRoot());
                
                // index the path at higher position
                if (i1 < i2){
                    indexPathEqExpr(p2, e1);
                }else if (i1 > i2){
                    indexPathEqExpr(p1, e2);
                }
                
            }else if (e1 instanceof Path){
                if (e2 instanceof EConstant){
                    indexPathEqExpr((Path<?>)e1, e2);    
                }                
                                    
            }else if (e2 instanceof Path){
                if (e1 instanceof EConstant){
                    indexPathEqExpr((Path<?>)e2, e1);    
                }
                
            }
            
        }else if (op.getOperator() == Ops.NOT){    
            // skip negative condition paths
            
        }else if (op.getOperator() == Ops.AND){
            for (Expr<?> e : op.getArgs()){
                if (e instanceof Operation) visitOperation((Operation<?, ?>) e);
            }
        }        
    }
    
    private Map<?, ? extends Iterable<?>> createIndexEntry(Path<?> path) {
        Evaluator ev = EvaluatorUtils.create(ops, Collections.<Expr<?>>singletonList((Expr<?>)path.getRoot()), (Expr<?>)path);
        Map<?,? extends Iterable<?>> map = QueryIteratorUtils.projectToMap(iteratorSource.getIterator((Expr<?>)path.getRoot()), ev);
        return map;
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

    private void indexPathEqExpr(Path<?> path, Expr<?> key) {
        if (!rootToIndexedPath.containsKey(path.getRoot())){
            if (!pathToKeyToValues.containsKey(path)){
                // create the index entry
                pathToKeyToValues.put(path, createIndexEntry(path));    
            }
            
            // create the key creator            
            Evaluator keyCreator;
            if (key instanceof EConstant){
                final Object constant = ((EConstant<?>)key).getConstant(); 
                keyCreator = new Evaluator(){
                    public <T> T evaluate(Object... args) {
                        return (T)constant;
                    }                
                }; 
            }else{
                keyCreator = EvaluatorUtils.create(ops, sources, key);
            }
            
            // update the index            
            rootToIndexedPath.put(path.getRoot(), new IndexedPath(path,keyCreator));  
        }
    }
 
    Map<Path<?>, Map<?, ? extends Iterable<?>>> getPathToKeyToValues() {
        return Collections.unmodifiableMap(pathToKeyToValues);
    }
    
    /**
     * Mapping from root path to indexed path and evaluator for the index key
     *
     */
    private class IndexedPath{
        private final Path<?> path;
        private final Evaluator ev;        
        IndexedPath(Path<?> path,Evaluator ev){            
            this.path = path;
            this.ev = ev;
        }
        public Path<?> getIndexedPath(){ return path;}
        public Evaluator getEvaluator(){ return ev; }        
    }
    
}
