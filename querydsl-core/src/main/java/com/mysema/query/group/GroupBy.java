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
import java.util.Set;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.types.Expression;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * Groups results by the first expression.
 * 
 * @author sasa
 * @author tiwe
 *
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GroupBy<K, V> implements ResultTransformer<Map<K,V>> {
    
    public static <K> GroupByBuilder<K> groupBy(Expression<K> key) {
        return new GroupByBuilder<K>(key);
    }
    
    public static <E extends Comparable<E>> SimpleExpression<E> min(Expression<E> expression) {
        return new GMin<E>(expression);
    }    
    
    public static <E extends Comparable<E>> SimpleExpression<E> max(Expression<E> expression) {
        return new GMax<E>(expression);
    }
           
    public static <E> SimpleExpression<List<E>> list(Expression<E> expression) {
        return new GList<E>(expression);
    }
    
    public static <E> SimpleExpression<Set<E>> set(Expression<E> expression) {
        return new GSet<E>(expression);
    }
    
    public static <K, V> SimpleExpression<Map<K, V>> map(Expression<K> key, Expression<V> value) {
        QPair<K,V> qPair = new QPair<K,V>(key, value);
        return new GMap<K,V>(qPair);
    }
    
    protected final List<GroupExpression<?, ?>> groupExpressions = new ArrayList<GroupExpression<?, ?>>();
    
    protected final List<QPair<?,?>> maps = new ArrayList<QPair<?,?>>();
        
    protected final Expression<?>[] expressions;
    
    GroupBy(Expression<K> key, Expression<?>... expressions) {
        
        List<Expression<?>> projection = new ArrayList<Expression<?>>(expressions.length);        
        groupExpressions.add(new GOne<K>(key));
        projection.add(key);
        
        for (Expression<?> expr : expressions) {
            if (expr instanceof GroupExpression<?,?>) {
                GroupExpression<?,?> groupExpr = (GroupExpression<?,?>)expr;
                groupExpressions.add(groupExpr);
                projection.add(groupExpr.getExpression());
                if (groupExpr instanceof GMap) {
                    maps.add((QPair<?, ?>) groupExpr.getExpression());
                }                
            } else {
                groupExpressions.add(new GOne(expr));
                projection.add(expr);
            }
        }
        
        this.expressions = projection.toArray(new Expression[projection.size()]);
    }       
    
    @Override
    public Map<K, V> transform(Projectable projectable) {
        Map<K, Group> groups = new LinkedHashMap<K, Group>();
        
        // create groups
        CloseableIterator<Object[]> iter = projectable.iterate(expressions);
        try {
            while (iter.hasNext()) {
                Object[] row = iter.next();
                K groupId = (K) row[0];                
                GroupImpl group = (GroupImpl)groups.get(groupId);                
                if (group == null) {
                    group = new GroupImpl(groupExpressions, maps);
                    groups.put(groupId, group);
                }
                group.add(row);
            }
        } finally {
            iter.close();
        }
        
        // transform groups
        return transform(groups);
        
    }

    protected Map<K, V> transform(Map<K, Group> groups) {
        return (Map<K,V>)groups;
    }

}
