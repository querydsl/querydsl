/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.support.GroupBy2.Group2;
import com.mysema.query.types.Expression;
import com.mysema.query.types.ExpressionBase;
import com.mysema.query.types.FactoryExpression;
import com.mysema.query.types.Visitor;

/**
 * Groups results by the first expression.
 * <ol>
 * <li>Order of groups by position of the first row of a group
 * <li>Rows belonging to a group may appear in any order
 * <li>Group of null is handled correctly
 * </ol>
 * 
 * @author sasa
 */
@SuppressWarnings("unchecked")
public class GroupBy2<S> implements ResultTransformer<Map<S, Group2>> {

    public static class Pair<K, V> {
        
        public final K key;
        
        public final V value;
        
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        public K getKey() {
            return key;
        }
        
        public V getValue() {
            return value;
        }
    }
    
    public static class QPair<K, V> extends ExpressionBase<Pair<K, V>> implements FactoryExpression<Pair<K, V>> {

        private static final long serialVersionUID = -1943990903548916056L;

        private final Expression<K> key;
        
        private final Expression<V> value;
        
        @SuppressWarnings("rawtypes")
        public QPair(Expression<K> key, Expression<V> value) {
            super((Class) Pair.class);
            this.key = key;
            this.value = value;
        }
        
        @Override
        public <R, C> R accept(Visitor<R, C> v, C context) {
            return v.visit(this, context);
        }

        @Override
        public List<Expression<?>> getArgs() {
            return Arrays.asList(key, value);
        }

        @Override
        public Pair<K, V> newInstance(Object... args) {
            return new Pair<K, V>((K) args[0], (V) args[1]);
        }
        
