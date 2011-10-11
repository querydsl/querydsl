/*
 * Copyright (c) 2011 Mysema Ltd.
 * All rights reserved.
 *
 */
package com.mysema.query.group;

import com.mysema.query.types.Expression;

class GMin<T extends Comparable<T>> extends AbstractGroupDefinition<T, T> {

    public GMin(Expression<T> expr) {
        super(expr);
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