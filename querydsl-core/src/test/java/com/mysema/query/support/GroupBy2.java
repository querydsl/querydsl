/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.ArrayList;
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
        <T> T first(Expression<T> expr);
        <T> Set<T> set(Expression<T> expr);
        <T> List<T> list(Expression<T> expr);
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
    
    
    public static class GFirst<T> extends AbstractGroupColumnDefinition<T, T>{

        public GFirst(Expression<T> expr) {
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
        columns.add(new GFirst<S>(groupBy));
    }
    
    public <T> GroupBy2(Expression<S> groupBy, GroupColumnDefinition<?, ?> group, GroupColumnDefinition<?, ?>... groups) {
        this(groupBy);
        columns.add(group);
        for (GroupColumnDefinition<?, ?> g : groups) {
            columns.add(g);
        }
    }
    
    public GroupBy2<S> group(GroupColumnDefinition<?, ?> g) {
        columns.add(g);
        return this;
    }
    
    public <T> GroupBy2<S> set(Expression<T> expr) {
        columns.add(new GSet<T>(expr));
        return this;
    }
    
    public <T> GroupBy2<S> list(Expression<T> expr) {
        columns.add(new GList<T>(expr));
        return this;
    }
    
    public <T> GroupBy2<S> first(Expression<T> expr) {
        columns.add(new GFirst<T>(expr));
        return this;
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
        public <T> T first(Expression<T> expr) {
            return (T) groupColumns.get(expr).get();
        }

        @Override
        public <T> Set<T> set(Expression<T> expr) {
            return (Set<T>) groupColumns.get(expr).get();
        }

        @Override
        public <T> List<T> list(Expression<T> expr) {
            return (List<T>) groupColumns.get(expr).get();
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
