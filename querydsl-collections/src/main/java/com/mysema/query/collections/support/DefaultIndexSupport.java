/*
 * Copyright (c) 2009 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections.support;

import java.util.*;

import com.mysema.query.collections.IndexSupport;
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
import com.mysema.query.util.Assert;

/**
 * DefaultIndexSupport provides
 *
 * @author tiwe
 * @version $Id$
 */
public class DefaultIndexSupport implements IndexSupport{
    
    private JavaOps ops;
    
    private List<? extends Expr<?>> sources;
    
    private Map<Expr<?>,Iterable<?>> exprToIt;
    
    Map<Path<?>,Map<?,? extends Iterable<?>>> pathEqExprIndex;
    
    Map<Path<?>,Iterable<?>> pathEqConstantIndex;
    
    Map<Path<?>,Evaluator> pathToIndexKey;
    
    private long fullSize = 0l;
    
    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr) {
        return (Iterator<A>)exprToIt.get(expr).iterator();
    }

    @SuppressWarnings("unchecked")
    public <A> Iterator<A> getIterator(Expr<A> expr, Object[] bindings) {
        // get filtered by constant
        if (pathEqConstantIndex.containsKey(expr)){
            return (Iterator<A>) pathEqConstantIndex.get(expr).iterator();
            
        // get filtered by expression
        }else if (pathToIndexKey.containsKey(expr)){
            Evaluator ev = pathToIndexKey.get(expr);
            Map<?,? extends Iterable<?>> indexEntry = pathEqExprIndex.get(expr);
            Object key = ev.evaluate(bindings);
            if (indexEntry.containsKey(key)){
                return (Iterator<A>)indexEntry.get(key).iterator();    
            }else{
                return Collections.<A>emptyList().iterator();
            }            
        }else{
            return (Iterator<A>)exprToIt.get(expr).iterator();    
        }        
    }

    public void init(Map<Expr<?>,Iterable<?>> exprToIt, JavaOps ops, List<? extends Expr<?>> sources, EBoolean condition) {
        this.exprToIt = exprToIt;
        this.ops = Assert.notNull(ops);
        this.sources = Assert.notNull(sources);        
        this.exprToIt = Assert.notNull(exprToIt);  
        this.pathEqExprIndex = new HashMap<Path<?>,Map<?,? extends Iterable<?>>>();
        this.pathEqConstantIndex = new HashMap<Path<?>,Iterable<?>>();
        this.pathToIndexKey = new HashMap<Path<?>,Evaluator>();
        
        for (Iterable<?> value : exprToIt.values()){
            if (value instanceof Collection) fullSize += ((Collection<?>)value).size();
        }
        
        // populate the "path eq path" index
        if (condition instanceof Operation){
            visitOperation((Operation<?,?>) condition);
        }
        
    }

    private void visitOperation(Operation<?,?> op) {
        if (op.getOperator() == Ops.EQ_OBJECT  || op.getOperator() == Ops.EQ_PRIMITIVE){
            Expr<?> e1 = op.getArgs()[0];
            Expr<?> e2 = op.getArgs()[1];
            if (e1 instanceof Path && e2 instanceof Path){
                Path<?> p1 = (Path<?>)e1, p2 = (Path<?>)e2;
                int i1 = sources.indexOf(p1.getRoot());
                int i2 = sources.indexOf(p2.getRoot());
                if (i1 < i2){
                    indexPathEqExpr(p2, e1);
                }else if (i1 > i2){
                    indexPathEqExpr(p1, e2);
                }
                
            }else if (e1 instanceof Path && sources.indexOf(e1) > 0){
                if (e2 instanceof EConstant){
                    indexPathEqConstant((Path<?>)e1, op);
                }else if (e2 instanceof Operation){
                    // TODO : validate that this works properly
                    indexPathEqExpr((Path<?>)e1, e2);
                }
            }else if (e2 instanceof Path && sources.indexOf(e2) > 0){
                if (e2 instanceof EConstant){
                    indexPathEqConstant((Path<?>)e2, op);
                }else{
                    // TODO : validate that this works properly
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
    
    private void indexPathEqConstant(Path<?> path, Operation<?,?> op){
        if (!pathEqConstantIndex.containsKey(path.getRoot())){
            // create the index entry
            Evaluator ev = EvaluatorUtils.create(ops, Collections.<Expr<?>>singletonList((Expr<?>)path.getRoot()), (Expr<?>)op);
            Iterable<?> it = QueryIteratorUtils.singleArgFilter(exprToIt.get(path.getRoot()), ev);
            
            // update the index
            pathEqConstantIndex.put(path.getRoot(), it);
        }
    }
    
    private void indexPathEqExpr(Path<?> path, Expr<?> key) {
        if (!pathEqExprIndex.containsKey(path.getRoot())){
            // create the index entry
            Evaluator ev = EvaluatorUtils.create(ops, Collections.<Expr<?>>singletonList((Expr<?>)path.getRoot()), (Expr<?>)path);
            Map<?,? extends Iterable<?>> map = QueryIteratorUtils.projectToMap(exprToIt.get(path.getRoot()), ev);
            
            // create the key creator            
            Evaluator keyCreator = EvaluatorUtils.create(ops, sources, key);
            
            // update the index
            pathEqExprIndex.put(path.getRoot(), map);   
            pathToIndexKey.put(path.getRoot(), keyCreator);            
        }
    }
    
}
