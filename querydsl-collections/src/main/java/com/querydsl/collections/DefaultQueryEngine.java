/*
 * Copyright 2011, Mysema Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.querydsl.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;
import com.mysema.codegen.Evaluator;
import com.mysema.commons.lang.IteratorAdapter;
import com.querydsl.core.JoinExpression;
import com.querydsl.core.JoinType;
import com.querydsl.core.QueryMetadata;
import com.querydsl.core.QueryModifiers;
import com.querydsl.core.types.ArrayConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Operator;
import com.querydsl.core.types.Ops;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;

/**
 * Default implementation of the {@link QueryEngine} interface
 *
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class DefaultQueryEngine implements QueryEngine {

    private static volatile QueryEngine DEFAULT;

    public static QueryEngine getDefault() {
        if (DEFAULT == null) {
            DEFAULT = new DefaultQueryEngine(new DefaultEvaluatorFactory(CollQueryTemplates.DEFAULT));
        }
        return DEFAULT;
    }

    private final DefaultEvaluatorFactory evaluatorFactory;

    public DefaultQueryEngine(DefaultEvaluatorFactory evaluatorFactory) {
        this.evaluatorFactory = evaluatorFactory;
    }

    @Override
    public long count(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables) {
        if (metadata.getJoins().size() == 1) {
            return evaluateSingleSource(metadata, iterables, true).size();
        } else {
            return evaluateMultipleSources(metadata, iterables, true).size();
        }
    }

    @Override
    public boolean exists(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables) {
        QueryModifiers modifiers = metadata.getModifiers();
        metadata.setLimit(1l);
        try {
            if (metadata.getJoins().size() == 1) {
                return !evaluateSingleSource(metadata, iterables, true).isEmpty();
            } else {
                return !evaluateMultipleSources(metadata, iterables, true).isEmpty();
            }
        } finally {
            metadata.setModifiers(modifiers);
        }
    }

    @Override
    public <T> List<T> list(QueryMetadata metadata, Map<Expression<?>, Iterable<?>> iterables,
            Expression<T> projection) {
        if (metadata.getJoins().size() == 1) {
            return evaluateSingleSource(metadata, iterables, false);
        } else {
            return evaluateMultipleSources(metadata, iterables, false);
        }
    }

    private <T> List<T> distinct(List<T> list) {
        List<T> rv = new ArrayList<T>(list.size());
        if (!list.isEmpty() && list.get(0) != null && list.get(0).getClass().isArray()) {
            Set set = new HashSet(list.size());
            for (T o : list) {
                if (set.add(ImmutableList.copyOf((Object[])o))) {
                    rv.add(o);
                }
            }
            return rv;
        } else {
            for (T o : list) {
                if (!rv.contains(o)) {
                    rv.add(o);
                }
            }
        }
        return rv;
    }

    private List evaluateMultipleSources(QueryMetadata metadata, Map<Expression<?>,
            Iterable<?>> iterables, boolean count) {
        // from where
        Evaluator<List<Object[]>> ev = evaluatorFactory.createEvaluator(metadata, metadata.getJoins(), metadata.getWhere());
        List<Iterable<?>> iterableList = new ArrayList<Iterable<?>>(metadata.getJoins().size());
        for (JoinExpression join : metadata.getJoins()) {
            if (join.getType() == JoinType.DEFAULT) {
                iterableList.add(iterables.get(join.getTarget()));
            }
        }
        List<?> list = ev.evaluate(iterableList.toArray());

        if (!count && !list.isEmpty()) {
            List<Expression<?>> sources = new ArrayList<Expression<?>>(metadata.getJoins().size());
            for (JoinExpression join : metadata.getJoins()) {
                if (join.getType() == JoinType.DEFAULT) {
                    sources.add(join.getTarget());
                } else {
                    Operation target = (Operation) join.getTarget();
                    sources.add(target.getArg(1));
                }
            }
            // ordered
            if (!metadata.getOrderBy().isEmpty()) {
                order(metadata, sources, list);
            }
            // projection
            list = project(metadata, sources, list);
            // limit + offset
            if (metadata.getModifiers().isRestricting()) {
                list = metadata.getModifiers().subList(list);
            }
            if (list.isEmpty()) {
                return list;
            }
        }

        // distinct
        if (metadata.isDistinct()) {
            list = distinct(list);
        }

        return list;
    }

    private List evaluateSingleSource(QueryMetadata metadata, Map<Expression<?>,
            Iterable<?>> iterables, boolean count) {
        final Expression<?> source = metadata.getJoins().get(0).getTarget();
        final List<Expression<?>> sources = Collections.<Expression<?>>singletonList(source);
        final Iterable<?> iterable = iterables.values().iterator().next();
        List<?> list;
        if (iterable instanceof List) {
            list = (List)iterable;
        } else {
            list = IteratorAdapter.asList(iterable.iterator());
        }

        // from & where
        if (metadata.getWhere() != null) {
            Evaluator<List<?>> evaluator = (Evaluator)evaluatorFactory
                    .createEvaluator(metadata, source, metadata.getWhere());
            list = evaluator.evaluate(list);
        }

        if (!count && !list.isEmpty()) {
            // ordered
            if (!metadata.getOrderBy().isEmpty()) {
                // clone list
                if (list == iterable) {
                    list = new ArrayList(list);
                }
                order(metadata, sources, list);
            }
            // projection
            if (metadata.getProjection().size() > 1 || !metadata.getProjection().get(0).equals(source)) {
                list = project(metadata, sources, list);
            }
            // limit + offset
            if (metadata.getModifiers().isRestricting()) {
                list = metadata.getModifiers().subList(list);
            }
            if (list.isEmpty()) {
                return list;
            }
        }

        // distinct
        if (metadata.isDistinct()) {
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
        Expression<?> projection = metadata.getProjection().get(0);
        Operator<?> aggregator = null;
        if (projection instanceof Operation && Ops.aggOps.contains(((Operation)projection).getOperator())) {
            Operation<?> aggregation = (Operation<?>)projection;
            aggregator = aggregation.getOperator();
            projection = aggregation.getArg(0);
        }
        Evaluator projectionEvaluator = evaluatorFactory.create(metadata, sources, projection);
        EvaluatorFunction transformer = new EvaluatorFunction(projectionEvaluator);
        List target = new ArrayList();
        Iterators.addAll(target, Iterators.transform(list.iterator(), transformer));
        if (aggregator != null) {
            return ImmutableList.of(CollQueryFunctions.aggregate(target, projection, aggregator));
        } else {
            return target;
        }
    }



}
