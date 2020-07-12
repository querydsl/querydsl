/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.r2dbc.group;

import com.querydsl.core.ReactiveResultTransformer;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * GroupByBuilder is a fluent builder for GroupBy transformer instances. This class is not to be used directly,
 * but via GroupBy.
 *
 * @param <K>
 * @author mc_fish
 */
public class ReactiveGroupByBuilder<K> {

    private final Expression<K> key;

    /**
     * Create a new GroupByBuilder for the given key expression
     *
     * @param key key for aggregating
     */
    public ReactiveGroupByBuilder(Expression<K> key) {
        this.key = key;
    }

    /**
     * Get the results as a map
     *
     * @param expressions projection
     * @return new result transformer
     */
    public ReactiveResultTransformer<Map<K, Group>> as(Expression<?>... expressions) {
        return new ReactiveGroupByMap<K, Group>(key, expressions);
    }

    /**
     * Get the results as a list
     *
     * @param expressions projection
     * @return new result transformer
     */
    public ReactiveResultTransformer<Group> flux(Expression<?>... expressions) {
        return new ReactiveGroupByList<K, Group>(key, expressions);
    }

    /**
     * Get the results as a map
     *
     * @param expression projection
     * @return new result transformer
     */
    @SuppressWarnings("unchecked")
    public <V> ReactiveResultTransformer<Map<K, V>> as(Expression<V> expression) {
        final Expression<V> lookup = getLookup(expression);
        return new ReactiveGroupByMap<K, V>(key, expression) {
            @Override
            protected Map<K, V> transform(Map<K, Group> groups) {
                Map<K, V> results = new LinkedHashMap<K, V>((int) Math.ceil(groups.size() / 0.75), 0.75f);
                for (Map.Entry<K, Group> entry : groups.entrySet()) {
                    results.put(entry.getKey(), entry.getValue().getOne(lookup));
                }
                return results;
            }
        };
    }

    /**
     * Get the results as a list
     *
     * @param expression projection
     * @return new result transformer
     */
    public <V> ReactiveResultTransformer<V> flux(Expression<V> expression) {
        final Expression<V> lookup = getLookup(expression);
        return new ReactiveGroupByList<K, V>(key, expression) {
            @Override
            protected V transform(Group group) {
                return group.getOne(lookup);
            }
        };
    }

    private <V> Expression<V> getLookup(Expression<V> expression) {
        if (expression instanceof GroupExpression) {
            @SuppressWarnings("unchecked") // This is the underlying type
                    GroupExpression<V, ?> groupExpression = (GroupExpression<V, ?>) expression;
            return groupExpression.getExpression();
        } else {
            return expression;
        }
    }

    /**
     * Get the results as a map
     *
     * @param expression projection
     * @return new result transformer
     */
    public <V> ReactiveResultTransformer<Map<K, V>> as(FactoryExpression<V> expression) {
        final FactoryExpression<?> transformation = FactoryExpressionUtils.wrap(expression);
        List<Expression<?>> args = transformation.getArgs();
        return new ReactiveGroupByMap<K, V>(key, args.toArray(new Expression<?>[args.size()])) {

            @Override
            protected Map<K, V> transform(Map<K, Group> groups) {
                Map<K, V> results = new LinkedHashMap<K, V>((int) Math.ceil(groups.size() / 0.75), 0.75f);
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
                return (V) transformation.newInstance(args.toArray());
            }

        };
    }

    /**
     * Get the results as a list
     *
     * @param expression projection
     * @return new result transformer
     */
    public <V> ReactiveResultTransformer<V> flux(FactoryExpression<V> expression) {
        final FactoryExpression<V> transformation = FactoryExpressionUtils.wrap(expression);
        List<Expression<?>> args = transformation.getArgs();
        return new ReactiveGroupByList<K, V>(key, args.toArray(new Expression<?>[args.size()])) {
            @Override
            protected V transform(Group group) {
                // XXX Isn't group.toArray() suitable here?
                List<Object> args = new ArrayList<Object>(groupExpressions.size() - 1);
                for (int i = 1; i < groupExpressions.size(); i++) {
                    args.add(group.getGroup(groupExpressions.get(i)));
                }
                return transformation.newInstance(args.toArray());
            }
        };
    }


}
