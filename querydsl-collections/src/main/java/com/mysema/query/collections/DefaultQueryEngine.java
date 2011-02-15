/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.IteratorUtils;

import com.mysema.codegen.Evaluator;
import com.mysema.commons.lang.IteratorAdapter;
import com.mysema.query.JoinExpression;
import com.mysema.query.JoinType;
import com.mysema.query.QueryMetadata;
import com.mysema.query.QueryModifiers;
import com.mysema.query.types.ArrayConstructorExpression;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Order;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.util.MultiComparator;

/**
 * Default implementation of the QueryEngine interface
 *
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class DefaultQueryEngine implements QueryEngine {

    private final DefaultEvaluatorFactory evaluatorFactory;

    public DefaultQueryEngine(DefaultEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    @Override
    public long count(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables){
        if (metadata.getJoins().size() == 1){
            return evaluateSingleSource(metadata, iterables, true).size();
        }else{
            return evaluateMultipleSources(metadata, iterables, true).size();
        }
    }

    @Override
    public boolean exists(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables) {
        QueryModifiers modifiers = metadata.getModifiers();
        metadata.setLimit(1l);
        try{
            if (metadata.getJoins().size() == 1){
                return !evaluateSingleSource(metadata, iterables, true).isEmpty();
            }else{
                return !evaluateMultipleSources(metadata, iterables, true).isEmpty();
            }
        }finally{
            metadata.setModifiers(modifiers);
        }
    }

    @Override
    public <T> List<T> list(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables, Expression<T> projection){
        if (metadata.getJoins().size() == 1){
            return evaluateSingleSource(metadata, iterables, false);
        }else{
            return evaluateMultipleSources(metadata, iterables, false);
        }
    }

    private <T> List<T> distinct(List<T> list) {
        List<T> rv = new ArrayList<T>(list.size());
        if (!list.isEmpty() && list.get(0).getClass().isArray()){
            Set set = new HashSet(list.size());
            for (T o : list){
                if (set.add(Arrays.asList((Object[])o))){
                    rv.add(o);
                }
            }
            return rv;
        }else{
            for (T o : list){
                if (!rv.contains(o)){
                    rv.add(o);
                }
            }
        }
        return rv;
    }

    private List evaluateMultipleSources(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables, boolean count) {
        // from where
        Evaluator<List<Object[]>> ev = evaluatorFactory.createEvaluator(metadata, metadata.getJoins(), metadata.getWhere());
        List<Iterable<?>> iterableList = new ArrayList<Iterable<?>>(metadata.getJoins().size());
        for (JoinExpression join : metadata.getJoins()){
            if (join.getType() == JoinType.DEFAULT){
                iterableList.add(iterables.get(join.getTarget()));
            }
        }
        List<?> list = ev.evaluate(iterableList.toArray());

        if (!count && !list.isEmpty()){
            List<Expression<?>> sources = new ArrayList<Expression<?>>();
            for (JoinExpression join : metadata.getJoins()){
                if (join.getType() == JoinType.DEFAULT){
                    sources.add(join.getTarget());
                }else{
                    Operation target = (Operation) join.getTarget();
                   sources.add(target.getArg(1));
                }
            }
            // ordered
            if (!metadata.getOrderBy().isEmpty()){
                order(metadata, sources, list);
            }
            // limit + offset
            if (metadata.getModifiers().isRestricting()){
                list = metadata.getModifiers().subList(list);
            }
            if (list.isEmpty()){
                return list;
            }
            // projection
            list = project(metadata, sources, list);
        }

        // distinct
        if (metadata.isDistinct()){
            list = distinct(list);
        }

        return list;
    }

    private List evaluateSingleSource(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables, boolean count) {
        Expression<?> source = metadata.getJoins().get(0).getTarget();
        List<Expression<?>> sources = Collections.<Expression<?>>singletonList(source);
        Iterable<?> iterable = iterables.values().iterator().next();
        List<?> list;
        if (iterable instanceof List){
            list = (List)iterable;
        }else{
            list = IteratorAdapter.asList(iterable.iterator());
        }

        // from & where
        if (metadata.getWhere() != null){
            Evaluator<List<?>> evaluator = (Evaluator)evaluatorFactory.createEvaluator(metadata, source, metadata.getWhere());
            list = evaluator.evaluate(list);
        }

        if (!count && !list.isEmpty()){
            // ordered
            if (!metadata.getOrderBy().isEmpty()){
                // clone list
                if (list == iterable){
                    list = new ArrayList(list);
                }
                order(metadata, sources, list);
            }
            // limit + offset
            if (metadata.getModifiers().isRestricting()){
                list = metadata.getModifiers().subList(list);
            }
            if (list.isEmpty()){
                return list;
            }
            // projection
            if (metadata.getProjection().size() > 1 || !metadata.getProjection().get(0).equals(source)){
                list = project(metadata, sources, list);
            }
        }

        // distinct
        if (metadata.isDistinct()){
            list = distinct(list);
        }

        return list;

    }

    private void order(QueryMetadata metadata, List<Expression<?>> sources, List<?> list) {
        // create a projection for the order
        List<OrderSpecifier<?>> orderBy = metadata.getOrderBy();
        Expression<Object>[] orderByExpr = new Expression[orderBy.size()];
        boolean[] directions = new boolean[orderBy.size()];
        for (int i = 0; i < orderBy.size(); i++) {
            orderByExpr[i] = (Expression) orderBy.get(i).getTarget();
            directions[i] = orderBy.get(i).getOrder() == Order.ASC;
        }
        Expression<?> expr = new ArrayConstructorExpression<Object>(Object[].class, orderByExpr);
        Evaluator orderEvaluator = evaluatorFactory.create(metadata, sources, expr);
        Collections.sort(list, new MultiComparator(orderEvaluator, directions));
    }

    private List<?> project(QueryMetadata metadata, List<Expression<?>> sources, List<?> list) {
        Evaluator projectionEvaluator = evaluatorFactory.create(metadata, sources, metadata.getProjection().get(0));
        EvaluatorTransformer transformer = new EvaluatorTransformer(projectionEvaluator);
        list = IteratorUtils.toList(IteratorUtils.transformedIterator(list.iterator(), transformer));
        return list;
    }



}
