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
import java.util.NoSuchElementException;
import java.util.Set;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Operation;
import com.querydsl.core.types.Ops;

/**
 * Default implementation of the Group interface
 *
 * @author sasa
 * @author tiwe
 *
 */
class GroupImpl implements Group {

    private final Map<Expression<?>, GroupCollector<?,?>> groupCollectorMap = new LinkedHashMap<Expression<?>, GroupCollector<?,?>>();

    private final List<GroupExpression<?, ?>> groupExpressions;

    private final List<GroupCollector<?,?>> groupCollectors = new ArrayList<GroupCollector<?,?>>();

    private final List<QPair<?, ?>> maps;

    public GroupImpl(List<GroupExpression<?, ?>> columnDefinitions,  List<QPair<?, ?>> maps) {
        this.groupExpressions = columnDefinitions;
        this.maps = maps;
        for (int i=0; i < columnDefinitions.size(); i++) {
            GroupExpression<?, ?> coldef = columnDefinitions.get(i);
            GroupCollector<?,?> collector = groupCollectorMap.get(coldef.getExpression());
            if (collector == null) {
                collector = coldef.createGroupCollector();
                Expression<?> coldefExpr = coldef.getExpression();
                groupCollectorMap.put(coldefExpr, collector);
                if (coldefExpr instanceof Operation && ((Operation)coldefExpr).getOperator() == Ops.ALIAS) {
                    groupCollectorMap.put(((Operation)coldefExpr).getArg(1), collector);
                }
            }
            groupCollectors.add(collector);
        }
    }

    @SuppressWarnings("unchecked")
    void add(Object[] row) {
        int i=0;
        for (GroupCollector groupCollector : groupCollectors) {
            groupCollector.add(row[i]);
            i++;
        }
    }

    @SuppressWarnings("unchecked")
    private <T, R> R get(Expression<T> expr) {
        GroupCollector<T,R> col = (GroupCollector<T,R>) groupCollectorMap.get(expr);
        if (col != null) {
            return col.get();
        }
        throw new NoSuchElementException(expr.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T, R> R getGroup(GroupExpression<T, R> definition) {
        for (GroupExpression<?, ?> def : groupExpressions) {
            if (def.equals(definition)) {
                return (R) groupCollectorMap.get(def.getExpression()).get();
            }
        }
        throw new NoSuchElementException(definition.toString());
    }

    @Override
    public <T> List<T> getList(Expression<T> expr) {
        return this.<T, List<T>>get(expr);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Map<K, V> getMap(Expression<K> key, Expression<V> value) {
        for (QPair<?, ?> pair : maps) {
            if (pair.equals(key, value)) {
                return (Map<K, V>) groupCollectorMap.get(pair).get();
            }
        }
        throw new NoSuchElementException("GMap(" + key + ", " + value + ")");
    }

    @Override
    public <T> T getOne(Expression<T> expr) {
        return this.<T, T>get(expr);
    }

    @Override
    public <T> Set<T> getSet(Expression<T> expr) {
        return this.<T, Set<T>>get(expr);
    }

    @Override
    public Object[] toArray() {
        List<Object> arr = new ArrayList<Object>(groupCollectors.size());
        for (GroupCollector<?,?> col : groupCollectors) {
            arr.add(col.get());
        }
        return arr.toArray();
    }

}