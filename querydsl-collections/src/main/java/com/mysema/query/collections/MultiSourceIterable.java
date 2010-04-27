/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 * 
 */
package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.codegen.Evaluator;
import com.mysema.query.JoinExpression;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.EArrayConstructor;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.util.MultiIterator;

/**
 * @author tiwe
 *
 */
public class MultiSourceIterable<T> extends AbstractIterable<Object[],T>{

    private final Map<Expr<?>, Iterable<?>> iterableMap;
    
    private final List<Expr<?>> sources = new ArrayList<Expr<?>>();
    
    public MultiSourceIterable(QueryMetadata metadata, 
            ExprEvaluatorFactory evaluatorFactory,
            IteratorFactory iteratorFactory, 
            Map<Expr<?>, Iterable<?>> iterables,
            boolean forCount) {
        super(metadata, evaluatorFactory, iteratorFactory, forCount);
        this.iterableMap = iterables;
        for (JoinExpression join : metadata.getJoins()){
            sources.add(join.getTarget());
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<Object[]> initialIterator() {
        List<Iterable<?>> iterableList = new ArrayList<Iterable<?>>(sources.size());
        for (Expr<?> expr : sources){
            iterableList.add(iterableMap.get(expr));
        }
        Iterator<Object[]> it = new MultiIterator(iterableList);
        if (metadata.getWhere() != null) {
            it = iteratorFactory.multiArgFilter(it, sources, metadata.getWhere());
        }
        return it;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<Object[]> orderedIterator(Iterator<Object[]> it) {
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
        List<Object[]> list = IteratorUtils.toList((Iterator<Object[]>) it);
        Collections.sort(list, new MultiComparator(ev, directions));
        return list.iterator();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<T> projectedIterator(Iterator<Object[]> it) {
        if (!metadata.getProjection().isEmpty()){
            return (Iterator<T>) iteratorFactory.transform(it, sources, metadata.getProjection().get(0));
        }else{
            return (Iterator<T>) it;
        }
    }

}
