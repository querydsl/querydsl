/*
 * Copyright 2020, The Querydsl Team (http://www.querydsl.com/team)
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
package com.querydsl.core.group.guava;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.querydsl.core.util.CloseableIterator;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.group.AbstractGroupByTransformer;
import com.querydsl.core.group.Group;
import com.querydsl.core.group.GroupExpression;
import com.querydsl.core.group.GroupImpl;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.util.ArrayUtils;

/**
 * Provides aggregated results as a table
 *
 * @author Jan-Willem Gmelig Meyling
 *
 * @param <R> row type
 * @param <C> column type
 * @param <V> value type
 * @param <RES> table type
 */
public class GroupByTable<R, C, V, RES extends Table<R, C, V>> extends AbstractGroupByTransformer<R, RES> {

    GroupByTable(Expression<R> rowKey, Expression<C> columnKey, Expression<?>... expressions) {
        super(rowKey, ArrayUtils.combine(Expression.class, columnKey, expressions));
    }

    @Override
    public RES transform(FetchableQuery<?,?> query) {
        // TODO Table<Object, Object, Group> after support for it in https://github.com/querydsl/querydsl/issues/2644
        Table<R, Object, Group> groups = HashBasedTable.create();

        // create groups
        FactoryExpression<Tuple> expr = FactoryExpressionUtils.wrap(Projections.tuple(expressions));
        boolean hasGroups = false;
        for (Expression<?> e : expr.getArgs()) {
            hasGroups |= e instanceof GroupExpression;
        }
        if (hasGroups) {
            expr = withoutGroupExpressions(expr);
        }
        CloseableIterator<Tuple> iter = query.select(expr).iterate();
        try {
            while (iter.hasNext()) {
                @SuppressWarnings("unchecked") //This type is mandated by the key type
                Object[] row = iter.next().toArray();
                R groupId = (R) row[0];
                Object rowId = row[1];
                GroupImpl group = (GroupImpl) groups.get(groupId, rowId);
                if (group == null) {
                    group = new GroupImpl(groupExpressions, maps);
                    groups.put(groupId, rowId, group);
                }
                group.add(row);
            }
        } finally {
            iter.close();
        }

        // transform groups
        return transform(groups);

    }

    @SuppressWarnings("unchecked")
    protected RES transform(Table<R, ?, Group> groups) {
        return (RES) groups;
    }

}
