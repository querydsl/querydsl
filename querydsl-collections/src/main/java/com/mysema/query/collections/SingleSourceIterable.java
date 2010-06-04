/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.codegen.Evaluator;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EArrayConstructor;

/**
 * @author tiwe
 *
 * @param <T>
 */
public class SingleSourceIterable<I,T> extends AbstractIterable<I,T> {
    
    private final Iterable<I> iterable;
    
    private final List<Expr<?>> sources;

    @SuppressWarnings("unchecked")
    public SingleSourceIterable(QueryMetadata metadata, 
            ExprEvaluatorFactory evaluatorFactory,
            IteratorFactory iteratorFactory, 
            Map<Expr<?>, Iterable<?>> iterables,
            boolean forCount) {
        super(metadata, evaluatorFactory, iteratorFactory, forCount);
        this.sources = Collections.<Expr<?>>singletonList(metadata.getJoins().get(0).getTarget());
        this.iterable = (Iterable<I>) iterables.get(sources.get(0));
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<I> initialIterator() {
        if (metadata.getWhere() != null){
            Evaluator ev = evaluatorFactory.createEvaluator(sources.get(0), metadata.getWhere());
            return ((Iterable)ev.evaluate(iterable)).iterator();
        }else{
            return iterable.iterator();
        }        
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<I> orderedIterator(Iterator<I> it) {
        // create a projection for the order
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        Expr<Object>[] orderByExpr = new Expr[orderBy.size()];
        boolean[] directions = new boolean[orderBy.size()];
        for (int i = 0; i < orderBy.size(); i++) {
            orderByExpr[i] = (Expr) orderBy.get(i).getTarget();
            directions[i] = orderBy.get(i).getOrder() == Order.ASC;
        }
        Expr<?> expr = new EArrayConstructor<Object>(Object[].class, orderByExpr);
        Evaluator ev = evaluatorFactory.create(sources, expr);

        // transform the iterator to list
        List<I> list = IteratorUtils.toList(it);
        Collections.sort(list, new MultiComparator(ev, directions));
        return list.iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<T> projectedIterator(Iterator<I> it) {
        if (!metadata.getProjection().isEmpty()){
            return (Iterator<T>) iteratorFactory.transform(it, sources, metadata.getProjection().get(0));    
        }else{
            return (Iterator<T>) it;
        }
        
    }

}
