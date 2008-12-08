/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.*;

import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.QueryBase;
import com.mysema.query.collections.internal.ColOps;
import com.mysema.query.collections.internal.JavaSerializer;
import com.mysema.query.collections.internal.Iterators.FilteringIterator;
import com.mysema.query.collections.internal.Iterators.ProjectingIterator;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.serialization.BaseOps;


/**
 * ColQuery is a Query implementation for querying on Java collections
 *
 * @author tiwe
 * @version $Id$
 */
public class ColQuery<S extends ColQuery<S>> extends QueryBase<Object,S>{
    
    private static final BaseOps OPS_DEFAULT = new ColOps();
    
    private final BaseOps ops;
    
    public ColQuery(){
        ops = OPS_DEFAULT;
    }
    
    public ColQuery(BaseOps ops){
        this.ops = ops;
    }
    
    private Map<Expr<?>, Iterable<?>> entityToIterable = new HashMap<Expr<?>,Iterable<?>>();
    
    public <RT> Iterator<RT> createIterator(Expr<RT> projection){
        // from 
        Expr<?> source = joins.get(0).getTarget();
        Iterator<?> it = null;
        if (joins.size() == 1){
            it = entityToIterable.get(source).iterator();
        }else{
            // TODO
        }
        
        // order by           
        if (!orderBy.isEmpty()){
            // TODO            
        }
        
        // where                   
        if (where.self() != null){            
            try {
                ExpressionEvaluator ev = new JavaSerializer(ops).handle(where.self())
                    .createExpressionEvaluator(source, boolean.class);
                it = new FilteringIterator<RT>(it, ev);
            } catch (Exception e) {
                String error = "Caught " + e.getClass().getName();
                throw new RuntimeException(error, e);
            }
        }
        
        // group by     NOT SUPPORTED        
        // having       NOT SUPPORTED
        
        // select
        if (source.equals(projection)){
            return (Iterator<RT>)it;    
        }else{
            try {
                ExpressionEvaluator ev = new JavaSerializer(ops).handle(projection)
                    .createExpressionEvaluator(source, projection);
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
    
    public <RT> Iterable<RT> iterate(final Expr<RT> projection){
        select(projection);
        return new Iterable<RT>(){
            public Iterator<RT> iterator() {
                return createIterator(projection);
            }            
        };               
    }   
    
    
}
