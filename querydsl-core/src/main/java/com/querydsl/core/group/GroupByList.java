/*
 * Copyright 2013, Mysema Ltd
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

import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.Projectable;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.QTuple;

/**
 * Provides aggregated results as a list
 *
 * @author tiwe
 *
 * @param <K>
 * @param <V>
 */
public class GroupByList<K, V> extends AbstractGroupByTransformer<K, List<V>> {

    GroupByList(Expression<K> key, Expression<?>... expressions) {
        super(key, expressions);
    }

    @Override
    public List<V> transform(Projectable projectable) {
        // create groups
        FactoryExpression<Tuple> expr = FactoryExpressionUtils.wrap(new QTuple(expressions));
        boolean hasGroups = false;
        for (Expression<?> e : expr.getArgs()) {
            hasGroups |= e instanceof GroupExpression;
        }
        if (hasGroups) {
            expr = withoutGroupExpressions(expr);
        }
        final CloseableIterator<Tuple> iter = projectable.iterate(expr);

        List<V> list = Lists.newArrayList();
        GroupImpl group = null;
        K groupId = null;
        while (iter.hasNext()) {
            Object[] row = iter.next().toArray();
            if (group == null) {
                group = new GroupImpl(groupExpressions, maps);
                groupId = (K) row[0];
            } else if (!Objects.equal(groupId, row[0])) {
                list.add(transform(group));
                group = new GroupImpl(groupExpressions, maps);
                groupId = (K) row[0];
            }
            group.add(row);
        }
        if (group != null) {
            list.add(transform(group));
        }
        iter.close();
        return list;
    }

    protected V transform(Group group) {
        return (V)group;
    }
}
