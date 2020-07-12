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

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.querydsl.core.ReactiveFetchableQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.group.GroupImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.Projections;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * Provides aggregated results as a list
 *
 * @param <K>
 * @param <V>
 * @author mc_fish
 */
public class ReactiveGroupByList<K, V> extends ReactiveAbstractGroupByTransformer<K, V> {

    ReactiveGroupByList(Expression<K> key, Expression<?>... expressions) {
        super(key, expressions);
    }

    @Override
    public Flux<V> transform(ReactiveFetchableQuery<?, ?> query) {
        // create groups
        FactoryExpression<Tuple> expr = FactoryExpressionUtils.wrap(Projections.tuple(expressions));
        boolean hasGroups = false;
        for (Expression<?> e : expr.getArgs()) {
            hasGroups |= e instanceof GroupExpression;
        }
        if (hasGroups) {
            expr = withoutGroupExpressions(expr);
        }
        final Flux<Tuple> result = query.select(expr).fetch();

        return result
                .collectList()
                .flatMapMany(tuples -> {
                    List<V> list = Lists.newArrayList();
                    GroupImpl group = null;
                    K groupId = null;

                    for (Tuple tuple : tuples) {
                        K[] row = (K[]) tuple.toArray();
                        if (group == null) {
                            group = new GroupImpl(groupExpressions, maps);
                            groupId = row[0];
                        } else if (!Objects.equal(groupId, row[0])) {
                            list.add(transform(group));
                            group = new GroupImpl(groupExpressions, maps);
                            groupId = row[0];
                        }
                        group.add(row);
                    }
                    if (group != null) {
                        list.add(transform(group));
                    }

                    return Flux.fromIterable(list);
                });
    }

    @SuppressWarnings("unchecked")
    protected V transform(Group group) {
        return (V) group;
    }
}
