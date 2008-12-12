/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.*;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryBase;
import com.mysema.query.collections.iterators.FilteringIterator;
import com.mysema.query.collections.iterators.MultiIterator;
import com.mysema.query.collections.iterators.ProjectingIterator;
import com.mysema.query.grammar.ColOps;
import com.mysema.query.grammar.JavaSerializer;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.ObjectArray;
import com.mysema.query.grammar.types.Expr.Entity;
import com.mysema.query.serialization.BaseOps;


/**
 * ColQuery is a Query implementation for querying on Java collections
 *
 * @author tiwe
 * @version $Id$
 */
public class ColQuery<S extends ColQuery<S>> extends QueryBase<Object,S>{
    
    private static final BaseOps OPS_DEFAULT = new ColOps();
    
    private Map<Expr<?>, Iterable<?>> entityToIterable = new HashMap<Expr<?>,Iterable<?>>();
    
    private final BaseOps ops;
    
    public ColQuery(){
        ops = OPS_DEFAULT;
    }
    
    public ColQuery(BaseOps ops){
        this.ops = ops;
    }
    
    public <RT> Iterator<RT> createIterator(Expr<RT> projection){
        // from
        List<Expr<?>> sources = new ArrayList<Expr<?>>();
        MultiIterator multiIt = new MultiIterator();
        for (JoinExpression<?> join : joins){
            sources.add(join.getTarget());
            multiIt.add(entityToIterable.get(join.getTarget()));
        }        
        Iterator<?> it = multiIt.init();

        // where              
        if (where.self() != null){ 
            try {
                ExpressionEvaluator ev1 = new JavaSerializer(ops).handle(where.self())
                    .createExpressionEvaluator(sources, boolean.class);
                it = new FilteringIterator<Object>(it, ev1);
            } catch (Exception e1) {
                String error1 = "Caught " + e1.getClass().getName();
                throw new RuntimeException(error1, e1);
            }    
        }
           
        // select
        if (sources.size() == 1 && sources.get(0).equals(projection)){
            // TODO : fix me
            return (Iterator<RT>)it;    
        }else{
            try {
                ExpressionEvaluator ev = new JavaSerializer(ops).handle(projection)
                    .createExpressionEvaluator(sources, projection);
                return new ProjectingIterator<RT>(it, ev);
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                throw new RuntimeException(error, e);
            }            
        }        
    }

    public <A> S from(Expr.Entity<A> entity, A first, A... rest){
        from(entity);
        List<A> list = new ArrayList<A>(rest.length +1);
        list.add(first);
        list.addAll(Arrays.asList(rest));
        entityToIterable.put(entity, list);
        return (S)this;
    }

    public <A> S from(Expr.Entity<A> entity, Iterable<A> col){
        from(entity);
        entityToIterable.put(entity, col);
        return (S)this;
    }

    public Iterable<Object[]> iterate(Expr<?> expr1, Expr<?> expr2, Expr<?>...rest){
        final Expr<?>[] expr = new Expr[rest.length +2];
        expr[0] = expr1;
        expr[1] = expr2;
        System.arraycopy(rest, 0, expr, 2, rest.length);
        return new Iterable<Object[]>(){
            public Iterator<Object[]> iterator() {
                return createIterator(new ObjectArray(expr));
            }            
        };               
    }   
    
    public <RT> Iterable<RT> iterate(final Expr<RT> projection){
        select(projection);
        return new Iterable<RT>(){
            public Iterator<RT> iterator() {
                return createIterator(projection);
            }            
        };               
    }  
    
    public S groupBy(Expr<?>... o) {
        throw new UnsupportedOperationException();
    }
        
    public S having(Expr.Boolean o) {
        throw new UnsupportedOperationException();
    }
    
    public S innerJoin(Entity<?> o) {
        throw new UnsupportedOperationException();
    }
    
    public S fullJoin(Entity<?> o) {
        throw new UnsupportedOperationException();
    }
    
}
