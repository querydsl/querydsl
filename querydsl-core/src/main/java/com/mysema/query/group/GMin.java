/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.types.Expression;

class GMin<T extends Comparable<T>> extends AbstractGroupExpression<T, T> {

    private static final long serialVersionUID = 8312168556148122576L;

    @SuppressWarnings("unchecked")
    public GMin(Expression<T> expr) {
        super((Class) expr.getType(), expr);
    }

    @Override
    public GroupCollector<T,T> createGroupCollector() {
        return new GroupCollector<T,T>() {
            private T min;
            
            @Override
            public void add(T o) {
                if (min != null) {
                    min = o.compareTo(min) < 0 ? o : min;
                } else {
                    min = (T)o;
                }
            }
            @Override
            public T get() {
                return min;
            }                
        };
    }        
}