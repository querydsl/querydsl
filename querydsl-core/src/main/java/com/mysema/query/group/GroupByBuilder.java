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

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.ResultTransformer;
import com.mysema.query.types.Expression;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.FactoryExpressionUtils;

/**
 * GroupByBuilder is a fluent builder for GroupBy instances. This class is not to be used directly, 
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
        
    public ResultTransformer<Map<K, Group>> as(Expression<?>... expressions) {
        return new GroupBy<K, Group>(key, expressions);
    }
    
    @SuppressWarnings("unchecked")
    public <V> ResultTransformer<Map<K, V>> as(Expression<V> expression) {
        final Expression<V> lookup = (Expression<V>)
                (expression instanceof GroupExpression ? ((GroupExpression<?,?> )expression).getExpression() : expression);
        return new GroupBy<K, V>(key, expression) {            

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
    
    public <V> ResultTransformer<Map<K, V>> as(FactoryExpression<V> expression) {
        final FactoryExpression<?> transformation = FactoryExpressionUtils.wrap(expression);
        List<Expression<?>> args = transformation.getArgs();        
        return new GroupBy<K, V>(key, args.toArray(new Expression<?>[args.size()])) {

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
    
    public <V, R extends FactoryExpression<V>> ResultTransformer<CloseableIterator<V>> iterate(R expression) {
        return new GroupByResultTransformer<K, V>(key, expression);
    }
    
    public <V, R extends Expression<V>> ResultTransformer<CloseableIterator<V>> iterate(R expression) {
        return new GroupByResultTransformer<K, V>(key, expression);
    }
    
}
