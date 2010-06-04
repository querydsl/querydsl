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
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.types.Expr;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.EArrayConstructor;

/**
 * @author tiwe
 *
 */
public class MultiSourceIterable<T> extends AbstractIterable<Object[],T>{

    private final Map<Expr<?>, Iterable<?>> iterableMap;
    
    private final List<Expr<?>> sources = new ArrayList<Expr<?>>();
    
    @SuppressWarnings("unchecked")
    public MultiSourceIterable(QueryMetadata metadata, 
            ExprEvaluatorFactory evaluatorFactory,
            IteratorFactory iteratorFactory, 
            Map<Expr<?>, Iterable<?>> iterables,
            boolean forCount) {
        super(metadata, evaluatorFactory, iteratorFactory, forCount);
        this.iterableMap = iterables;
        for (JoinExpression join : metadata.getJoins()){
            if (join.getType() == JoinType.DEFAULT){
                sources.add(join.getTarget());    
            }else{
                Operation target = (Operation) join.getTarget();
                sources.add(target.getArg(1));
            }
            
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Iterator<Object[]> initialIterator() {
        Evaluator ev = evaluatorFactory.createEvaluator(metadata.getJoins(), metadata.getWhere());
        List<Iterable<?>> iterableList = new ArrayList<Iterable<?>>(metadata.getJoins().size());
        for (JoinExpression join : metadata.getJoins()){
            if (join.getType() == JoinType.DEFAULT){
                iterableList.add(iterableMap.get(join.getTarget()));
            }
        }
        return ((Iterable)ev.evaluate(iterableList.toArray())).iterator();
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
