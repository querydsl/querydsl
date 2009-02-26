/*
 * Copyright (c) 2008 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.*;

import org.apache.commons.collections15.IteratorUtils;
import org.codehaus.janino.ExpressionEvaluator;

import com.mysema.query.JoinExpression;
import com.mysema.query.QueryBase;
import com.mysema.query.collections.comparators.MultiComparator;
import com.mysema.query.collections.iterators.FilteringMultiIterator;
import com.mysema.query.collections.iterators.MultiArgFilteringIterator;
import com.mysema.query.collections.iterators.MultiIterator;
import com.mysema.query.collections.iterators.ProjectingIterator;
import com.mysema.query.collections.iterators.WrappingIterator;
import com.mysema.query.grammar.JavaSerializer;
import com.mysema.query.grammar.Order;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.serialization.OperationPatterns;

/**
 * InnerQuery is used internally in ColQuery as the backend Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
public class InnerQuery extends QueryBase<Object, InnerQuery> {

    private Map<Expr<?>, Iterable<?>> exprToIterable = new HashMap<Expr<?>, Iterable<?>>();

    private final OperationPatterns ops;

    public InnerQuery(OperationPatterns ops) {
        if (ops == null) throw new IllegalArgumentException("ops was null");
        this.ops = ops;
    }

    public <A> InnerQuery alias(Expr<A> path, Iterable<A> col) {
        exprToIterable.put(path, col);
        return this;
    }
    
    private <RT> Iterator<RT> createIterator(Expr<RT> projection) throws Exception {
        List<Expr<?>> sources = new ArrayList<Expr<?>>();
        // from  / where       
        Iterator<?> it = handleFromAndWhere(sources);
        
        // order
        if (!orderBy.isEmpty()) it = handleOrderBy(sources, it);
        
        // select
        return handleSelect(it, sources, projection);                   
    }
    

    /**
     * creates an iterator based on the given sources and filters it based on the constraints
     * of the where block
     * 
     * @param sources
     * @return
     */
    protected Iterator<?> handleFromAndWhere(List<Expr<?>> sources){
        // from
        Iterator<?> it = handleFrom(sources);
        
        // where
        if (where.create() != null) it = handleWhere(sources, it);
        return it;
    }
    
    /**
     * creates an Iterator that projects all the sources into a cartesian view
     * 
     * @param sources
     * @return
     */
    protected Iterator<?> handleFrom(List<Expr<?>> sources){          
        if (joins.size() == 1){
            JoinExpression<?> join = joins.get(0);
            sources.add(join.getTarget());
            return new WrappingIterator<Object[]>(exprToIterable.get(join.getTarget()).iterator()){
               public Object[] next() {
                   return new Object[]{nextFromOrig()};
               }               
           };
        }else{
            MultiIterator multiIt;
            if (where.create() == null){
                multiIt = new MultiIterator();
            }else{
                multiIt = new FilteringMultiIterator(ops, where.create());
            }        
            for (JoinExpression<?> join : joins) {
                sources.add(join.getTarget());
                multiIt.add(join.getTarget(), exprToIterable.get(join.getTarget()));
                switch(join.getType()){
                case JOIN :       
                case INNERJOIN :  // TODO
                case LEFTJOIN :   // TODO
                case FULLJOIN :   // TODO
                case DEFAULT :    // do nothing
                }
            }   
            return multiIt.init();
        }            
    }

    /**
     * creates a wrapped iterator that filters the the rows not matched by the where
     * constraints out
     * 
     * @param sources
     * @param it source iterator
     * @return 
     */
    protected Iterator<?> handleWhere(List<Expr<?>> sources, Iterator<?> it){
        try {
            ExpressionEvaluator ev = new JavaSerializer(ops).handle(
                    where.create()).createExpressionEvaluator(sources,
                    boolean.class);
            it = new MultiArgFilteringIterator<Object>(it, ev);
            return it;
        } catch (Exception e) {
            String error = "Caught " + e.getClass().getName();
            throw new RuntimeException(error, e);
        }        
    }

    /**
     * transforms the given iterator into a sorted view based on the ordering data
     * 
     * @param sources
     * @param it source iterator
     * @return
     * @throws Exception
     */
    protected Iterator<?> handleOrderBy(List<Expr<?>> sources, Iterator<?> it)
            throws Exception {
        // create a projection for the order
        Expr<Object>[] orderByExpr = new Expr[orderBy.size()];
        boolean[] directions = new boolean[orderBy.size()];
        for (int i = 0; i < orderBy.size(); i++){
            orderByExpr[i] = (Expr)orderBy.get(i).target;
            directions[i] = orderBy.get(i).order == Order.ASC;
        }
        Expr<?> expr = new Expr.EArrayConstructor<Object>(Object.class, orderByExpr);
        ExpressionEvaluator ev = new JavaSerializer(ops).handle(expr)
            .createExpressionEvaluator(sources, expr);
        
        // transform the iterator to list
        List<Object[]> itAsList = IteratorUtils.toList((Iterator<Object[]>)it);               
        Collections.sort(itAsList, new MultiComparator(ev, directions));
        it = itAsList.iterator();
        return it;
    }
    
    /**
     * create the final projecting iterator
     * 
     * @param <RT>
     * @param it source iterator
     * @param sources
     * @param projection
     * @return
     * @throws Exception
     */
    protected <RT> Iterator<RT> handleSelect(Iterator<?> it, List<Expr<?>> sources, Expr<RT> projection) throws Exception {
        ExpressionEvaluator ev = new JavaSerializer(ops).handle(projection)
            .createExpressionEvaluator(sources,projection);        
        return new ProjectingIterator<RT>(it, ev);
    }

    public <RT> Iterable<RT> iterate(final Expr<RT> projection) {
        select(projection);
        return new Iterable<RT>() {
            public Iterator<RT> iterator() {
                try {
                    return createIterator(projection);
                } catch (Exception e) {
                    throw new RuntimeException("error", e);
                }
            }
        };
    }
}
