/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.Tuple;
import com.mysema.query.types.Expression;
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
//@SuppressWarnings("unchecked")
public class GroupBy2 implements ResultTransformer<Collection<Tuple>> {

    private final Expression<?>[] expressions;
    
    private static interface GroupFactory<T, R> {
        
        public void add(Object o);

        public R get();
        
    }
    
    /**
     * NOTE: This expression only applies to GroupBy.transform 
     * 
     * @param <T>
     * @param <R>
     */
    public static abstract class GroupAsExpression<T, R> implements Expression<R> {

        private static final long serialVersionUID = -8164758792405567077L;

        private final Expression<T> expr;
        
        public GroupAsExpression(Expression<T> expr) {
            this.expr = expr;
        }
        
        @Override
        public <S, C> S accept(Visitor<S, C> v, C context) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Class<? extends R> getType() {
            throw new UnsupportedOperationException();
        }
        
        public int hashCode() {
            return expr.hashCode();
        }
        
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            } else if (o instanceof GroupAsExpression) {
                GroupAsExpression<?, ?> other = (GroupAsExpression<?, ?>) o;
                return this.expr.equals(other.expr);
            } else {
                return false;
            }
        }
        
        public abstract GroupFactory<T, R> createGroupFactory();
        
    }
    
    public static <T> GroupAsExpression<T, Set<T>> set(Expression<T> expr) {
        return new GroupAsExpression<T, Set<T>>(expr) {

            private static final long serialVersionUID = -2507144565843468159L;

            @Override
            public GroupFactory<T, Set<T>> createGroupFactory() {
                return new GroupFactory<T, Set<T>>() {

                    private final Set<T> set = new HashSet<T>();
                    
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
            
        };
    }
    
    public static <T> GroupAsExpression<T, List<T>> list(Expression<T> expr) {
        return new GroupAsExpression<T, List<T>>(expr) {

            private static final long serialVersionUID = -6941324182786049824L;

            @Override
            public GroupFactory<T, List<T>> createGroupFactory() {
                return new GroupFactory<T, List<T>>() {

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
            
        };
    }
    
    private static class ValueGroupFactory<T> implements GroupFactory<T, T> {
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
        
    }
    
    public GroupBy2(Expression<?> groupBy, Expression<?>... args) {
        expressions = new Expression<?>[args.length + 1];
        expressions[0] = groupBy;
        System.arraycopy(args, 0, expressions, 1, args.length);        
    }

    @Override
    public Collection<Tuple> transform(Projectable projectable) {
        final LinkedHashMap<Object, Tuple> groups = new LinkedHashMap<Object, Tuple>();
        
        CloseableIterator<Object[]> iter = projectable.iterate(unwrap(expressions));
        try {
            while (iter.hasNext()) {
                Object[] row = iter.next();
                Object groupBy = row[0];
                // groups.values() should return Collection<GTuple> instead of Collection<? extends GTuple>
                GroupTuple group = (GroupTuple) groups.get(groupBy);
                if (group == null) {
                    group = new GroupTuple();
                    groups.put(groupBy, group);
                }
                group.add(row);
            }
        } finally {
            iter.close();
        }
        return groups.values();
    }
    
    private static Expression<?>[] unwrap(Expression<?>... expressions) {
        Expression<?>[] unwrapped = new Expression<?>[expressions.length];
        for (int i=0; i < expressions.length; i++) {
            if (expressions[i] instanceof GroupAsExpression) {
                unwrapped[i] = ((GroupAsExpression<?, ?>) expressions[i]).expr;
            } else {
                unwrapped[i] = expressions[i];
            }
        }
        return unwrapped;
    }
    
    private int indexOf(Expression<?> expr) {
        for (int i=0; i < expressions.length; i++) {
            if (expressions[i].equals(expr)) {
                return i;
            }
        }
        return -1;
    }
    
    private class GroupTuple implements Tuple {
        
        private final GroupFactory<?, ?>[] groupFactories;
        
        public GroupTuple() {
            groupFactories = new GroupFactory<?, ?>[expressions.length];
            for (int i=0; i < expressions.length; i++) {
                if (expressions[i] instanceof GroupAsExpression<?, ?>) {
                    groupFactories[i] = ((GroupAsExpression<?, ?>) expressions[i]).createGroupFactory();
                } else {
                    groupFactories[i] = new ValueGroupFactory<Object>();
                }
            }
        }

        @Override
        public <T> T get(int index, Class<T> type) {
            return (T) groupFactories[index].get();
        }

        @Override
        public <T> T get(Expression<T> expr) {
            int index = indexOf(expr);
            return (T) groupFactories[index].get();
        }

        @Override
        public Object[] toArray() {
            Object[] row = new Object[groupFactories.length];
            for (int i=0; i < groupFactories.length; i++) {
                row[i] = groupFactories[i].get();
            }
            return row;
        }
        
        public void add(Object[] row) {
            for (int i=0; i < groupFactories.length; i++) {
                groupFactories[i].add(row[i]);
            }
        }
    }
    
}
