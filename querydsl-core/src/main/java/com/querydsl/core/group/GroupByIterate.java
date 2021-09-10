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

import java.util.NoSuchElementException;
import java.util.Objects;

import com.mysema.commons.lang.CloseableIterator;
import com.querydsl.core.FetchableQuery;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.FactoryExpression;
import com.querydsl.core.types.FactoryExpressionUtils;
import com.querydsl.core.types.Projections;

/**
 * Provides aggregated results as an iterator
 *
 * @author tiwe
 *
 * @param <K>
 * @param <V>
 */
public class GroupByIterate<K, V> extends AbstractGroupByTransformer<K, CloseableIterator<V>> {

    GroupByIterate(Expression<K> key, Expression<?>... expressions) {
        super(key, expressions);
    }

    @Override
    public CloseableIterator<V> transform(FetchableQuery<?,?> query) {
        // create groups
        FactoryExpression<Tuple> expr = FactoryExpressionUtils.wrap(Projections.tuple(expressions));
        boolean hasGroups = false;
        for (Expression<?> e : expr.getArgs()) {
            hasGroups |= e instanceof GroupExpression;
        }
        if (hasGroups) {
            expr = withoutGroupExpressions(expr);
        }
        final CloseableIterator<Tuple> iter = query.select(expr).iterate();

        return new CloseableIterator<V>() {

            private GroupImpl group;

            private K groupId;

            @Override
            public boolean hasNext() {
                return group != null || iter.hasNext();
            }

            @Override
            public V next() {
                if (!iter.hasNext()) {
                    if (group != null) {
                        Group current = group;
                        group = null;
                        return transform(current);
                    } else {
                        throw new NoSuchElementException();
                    }
                }

                while (iter.hasNext()) {
                    @SuppressWarnings("unchecked") //This type is mandated by the key type
                    K[] row = (K[]) iter.next().toArray();
                    if (group == null) {
                        group = new GroupImpl(groupExpressions, maps);
                        groupId = row[0];
                        group.add(row);
                    } else if (Objects.equals(groupId, row[0])) {
                        group.add(row);
                    } else {
                        Group current = group;
                        group = new GroupImpl(groupExpressions, maps);
                        groupId = row[0];
                        group.add(row);
                        return transform(current);
                    }
                }
                Group current = group;
                group = null;
                return transform(current);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void close() {
                iter.close();
            }

        };
    }

    @SuppressWarnings("unchecked")
    protected V transform(Group group) {
        return (V) group;
    }
}
