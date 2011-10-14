/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mysema.commons.lang.Assert;
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
    
    public GroupByBuilder(Expression<K> key) {
        this.key = key;
    }
        
    public ResultTransformer<Map<K, Group>> as(Expression<?>... expressions) {
        return new GroupBy<K, Group>(key, expressions);
    }
    
    public <V> ResultTransformer<Map<K, V>> as(FactoryExpression<V> expression) {
        Assert.notNull(expression, "expression");
        
        final FactoryExpression<?> transformation = FactoryExpressionUtils.wrap(expression);

        List<Expression<?>> args = transformation.getArgs();
        
        return new GroupBy<K, V>(key, args.toArray(new Expression<?>[args.size()])) {

            @Override
            protected Map<K, V> transform(Map<K, Group> groups) {
                // NOTE: Using new groups.size() as initialCapacity leads to unnecessary rehashing 
                // if size is close to some power of 2
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
    
    
}
