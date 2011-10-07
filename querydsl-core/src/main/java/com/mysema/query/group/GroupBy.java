package com.mysema.query.group;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.types.Expression;
import com.mysema.query.types.Operation;
import com.mysema.query.types.expr.SimpleExpression;

/**
 * Groups results by the first expression.
 * 
 * @author sasa
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class GroupBy<K, V> implements ResultTransformer<Map<K,V>> {
    
    public static <K> GroupByBuilder<K> groupBy(Expression<K> key) {
        return new GroupByBuilder<K>(key);
    }
    
    @SuppressWarnings("rawtypes")
    public static <E> SimpleExpression<List<E>> list(Expression<E> expression) {
        return new GroupExpression<List<E>>((Class)List.class, new GList<E>(expression), expression);
    }
    
    @SuppressWarnings("rawtypes")
    public static <E> SimpleExpression<Set<E>> set(Expression<E> expression) {
        return new GroupExpression<Set<E>>((Class)Set.class, new GSet<E>(expression), expression);
    }
    
    @SuppressWarnings("rawtypes")
    public static <K, V> SimpleExpression<Map<K, V>> map(Expression<K> key, Expression<V> value) {
        QPair<K,V> qPair = new QPair<K,V>(key, value);
        return new GroupExpression<Map<K,V>>((Class)Map.class, new GMap<K,V>(qPair), qPair);
    }
    
    static class GList<T> extends AbstractGroupDefinition<T, List<T>>{
        
        public GList(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupCollector<List<T>> createGroupCollector() {
            return new GroupCollector<List<T>>() {

                private final List<T> list = new ArrayList<T>();
                
                @Override
                public void add(Object o) {
                    list.add((T) o);
                }

                @Override
                public List<T> get() {
                    return list;
                }
                
            };
        }
    }
    
    @SuppressWarnings("hiding")
    static class GMap<K, V> extends AbstractGroupDefinition<Pair<K, V>, Map<K, V>>{
        
        public GMap(QPair<K,V> qpair) {
            super(qpair);
        }

        @Override
        public GroupCollector<Map<K, V>> createGroupCollector() {
            return new GroupCollector<Map<K, V>>() {

                private final Map<K, V> set = new LinkedHashMap<K, V>();
                
                @Override
                public void add(Object o) {
                    Pair<K, V> pair = (Pair<K, V>) o;
                    set.put(pair.getFirst(), pair.getSecond());
                }

                @Override
                public Map<K, V> get() {
                    return set;
                }
                
            };
        }
    }
    
    static class GSet<T> extends AbstractGroupDefinition<T, Set<T>>{
        
        public GSet(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupCollector<Set<T>> createGroupCollector() {
            return new GroupCollector<Set<T>>() {

                private final Set<T> set = new LinkedHashSet<T>();
                
                @Override
                public void add(Object o) {
                    set.add((T) o);
                }

                @Override
                public Set<T> get() {
                    return set;
                }
                
            };
        }
    }
    
    static class GOne<T> extends AbstractGroupDefinition<T, T>{

        public GOne(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupCollector<T> createGroupCollector() {
            return new GroupCollector<T>() {
                private boolean first = true;
                
                private T val;
                
                @Override
                public void add(Object o) {
                    if (first) {
                        val = (T) o;
                        first = false;
                    }
                }
        
                @Override
                public T get() {
                    return val;
                }
            };
        }
    }
    
    protected final List<GroupDefinition<?, ?>> columnDefinitions = new ArrayList<GroupDefinition<?, ?>>();
    
    protected final List<QPair<?,?>> maps = new ArrayList<QPair<?,?>>();
        
    protected final Expression<?>[] expressions;
    
    @SuppressWarnings("rawtypes")
    GroupBy(Expression<K> key, Expression<?>... expressions) {
        
        List<Expression<?>> projection = new ArrayList<Expression<?>>(expressions.length);        
        columnDefinitions.add(new GOne<K>(key));
        projection.add(key);
        
        for (Expression<?> expr : expressions) {
            if (expr instanceof GroupExpression<?>) {
                GroupExpression<?> groupExpr = (GroupExpression<?>)expr;
                columnDefinitions.add(groupExpr.getDefinition());
                projection.add(groupExpr.getDefinition().getExpression());
                if (groupExpr.getDefinition() instanceof GMap) {
                    maps.add((QPair<?, ?>) groupExpr.getDefinition().getExpression());
                }                
            } else {
                columnDefinitions.add(new GOne(expr));
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
                    group = new GroupImpl(columnDefinitions, maps);
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