        @Override
        public int hashCode() {
            int hashCode = key.hashCode();
            return 31*hashCode + value.hashCode();
        }
        
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof QPair) {
                @SuppressWarnings("rawtypes")
                QPair other = (QPair) o;
                return this.key.equals(other.key) && this.value.equals(other.value);
            } else {
                return false;
            }
        }
    }
    
    public static interface GroupColumnDefinition<T, R> {
        
        Expression<T> getExpression();
        
        GroupColumn<R> createGroupColumn();
        
    }
    
    public static interface GroupColumn<R> {
        
        void add(Object o);

        R get();
        
    }
    
    public static interface Group2 {
        
        Object[] toArray();
        
        <T> T getOne(Expression<T> expr);
        
        <T> Set<T> getSet(Expression<T> expr);
        
        <T> List<T> getList(Expression<T> expr);
        
        <K, V> Map<K, V> getMap(Expression<K> key, Expression<V> value);
        
    }
    
    public static abstract class AbstractGroupColumnDefinition<T, R> implements GroupColumnDefinition<T, R> {
        
        private final Expression<T> expr;
        
        public AbstractGroupColumnDefinition(Expression<T> expr) {
            this.expr = expr;
        }
        
        @Override
        public Expression<T> getExpression() {
            return expr;
        }
    }
    
    public static class GMap<K, V> extends AbstractGroupColumnDefinition<Pair<K, V>, Map<K, V>>{
        
        public GMap(Expression<K> key, Expression<V> value) {
            super(new QPair<K, V>(key, value));
        }

        @Override
        public GroupColumn<Map<K, V>> createGroupColumn() {
            return new GroupColumn<Map<K, V>>() {

                private final Map<K, V> set = new LinkedHashMap<K, V>();
                
                @Override
                public void add(Object o) {
                    Pair<K, V> pair = (Pair<K, V>) o;
                    set.put(pair.key, pair.value);
                }

                @Override
                public Map<K, V> get() {
                    return set;
                }
                
            };
        }
    }
    
    public static class GSet<T> extends AbstractGroupColumnDefinition<T, Set<T>>{
        
        public GSet(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupColumn<Set<T>> createGroupColumn() {
            return new GroupColumn<Set<T>>() {

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
    
    public static class GList<T> extends AbstractGroupColumnDefinition<T, List<T>>{
        
        public GList(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupColumn<List<T>> createGroupColumn() {
            return new GroupColumn<List<T>>() {

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
    
    
    public static class GOne<T> extends AbstractGroupColumnDefinition<T, T>{

        public GOne(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupColumn<T> createGroupColumn() {
            return new GroupColumn<T>() {
                private T val;
                
                private boolean first = true;
                
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
    
    private final List<GroupColumnDefinition<?, ?>> columns = new ArrayList<GroupBy2.GroupColumnDefinition<?,?>>();
    
    public static <T> GroupBy2<T> groupBy(Expression<T> expr) {
        return new GroupBy2<T>(expr);
    }
    
    public GroupBy2(Expression<S> groupBy) {
        columns.add(new GOne<S>(groupBy));
    }
    
    public <T> GroupBy2(Expression<S> groupBy, GroupColumnDefinition<?, ?> group, GroupColumnDefinition<?, ?>... groups) {
        this(groupBy);
        columns.add(group);
        for (GroupColumnDefinition<?, ?> g : groups) {
            columns.add(g);
        }
    }
    
    public GroupBy2<S> withGroup(GroupColumnDefinition<?, ?> g) {
        columns.add(g);
        return this;
    }
    
    public <T> GroupBy2<S> withSet(Expression<T> expr) {
        return withGroup(new GSet<T>(expr));
    }
    
    public <T> GroupBy2<S> withList(Expression<T> expr) {
        return withGroup(new GList<T>(expr));
    }
    
    public <T> GroupBy2<S> withOne(Expression<T> expr) {
        return withGroup(new GOne<T>(expr));
    }

    public <K, V> GroupBy2<S> withMap(Expression<K> key, Expression<V> value) {
        return withGroup(new GMap<K, V>(key, value));
    }

    private class GroupImpl implements Group2 {
        
        private final Map<Expression<?>, GroupColumn<?>> groupColumns;
        
        public GroupImpl() {
            groupColumns = new LinkedHashMap<Expression<?>, GroupColumn<?>>();
            for (int i=0; i < columns.size(); i++) {
                GroupColumnDefinition<?, ?> coldef = columns.get(i);
                groupColumns.put(coldef.getExpression(), coldef.createGroupColumn());
            }
        }
        
        @Override
        public <T> T getOne(Expression<T> expr) {
            return (T) groupColumns.get(expr).get();
        }

        @Override
        public <T> Set<T> getSet(Expression<T> expr) {
            return (Set<T>) groupColumns.get(expr).get();
        }

        @Override
        public <T> List<T> getList(Expression<T> expr) {
            return (List<T>) groupColumns.get(expr).get();
        }
        
        public <K, V> Map<K, V> getMap(Expression<K> key, Expression<V> value) {
            return (Map<K, V>) groupColumns.get(new QPair<K, V>(key, value)).get();
        }
        
        public void add(Object[] row) {
            int i=0;
            for (GroupColumn<?> groupColumn : groupColumns.values()) {
                groupColumn.add(row[i]);
                i++;
            }
        }

        @Override
        public Object[] toArray() {
            List<Object> arr = new ArrayList<Object>(groupColumns.size());
            for (GroupColumn<?> col : groupColumns.values()) {
                arr.add(col.get());
            }
            return arr.toArray();
        }


    }

    @Override
    public Map<S, Group2> transform(Projectable projectable) {
        final Map<S, Group2> groups = new LinkedHashMap<S, Group2>();
        
        CloseableIterator<Object[]> iter = projectable.iterate(unwrapExpressions());
        try {
            while (iter.hasNext()) {
                Object[] row = iter.next();
                S groupId = (S) row[0];
                GroupImpl group = (GroupImpl) groups.get(groupId);
                if (group == null) {
                    group = new GroupImpl();
                    groups.put(groupId, group);
                }
                group.add(row);
            }
        } finally {
            iter.close();
        }
        return groups;
    }
    
    private Expression<?>[] unwrapExpressions() {
        Expression<?>[] unwrapped = new Expression<?>[columns.size()];
        for (int i=0; i < columns.size(); i++) {
            unwrapped[i] = columns.get(i).getExpression();
        }
        return unwrapped;
    }
    
}
