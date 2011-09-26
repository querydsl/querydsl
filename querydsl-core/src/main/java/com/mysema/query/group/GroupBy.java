/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.apache.commons.collections15.Transformer;

import com.mysema.commons.lang.Assert;
import com.mysema.commons.lang.CloseableIterator;
import com.mysema.commons.lang.Pair;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.types.Expression;

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
public class GroupBy<S> implements ResultTransformer<Map<S, Group>> {
    
    private static class GList<T> extends AbstractGroupColumnDefinition<T, List<T>>{
        
        public GList(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupColumn<List<T>> createGroupColumn() {
            return new GroupColumn<List<T>>() {

                private final List<T> list = new ArrayList<T>();
                
                @Override
                @SuppressWarnings("unchecked")
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
    
    private static class GMap<K, V> extends AbstractGroupColumnDefinition<Pair<K, V>, Map<K, V>>{
        
        public GMap(QPair<K, V> qpair) {
            super(qpair);
        }

        @Override
        public GroupColumn<Map<K, V>> createGroupColumn() {
            return new GroupColumn<Map<K, V>>() {

                private final Map<K, V> set = new LinkedHashMap<K, V>();
                
                @Override
                public void add(Object o) {
                    @SuppressWarnings("unchecked")
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
    
    private static class GOne<T> extends AbstractGroupColumnDefinition<T, T>{

        public GOne(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupColumn<T> createGroupColumn() {
            return new GroupColumn<T>() {
                private boolean first = true;
                
                private T val;
                
                @Override
                @SuppressWarnings("unchecked")
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
    
    private class GroupImpl implements Group {
        
        private final Map<Expression<?>, GroupColumn<?>> groupColumns = new LinkedHashMap<Expression<?>, GroupColumn<?>>();
        
        public GroupImpl() {
            for (int i=0; i < columnDefinitions.size(); i++) {
                GroupColumnDefinition<?, ?> coldef = columnDefinitions.get(i);
                groupColumns.put(coldef.getExpression(), coldef.createGroupColumn());
            }
        }

        void add(Object[] row) {
            int i=0;
            for (GroupColumn<?> groupColumn : groupColumns.values()) {
                groupColumn.add(row[i]);
                i++;
            }
        }
        
        @SuppressWarnings("unchecked")
        private <T, R> R get(Expression<T> expr) {
            GroupColumn<R> col = (GroupColumn<R>) groupColumns.get(expr);
            if (col != null) {
                return col.get();
            }
            throw new NoSuchElementException(expr.toString());
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T, R> R getGroup(GroupColumnDefinition<T, R> definition) {
            Iterator<GroupColumn<?>> iter = groupColumns.values().iterator();
            for (GroupColumnDefinition<?, ?> def : columnDefinitions) {
                GroupColumn<?> groupColumn = iter.next();
                if (def.equals(definition)) {
                    return (R) groupColumn.get();
                }
            }
            throw new NoSuchElementException(definition.toString());
        }
        
        @Override
        public <T> List<T> getList(Expression<T> expr) {
            return this.<T, List<T>>get(expr);
        }

        @SuppressWarnings("unchecked")
        public <K, V> Map<K, V> getMap(Expression<K> key, Expression<V> value) {
            for (QPair<?, ?> pair : maps) {
                if (pair.equals(key, value)) {
                    return (Map<K, V>) groupColumns.get(pair).get();
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
            List<Object> arr = new ArrayList<Object>(groupColumns.size());
            for (GroupColumn<?> col : groupColumns.values()) {
                arr.add(col.get());
            }
            return arr.toArray();
        }

    }

    private static class GSet<T> extends AbstractGroupColumnDefinition<T, Set<T>>{
        
        public GSet(Expression<T> expr) {
            super(expr);
        }

        @Override
        @SuppressWarnings("unchecked")
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

    public static <T> GroupBy<T> create(Expression<T> expr) {
        return new GroupBy<T>(expr);
    }
    
    public static <T> GroupBy<T> create(Expression<T> expr, Expression<?> first, Expression<?>... rest) {
        return new GroupBy<T>(expr, first, rest);
    }
    
    protected final List<GroupColumnDefinition<?, ?>> columnDefinitions;

    private final GroupProcessor<S, Map<S, Group>> defaultProcessor = new GroupProcessor<S, Map<S, Group>>() {

        @Override
        public boolean accept(Object[] row) {
            return true;
        }

        @Override
        public Map<S, Group> transform(Map<S, Group> groups) {
            return groups;
        }
    };
    
    private final List<QPair<?, ?>> maps;
    
    public GroupBy(Expression<S> groupBy) {
        this(new ArrayList<GroupColumnDefinition<?,?>>(), new ArrayList<QPair<?,?>>());
        withGroup(new GOne<S>(groupBy));
    }
    
    public GroupBy(Expression<S> groupBy, Expression<?> first, Expression<?>... rest) {
        this(groupBy);
        withOne(first);
        for (Expression<?> expr : rest) {
            withOne(expr);
        }
    }
    
    public <T> GroupBy(Expression<S> groupBy, GroupColumnDefinition<?, ?> group, GroupColumnDefinition<?, ?>... groups) {
        this(groupBy);
        withGroup(group);
        for (GroupColumnDefinition<?, ?> g : groups) {
            withGroup(g);
        }
    }
    
    protected GroupBy(List<GroupColumnDefinition<?, ?>> columnDefinitions, List<QPair<?, ?>> maps) {
        this.columnDefinitions = columnDefinitions;
        this.maps = maps;
    }
    
    public List<GroupColumnDefinition<?, ?>> getColumnDefinitions() {
        return Collections.unmodifiableList(columnDefinitions);
    }
    
    private Expression<?>[] getExpressions() {
        Expression<?>[] unwrapped = new Expression<?>[columnDefinitions.size()];
        for (int i=0; i < columnDefinitions.size(); i++) {
            unwrapped[i] = columnDefinitions.get(i).getExpression();
        }
        return unwrapped;
    }
    
    public <O> O process(Projectable projectable, GroupProcessor<S, O> processor) {
        Assert.notNull(projectable, "projectable");
        Assert.notNull(processor, "processor");

        final Map<S, Group> groups = new LinkedHashMap<S, Group>();
        
        CloseableIterator<Object[]> iter = projectable.iterate(getExpressions());
        try {
            while (iter.hasNext()) {
                Object[] row = iter.next();
                if (processor.accept(row)) {
                    @SuppressWarnings("unchecked")
                    S groupId = (S) row[0];

                    @SuppressWarnings("unchecked")
                    GroupImpl group = (GroupImpl) groups.get(groupId);
                    
                    if (group == null) {
                        group = new GroupImpl();
                        groups.put(groupId, group);
                    }
                    group.add(row);
                }
            }
        } finally {
            iter.close();
        }
        return processor.transform(groups);
    }
    
    @Override
    public Map<S, Group> transform(Projectable projectable) {
        return process(projectable, defaultProcessor);
    }
    
    public GroupBy<S> withGroup(GroupColumnDefinition<?, ?> g) {
        columnDefinitions.add(g);
        return this;
    }

    public <T> GroupBy<S> withList(Expression<T> expr) {
        return withGroup(new GList<T>(expr));
    }
    
    public <K, V> GroupBy<S> withMap(Expression<K> key, Expression<V> value) {
        QPair<K, V> qpair = new QPair<K, V>(key, value);
        maps.add(qpair);
        return withGroup(new GMap<K, V>(qpair));
    }

    public <T> GroupBy<S> withOne(Expression<T> expr) {
        return withGroup(new GOne<T>(expr));
    }
    
    public <O> ProcessorGroupBy<S, O> withProcessor(GroupProcessorFactory<S, O> processorFactory) {
        return ProcessorGroupBy.create(this, processorFactory);
    }
    
    public <O> ProcessorGroupBy<S, O> withProcessor(GroupProcessor<S, O> processor) {
        return ProcessorGroupBy.create(this, processor);
    }
    
    public <T> GroupBy<S> withSet(Expression<T> expr) {
        return withGroup(new GSet<T>(expr));
    }
    
    public <W> ProcessorGroupBy<S, W> withTransformer(final Transformer<Map<S, Group>, W> transformer) {
        return withProcessor(new GroupProcessor<S, W>() {

            @Override
            public W transform(Map<S, Group> input) {
                return transformer.transform(input);
            }

            @Override
            public boolean accept(Object[] row) {
                return true;
            }
            
        });
    }

    public <W> ProcessorGroupBy<S, Map<S, W>> withValueTransformer(final Transformer<? super Group, ? extends W> transformer) {
        return withTransformer(new Transformer<Map<S, Group>, Map<S, W>>() {

            @Override
            public Map<S, W> transform(Map<S, Group> input) {
                return ValueTransformerMap.create(input, transformer);
            }
            
        });
    }
    
}
