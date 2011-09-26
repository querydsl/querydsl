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
 * Results can be analyzed, filtered and transformed using GroupProcessor for stateless processors
 * and GroupProcessorFactory for stateful processors. 
 * 
 * There exists 
 * 
 * @author sasa
 */
public class GroupBy<S> implements ResultTransformer<Map<S, Group>> {
    
    private static class GList<T> extends AbstractGroupDefinition<T, List<T>>{
        
        public GList(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupCollector<List<T>> createGroupCollector() {
            return new GroupCollector<List<T>>() {

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
    
    private static class GMap<K, V> extends AbstractGroupDefinition<Pair<K, V>, Map<K, V>>{
        
        public GMap(QPair<K, V> qpair) {
            super(qpair);
        }

        @Override
        public GroupCollector<Map<K, V>> createGroupCollector() {
            return new GroupCollector<Map<K, V>>() {

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
    
    private static class GOne<T> extends AbstractGroupDefinition<T, T>{

        public GOne(Expression<T> expr) {
            super(expr);
        }

        @Override
        public GroupCollector<T> createGroupCollector() {
            return new GroupCollector<T>() {
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
        
        private final Map<Expression<?>, GroupCollector<?>> groupCollectors = new LinkedHashMap<Expression<?>, GroupCollector<?>>();
        
        public GroupImpl() {
            for (int i=0; i < columnDefinitions.size(); i++) {
                GroupDefinition<?, ?> coldef = columnDefinitions.get(i);
                groupCollectors.put(coldef.getExpression(), coldef.createGroupCollector());
            }
        }

        void add(Object[] row) {
            int i=0;
            for (GroupCollector<?> groupCollector : groupCollectors.values()) {
                groupCollector.add(row[i]);
                i++;
            }
        }
        
        @SuppressWarnings("unchecked")
        private <T, R> R get(Expression<T> expr) {
            GroupCollector<R> col = (GroupCollector<R>) groupCollectors.get(expr);
            if (col != null) {
                return col.get();
            }
            throw new NoSuchElementException(expr.toString());
        }

        @Override
        @SuppressWarnings("unchecked")
        public <T, R> R getGroup(GroupDefinition<T, R> definition) {
            Iterator<GroupCollector<?>> iter = groupCollectors.values().iterator();
            for (GroupDefinition<?, ?> def : columnDefinitions) {
                GroupCollector<?> groupCollector = iter.next();
                if (def.equals(definition)) {
                    return (R) groupCollector.get();
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
                    return (Map<K, V>) groupCollectors.get(pair).get();
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
            for (GroupCollector<?> col : groupCollectors.values()) {
                arr.add(col.get());
            }
            return arr.toArray();
        }

    }

    private static class GSet<T> extends AbstractGroupDefinition<T, Set<T>>{
        
        public GSet(Expression<T> expr) {
            super(expr);
        }

        @Override
        @SuppressWarnings("unchecked")
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

    public static <T> GroupBy<T> create(Expression<T> expr) {
        return new GroupBy<T>(expr);
    }
    
    public static <T> GroupBy<T> create(Expression<T> expr, Expression<?> first, Expression<?>... rest) {
        return new GroupBy<T>(expr, first, rest);
    }
    
    protected final List<GroupDefinition<?, ?>> columnDefinitions;

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
    
    protected final List<QPair<?, ?>> maps;
    
    /**
     * Group ResultSet rows by the value of given expression.
     * 
     * @param groupBy
     */
    public GroupBy(Expression<S> groupBy) {
        this(new ArrayList<GroupDefinition<?,?>>(), new ArrayList<QPair<?,?>>());
        withGroup(new GOne<S>(groupBy));
    }
    
    /**
     * Group ResultSet rows by the value of given expression.
     * 
     * @param groupBy
     * @param first One-valued expression
     * @param rest More one-valued expressions
     */
    public GroupBy(Expression<S> groupBy, Expression<?> first, Expression<?>... rest) {
        this(groupBy);
        withOne(first);
        for (Expression<?> expr : rest) {
            withOne(expr);
        }
    }
    
    /**
     * Group ResultSet rows by the value of given expression.
     * 
     * @param groupBy
     * @param group
     * @param groups
     */
    public GroupBy(Expression<S> groupBy, GroupDefinition<?, ?> group, GroupDefinition<?, ?>... groups) {
        this(groupBy);
        withGroup(group);
        for (GroupDefinition<?, ?> g : groups) {
            withGroup(g);
        }
    }
    
    /**
     * A constructor that allows cloning in sub classes.
     * 
     * @param columnDefinitions
     * @param maps
     */
    protected GroupBy(List<GroupDefinition<?, ?>> columnDefinitions, List<QPair<?, ?>> maps) {
        this.columnDefinitions = columnDefinitions;
        this.maps = maps;
    }
    
    public List<GroupDefinition<?, ?>> getColumnDefinitions() {
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
    
    /**
     * A way to project custom group types.
     * 
     * @param groupDefinition Custom group definition
     * @return
     */
    public GroupBy<S> withGroup(GroupDefinition<?, ?> groupDefinition) {
        Assert.notNull(groupDefinition, "groupDefinition");
        
        columnDefinitions.add(groupDefinition);
        return this;
    }

    /**
     * Values of given expression in all rows belonging to this group as List. 
     * 
     * @param <T>
     * @param expr
     * @return
     */
    public <T> GroupBy<S> withList(Expression<T> expr) {
        return withGroup(new GList<T>(expr));
    }
    
    /**
     * Map of given expressions in all rows belonging to this group as Map. 
     * 
     * @param <K>
     * @param <V>
     * @param key
     * @param value
     * @return
     */
    public <K, V> GroupBy<S> withMap(Expression<K> key, Expression<V> value) {
        QPair<K, V> qpair = new QPair<K, V>(key, value);
        maps.add(qpair);
        return withGroup(new GMap<K, V>(qpair));
    }

    /**
     * Value of given expression in the first row belonging to this group.
     * 
     * @param <T>
     * @param expr
     * @return
     */
    public <T> GroupBy<S> withOne(Expression<T> expr) {
        return withGroup(new GOne<T>(expr));
    }
    
    /**
     * Process results with a stateful processor created by given processorFactory.
     * 
     * @param <O>
     * @param processorFactory 
     * @return
     */
    public <O> ProcessorGroupBy<S, O> withProcessor(GroupProcessorFactory<S, O> processorFactory) {
        return ProcessorGroupBy.create(this, processorFactory);
    }
    
    /**
     * Process results with the given stateless processor.
     * 
     * @param <O>
     * @param processor
     * @return
     */
    public <O> ProcessorGroupBy<S, O> withProcessor(GroupProcessor<S, O> processor) {
        return ProcessorGroupBy.create(this, processor);
    }
    
    /**
     * Values of given expression in all rows belonging to this group as Set. 
     * 
     * @param <T>
     * @param expr
     * @return
     */
    public <T> GroupBy<S> withSet(Expression<T> expr) {
        return withGroup(new GSet<T>(expr));
    }
    
    /**
     * Transforms results using given transformer.
     * 
     * @param <W>
     * @param transformer Stateless Map transformer
     * @return
     */
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

    /**
     * Transforms Map values with given transformer.
     * 
     * @param <W>
     * @param transformer Stateless Group transformer
     * @return
     */
    public <W> ProcessorGroupBy<S, Map<S, W>> withValueTransformer(final Transformer<? super Group, ? extends W> transformer) {
        return withTransformer(new Transformer<Map<S, Group>, Map<S, W>>() {

            @Override
            public Map<S, W> transform(Map<S, Group> input) {
                return ValueTransformerMap.create(input, transformer);
            }
            
        });
    }
    
}
