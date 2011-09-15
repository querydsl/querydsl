/*
 * Copyright (c) 2010 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import com.mysema.commons.lang.CloseableIterator;
import com.mysema.query.Projectable;
import com.mysema.query.ResultTransformer;
import com.mysema.query.Tuple;
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
public class GroupBy implements ResultTransformer<Collection<Group>> {

    private static final long serialVersionUID = 1L;

    private final Expression<?>[] expressions;
    
    public GroupBy(Expression<?> groupBy, Expression<?>... args) {
        expressions = new Expression<?>[args.length + 1];
        expressions[0] = groupBy;
        System.arraycopy(args, 0, expressions, 1, args.length);        
    }

    @Override
    public Collection<Group> transform(Projectable projectable) {
        final LinkedHashMap<Object, Group> groups = new LinkedHashMap<Object, Group>();
        
        CloseableIterator<Object[]> iter = projectable.iterate(expressions);
        try {
            while (iter.hasNext()) {
                Object[] row = iter.next();
                Object groupBy = row[0];
                // groups.values() should return Collection<GTuple> instead of Collection<? extends GTuple>
                GroupImpl group = (GroupImpl) groups.get(groupBy);
                if (group == null) {
                    group = new GroupImpl();
                    groups.put(groupBy, group);
                }
                group.add(row);
            }
        } finally {
            iter.close();
        }
        return groups.values();
    }
    
    private int indexOf(Expression<?> expr) {
        for (int i=0; i < expressions.length; i++) {
            if (expressions[i].equals(expr)) {
                return i;
            }
        }
        return -1;
    }
    
    @SuppressWarnings("unchecked")
    private class GroupImpl implements Group {
        
        private final List<Object[]> values = new ArrayList<Object[]>();

        @Override
        public <T> T get(int index, Class<T> type) {
            return (T) values.get(0)[index];
        }

        private void add(Object[] row) {
            this.values.add(row);
        }

        @Override
        public <T> T get(Expression<T> expr) {
            int index = indexOf(expr);
            return index != -1 ? (T) values.get(0)[index] : null;
        }

        @Override
        public <T> List<T> getList(int index, Class<T> type) {
            List<T> list = new ArrayList<T>(values.size());
            for (Object[] o : values) {
                list.add((T) o[index]);
            }
            return list;
        }

        @Override
        public <T> List<T> getList(Expression<T> expr) {
            int index = indexOf(expr);
            if (index < 0) {
                return null;
            } else {
                List<T> list = new ArrayList<T>(values.size());
                for (Object[] o : values) {
                    list.add((T) o[index]);
                }
                return list;
            }
        }
        
        @Override
        public Tuple getRow(int i) {
            return new TupleImpl(values.get(i));
        }
        
        @Override
        public int size() {
            return values.size();
        }
    }
    
    @SuppressWarnings("unchecked")
    private class TupleImpl implements Tuple {
        
        final Object[] row;
        
        public TupleImpl(Object[] row) {
            this.row = row;
        }
        @Override
        public <T> T get(int index, Class<T> type) {
            return (T) row[index];
        }

        @Override
        public <T> T get(Expression<T> expr) {
            int index = indexOf(expr);
            return index != -1 ? (T) row[index] : null;
        }

        @Override
        public Object[] toArray() {
            return row;
        }
    }
    
}
