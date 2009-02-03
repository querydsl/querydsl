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
import com.mysema.query.JoinType;
import com.mysema.query.QueryBase;
import com.mysema.query.collections.comparators.MultiComparator;
import com.mysema.query.collections.iterators.FilteringIterator;
import com.mysema.query.collections.iterators.MultiIterator;
import com.mysema.query.collections.iterators.ProjectingIterator;
import com.mysema.query.grammar.JavaSerializer;
import com.mysema.query.grammar.Order;
import com.mysema.query.grammar.types.Expr;
import com.mysema.query.grammar.types.Path;
import com.mysema.query.grammar.types.Constructor.CArray;
import com.mysema.query.serialization.OperationPatterns;

/**
 * InnerQuery is used internally in ColQuery as the backend Query instance
 * 
 * @author tiwe
 * @version $Id$
 */
public class InnerQuery extends QueryBase<Object, InnerQuery> {

    private Map<Expr<?>, Iterable<?>> pathToIterable = new HashMap<Expr<?>, Iterable<?>>();

    private final OperationPatterns ops;

    InnerQuery(OperationPatterns ops) {
        if (ops == null) throw new IllegalArgumentException("ops was null");
        this.ops = ops;
    }

    public <A> InnerQuery alias(Path<A> path, Iterable<A> col) {
        pathToIterable.put((Expr<?>) path, col);
        return this;
    }
    
    public InnerQuery from(Path<?>... o) {
        for (Path<?> expr : o){
            joins.add(new JoinExpression<Object>(JoinType.DEFAULT, (Expr<?>) expr));
        }
        return this;
    }

    private <RT> Iterator<RT> createIterator(Expr<RT> projection) throws Exception {        
        // from
        List<Expr<?>> sources = new ArrayList<Expr<?>>();
        MultiIterator multiIt = new MultiIterator();
        
        for (JoinExpression<?> join : joins) {
            sources.add(join.getTarget());
            multiIt.add(pathToIterable.get(join.getTarget()));
        }              
        Iterator<?> it = multiIt.init();
        
        // TODO : joins

        // where
        if (where.self() != null) {
            ExpressionEvaluator ev = new JavaSerializer(ops).handle(
                    where.self()).createExpressionEvaluator(sources,
                    boolean.class);
            it = new FilteringIterator<Object>(it, ev);
        }
        
        // order
        if (!orderBy.isEmpty()){
            // create a projection for the order
            Expr<Object>[] orderByExpr = new Expr[orderBy.size()];
            boolean[] directions = new boolean[orderBy.size()];
            for (int i = 0; i < orderBy.size(); i++){
                orderByExpr[i] = (Expr<Object>)orderBy.get(i).target;
                directions[i] = orderBy.get(i).order == Order.ASC;
            }
            Expr<?> expr = new CArray<Object>(Object.class, orderByExpr);
            ExpressionEvaluator ev = new JavaSerializer(ops).handle(expr)
                .createExpressionEvaluator(sources, expr);
            
            // transform the iterator to list
            List<Object[]> itAsList = IteratorUtils.toList((Iterator<Object[]>)it);               
            Collections.sort(itAsList, new MultiComparator(ev, directions));
            it = itAsList.iterator();
        }
        
        // select
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
