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
package com.mysema.query.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionBase;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.FactoryExpressionUtils;
import com.mysema.query.types.Operation;
import com.mysema.query.types.Ops;
import com.mysema.query.types.QList;
import com.mysema.query.types.QTuple;
import com.mysema.query.types.Visitor;

/**
 * Groups results by the first expression.
 *
 * @author sasa
 * @author tiwe
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GroupBy<K, V> implements ResultTransformer<Map<K,V>> {

    private static final class FactoryExpressionAdapter<T> extends ExpressionBase<T> implements FactoryExpression<T> {
        private final FactoryExpression<T> expr;

        private final List<Expression<?>> args;

        private FactoryExpressionAdapter(FactoryExpression<T> expr, List<Expression<?>> args) {
            super(expr.getType());
            this.expr = expr;
            this.args = args;
        }

        @Override
        public <R, C> R accept(Visitor<R, C> v, C context) {
            return expr.accept(v, context);
        }

        @Override
        public List<Expression<?>> getArgs() {
            return args;
        }

        @Override
        public T newInstance(Object... args) {
            return expr.newInstance(args);
        }
    }

    /**
     * Create a new GroupByBuilder for the given key expression
     *
     * @param key
     * @return
     */
    public static <K> GroupByBuilder<K> groupBy(Expression<K> key) {
        return new GroupByBuilder<K>(key);
    }

    /**
     * Create a new GroupByBuilder for the given key expressions
     *
     * @param keys
     * @return
     */
    public static GroupByBuilder<List<?>> groupBy(Expression<?>... keys) {
        return new GroupByBuilder<List<?>>(new QList(keys));
    }

    /**
     * Create a new aggregating min expression
     *
     * @param expression
     * @return
     */
    public static <E extends Comparable<E>> AbstractGroupExpression<?,E> min(Expression<E> expression) {
        return new GMin<E>(expression);
    }

    /**
     * Create a new aggregating sum expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number & Comparable<E>> AbstractGroupExpression<?,E> sum(Expression<E> expression) {
        return new GSum<E>(expression);
    }

    /**
     * Create a new aggregating avg expression
     *
     * @param expression
     * @return
     */
    public static <E extends Number & Comparable<E>> AbstractGroupExpression<?,E> avg(Expression<E> expression) {
        return new GAvg<E>(expression);
    }

    /**
     * Create a new aggregating max expression
     *
     * @param expression
     * @return
     */
    public static <E extends Comparable<E>> AbstractGroupExpression<?,E> max(Expression<E> expression) {
        return new GMax<E>(expression);
    }

    /**
     * Create a new aggregating list expression
     *
     * @param expression
     * @return
     */
    public static <E> AbstractGroupExpression<?,List<E>> list(Expression<E> expression) {
        return new GList<E>(expression);
    }

    /**
     * Create a new aggregating set expression
     *
     * @param expression
     * @return
     */
    public static <E> AbstractGroupExpression<?,Set<E>> set(Expression<E> expression) {
        return new GSet<E>(expression);
    }

    /**
     * Create a new aggregating map expression
     *
     * @param key
     * @param value
     * @return
     */
    public static <K, V> Expression<Map<K, V>> map(Expression<K> key, Expression<V> value) {
        QPair<K,V> qPair = new QPair<K,V>(key, value);
        return new GMap<K,V>(qPair);
    }

    protected final List<GroupExpression<?, ?>> groupExpressions = new ArrayList<GroupExpression<?, ?>>();

    protected final List<QPair<?,?>> maps = new ArrayList<QPair<?,?>>();

    protected final Expression<?>[] expressions;

    GroupBy(Expression<K> key, Expression<?>... expressions) {
        List<Expression<?>> projection = new ArrayList<Expression<?>>(expressions.length);
        groupExpressions.add(new GOne<K>(key));
        projection.add(key);

        for (Expression<?> expr : expressions) {
            if (expr instanceof GroupExpression<?,?>) {
                GroupExpression<?,?> groupExpr = (GroupExpression<?,?>)expr;
                groupExpressions.add(groupExpr);
                Expression<?> colExpression = groupExpr.getExpression();
                if (colExpression instanceof Operation && ((Operation)colExpression).getOperator() == Ops.ALIAS) {
                    projection.add(((Operation)colExpression).getArg(0));
                } else {
                    projection.add(colExpression);
                }
                if (groupExpr instanceof GMap) {
                    maps.add((QPair<?, ?>) colExpression);
                }
            } else {
                groupExpressions.add(new GOne(expr));
                projection.add(expr);
            }
        }

        this.expressions = projection.toArray(new Expression[projection.size()]);
    }

    @Override
    public Map<K, V> transform(Projectable projectable) {
        Map<K, Group> groups = new LinkedHashMap<K, Group>();

        // create groups
        FactoryExpression<Tuple> expr = FactoryExpressionUtils.wrap(new QTuple(expressions));
        boolean hasGroups = false;
        for (Expression<?> e : expr.getArgs()) {
            hasGroups |= e instanceof GroupExpression;
        }
        if (hasGroups) {
            expr = withoutGroupExpressions(expr);
        }
        CloseableIterator<Tuple> iter = projectable.iterate(expr);
        try {
            while (iter.hasNext()) {
                Object[] row = iter.next().toArray();
                K groupId = (K) row[0];
                GroupImpl group = (GroupImpl)groups.get(groupId);
                if (group == null) {
                    group = new GroupImpl(groupExpressions, maps);
                    groups.put(groupId, group);
                }
                group.add(row);
            }
        } finally {
            iter.close();
        }

        // transform groups
        return transform(groups);

    }

    private FactoryExpression<Tuple> withoutGroupExpressions(final FactoryExpression<Tuple> expr) {
        List<Expression<?>> args = new ArrayList<Expression<?>>(expr.getArgs().size());
        for (Expression<?> arg : expr.getArgs()) {
            if (arg instanceof GroupExpression) {
                args.add(((GroupExpression)arg).getExpression());
            } else {
                args.add(arg);
            }
        }
        return new FactoryExpressionAdapter<Tuple>(expr, args);
    }

    protected Map<K, V> transform(Map<K, Group> groups) {
        return (Map<K,V>)groups;
    }

}
