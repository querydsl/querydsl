package com.mysema.query.group;

import java.util.ArrayList;
import java.util.Collection;
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
import com.mysema.query.types.Operator;
import com.mysema.query.types.OperatorImpl;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.expr.SimpleOperation;

/**
 * Groups results by the first expression.
 * 
 * @author sasa
 * @author tiwe
 *
 */
@SuppressWarnings("unchecked")
public class GroupBy<K, V> implements ResultTransformer<Map<K,V>> {
    
    private static final Operator<Object> WRAPPED = new OperatorImpl<Object>("WRAPPED", Object.class);
    
    public static <K> GroupByBuilder<K> groupBy(Expression<K> key) {
        return new GroupByBuilder<K>(key);
    }
    
    @SuppressWarnings("rawtypes")
    public static <E> SimpleExpression<List<E>> list(Expression<E> expression) {
        return SimpleOperation.<List<E>>create((Class)List.class, WRAPPED, expression);
    }
    
    @SuppressWarnings("rawtypes")
    public static <E> SimpleExpression<Set<E>> set(Expression<E> expression) {
        return SimpleOperation.<Set<E>>create((Class)Set.class, WRAPPED, expression);
    }
    
    @SuppressWarnings("rawtypes")
    public static <E> SimpleExpression<Collection<E>> collection(Expression<E> expression) {
        return SimpleOperation.<Collection<E>>create((Class)Collection.class, WRAPPED, expression);
    }
    
    @SuppressWarnings("rawtypes")
    public static <K, V> SimpleExpression<Map<K, V>> map(Expression<K> key, Expression<V> value) {
        return SimpleOperation.<Map<K,V>>create((Class)Map.class, WRAPPED, key, value);
    }
    
    class GList<T> extends AbstractGroupDefinition<T, List<T>>{
        
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
    class GMap<K, V> extends AbstractGroupDefinition<Pair<K, V>, Map<K, V>>{
        
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
    
    class GSet<T> extends AbstractGroupDefinition<T, Set<T>>{
        
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
    
    class GOne<T> extends AbstractGroupDefinition<T, T>{

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
            if (expr instanceof Operation<?> && ((Operation<?>)expr).getOperator() == WRAPPED) {
                // TODO: Refactor this if-else to allow custom GroupDefinitions
                Operation<?> operation = (Operation<?>)expr;
                if (expr.getType().equals(List.class)) {
                    columnDefinitions.add(new GList(operation.getArg(0)));
                    projection.addAll(operation.getArgs());
                } else if (expr.getType().equals(Set.class)) {
                    columnDefinitions.add(new GSet(operation.getArg(0)));
                    projection.addAll(operation.getArgs());
                } else if (expr.getType().equals(Map.class)) {
                    QPair qPair = new QPair(operation.getArg(0), operation.getArg(1));
                    maps.add(qPair);
                    columnDefinitions.add(new GMap(qPair));
                    projection.add(qPair);
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
