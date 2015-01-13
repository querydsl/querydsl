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
package com.querydsl.core.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.ResultTransformer;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;

/**
 * GroupByBuilder is a fluent builder for GroupBy transformer instances. This class is not to be used directly,
 * but via GroupBy.
 *
 * @author tiwe
 *
 * @param <K>
 */
public class GroupByBuilder<K> {

    private final Expression<K> key;

    /**
     * Create a new GroupByBuilder for the given key expression
     *
     * @param key
     */
    public GroupByBuilder(Expression<K> key) {
        this.key = key;
    }

    /**
     * Get the results as a map
     *
     * @param expressions
     * @return
     */
    public ResultTransformer<Map<K, Group>> as(Expression<?>... expressions) {
        return new GroupByMap<K, Group>(key, expressions);
    }

    /**
     * Get the results as a closeable iterator
     *
     * @param expressions
     * @return
     */
    public ResultTransformer<CloseableIterator<Group>> iterate(Expression<?>... expressions) {
        return new GroupByIterate<K, Group>(key, expressions);
    }

    /**
     * Get the results as a list
     *
     * @param expressions
     * @return
     */
    public ResultTransformer<List<Group>> list(Expression<?>... expressions) {
        return new GroupByList<K, Group>(key, expressions);
    }

    /**
     * Get the results as a map
     *
     * @param expression
     * @return
     */
    @SuppressWarnings("unchecked")
    public <V> ResultTransformer<Map<K, V>> as(Expression<V> expression) {
        final Expression<V> lookup = getLookup(expression);
        return new GroupByMap<K, V>(key, expression) {
            @Override
            protected Map<K, V> transform(Map<K, Group> groups) {
                Map<K, V> results = new LinkedHashMap<K, V>((int) Math.ceil(groups.size()/0.75), 0.75f);
                for (Map.Entry<K, Group> entry : groups.entrySet()) {
                    results.put(entry.getKey(), entry.getValue().getOne(lookup));
                }
                return results;
            }
        };
    }

    /**
     * Get the results as a closeable iterator
     *
     * @param expression
     * @return
     */
    public <V> ResultTransformer<CloseableIterator<V>> iterate(Expression<V> expression) {
        final Expression<V> lookup = getLookup(expression);
        return new GroupByIterate<K, V>(key, expression) {
            @Override
            protected V transform(Group group) {
                return group.getOne(lookup);
            }
        };
    }

    /**
     * Get the results as a list
     *
     * @param expression
     * @return
     */
    public <V> ResultTransformer<List<V>> list(Expression<V> expression) {
        final Expression<V> lookup = getLookup(expression);
        return new GroupByList<K, V>(key, expression) {
            @Override
            protected V transform(Group group) {
                return group.getOne(lookup);
            }
        };
    }

    private <V> Expression<V> getLookup(Expression<V> expression) {
        if (expression instanceof GroupExpression) {
            return ((GroupExpression)expression).getExpression();
        } else {
            return expression;
        }
    }

    /**
     * Get the results as a map
     *
     * @param expression
     * @return
     */
    public <V> ResultTransformer<Map<K, V>> as(FactoryExpression<V> expression) {
        final FactoryExpression<?> transformation = FactoryExpressionUtils.wrap(expression);
        List<Expression<?>> args = transformation.getArgs();
        return new GroupByMap<K, V>(key, args.toArray(new Expression<?>[args.size()])) {

            @Override
            protected Map<K, V> transform(Map<K, Group> groups) {
                Map<K, V> results = new LinkedHashMap<K, V>((int) Math.ceil(groups.size()/0.75), 0.75f);
                for (Map.Entry<K, Group> entry : groups.entrySet()) {
                    results.put(entry.getKey(), transform(entry.getValue()));
                }
                return results;
            }

            @SuppressWarnings("unchecked")
            protected V transform(Group group) {
                // XXX Isn't group.toArray() suitable here?
                List<Object> args = new ArrayList<Object>(groupExpressions.size() - 1);
                for (int i = 1; i < groupExpressions.size(); i++) {
                    args.add(group.getGroup(groupExpressions.get(i)));
                }
                return (V)transformation.newInstance(args.toArray());
            }

        };
    }

    /**
     * Get the results as a closeable iterator
     *
     * @param expression
     * @return
     */
    public <V> ResultTransformer<CloseableIterator<V>> iterate(FactoryExpression<V> expression) {
        final FactoryExpression<?> transformation = FactoryExpressionUtils.wrap(expression);
        List<Expression<?>> args = transformation.getArgs();
        return new GroupByIterate<K, V>(key, args.toArray(new Expression<?>[args.size()])) {
            @Override
            protected V transform(Group group) {
                // XXX Isn't group.toArray() suitable here?
                List<Object> args = new ArrayList<Object>(groupExpressions.size() - 1);
                for (int i = 1; i < groupExpressions.size(); i++) {
                    args.add(group.getGroup(groupExpressions.get(i)));
                }
                return (V)transformation.newInstance(args.toArray());
            }
        };
    }

    /**
     * Get the results as a list
     *
     * @param expression
     * @return
     */
    public <V> ResultTransformer<List<V>> list(FactoryExpression<V> expression) {
        final FactoryExpression<?> transformation = FactoryExpressionUtils.wrap(expression);
        List<Expression<?>> args = transformation.getArgs();
        return new GroupByList<K, V>(key, args.toArray(new Expression<?>[args.size()])) {
            @Override
            protected V transform(Group group) {
                // XXX Isn't group.toArray() suitable here?
                List<Object> args = new ArrayList<Object>(groupExpressions.size() - 1);
                for (int i = 1; i < groupExpressions.size(); i++) {
                    args.add(group.getGroup(groupExpressions.get(i)));
                }
                return (V)transformation.newInstance(args.toArray());
            }
        };
    }


}
