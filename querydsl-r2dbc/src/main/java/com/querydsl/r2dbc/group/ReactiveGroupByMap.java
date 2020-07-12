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

import com.querydsl.core.ReactiveFetchableQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.group.GroupImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.Projections;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides aggregated results as a map
 *
 * @param <K>
 * @param <V>
 * @author mc_fish
 */
@IgnoreJRERequirement
public class ReactiveGroupByMap<K, V> extends ReactiveAbstractGroupByTransformer<K, Map<K, V>> {

    ReactiveGroupByMap(Expression<K> key, Expression<?>... expressions) {
        super(key, expressions);
    }

    @Override
    public Mono<Map<K, V>> transform(ReactiveFetchableQuery<?, ?> query) {
        final Map<K, Group> groups = new LinkedHashMap<K, Group>();

        // create groups
        FactoryExpression<Tuple> expr = FactoryExpressionUtils.wrap(Projections.tuple(expressions));
        boolean hasGroups = false;
        for (Expression<?> e : expr.getArgs()) {
            hasGroups |= e instanceof GroupExpression;
        }
        if (hasGroups) {
            expr = withoutGroupExpressions(expr);
        }
        Flux<Tuple> result = query.select(expr).fetch();

        return result
                .collectList()
                .map(tupels -> {
                    tupels.forEach(tuple -> {
                        K[] row = (K[]) tuple.toArray();
                        K groupId = row[0];
                        GroupImpl group = (GroupImpl) groups.get(groupId);
                        if (group == null) {
                            group = new GroupImpl(groupExpressions, maps);
                            groups.put(groupId, group);
                        }
                        group.add(row);
                    });

                    return transform(groups);
                });
    }

    @SuppressWarnings("unchecked")
    protected Map<K, V> transform(Map<K, Group> groups) {
        return (Map<K, V>) groups;
    }

}
