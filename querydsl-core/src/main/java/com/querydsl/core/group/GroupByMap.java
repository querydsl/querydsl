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
package com.querydsl.core.group;

import java.util.LinkedHashMap;
import java.util.Map;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.Projections;

/**
 * Provides aggregated results as a map
 *
 * @author tiwe
 *
 * @param <K>
 * @param <V>
 */
public class GroupByMap<K,V> extends AbstractGroupByTransformer<K, Map<K,V>> {

    GroupByMap(Expression<K> key, Expression<?>... expressions) {
        super(key, expressions);
    }

    @Override
    public Map<K, V> transform(FetchableQuery<?,?> query) {
        Map<K, Group> groups = new LinkedHashMap<K, Group>();

        // create groups
        FactoryExpression<Tuple> expr = FactoryExpressionUtils.wrap(Projections.tuple(expressions));
        boolean hasGroups = false;
        for (Expression<?> e : expr.getArgs()) {
            hasGroups |= e instanceof GroupExpression;
        }
        if (hasGroups) {
            expr = withoutGroupExpressions(expr);
        }
        try (CloseableIterator<Tuple> iter = query.select(expr).iterate()) {
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked") //This type is mandated by the key type
                K[] row = (K[]) iter.next().toArray();
                K groupId = row[0];
                GroupImpl group = (GroupImpl) groups.get(groupId);
                if (group == null) {
                    group = new GroupImpl(groupExpressions, maps);
                    groups.put(groupId, group);
                }
                group.add(row);
            }
        }

        // transform groups
        return transform(groups);

    }

    @SuppressWarnings("unchecked")
    protected Map<K, V> transform(Map<K, Group> groups) {
        return (Map<K,V>) groups;
    }

}
